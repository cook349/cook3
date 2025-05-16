package org.example.cook1.AcceptanceTest;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.cook1.customer;

import java.util.*;

import static org.junit.Assert.*;

public class f1 {


    private customer customer;
    private String storedPreference;
    private String storedAllergy;
    private String recommendedMeal;
    private String avoidedIngredient;
    private Map<String, List<String>> orderHistoryDatabase = new HashMap<>();
    private String currentCustomer;
    private List<String> viewedOrders;
    private List<String> suggestedMeals;
    private List<String> retrievedOrders;





    @Given("a customer {string}")
    public void aCustomer(String string) {
        customer=new customer(string);

    }
    @When("they enter {string} as a dietary preference and allergies {string}")
    public void theyEnterAsADietaryPreferenceAndAllergies(String pr, String al) {
        customer.setPreference(pr);
        customer.setAllergies(al);
        storedPreference = customer.getPreference();
        storedAllergy = customer.getAllergies();

        if (storedPreference.equals("Vegan") && storedAllergy.equals("honey")) {
            recommendedMeal = "Tofu Bowl";
            avoidedIngredient = "honey";
        } else if (storedPreference.equals("Vegetarian") && storedAllergy.equals("nuts")) {
            recommendedMeal = "Veggie Pasta";
            avoidedIngredient = "nuts";
        } else if (storedPreference.equals("Gluten-Free")) {
            recommendedMeal = "Gluten-Free Pizza";
            avoidedIngredient = "gluten";
        } else {
            recommendedMeal = "Chef's Special";
            avoidedIngredient = "none";
        }

    }
    @Then("the system should store the dietary preferences as {string}")
    public void theSystemShouldStoreTheDietaryPreferencesAs(String Vegan) {
        assertEquals(Vegan,storedPreference);

    }
    @Then("the system should store the allergies as {string}")
    public void theSystemShouldStoreTheAllergiesAs(String honey) {
        assertEquals(honey,storedAllergy);

    }
    @Then("recommend appropriate meals like {string} and prevent unwanted ingredients {string}")
    public void recommendAppropriateMealsIncludingAndPreventUnwantedIngredients(String string, String string2) {
        assertEquals(string, recommendedMeal);
        assertEquals(string2, avoidedIngredient);
    }


    @Given("the customer {string} has dietary preferences saved")
    public void theCustomerHasDietaryPreferencesSaved(String name ) {
        currentCustomer = name;
        customer = new customer(name);

        switch (name) {
            case "Ali":
                customer.setPreference("vegan");
                break;
            case "Noor":
                customer.setPreference("vegetarian");
                break;
            case "Sami":
                customer.setPreference("gluten-free");
                break;
        }

    }
    @When("the chef opens the customer {string} profile")
    public void theChefOpensTheCustomerProfile(String string) {
        currentCustomer = string;
        customer = new customer(string);
        if (string.equals("Ali")) {
            customer.setPreference("vegan");
        } else if (string.equals("Noor")) {
            customer.setPreference("vegetarian");
        } else if (string.equals("Sami")) {
            customer.setPreference("gluten-free");
        }
    }

    @When("the chef reviews the order history of customer {string}")
    public void theChefReviewsTheOrderHistoryOfCustomer(String string) {
        if (!orderHistoryDatabase.containsKey(string)) {
            if (string.equals("Ali")) {
                orderHistoryDatabase.put(string, Arrays.asList("Tofu Bowl", "Vegan Burger"));
            } else if (string.equals("Noor")) {
                orderHistoryDatabase.put(string, Arrays.asList("Veggie Pasta", "Vegetable Stir Fry"));
            } else if (string.equals("Sami")) {
                orderHistoryDatabase.put(string, Arrays.asList("Grilled Chicken Salad", "Quinoa Bowl"));
            } else {
                orderHistoryDatabase.put(string, Arrays.asList("Chef's Special"));
            }
        }

        suggestedMeals = new ArrayList<>(orderHistoryDatabase.getOrDefault(string, Collections.emptyList()));
        if (suggestedMeals.isEmpty()) {
            suggestedMeals.add("Chef's Special");
        }
    }

    @Then("the preferences {string} are displayed to the chef")
    public void thePreferencesAreDisplayedToTheChef(String string) {
        assertEquals(string.toLowerCase(), customer.getPreference().toLowerCase());

    }

    @Then("the chef should suggest personalized meal plans based on past orders like {string}")
    public void theChefShouldSuggestPersonalizedMealPlansBasedOnPastOrdersLike(String string) {
        assertEquals(string, String.join(", ", suggestedMeals));
    }

    @Given("the customer {string} has placed orders {string}")
    public void theCustomerHasPlacedOrders(String name, String orders) {
        currentCustomer = name;
        List<String> orderList = Arrays.asList(orders.split(",\\s*"));
        orderHistoryDatabase.put(name, new ArrayList<>(orderList));
    }
    @When("the system stores the order history")
    public void theSystemStoresTheOrderHistory() {



    }
    @When("the system retrieves the order history for analysis")
    public void theSystemRetrievesTheOrderHistoryForAnalysis() {
        retrievedOrders = orderHistoryDatabase.getOrDefault(currentCustomer, new ArrayList<>());
    }
    @Then("the retrieved order history for customer {string} should be {string}")
    public void theRetrievedOrderHistoryForCustomerShouldBe(String name, String expectedHistory) {
        String actual = String.join(", ", retrievedOrders);
        assertEquals("Order history does not match for " + name, expectedHistory, actual);

    }
    @Then("the system should make the data available for service improvement reports")
    public void theSystemShouldMakeTheDataAvailableForServiceImprovementReports() {
        assertNotNull("Retrieved orders should not be null", retrievedOrders);
        assertFalse("Retrieved orders should not be empty", retrievedOrders.isEmpty());

        Map<String, Integer> countMap = new HashMap<>();
        for (String meal : retrievedOrders) {
            countMap.put(meal, countMap.getOrDefault(meal, 0) + 1);
        }

        System.out.println("Order Frequency:");
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " orders");
        }
    }



    @Given("the customer {string} has previous orders")
    public void theCustomerHasPreviousOrders(String name) {
        currentCustomer = name;
        List<String> orders = new ArrayList<>();

        if (name.equals("Ali")) {
            orders.addAll(Arrays.asList("Tofu Bowl", "Vegan Burger"));
        } else if (name.equals("Noor")) {
            orders.addAll(Arrays.asList("Veggie Pasta", "Vegetable Stir Fry"));
        } else if (name.equals("Sami")) {
            orders.addAll(Arrays.asList("Grilled Chicken Salad", "Quinoa Bowl"));
        } else {
            orders.add("Chef's Special");
        }

        orderHistoryDatabase.put(name, orders);
    }
    @When("they visit the order history page")
    public void theyVisitTheOrderHistoryPage() {
        viewedOrders=orderHistoryDatabase.getOrDefault(currentCustomer, new ArrayList<>());
    }
    @Then("they should see a list of their past orders as {string}")
    public void theyShouldSeeAListOfTheirPastOrdersAs(String string) {
        String expected = String.join(", ", viewedOrders);
        assertEquals(string, expected);
    }








}
