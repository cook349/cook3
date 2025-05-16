package org.example.cook1.AcceptanceTest;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions.SnippetType;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "cases",
        plugin = {"html:target/cucumber/wikipedia.html"},
        monochrome = true,
        snippets = SnippetType.CAMELCASE,
        glue = {"org.example.cook1.AcceptanceTest"},
        publish = true)

public class acceptanceTest {


}
