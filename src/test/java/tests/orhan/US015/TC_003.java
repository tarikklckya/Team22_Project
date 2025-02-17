package tests.orhan.US015;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.OrhanPage;
import utilities.ConfigReader;
import utilities.Driver;
import utilities.ReusableMethods;

public class TC_003 {
    /*
    1- Belirtilen URL' e gidilir
    2- Sign In butonuna tiklanir
    3- Kullanici bilgileri girilir
    4- My Account a tiklanir
    5- Store Manager a tiklanir
    6- Coupons basligina tiklanir
    7- Istenen coupon tiklanir
    8- Usage limit per user kutusuna email adres yazilir
    9- Submit butonuna basilir
    10- Submit oldugu test edilir
     */

    OrhanPage orhanPage = new OrhanPage();

    Actions actions = new Actions(Driver.getDriver());

    JavascriptExecutor jse = (JavascriptExecutor) Driver.getDriver();

    @Test
    public void testUsageLimitPerUser(){
        //1
        Driver.getDriver().get(ConfigReader.getProperty("url"));

        //2
        orhanPage.anaSayfaSignInLink.click();

        //3
        orhanPage.signInUsernameBox.sendKeys(ConfigReader.getProperty("userNameOrhan"));
        orhanPage.signInPasswordBox.sendKeys(ConfigReader.getProperty("passwordOrhan"));
        orhanPage.signInButton.click();


        //4
        Driver.getDriver().navigate().refresh();
        ReusableMethods.waitFor(3);
        jse.executeScript("arguments[0].scrollIntoView(true);",orhanPage.myAccountButton);
        jse.executeScript("arguments[0].click();",orhanPage.myAccountButton);


        //5
        orhanPage.storeManagerLink.click();

        //6
        orhanPage.cuponsLink.click();

        //7
        orhanPage.couponCodeC005Link.click();

        //8

        jse.executeScript("arguments[0].scrollIntoView(true);",orhanPage.logOutLink);
        actions.sendKeys(Keys.DOWN).sendKeys(Keys.DOWN).sendKeys(Keys.DOWN).sendKeys(Keys.DOWN).perform();
        ReusableMethods.waitForVisibility(orhanPage.limitLink,1000);
        jse.executeScript("arguments[0].click();",orhanPage.limitLink);

        orhanPage.usageLimitPerUserBox.clear();
        orhanPage.usageLimitPerUserBox.sendKeys("1");

        //9
        jse.executeScript("arguments[0].scrollIntoView(true);",orhanPage.cuoponManagerSubmitButton);
        ReusableMethods.waitForVisibility(orhanPage.cuoponManagerSubmitButton,1000);
        jse.executeScript("arguments[0].click();",orhanPage.cuoponManagerSubmitButton);

        //10
        ReusableMethods.waitForVisibility(orhanPage.successfullyScriptText,1500);
        String expectedText ="Coupon Successfully Published.";
        Assert.assertEquals(orhanPage.successfullyScriptText.getText(),expectedText);
    }
}
