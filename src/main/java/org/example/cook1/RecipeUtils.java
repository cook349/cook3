package org.example.cook1;

import java.util.List;
import java.util.stream.Collectors;

public class RecipeUtils {
    public static int calculateRecipeScore(AI recipe, List<String> availableIngredients) {
        int score = 1000;
        long matchingOptional = recipe.getOptionalIngredients().stream()
                .filter(availableIngredients::contains)
                .count();
        score += matchingOptional * 100;
        long totalMatching = recipe.getRequiredIngredients().size() + matchingOptional;
        score += totalMatching * 50;
        score -= recipe.getTime();
        return score;
    }

    public static boolean hasAllRequiredIngredients(AI recipe, List<String> availableIngredients) {
        return recipe.getRequiredIngredients().stream()
                .allMatch(req -> availableIngredients.stream()
                        .anyMatch(avail -> avail.equalsIgnoreCase(req)));
    }

    public static int countMatchingIngredients(AI recipe, List<String> availableIngredients) {
        int count = 0;
        for (String ing : recipe.getRequiredIngredients()) {
            if (availableIngredients.contains(ing)) {
                count++;
            }
        }
        for (String ing : recipe.getOptionalIngredients()) {
            if (availableIngredients.contains(ing)) {
                count++;
            }
        }
        return count;
    }

    public static String buildExplanation(AI recipe, List<String> availableIngredients, int maxCookingTime, String dietaryRestriction) {
        List<String> missingOptional = recipe.getOptionalIngredients().stream()
                .filter(opt -> !availableIngredients.contains(opt))
                .collect(Collectors.toList());

        return String.format(
                "Recommended '%s' (Score: %d)%n" +
                        "- Preparation time: %d/%d minutes%n" +
                        "- Uses %d/%d available ingredients%n" +
                        "- Meets %s dietary requirements%n" +
                        "%s",
                recipe.getName(),
                calculateRecipeScore(recipe, availableIngredients),
                recipe.getTime(),
                maxCookingTime,
                countMatchingIngredients(recipe, availableIngredients),
                availableIngredients.size(),
                dietaryRestriction,
                missingOptional.isEmpty() ?
                        "✔ Includes all optional ingredients" :
                        "Note: Missing optional ingredients: " + String.join(", ", missingOptional)
        );
    }
    }

