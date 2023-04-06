import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Webscraping {


    public static void main(String[] args) throws InterruptedException, IOException {

        SeleniumDrive selenium = new SeleniumDrive();
        String url = selenium.login();

        crawlListings(1, url, new ArrayList<>());

    }

    /**
     * method position fetches the all the  Work Postions on the first Webpage
     *
     * @param url
     * @return Document to pass it to the crawl method
     */
    public static Document request(String url, ArrayList<String> v) throws IOException {
        Document document = null;
        try {
            Connection con = Jsoup.connect(url);
            document = con.get();
            if (con.response().statusCode() == 200) {
                System.out.println("Statuscode==200");
            } else {
                System.out.println(" No Connection");
            }
            v.add(url);

            String classname = "sc-fzqBZW dkGKpH";
            Elements jobPosition = document.getElementsByClass(classname);                                              // thats the link where the joposting is

            JSONObject jsonObject1 = new JSONObject();
            JSONArray jsonArray = new JSONArray();

            for (Element e : jobPosition) {
                jsonArray.put(e.text());                                                                                // inhalt von dem element in das jsonarray
            }
            try {
                JSONObject jsonObject2 = new JSONObject();                                                              // nested Json for the job requirements
                jsonObject1.put("position", jsonArray);
                jsonObject2.put("Software entwicklung", jsonObject1);

            } catch (JSONException e) {
            }
            System.out.println(jsonObject1);
        } catch (IOException e) {

            e.printStackTrace();
        }
        return document;

    }

    public static void crawlListings(int level, String url, ArrayList<String> visited) throws IOException, InterruptedException {
        Connection con = Jsoup.connect(url);
        Document document = con.get();
        ArrayList<String> list = new ArrayList<>();
        LinkedPage linkedPage = new LinkedPage();


        if (con.response().statusCode() == 200) {
            System.out.println("Statuscode==200");
        } else {
            System.out.println(" No Connection");
        }

        String classname = "sc-fzqBZW dkGKpH";                              // Element for the job description
        Elements jobPosition = document.getElementsByClass(classname);                                              // thats the link where the joposting is                                                                                            // links werden hier gespeichert

        String classnameLink = "sc-fzokOt hTheZK";                                      // Element for the link
        String cleanUrl = "";

        if (document != null) {
            for (Element link : document.getElementsByClass(classnameLink)) {

                cleanUrl = link.attr("abs:href");                                                             // all the links that are from the job postings
                System.out.println(cleanUrl);
                list.add(cleanUrl);
                Thread.sleep(100);
            }
            System.out.println(" Das ist die Arraylist mit den joblistings" + list);

            linkedPage.extractInformation(list);

        }
    }



}







