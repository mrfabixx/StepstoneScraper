import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlSpan;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class LinkedPage {

    WebClient webClient;
    HtmlPage page;
    HtmlPage linkedpage;

    public LinkedPage() {

    }

    public void extractInformation( ArrayList<String> arrayList) throws IOException {

        if (arrayList != null) {
            webClient = new WebClient();
            page = null;
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            String nextPage;
            String listelement;
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();

//            for (int i = 0; i < nextpage.size(); i++) {
//                nextPage = nextpage.get(i);
//                linkedpage = webClient.getPage(nextPage);
                for (int y = 0; y < arrayList.size(); y++) {
                    if (arrayList.get(y) != null) {

                        LinkedHashMap<String, Object> nestedMap = new LinkedHashMap<>();

                        listelement = arrayList.get(y);
                        page = webClient.getPage(listelement);
                        HtmlSpan jobPostion = page.getFirstByXPath("(//span[@class='listing-content-provider-1idv5t9'])[1]");
                        String jobDescription = jobPostion.asNormalizedText().toLowerCase();
                        HtmlSpan rolle = page.getFirstByXPath("(//span[@class='listing-content-provider-pz58b2'])[2]");
                        String jobRolle = rolle.asNormalizedText().toLowerCase();
                        String currentUrl = String.valueOf(page.getEnclosingWindow().getEnclosedPage().getUrl());
                        // System.out.println(currentUrl);

                        HtmlSpan span = page.getFirstByXPath("(//span[@class='listing-content-provider-pz58b2'])[3]");
                        String content = span.asNormalizedText().toLowerCase();
                        String lines = content.replace("\n", "");

//                    String[] lines = content.split("\\n");
//                    String newContent = String.join("\n", lines);


                        nestedMap.put("Jobtitle", jobDescription);
                        nestedMap.put("Job Description", jobRolle);
                        nestedMap.put("Skills", lines);
                        nestedMap.put("URL", currentUrl);
                        JSONObject mapJson = new JSONObject(nestedMap);
                        jsonArray.put(mapJson);

                        jsonObject.put("JobPostions", jsonArray);

                }

            }
                String prettyJson = jsonObject.toString(3);
                System.out.println(prettyJson);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDateTime now = LocalDateTime.now();
                String company = System.getenv("Search.company");
                String dateTimeString = now.format(formatter);
                String newJson = company + "_" + dateTimeString + ".json";

                try (FileWriter jsonfile = new FileWriter(newJson)) {
                    jsonfile.write(prettyJson);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String filename = writeToCSVFile(jsonObject);

                readCsvFile(filename);
            }


        }

        /**
         * takes the Jsonobject and writes to a CSV File
         *
         * @param jsonObject is the object wich is passed
         */

        public String writeToCSVFile (JSONObject jsonObject) throws IOException {
            String filePath = "C://Users//fhoti//Desktop//StepstoneScraper";
            File file = new File(filePath);
            String newFilename;
            String content = "";

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            String dateTimeString = now.format(formatter);
            String company = System.getenv("Search.company");
            newFilename = "Stepstone_Listings_" + company + dateTimeString + ".csv";

            String[] header = {"Title", "Description", "Skills", "Url"};
            String jobTitle = "Job Title:";
            String JobDesription = "Job Description:";
            String skills = "Skills:";
            String url = "URL:";

            if (jsonObject != null) {

                try (CSVWriter writer = new CSVWriter(new FileWriter(newFilename))) {
                    writer.writeNext(header);
                    JSONArray array = jsonObject.getJSONArray("JobPostions");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject result = array.getJSONObject(i);
                        String[] line = {jobTitle + result.getString("Jobtitle").replaceAll("(Job Title:)", "$1 "),
                                JobDesription + result.getString("Job Description").replaceAll("(Job Description:)", "\n$1 "),
                                skills + result.getString("Skills").replaceAll("(Skills:)", "\n$1"),
                                url + result.getString("URL").replaceAll("(URL:)", "\n$1")};


                        writer.writeNext(line);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//        String newContentHash = DigestUtils.md5Hex(content);    // read the Hash
//        File outputFile = new File(newFilename);
//        if (outputFile.exists()){
//            try {
//                String oldcontent = FileUtils.readFileToString(outputFile,"UTF-8");
//                String oldcontentHash = DigestUtils.md5Hex(oldcontent);
//                if (!oldcontentHash.equals(newContentHash)){
//                    return newFilename;
//                }
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//        }
//        try (CSVWriter writer = new CSVWriter(new FileWriter(outputFile))) {
//            writer.writeNext(header);
//            writer.writeAll((Iterable<String[]>) Arrays.asList((content.split("\n"))));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

            return newFilename;
        }

        /**
         * takes the file and checks if the file has already double joblistings
         *
         * @param filename
         * @throws FileNotFoundException
         */
        public void readCsvFile (String filename) throws FileNotFoundException {
            List<String> existingFile = new ArrayList<>();
            try {
                CSVReader reader = new CSVReader(new FileReader(filename));
                String[] nextLine;
                while ((nextLine = reader.readNext()) != null) {
                    String lines = Arrays.toString(nextLine);
                    existingFile.add(lines);
                    System.out.println(existingFile);


                }
                reader.close();
            } catch (IOException | CsvValidationException e) {
                System.err.println(e.getMessage());
            }
        }

    }



