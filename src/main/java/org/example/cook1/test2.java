package org.example.cook1;

import java.util.*;
import java.util.stream.Collectors;
import java.util.Objects;

public class test2 {
    private String dietaryRestriction;
    private List<String> selectedIngredients;

    private static final Map<String, List<String>> INCOMPATIBLE_ITEMS = Map.of(
            "vegan", Arrays.asList("meat", "cheese", "eggs", "milk", "honey"),
            "halal", Arrays.asList("pork", "alcohol", "gelatin"),
            "nut allergy", Arrays.asList("peanuts", "almonds", "cashews"),
            "gluten-free", Arrays.asList("wheat", "barley", "rye"),
            "dairy-free", Arrays.asList("cheese", "milk", "yogurt", "butter"),
            "shellfish all.", Arrays.asList("shrimp", "lobster", "crab"),
            "low-sodium", Arrays.asList("soy sauce", "salt", "pickles")
    );

    private static final Map<String, Map<String, String>> SUBSTITUTION_DB = Map.of(
            "vegan", Map.of(
                    "meat", "tofu",
                    "cheese", "nutritional yeast",
                    "eggs", "flaxseed",
                    "milk", "almond milk"
            ),
            "nut allergy", Map.of(
                    "peanuts", "sunflower seeds",
                    "almond milk", "oat milk",
                    "almonds", "sunflower seeds"
            ),
            "gluten-free", Map.of(
                    "wheat flour", "almond flour",
                    "soy sauce", "tamari"
            ),
            "shellfish all.", Map.of(
                    "shrimp", "mushrooms"
            ),
            "low-sodium", Map.of(
                    "soy sauce", "coconut aminos"
            )
    );

    private static final Map<String, Map<String, List<String>>> ALTERNATIVE_SUBSTITUTES = Map.of(
            "vegan", Map.of(
                    "meat", Arrays.asList("tofu", "tempeh", "seitan", "jackfruit"),
                    "cheese", Arrays.asList("nutritional yeast", "cashew cheese", "soy cheese", "almond cheese"),
                    "eggs", Arrays.asList("flaxseed", "chia seeds", "apple sauce", "banana"),
                    "milk", Arrays.asList("almond milk", "oat milk", "soy milk", "coconut milk")
            ),
            "nut allergy", Map.of(
                    "peanuts", Arrays.asList("sunflower seeds", "pumpkin seeds", "soy nuts"),
                    "almonds", Arrays.asList("sunflower seeds", "pepitas"),
                    "almond milk", Arrays.asList("oat milk", "soy milk", "rice milk", "hemp milk")
            ),
            "gluten-free", Map.of(
                    "wheat flour", Arrays.asList("almond flour", "coconut flour", "rice flour", "tapioca flour"),
                    "soy sauce", Arrays.asList("tamari", "coconut aminos", "liquid aminos")
            ),
            "shellfish all.", Map.of(
                    "shrimp", Arrays.asList("mushrooms", "tofu", "tempeh")
            ),
            "low-sodium", Map.of(
                    "soy sauce", Arrays.asList("coconut aminos", "liquid aminos")
            )
    );


    public boolean validateMealCombination() {
        if (dietaryRestriction == null || selectedIngredients == null) {
            return false;
        }

        String restriction = dietaryRestriction.toLowerCase();
        List<String> ingredients = selectedIngredients.stream()
                .filter(Objects::nonNull)
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        return INCOMPATIBLE_ITEMS.getOrDefault(restriction, Collections.emptyList())
                .stream()
                .noneMatch(ingredients::contains);
    }


    public String findSubstitute(String ingredient, String restriction) {
        Map<String, String> substitutes = new HashMap<>();

        substitutes.put("salmon", "trout or sea bass");
        substitutes.put("eggs", "flaxseed");
        substitutes.put("wheat flour", "almond flour");
        substitutes.put("shrimp", "mushrooms");
        substitutes.put("soy sauce", "coconut aminos");
        substitutes.put("cheese", "vegan cheese");
        substitutes.put("dairy milk", "oat milk");
        substitutes.put("peanuts", "sunflower seeds");
        substitutes.put("gluten pasta", "zucchini noodles");
        substitutes.put("organic basil", "dried basil");
        substitutes.put("almond flour", "coconut flour");

        return substitutes.getOrDefault(ingredient, "No substitute available");
    }


    public String handleChefApproval(String originalItem, String proposedSubstitute, String chefName, boolean approved) {
        if (approved) {
            return "Chef " + chefName + " approved. Action: update recipe";
        } else {
            return "Chef " + chefName + " rejected. Action: request new option";
        }
    }


    public String checkIngredientAvailability(String ingredient, String stockStatus, String alternative) {
        if (stockStatus == null) {
            return "Unknown stock status";
        }

        switch (stockStatus.toLowerCase()) {
            case "low":
                return "Limited stock warning";
            case "out":
                return "Unavailable alert. Suggested alternative: " +
                        (alternative != null ? alternative : "none available");
            case "ample":
                return "Available";
            case "arriving":
                return "Available tomorrow";
            default:
                return "Unknown stock status";
        }
    }


    public String findAlternativeSubstitute(String original, String restriction, String rejectedSubstitute) {
        if (restriction == null || original == null) {
            return "No alternative available (invalid input)";
        }

        try {
            Map<String, List<String>> allSubstitutes = getAlternativeSubstitutes(restriction);
            List<String> alternatives = allSubstitutes.getOrDefault(original.toLowerCase(), Collections.emptyList());

            List<String> validAlternatives = alternatives.stream()
                    .filter(alt -> alt != null && !alt.isEmpty())
                    .filter(alt -> !alt.equalsIgnoreCase(rejectedSubstitute))
                    .collect(Collectors.toList());

            return validAlternatives.isEmpty() ?
                    "No alternative substitute available" :
                    validAlternatives.get(0);
        } catch (Exception e) {
            return "Error finding alternative substitute: " + e.getMessage();
        }
    }

    public void setDietaryRestriction(String dietaryRestriction) {
        this.dietaryRestriction = dietaryRestriction != null ? dietaryRestriction : "none";
    }


    public void setSelectedIngredients(List<String> selectedIngredients) {
        this.selectedIngredients = selectedIngredients != null ?
                new ArrayList<>(selectedIngredients) :
                new ArrayList<>();
    }


    private Map<String, String> getAllSubstitutes(String restriction) {
        return SUBSTITUTION_DB.entrySet().stream()
                .filter(entry -> restriction.toLowerCase().contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map<String, List<String>> getAlternativeSubstitutes(String restriction) {
        String restrictionKey = ALTERNATIVE_SUBSTITUTES.keySet().stream()
                .filter(key -> restriction.toLowerCase().contains(key))
                .findFirst()
                .orElse("default");

        return ALTERNATIVE_SUBSTITUTES.getOrDefault(restrictionKey, Collections.emptyMap());
    }
}
