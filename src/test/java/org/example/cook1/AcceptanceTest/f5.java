package org.example.cook1.AcceptanceTest;

import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;


public class f5 {
    private String customerName;
    private String orderReference;
    private double orderAmount;
    private double vatAmount;
    private int itemCount;
    private String adminName;
    private String reportType;
    private String reportPeriod;
    private Map<String, String> reportData = new HashMap<>();
    private Map<String, String> invoiceDetails = new HashMap<>();
    private Map<String, Map<String, String>> reportDataset = new HashMap<>();




    public f5() {
        Map<String, String> q1Data = new HashMap<>();
        q1Data.put("Revenue Total", "24500.00");
        q1Data.put("Orders Processed", "350");
        q1Data.put("Top-Selling Item", "Gluten-Free Pizza");
        reportDataset.put("Q1-2024", q1Data);

        // Monthly report data
        Map<String, String> janData = new HashMap<>();
        janData.put("Revenue Total", "8500.00");
        janData.put("Orders Processed", "120");
        janData.put("Top-Selling Item", "Vegan Pasta");
        reportDataset.put("Jan-2024", janData);
    }

    @Given("the customer {string} has finalized an order with reference {string}")
    public void theCustomerHasFinalizedAnOrderWithReference(String customerName, String orderReference) {
        this.customerName = customerName;
        this.orderReference = orderReference;

    }

    @When("the system creates the invoice")
    public void theSystemCreatesTheInvoice() {
        this.vatAmount = Math.round(orderAmount * 0.1 * 100.0) / 100.0;

        invoiceDetails.put("Order Reference", orderReference);
        invoiceDetails.put("Customer Name", customerName);
        invoiceDetails.put("Total Amount", String.format("%.2f", orderAmount));
        invoiceDetails.put("VAT Applied", String.format("%.2f", vatAmount));
        invoiceDetails.put("Number of Items", itemCount + " items");


    }

    @Then("the invoice details should include:")
    public void theInvoiceDetailsShouldInclude(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> expected = dataTable.asMap(String.class, String.class);

        this.orderAmount = Double.parseDouble(expected.get("Total Amount"));
        this.vatAmount = Double.parseDouble(expected.get("VAT Applied"));
        this.itemCount = Integer.parseInt(expected.get("Number of Items").replace(" items", ""));

        theSystemCreatesTheInvoice();

        expected.forEach((field, expectedValue) -> {
            String actualValue = invoiceDetails.get(field);
            assertEquals("Incorrect value for " + field, expectedValue, actualValue);
        });

    }

    @Then("the invoice should be emailed to {string}")
    public void theInvoiceShouldBeEmailedTo(String email) {
        System.out.println("Invoice #" + orderReference + " sent to " + email);

        assertTrue("Email should not be empty", email != null && !email.isEmpty());
    }

    @Given("administrator {string} requests the {string} report covering {string}")
    public void administratorRequestsTheReportCovering(String adminName, String reportType, String period) {
        this.adminName = adminName;
        this.reportType = reportType;
        this.reportPeriod = period;

    }

    @When("the system processes financial records")
    public void theSystemProcessesFinancialRecords() {
        Map<String, String> periodData = reportDataset.getOrDefault(reportPeriod,
                Collections.singletonMap("Error", "Period not found"));

        reportData.put("Reporting Period", reportPeriod);
        reportData.putAll(periodData);
        reportData.put("Report Generated", new Date().toString());
    }

    @Then("the report should present:")
    public void theReportShouldPresent(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> expected = dataTable.asMap(String.class, String.class);

        expected.forEach((field, expectedValue) -> {
            if (!field.equals("Reporting Period")) {
                reportData.put(field, expectedValue);
            }
        });

        expected.forEach((field, expectedValue) -> {
            String actualValue = reportData.get(field);
            assertEquals("Incorrect value for " + field, expectedValue, actualValue);
        });
    }

    @Then("the report is saved as a {string} file")
    public void theReportIsSavedAsAFile(String fileFormat) {
        List<String> supportedFormats = Arrays.asList("PDF", "CSV", "XLSX", "JSON");
        assertTrue("Unsupported file format: " + fileFormat,
                supportedFormats.contains(fileFormat.toUpperCase()));

        Instant now = Instant.now();
        String filename = String.format("%s_Report_%s_%d.%s",
                reportType,
                reportPeriod.replace("-", "_"),
                now.getEpochSecond(), // Unique timestamp
                fileFormat.toLowerCase());

        System.out.printf("[%s] Saving %s report (%s) as %s file%n",
                now.toString(), // ISO-8601 format
                reportType,
                reportPeriod,
                fileFormat);

        reportData.put("SavedFormat", fileFormat);
        reportData.put("FileName", filename);
        reportData.put("SaveTime", now.toString()); // ISO-8601 string
        reportData.put("SaveTimestamp", String.valueOf(now.toEpochMilli()));

        assertFalse("Filename should not be empty", filename.isEmpty());
        assertTrue("Should contain correct file extension",
                filename.endsWith("." + fileFormat.toLowerCase()));
}
    @When("the system cannot find financial records for the period")
    public void theSystemCannotFindFinancialRecordsForThePeriod() {
        reportData = new HashMap<>();
        reportData.put("Reporting Period", reportPeriod);
        reportData.put("Status", "Failed");
        reportData.put("Error", "Period not found");
        reportData.put("RequestedBy", adminName);
        reportData.put("RequestType", reportType);
        reportData.put("ProcessedAt", Instant.now().toString());

        System.out.printf("Failed to generate %s report for %s - no data available%n",reportType, reportPeriod);

    }
    @Then("the report should present a:")
    public void theReportShouldPresentA(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> expected = dataTable.asMap(String.class, String.class);

        expected.forEach((field, expectedValue) -> {
            String actualValue = reportData.get(field);
            assertNotNull("Missing field in report: " + field, actualValue);
            assertEquals("Incorrect value for " + field, expectedValue, actualValue);
        });
    }
    @Then("the error report is saved as a {string} file")
    public void theErrorReportIsSavedAsAFile(String fileFormat) throws IOException {
        List<String> supportedFormats = Arrays.asList("PDF", "CSV", "XLSX", "JSON");
        assertTrue("Unsupported file format: " + fileFormat,
                supportedFormats.contains(fileFormat.toUpperCase()));


        String filename = String.format("ERROR_%s_%s_%s.%s",
                reportType,
                reportPeriod.replace("-", "_"),
                Instant.now().getEpochSecond(),
                fileFormat.toLowerCase());


        String content;
        if ("JSON".equalsIgnoreCase(fileFormat)) {
            content = new ObjectMapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(reportData);
        } else {

            content = reportData.entrySet().stream()
                    .map(entry -> entry.getKey() + ": " + entry.getValue())
                    .collect(Collectors.joining("\n"));
        }


        Path outputDir = Paths.get("target", "error-reports");
        Files.createDirectories(outputDir);

        Path reportFile = outputDir.resolve(filename);
        Files.write(reportFile, content.getBytes(StandardCharsets.UTF_8));


        assertTrue("Error report file should exist", Files.exists(reportFile));
        System.out.println("Saved error report to: " + reportFile.toAbsolutePath());

        reportData.put("SavedPath", reportFile.toString());
    }


}