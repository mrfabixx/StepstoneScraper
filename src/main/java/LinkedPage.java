import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlSpan;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.LinkedHashMap;

public class LinkedPage {

    WebClient webClient;
    HtmlPage page;                                                                                                      // initialsieren Object HTMLPage


    public LinkedPage() {

    }

    public void exctractInformation(ArrayList<String> arrayList) throws IOException {

        if (arrayList != null) {
            webClient = new WebClient();
            page = null;
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);

            String listelement;
            JSONObject jsonObject = new JSONObject();    //
            JSONArray jsonArray = new JSONArray();


            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i) != null) {

                    LinkedHashMap<String, Object> nestesMap = new LinkedHashMap<>();

                    listelement = arrayList.get(i);
                    page = webClient.getPage(listelement);
                    HtmlSpan jobPostion = page.getFirstByXPath("(//span[@class='listing-content-provider-1idv5t9'])[1]");
                    String jobDescription = jobPostion.asNormalizedText();
                    HtmlSpan rolle = page.getFirstByXPath("(//span[@class='listing-content-provider-pz58b2'])[2]");
                    String jobRolle = rolle.asNormalizedText();
                    String currentUrl = String.valueOf(page.getEnclosingWindow().getEnclosedPage().getUrl());
                    System.out.println(currentUrl);

                    HtmlSpan span = page.getFirstByXPath("(//span[@class='listing-content-provider-pz58b2'])[3]");
                    String content = span.asNormalizedText();
                    String[] lines = content.split("\\n");
                    String newContent = String.join("\n", lines);

//                    String[] array = content.split("\\n");
//                    String newLines = Arrays.toString(array);
//                    System.out.println(newLines);
//
//                    StringBuilder stringBuilder = new StringBuilder();
//                    for(int x=0;x< newLines.length();x++){
//                         stringBuilder.append(x);
//                         stringBuilder.append("\n");
//
//                    }


//                    }
                    nestesMap.put("Jobtitle", jobDescription);
                    nestesMap.put("Job Description", jobRolle);
                    nestesMap.put("Skills", newContent);
                    nestesMap.put("URL", currentUrl);
                    JSONObject mapJson = new JSONObject(nestesMap);
                    jsonArray.put(mapJson);

                    jsonObject.put("JobPostions", jsonArray);
                    }
            }
            String prettyJson = jsonObject.toString(3);
            System.out.println(prettyJson);
            String filename = writeToCSVFile(jsonObject);
            readCsvFile(filename);
        }


    }

    /**
     * takes the Jsonobject and writes to a CSV File
     * @param jsonObject is the object wich is passed
     */

    public String writeToCSVFile(JSONObject jsonObject) {
        String filename = "Stepstone_Listings.csv";
        String[] header = {"Title","Description", "Skills", "Url"};
        if (jsonObject != null) {
            try (CSVWriter writer = new CSVWriter(new FileWriter(filename))) {
                writer.writeNext(header);
                JSONArray array = jsonObject.getJSONArray("JobPostions");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject result = array.getJSONObject(i);
                    String[] line = {result.getString("Jobtitle"),result.getString("Job Description"), result.getString("Skills"), result.getString("URL")};
                    writer.writeNext(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } return filename;

    }

    /**
     * takes the file and checks if the file has already double joblistings
     * @param filename
     * @throws FileNotFoundException
     */
    public void readCsvFile(String filename) throws FileNotFoundException {

        try {
            CSVReader reader = new CSVReader(new FileReader(filename));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                String lines = Arrays.toString(nextLine);
                System.out.println(lines);// reads the nextlines and stores them into nextilne array if not null
//                for (int i=0; i<=nextLine.length;i++) {
//                    System.out.print( i + " ");
//                }
              }
            reader.close();
        } catch (IOException | CsvValidationException e) {
            System.err.println(e.getMessage());
        }    }

}



