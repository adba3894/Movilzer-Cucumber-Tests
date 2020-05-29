package StepDefinitions;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import io.cucumber.java8.En;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.*;

import Api.Helpers.XmlHelper;
import Utils.DIContext;

import static org.assertj.core.api.Assertions.*;

public class SoapAPISteps implements En {
    DIContext scenarioContext;
    XmlHelper xhelper;

    public SoapAPISteps(DIContext context) {
        this.scenarioContext = context;
    }

    @Given("I read the Xml file {string}, modify {string} and {string}")
    public void IReadTheXmlFileModify(String fileName, String itmAVal, String itmBVal) {
        try {
            File xmlFile = new File(System.getProperty("user.dir") + "//src//test//java//Api//Templates//" + fileName);
            xhelper = new XmlHelper();
            Document xdoc = xhelper.CreateXDocument(xmlFile);
            xhelper.SetNodeValue(xdoc, "/ns:soapenv:Envelope/ns:soapenv:Body/ns:tem:Add/ns:tem:intA/text()", itmAVal);
            xhelper.SetNodeValue(xdoc, "/ns:soapenv:Envelope/ns:soapenv:Body/ns:tem:Add/ns:tem:intB/text()", itmBVal);
            String xmlContent = xhelper.ConvertToString(xdoc);
            this.scenarioContext.SetXmlRequest(xmlContent);
            xhelper.dispose();
            this.scenarioContext.GetExtentTest().pass("I read the Xml file {string}, modify {string} and {string}");
        } catch (Exception e) {
            this.scenarioContext.IncrementStepErrorCount(e);
        }
    }

    @Given("I read the Xml file {string}")
    public void IReadTheXmlFile(String fileName) {
        try {
            File xmlFile = new File(System.getProperty("user.dir") + "//src//test//java//Api//Templates//" + fileName);
            xhelper = new XmlHelper();
            xhelper.CreateXDocument(xmlFile);
            this.scenarioContext.GetExtentTest().pass("I read the Xml file {string}, modify {string} and {string}");
        } catch (Exception e) {
            this.scenarioContext.IncrementStepErrorCount(e);
        }
    }

    @Given("I select for nodes with filterxpath = {string} to verify {string} are present")
    public void ISelectNodesWithFilterpathToVerify(String queryXpath, String values) {
        try {
            List<String> valuesToVerify = Arrays.stream(values.split(",")).collect(Collectors.toList());
            List<String> valuesReceived = xhelper.GetNodeValues(xhelper.GetXDocument(), queryXpath);
            assertThat(valuesReceived).containsAll(valuesToVerify);
            this.scenarioContext.GetExtentTest()
                    .pass("I select for nodes with filterxpath = {string} to verify {string} are present");
        } catch (Exception e) {
            this.scenarioContext.IncrementStepErrorCount(e);
        }
    }

    @Given("I invoke SOAP call {string} on {string}")
    public void IinvokeSoapCall(String method, String url) {
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(new URI(url));
            request.addHeader("Content-Type", "text/xml");
            request.setEntity(new StringEntity(this.scenarioContext.GetXmlRequest()));
            CloseableHttpResponse response = client.execute(request);
            this.scenarioContext.SetSoapResponse(response);
            this.scenarioContext.GetExtentTest().pass("I invoke SOAP call {string} on {string}");
        } catch (Exception e) {
            this.scenarioContext.IncrementStepErrorCount(e);
        }
    }

    @Given("I invoke DataGenerator TPD 3-1 SOAP call {string} on {string}")
    public void IinvokeSoapCallForDataGeneratorTPD31(String method, String url) {
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(new URI(url));
//            Headers Of Postman Script
//            request.addHeader("Postman-Token", "<calculated when request is sent>");
//            request.addHeader("Content-Length", "<calculated when request is sent>");
//            request.addHeader("Host", "<calculated when request is sent>");
            request.addHeader("Content-Type", "text/plain");
            request.addHeader("User-Agent", "PostmanRuntime/7.25.0");
            request.addHeader("Accept", "*/*");
            request.addHeader("Accept-Encoding", "gzip, deflate, br");
            request.addHeader("Connection", "keep-alive");
            request.addHeader("Content-Type", "application/xml");
            request.setEntity(new StringEntity(this.scenarioContext.GetXmlRequest()));
            CloseableHttpResponse response = client.execute(request);
            this.scenarioContext.SetSoapResponse(response);
            this.scenarioContext.GetExtentTest().pass("I invoke DataGenerator TPD 3-1 SOAP call {string} on {string}");
        } catch (Exception e) {
            this.scenarioContext.IncrementStepErrorCount(e);
        }
    }

    @Then("I verify Soap ResponseCode={int}")
    public void IVerifyResponseCode(int respCode) {
        try {
            int code = this.scenarioContext.GetSoapResponse().getStatusLine().getStatusCode();
            assertThat(respCode).isEqualTo(code);
            this.scenarioContext.GetExtentTest().pass("I verify Soap ResponseCode={int}");
        } catch (Exception e) {
            this.scenarioContext.IncrementStepErrorCount(e);
        }
    }

    @Then("I verify in Soap response {string}={string}")
    public void IVerifySoapResponse(String field, String value) {
        try {
            String xmlFromResponse = EntityUtils.toString(this.scenarioContext.GetSoapResponse().getEntity());
            xhelper = new XmlHelper();
            Document xRespDoc = xhelper.CreateXDocument(xmlFromResponse);
            String actualVal = xhelper.GetNodeValues(xRespDoc,
                    "/ns:soap:Envelope/ns:soap:Body/ns:tem:AddResponse/ns:tem:" + field + "/text()").get(0);
            assertThat(actualVal).isEqualTo(value);
            this.scenarioContext.GetExtentTest().pass("I verify in Soap response {string}={string}");
            xhelper.dispose();
        } catch (Exception e) {
            this.scenarioContext.IncrementStepErrorCount(e);
        }
    }

    public String replacePlaceholders(String placeholderToReplace, String valueToSetToPlaceholder, String context) {
        context = context.replace(placeholderToReplace, valueToSetToPlaceholder);
        return context;
    }


    @Given("I read the Xml file {string}, modify {string} and {string} and {string} and {string}")
    public void iReadTheXmlFileModifyAndAndAnd(String fileName, String month, String day, String hours, String mins) {
        Date date = new Date();
        month = new SimpleDateFormat("MM").format(date);
        day = new SimpleDateFormat("dd").format(date);
        hours = new SimpleDateFormat("HH").format(date);
        mins = new SimpleDateFormat("mm").format(date);

        try {
            File xmlFile = new File(System.getProperty("user.dir") + "//src//test//java//Api//Templates//" + fileName);
            xhelper = new XmlHelper();
            Document xdoc = xhelper.CreateXDocument(xmlFile);
            String xmlContent = xhelper.ConvertToString(xdoc);
            xmlContent = replacePlaceholders("{Month}", month, xmlContent);
            xmlContent = replacePlaceholders("{Day}", day, xmlContent);
            xmlContent = replacePlaceholders("{Hours}", hours, xmlContent);
            xmlContent = replacePlaceholders("{Mins}", mins, xmlContent);
            this.scenarioContext.SetXmlRequest(xmlContent);
            xhelper.dispose();
            this.scenarioContext.GetExtentTest().pass("I read the Message XML file {string}, modify {string} and {string} and {string} and {string}");
        } catch (Exception e) {
            this.scenarioContext.IncrementStepErrorCount(e);
        }
    }

}
