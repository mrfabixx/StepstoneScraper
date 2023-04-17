import org.jetbrains.annotations.NotNull;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class Webscraping {


    public static void main(String[] args) throws InterruptedException, IOException {

        SeleniumDrive selenium = new SeleniumDrive();
        String url = selenium.login();
        crawlListings(5, url);


    }


    /**
     * method position fetches the all the  Work Postions on the first Webpage and adds all the Url links into the List
     *
     * @param url
     * Document to pass it to the crawl method
     */

    public static void crawlListings(int level, String url) throws IOException {
        Connection con = Jsoup.connect(url);
        Document document = con.get();
        String baseUrl = document.location();
        System.out.println(baseUrl);

//        ArrayList<String> list = new ArrayList<>();
//        LinkedPage linkedPage = new LinkedPage();

        ArrayList<String> pages = new ArrayList<>();
        for (int page = 1; page <= level; page++) {
            try {
                String nextUrl = baseUrl + "?page=" + page;
//                Connection connection = Jsoup.connect(nextUrl);
//                Document doc = connection.get();
                pages.add(nextUrl);
                Links(pages);

            } catch (HttpStatusException e) {
                System.out.println("Page " + page + " not found: " + e.getMessage());
                break;                                                                                   // Exit the loop if the page is not fou)

            }


//            if (con.response().statusCode() == 200) {
//                System.out.println("Statuscode==200");
//            } else {
//                System.out.println(" No Connection");
//            }
//
//            String classnameLink = "sc-fzokOt hTheZK";                                      // Element for the link
//            String cleanUrl = "";
//
//            if (document != null) {
//                for (Element link : document.getElementsByClass(classnameLink)) {
//
//                    cleanUrl = link.attr("abs:href");                                                             // all the links that are from the job postings
//                    System.out.println(cleanUrl);
//                    list.add(cleanUrl);
//                }
//                System.out.println(" Das ist die Arraylist mit den Joblistings:" + list);
//
//                linkedPage.extractInformation(list);
//
//            }
        }
        System.out.println(pages);
    }
    /**
     * this function should take a list from the collected pages 1-10 and save all the Joblinks from all pages
     *
     * @param visited
     */
    public static void Links(@NotNull ArrayList<String> visited) throws IOException {
        LinkedPage linkedPage = new LinkedPage();
        String cleanUrl ;
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i <= visited.size(); i++) {
            Connection next = Jsoup.connect(String.valueOf(i));
            Document LinksDocument = next.get();

            for (Element link : LinksDocument.getElementsByClass("sc-fzokOt hTheZK")) {
                cleanUrl = link.attr("abs:href");                                                             // all the links that are from the job postings
                System.out.println(cleanUrl);
                list.add(cleanUrl);

            }
            System.out.println(" Das ist die Arraylist mit den Joblistings:" + list);
            linkedPage.extractInformation(list);
        }


    }

}







