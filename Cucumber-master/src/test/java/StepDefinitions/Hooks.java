package StepDefinitions;

import java.io.FileNotFoundException;
import java.io.IOException;
import com.aventstack.extentreports.ExtentTest;
import Utils.DIContext;
import Utils.ExtentManager;
import Utils.WebdriverFactory;
import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {

    DIContext scenarioContext;
    ExtentTest test;
    WebdriverFactory driverFactory;

    public Hooks(DIContext context) {
        this.scenarioContext = context;
    }

    @Before("@API")
    public void beforeHooks(Scenario scenario) throws Exception {
        System.out.println("Scenario ID :" + scenario.getId());
        this.test = this.scenarioContext.GetExtentTest(ExtentManager.ExtentReportsInstance(), scenario.getName());
        System.out.println("Extent Test Object stored in Scenario context!");
        this.test.info("INSIDE BEFORE API HOOKS!! Scenario name :" + scenario.getName());
    }

    @Before("@UI")
    public void beforeUIHooks(Scenario scenario) throws Exception {
        this.test = this.scenarioContext.GetExtentTest(ExtentManager.ExtentReportsInstance(), scenario.getName());
        System.out.println("Extent Test Object stored in Scenario context!");
        try {
            driverFactory = new WebdriverFactory(this.scenarioContext);
            driverFactory.GetDriver();
        } catch (FileNotFoundException e) {
            this.test.error("Exception caught from WebdriverFactory!" + e.getMessage());
            throw e;
        } catch (IOException e) {
            this.test.error("Exception caught from WebdriverFactory!" + e.getMessage());
            throw e;
        }
        this.test.info("INSIDE BEFORE UI HOOKS!! Scenario name :" + scenario.getName());
    }


    @After("@UI")
    public void afterUIHooks(Scenario scenario) {
        driverFactory.DisposeDriver();
        this.test.info("INSIDE AFTER UI HOOKS!! Scenario name :" + scenario.getName());
    }

    @After
    public void afterHooks(Scenario scenario) {

        this.test.info("INSIDE AFTER HOOKS NOW!!  Scenario name: " + scenario.getName());
    }

}
