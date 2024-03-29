import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Scanner;

public class SeleniumDrive {


    WebDriver webDriver;
    private Properties properties;

    public String login() throws InterruptedException, IOException {

        String url = "https://www.stepstone.de";
        String path = "C://Users//fhoti//Desktop//StepstoneScraper//chromedriver.exe";

        String company = System.getenv("Search.company");

        String mail = System.getenv("Stepstone.mail");                          // Envirement Varibles
        String stepstonePassword = System.getenv("Stepstone.password");

        System.setProperty("webdriver.chrome.driver", path);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        DesiredCapabilities cp = new DesiredCapabilities();
        cp.setCapability(ChromeOptions.CAPABILITY, options);
        options.merge(cp);

        webDriver = new ChromeDriver(options);
        webDriver.get(url);

        Thread.sleep(2000);
        WebElement accept_cookies = webDriver.findElement(By.id("ccmgt_explicit_accept"));
        accept_cookies.click();
        Thread.sleep(2000);
        String uppercase = company.toUpperCase();
        WebElement search = webDriver.findElement(By.xpath("(//input[@id='stepstone-autocomplete-162'])[1]"));
        search.sendKeys(uppercase);
        Thread.sleep(1000);
        WebElement dropmenu = webDriver.findElement(By.cssSelector("li[id='stepstone-autocomplete-162-item-0'] span[class='sbr-cojtx3']"));
        dropmenu.click();
        search.sendKeys(Keys.RETURN);

//        WebElement searchbox = webDriver.findElement(By.id("stepstone-menubar-38-5"));
//        searchbox.click();
//        WebElement login_menu = webDriver.findElement(By.className("hf-provider-1yjlzju"));
//        login_menu.click();
//        Thread.sleep(1000);


        /**
         * Login into Stepstone Account
         */
//        WebElement email = webDriver.findElement(By.name("email"));
//        WebElement password = webDriver.findElement(By.name("password"));
//
//        email.sendKeys(mail);
//        password.sendKeys(stepstonePassword);
//        Thread.sleep(2000);
//        WebElement login_button = webDriver.findElement(By.cssSelector("button[type='submit']"));
//        login_button.click();

        /**
         *  the Company to Search
         */
//        String uppercase = company.toUpperCase();
//        WebElement search_key = webDriver.findElement(By.xpath("(//input[@id='stepstone-autocomplete-162'])[1]"));
//        search_key.sendKeys(uppercase+" ");
//        Thread.sleep(2500);


        /**
         * dropmenu auswählen sont wird nichts ausgeführt
         */
//        WebElement dropmenu = webDriver.findElement(By.xpath("(//span[@class='sbr-cojtx3'])[1]"));
//        dropmenu.click();
//       // search_key.sendKeys(Keys.RETURN);
//        WebElement findJob_button = webDriver.findElement(By.cssSelector("button[aria-label='Jobs finden'] span[class='sbr-1jx3vjx']"));
//        findJob_button.click();

        Thread.sleep(1000);
        //  String current_url = null;

        String current_url = webDriver.getCurrentUrl();

        return current_url;
    }


}

