package org.example.cook1.AcceptanceTest;

import io.cucumber.java.en.*;
import org.example.cook1.AI;
import org.example.cook1.RecipeScore;
import org.example.cook1.RecipeUtils;

import java.util.*;
import static org.junit.Assert.*;

public class f7 {

    private String dietaryRestriction;
    private List<String> availableIngredients;
    private int maxCookingTime;
    private String recommendedRecipe;
    private String explanation;

    private final List<AI> recipeDatabase = Arrays.asList(
            new AI("Spaghetti with Tomato Sauce",
                    new String[]{"Tomatoes", "pasta", "basil"},
                    new String[]{"olive oil"}, 25, true),
            new AI("Tomato Basil Soup",
                    new String[]{"Tomatoes", "basil"},
                    new String[]{"garlic"}, 40, true),
            new AI("Vegan Pesto Pasta",
                    new String[]{"basil", "pasta"},
                    new String[]{"olive oil", "garlic"}, 20, true)
    );

    @Given("a user with dietary restriction {string}")
    public void aUserWithDietaryRestriction(String string) {
        this.dietaryRestriction = string;
    }

    @Given("available ingredients {string}")
    public void availableIngredients(String string) {
        this.availableIngredients = Arrays.asList(string.split(",\\s*"));
    }

    @Given("maximum cooking time of {string} minutes")
    public void maximumCookingTimeOfMinutes(String string) {
        this.maxCookingTime = Integer.parseInt(string);
    }

    @When("the AI recommends a recipe")
    public void theAIRecommendsARecipe() {
        List<RecipeScore> scoredRecipes = new ArrayList<>();

        for (AI recipe : recipeDatabase) {
            if (dietaryRestriction.equalsIgnoreCase("Vegan") && !recipe.isVegan()) {
                continue;
            }

            if (recipe.getTime() > maxCookingTime) {
                continue;
            }

            boolean hasAllRequired = RecipeUtils.hasAllRequiredIngredients(recipe, availableIngredients);
            if (!hasAllRequired) continue;

            int score = RecipeUtils.calculateRecipeScore(recipe, availableIngredients);
            scoredRecipes.add(new RecipeScore(recipe, score));
        }

        if (scoredRecipes.isEmpty()) {
            this.recommendedRecipe = "No suitable recipe found";
            this.explanation = "No recipes match all constraints";
        } else {
            scoredRecipes.sort(Comparator.comparingInt(RecipeScore::getScore).reversed());
            AI bestMatch = scoredRecipes.get(0).getRecipe();
            this.recommendedRecipe = bestMatch.getName();
            this.explanation = RecipeUtils.buildExplanation(
                    bestMatch,
                    availableIngredients,
                    maxCookingTime,
                    dietaryRestriction
            );
        }
    }

    @Then("the recommendation should be {string}")
    public void theRecommendationShouldBe(String string) {
        assertEquals(string, recommendedRecipe);
    }
}
