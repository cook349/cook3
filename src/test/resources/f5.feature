Feature: Billing and Financial Management

  Scenario Outline: Issue invoice to customer after purchase
    Given the customer "<Name>" has finalized an order with reference "<OrderID>"
    When the system creates the invoice
    Then the invoice details should include:
      | Order Reference | <OrderID>         |
      | Customer Name   | <Name>            |
      | Total Amount    | <Amount>          |
      | VAT Applied     | <VATAmount>       |
      | Number of Items | <ItemCount> items |
    And the invoice should be emailed to "<Email>"

    Examples:
      | Name   | OrderID | Amount | VATAmount | ItemCount | Email                 |
      | Ali    | #1001   | 25.00  | 2.50      | 3         | ali@example.com       |
      | Sarah  | #1002   | 42.75  | 4.28      | 5         | sarah@business.com    |

  Scenario Outline: Provide financial summary reports to administrator
    Given administrator "<AdminName>" requests the "<ReportType>" report covering "<Period>"
    When the system processes financial records
    Then the report should present:
      | Reporting Period | <Period>    |
      | Revenue Total    | <Revenue>   |
      | Orders Processed | <OrderCount>|
      | Top-Selling Item | <TopItem>   |
    And the report is saved as a "<FileFormat>" file

    Examples:
      | AdminName | ReportType | Period    | Revenue   | OrderCount | TopItem           | FileFormat |
      | Admin1    | Monthly    | Jan-2024  | 8500.00   | 120        | Vegan Pasta      | PDF        |
      | Admin2    | Quarterly  | Q1-2024   | 24500.00  | 350        | Gluten-Free Pizza| CSV        |




  Scenario Outline: Handle report generation failures
    Given administrator "<AdminName>" requests the "<ReportType>" report covering "<Period>"
    When the system cannot find financial records for the period
    Then the report should present a:

      | Reporting Period   | <Period>            |
      | Status             | Failed            |
      | Error              | Period not found  |
    And the error report is saved as a "<FileFormat>" file

    Examples:
      | AdminName | ReportType | Period    | FileFormat |
      | Admin3    | Monthly    | Feb-2025  | JSON       |
      | Admin4    | Quarterly  | Q3-2025   | PDF        |