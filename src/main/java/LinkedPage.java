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

            String listelement = null;
            JSONObject jsonObject = new JSONObject();    //
            JSONArray jsonArray = new JSONArray();


            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i) != null) {
                    JSONObject nested = new JSONObject();

                    LinkedHashMap<String, Object> nestesMap = new LinkedHashMap<>();

                    listelement = arrayList.get(i);
                    page = webClient.getPage(listelement);
                    HtmlSpan jobPostion = page.getFirstByXPath("(//span[@class='listing-content-provider-1idv5t9'])[1]");
                    String jobDescription = jobPostion.asNormalizedText();
                    String currentUrl = String.valueOf(page.getEnclosingWindow().getEnclosedPage().getUrl());
                    System.out.println(currentUrl);

                    HtmlSpan span = page.getFirstByXPath("(//span[@class='listing-content-provider-pz58b2'])[3]");
                    String skills = span.asNormalizedText();

                    nestesMap.put("Title", jobDescription);
                    nestesMap.put("Skills", skills);
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
     * @param jsonObject
     */

    public String writeToCSVFile(JSONObject jsonObject) {
        String filename = "Stepstone_Listings.csv";
        String[] header = {"Title", "Skills", "Url"};
        if (jsonObject != null) {
            try (CSVWriter writer = new CSVWriter(new FileWriter(filename))) {
                writer.writeNext(header);
                JSONArray array = jsonObject.getJSONArray("JobPostions");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject result = array.getJSONObject(i);
                    String[] line = {result.getString("Title"), result.getString("Skills"), result.getString("URL")};
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
            while ((nextLine = reader.readNext()) != null) {                                    // reads the nextlines and stores them into nextilne array if not null
                for (int i=0; i<=nextLine.length;i++) {
                    System.out.print( i + " ");
                }
                System.out.println();
            }
            reader.close();
        } catch (IOException | CsvValidationException e) {
            System.err.println(e.getMessage());
        }    }

}



