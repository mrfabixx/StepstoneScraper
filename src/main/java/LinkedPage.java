import com.opencsv.CSVWriter;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlSpan;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class LinkedPage {

    WebClient webClient;
    HtmlPage page;                                                                                                      // initialsieren Object HTMLPage


    public LinkedPage() {

    }

    public void exctractInformation(ArrayList<String> arrayList) throws IOException {


        webClient = new WebClient();
        page = null;
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);

        String listelement = null;
        JSONObject jsonObject = new JSONObject();    //
        JSONArray jsonArray= new JSONArray();


        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i) != null) {
                JSONObject nested = new JSONObject();

                LinkedHashMap<String,Object> nestesMap = new LinkedHashMap<>();

                listelement = arrayList.get(i);
                page = webClient.getPage(listelement);
                HtmlSpan jobPostion = page.getFirstByXPath("(//span[@class='listing-content-provider-1idv5t9'])[1]");
                String jobDescription = jobPostion.asNormalizedText();
                String currentUrl = String.valueOf(page.getEnclosingWindow().getEnclosedPage().getUrl());
                System.out.println(currentUrl);

                HtmlSpan span = page.getFirstByXPath("(//span[@class='listing-content-provider-pz58b2'])[3]");
                String skills = span.asNormalizedText();

                nestesMap.put("Title",jobDescription);
                nestesMap.put("Skills",skills);
                nestesMap.put("URL",currentUrl);
                JSONObject mapJson= new JSONObject(nestesMap);
                jsonArray.put(mapJson);

                jsonObject.put("JobPostions",jsonArray);

                //  System.out.println(" Das sind Anforderungen : " + "\n" + content.toString());
            }

        }
        String prettyJson = jsonObject.toString(3);
        System.out.println(prettyJson);
        writeToCSVFile(jsonObject);

    }
    public void writeToCSVFile(JSONObject jsonObject){
        String[] header = {"Title", "Skills", "Url"};
        if (jsonObject!=null){
            try (CSVWriter writer = new CSVWriter(new FileWriter("Stesptone_Listings.csv"))) {
                writer.writeNext(header);
                JSONArray results = jsonObject.getJSONArray("JobPostions");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject result = results.getJSONObject(i);
                    String[] line = {result.getString("Title"), result.getString("Skills"), result.getString("URL")};
                    writer.writeNext(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



}



