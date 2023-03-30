import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlSpan;
import org.json.JSONArray;
import org.json.JSONObject;

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
        // that should be the big Jsonobject
        //  jsonObject.put("position",nested);


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

                jsonObject.put("Job_Postions",jsonArray);

                //  System.out.println(" Das sind Anforderungen : " + "\n" + content.toString());
            }

        }
        String prettyJson = jsonObject.toString(3);
        // writeToCSVFile(prettyJson);
        System.out.println(prettyJson);


    }
    public void writeToCSVFile(String filname){
    }



}



