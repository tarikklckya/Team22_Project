package utilities;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.SourceType;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Thread.sleep;

public class ReusableMethods {
    //========ScreenShot(Sayfanın resmini alma)=====//
    public static String getScreenshot(String name) throws IOException {
        // naming the screenshot with the current date to avoid duplication
        String date = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        // TakesScreenshot is an interface of selenium that takes the screenshot
        TakesScreenshot ts = (TakesScreenshot) Driver.getDriver();
        File source = ts.getScreenshotAs(OutputType.FILE);
        // full path to the screenshot location
        String target = System.getProperty("user.dir") + "/target/Screenshots/" + name + date + ".png";
        File finalDestination = new File(target);
        // save the screenshot to the path given
        FileUtils.copyFile(source, finalDestination);
        return target;
    }
    //========ScreenShot Web Element(Bir webelementin resmini alma)=====//
    public static String getScreenshotWebElement(String name,WebElement element) throws IOException {
        String date = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        // TakesScreenshot is an interface of selenium that takes the screenshot
        File source = element.getScreenshotAs(OutputType.FILE);
        // full path to the screenshot location
        String wElementSS = System.getProperty("user.dir") + "/target/WElementScreenshots/" + name + date + ".png";
        File finalDestination = new File(wElementSS);
        // save the screenshot to the path given
        FileUtils.copyFile(source, finalDestination);
        return  wElementSS;
    }

    //========Switching Window(Pencereler arası geçiş)=====//
    public static void switchToWindow(String targetTitle) {
        String origin = Driver.getDriver().getWindowHandle();
        for (String handle : Driver.getDriver().getWindowHandles()) {
            Driver.getDriver().switchTo().window(handle);
            if (Driver.getDriver().getTitle().equals(targetTitle)) {
                return;
            }
        }
        Driver.getDriver().switchTo().window(origin);
    }

    //========Hover Over(Elementin üzerinde beklemek)=====//
    public static void hover(WebElement element) {
        Actions actions = new Actions(Driver.getDriver());
        actions.moveToElement(element).perform();
    }

    //==========Return a list of string given a list of Web Element====////
    public static List<String> getElementsText(List<WebElement> list) {
        List<String> elemTexts = new ArrayList<>();
        for (WebElement el : list) {
            if (!el.getText().isEmpty()) {
                elemTexts.add(el.getText());
            }
        }
        return elemTexts;
    }

    //========Returns the Text of the element given an element locator==//
    public static List<String> getElementsText(By locator) {
        List<WebElement> elems = Driver.getDriver().findElements(locator);
        List<String> elemTexts = new ArrayList<>();
        for (WebElement el : elems) {
            if (!el.getText().isEmpty()) {
                elemTexts.add(el.getText());
            }
        }
        return elemTexts;
    }

    //   HARD WAIT WITH THREAD.SLEEP
//   waitFor(5);  => waits for 5 second
    public static void waitFor(int sec) {
        try {
            sleep(sec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //===============Explicit Wait==============//
    public static WebElement waitForVisibility(WebElement element, int timeout) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public static WebElement waitForVisibility(By locator, int timeout) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickablility(WebElement element, int timeout) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public static WebElement waitForClickablility(By locator, int timeout) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static void clickWithTimeOut(WebElement element, int timeout) {
        for (int i = 0; i < timeout; i++) {
            try {
                element.click();
                return;
            } catch (WebDriverException e) {
                waitFor(1);
            }
        }
    }

    public static void waitForPageToLoad(long timeout) {
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            }
        };
        try {
            System.out.println("Waiting for page to load...");
            WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
            wait.until(expectation);
        } catch (Throwable error) {
            System.out.println(
                    "Timeout waiting for Page Load Request to complete after " + timeout + " seconds");
        }
    }

    //---------------------------FILE DOWNLOADED CHECK----------------------------//

    public static boolean isFileDownloaded(String filePath,String fileName) throws Exception {
        final int SLEEP_TIME_MILLIS = 1;
        File file = new File(filePath);
        final int timeout = 10* SLEEP_TIME_MILLIS;
        int timeElapsed = 0;
        while (timeElapsed<timeout){
            if (file.getName().equals(fileName)) {
                System.out.println(fileName + " is present");
                return true;
            } else {
                timeElapsed +=SLEEP_TIME_MILLIS;
                sleep(SLEEP_TIME_MILLIS);
            }
        }
        return false;
    }

    public static void fileNameWrittenExtensionPDF(String release,String expectedFileName) {
        System.out.println(("searching for configuration files in folder " + release));
        Path releaseFolder = Paths.get(release);
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(releaseFolder, "*.pdf")) {

            for (Path entry: stream){
                if (entry.getFileName().toString().contains(expectedFileName)) {
                  System.out.println(("working on file " + entry.getFileName()));
                }
            }
        }
        catch (IOException e){
            System.out.println(("error while retrieving update configuration files " + e.getMessage()));
        }
    }

    public static void fileNameWrittenExtensionEXCEL(String release,String expectedFileName) {
        System.out.println(("searching for configuration files in folder " + release));
        Path releaseFolder = Paths.get(release);
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(releaseFolder, "*.xlsx")) {

            for (Path entry: stream){
                if (entry.getFileName().toString().contains(expectedFileName)) {
                    System.out.println(("working on file " + entry.getFileName()));
                }
            }
        }
        catch (IOException e){
            System.out.println(("error while retrieving update configuration files " + e.getMessage()));
        }
    }

    public static void fileNameWrittenExtensionCSV(String release,String expectedFileName) {
        System.out.println(("searching for configuration files in folder " + release));
        Path releaseFolder = Paths.get(release);
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(releaseFolder, "*.csv")) {

            for (Path entry: stream){
                if (entry.getFileName().toString().contains(expectedFileName)) {
                    System.out.println(("working on file " + entry.getFileName()));
                }
            }
        }
        catch (IOException e){
            System.out.println(("error while retrieving update configuration files " + e.getMessage()));
        }
    }

}

