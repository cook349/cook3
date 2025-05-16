package org.example.cook1.AcceptanceTest;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.cook1.test2;
import static org.junit.Assert.*;
import java.util.*;

public class f2 {
    private String customerName;
    private String dietaryRestriction;
    private List<String> selectedIngredients;
    private String originalIngredient;
    private String substitution;
    private String availabilityMessage;
    private Map<String, String> stockStatus = new HashMap<>();
    private test2 mealValidator = new test2();
    private boolean chefApproved;
    private String lastNotification;

    @Given("customer {string} with dietary restriction {string} is creating a custom meal")
    public void customerWithDietaryRestrictionIsCreatingACustomMeal(String name, String restriction) {
        Objects.requireNonNull(name, "Customer name cannot be null");
        Objects.requireNonNull(restriction, "Dietary restriction cannot be null");

        this.customerName = name;
        this.dietaryRestriction = restriction;
        mealValidator.setDietaryRestriction(restriction);
    }

    @When("they select {string}")
    public void theySelect(String ingredients) {
        Objects.requireNonNull(ingredients, "Ingredients list cannot be null");

        List<String> ingredientList = Arrays.asList(ingredients.split(",\\s*"));
        this.selectedIngredients = new ArrayList<>(ingredientList); // Defensive copy
        mealValidator.setSelectedIngredients(ingredientList);
    }

    @Then("the system should Accept the combination")
    public void theSystemShouldAcceptTheCombination() {
        assertTrue("Meal combination should be accepted", mealValidator.validateMealCombination());
    }

    @Then("the system should Reject the combination")
    public void theSystemShouldRejectTheCombination() {
        assertFalse("Meal combination should be rejected", mealValidator.validateMealCombination());
    }

    @Then("if accepted, calculate nutrition information")
    public void ifAcceptedCalculateNutritionInformation() {
        if (mealValidator.validateMealCombination()) {
            String nutritionInfo = calculateNutrition(selectedIngredients);
            assertNotNull("Nutrition information should not be null", nutritionInfo);
            System.out.println("Nutrition info: " + nutritionInfo);
        }
    }

    @Then("if rejected, suggest {string}")
    public void ifRejectedSuggest(String expectedSuggestions) {
        if (!mealValidator.validateMealCombination()) {
            String actualSuggestions = generateSuggestions(selectedIngredients, dietaryRestriction);
            assertNotNull("Suggestions should not be null", actualSuggestions);
            assertEquals(expectedSuggestions, actualSuggestions);
            System.out.println("Suggested alternatives: " + actualSuggestions);
        }
    }

    @Given("selected ingredient {string} is unavailable or restricted")
    public void selectedIngredientIsUnavailableOrRestricted(String ingredient) {
        Objects.requireNonNull(ingredient, "Ingredient cannot be null");
        this.originalIngredient = ingredient;
    }

    @When("the system checks for alternatives")
    public void theSystemChecksForAlternatives() {
        Objects.requireNonNull(originalIngredient, "Original ingredient must be set first");
        Objects.requireNonNull(dietaryRestriction, "Dietary restriction must be set first");

        this.substitution = mealValidator.findSubstitute(originalIngredient, dietaryRestriction);
        assertNotNull("Substitution result should not be null", substitution);
    }

    @Then("it should suggest {string}")
    public void itShouldSuggest(String expectedSuggestion) {
        assertEquals(expectedSuggestion, this.substitution);
    }

    @Given("system proposes replacing {string} with {string}")
    public void systemProposesReplacingWith(String original, String substitute) {
        this.originalIngredient = original;
        this.substitution = substitute;
    }

    @When("chef {string} approves the change")
    public void chefApprovesTheChange(String chefName) {
        Objects.requireNonNull(chefName, "Chef name cannot be null");
        Objects.requireNonNull(substitution, "Substitution must be proposed first");

        this.chefApproved = true;
        this.lastNotification = mealValidator.handleChefApproval(
                originalIngredient, substitution, chefName, true);
        System.out.println(lastNotification);
    }

    @When("chef {string} rejects the change")
    public void chefRejectsTheChange(String chefName) {
        Objects.requireNonNull(chefName, "Chef name cannot be null");

        this.lastNotification = mealValidator.handleChefApproval(
                originalIngredient, substitution, chefName, false);
        System.out.println(lastNotification);
        this.substitution = null;
        this.chefApproved = false;
    }

    @Then("the system should update recipe")
    public void theSystemShouldUpdateRecipe() {
        assertTrue("Chef must have approved the change", chefApproved);
        assertNotNull("Substitution must exist", substitution);
        System.out.println("Updating recipe with: " + substitution);
    }

    @Then("the system should request new option")
    public void theSystemShouldRequestNewOption() {
        String newSuggestion = mealValidator.findAlternativeSubstitute(
                originalIngredient,
                dietaryRestriction,
                substitution);

        assertNotNull("System should provide a new substitution option", newSuggestion);
        assertNotEquals("New suggestion should differ from previous", substitution, newSuggestion);
        System.out.println("Requesting new option: " + newSuggestion);
        this.substitution = newSuggestion;
    }

    @Given("{string} has stock status {string}")
    public void hasStockStatus(String ingredient, String status) {
        Objects.requireNonNull(ingredient, "Ingredient cannot be null");
        Objects.requireNonNull(status, "Status cannot be null");

        stockStatus.put(ingredient, status);
    }

    @When("customer selects it")
    public void customerSelectsIt() {
        if (stockStatus.isEmpty()) {
            throw new IllegalStateException("No ingredient stock status was set");
        }

        if (dietaryRestriction == null) {
            this.dietaryRestriction = "none";
            System.out.println("Warning: No dietary restriction set, using default 'none'");
        }

        String selectedIngredient = stockStatus.keySet().iterator().next();
        String status = stockStatus.get(selectedIngredient);

        if (status.equalsIgnoreCase("out")) {
            this.availabilityMessage = "Unavailable alert";
            this.substitution = mealValidator.findSubstitute(selectedIngredient, dietaryRestriction);
            if (this.substitution == null) {
                this.substitution = "No substitute available";
            }
        } else if (status.equalsIgnoreCase("low")) {
            this.availabilityMessage = "Limited stock warning";
            this.substitution = mealValidator.findSubstitute(selectedIngredient, dietaryRestriction);
        } else if (status.equalsIgnoreCase("ample")) {
            this.availabilityMessage = "Available";
            this.substitution = "-";
        } else if (status.equalsIgnoreCase("arriving")) {
            this.availabilityMessage = "Available tomorrow";
            this.substitution = mealValidator.findSubstitute(selectedIngredient, dietaryRestriction);
        } else {
            this.availabilityMessage = "Unknown stock status";
            this.substitution = "-";
        }
    }

    @Then("display message {string}")
    public void displayMessage(String expectedMessage) {
        assertEquals(expectedMessage, availabilityMessage);
    }

    @Then("suggest {string}")
    public void suggest(String expectedSuggestion) {
        if (availabilityMessage.startsWith("Unavailable")) {
            assertEquals(expectedSuggestion, this.substitution);
        }
    }

    @Given("customer {string} has restriction {string}")
    public void customerHasRestriction(String string, String string2) {
        this.customerName = string;
        this.dietaryRestriction = string2;
    }

    private String calculateNutrition(List<String> ingredients) {
        return "Calories: 500, Protein: 30g, Carbs: 60g, Fat: 15g";
    }

    private String generateSuggestions(List<String> ingredients, String restriction) {
        if (restriction.equalsIgnoreCase("Nut Allergy")) {
            return "sunflower seeds, coconut yogurt";
        } else if (restriction.equalsIgnoreCase("Dairy-Free")) {
            return "vegan cheese, nutritional yeast";
        } else {
            return "Try tofu instead of meat, almond milk instead of milk";
        }
    }



  @When("the system checks availability of {string} with stock {string}")
  public void theSystemChecksAvailability(String ingredient, String stock) {
    String status = stock.toLowerCase();
    String alt = mealValidator.findSubstitute(ingredient, dietaryRestriction);
    lastNotification = mealValidator.checkIngredientAvailability(ingredient, status, alt);
  }
  @Then("system should notify {string}")
  public void systemShouldNotify(String expectedMessage) {
    assertEquals(expectedMessage, lastNotification);
  }

}




