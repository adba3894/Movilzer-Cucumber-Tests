package Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;


import org.openqa.selenium.UnexpectedAlertBehaviour;

public class WebdriverFactory implements IWebdriverFactory {

    DIContext scenarioContext;

    public WebdriverFactory(DIContext context) {
        this.scenarioContext = context;
    }

    /*
     * Reads the property value of browser from config.properties file
     */
    private String readBrowserProperty() throws FileNotFoundException, IOException {
        String browser;
        Properties prop = new Properties();
        this.scenarioContext.GetExtentTest().info("Trying to read properties file");
        try (InputStream iStr = new FileInputStream(System.getProperty("user.dir") + "\\config.properties")) {
            prop.load(iStr);
            browser = prop.getProperty("browser");
        }
        return browser;
    }

    @Override
    public void GetDriver() throws FileNotFoundException, IOException {
        WebDriver driver;
        String browser = readBrowserProperty();
        this.scenarioContext.GetExtentTest().info("Read Properties File Browser set to :" + browser);
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("start-maximized");
        chromeOptions.addArguments("disable-infobars");
        chromeOptions.setHeadless(false);
        this.scenarioContext.GetExtentTest().info("bianry set to :" + System.getProperty("user.dir") + "\\Binaries\\Chrome81\\Application\\chrome.exe");
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\Binaries\\chromedriver_win32\\chromedriver.exe");
        this.scenarioContext.GetExtentTest().info("webdriver.chrome.driver set at :" + System.getProperty("webdriver.chrome.driver"));
        driver = new ChromeDriver(chromeOptions);
        this.scenarioContext.GetExtentTest().info("Created Chrome Driver");
		this.scenarioContext.SetWebDriver(driver);
        this.scenarioContext.GetExtentTest().info("Driver object added to Scenario Context");
    }

    @Override
    public void DisposeDriver() {
        this.scenarioContext.GetWebDriver().close();
        this.scenarioContext.GetExtentTest().info("WebDriver Closed");
        this.scenarioContext.GetWebDriver().quit();
        this.scenarioContext.GetExtentTest().info("WebDriver Quit!");
    }
}
