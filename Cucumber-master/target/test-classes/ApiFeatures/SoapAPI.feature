@API @SoapApi
Feature: SoapAPI

#  @soapapi @calcapi
#  Scenario Outline: Add Numbers using Calculator
#    Given I read the Xml file "CalculatorAdd.xml", modify "<itemA>" and "<itemB>"
#    And I invoke SOAP call "POST" on "http://www.dneonline.com/calculator.asmx"
#    Then I verify Soap ResponseCode=200
#    And I verify in Soap response "AddResult"="45"
#
#    Examples:
#      | itemA | itemB |
#      | 23    | 22    |

  @soapapi @DataGeneratorTPD3-1
  Scenario Outline: Get logs and check them
    Given I read the Xml file "DataGeneratorTPD3-1.xml", modify "<month>" and "<day>" and "<hours>" and "<mins>"
    And  I invoke DataGenerator TPD 3-1 SOAP call "POST" on "https://preprod.itg.plus.movilizer.cloud/v3/misl/epcis/capture"
    Then I verify Soap ResponseCode=200

    Examples:
      | month | day | hours | mins |
      | 05    | 28  | 00    | 00   |