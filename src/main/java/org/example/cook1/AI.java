package org.example.cook1;

import java.util.Arrays;
import java.util.List;

public class AI {
    private String name;
    private List<String> requiredIngredients;
    private List<String> optionalIngredients;
    private int time;
    private boolean vegan;

    public AI(String name, String[] requiredIngredients, String[] optionalIngredients, int time, boolean vegan) {
        this.name = name;
        this.requiredIngredients = Arrays.asList(requiredIngredients);
        this.optionalIngredients = Arrays.asList(optionalIngredients);
        this.time = time;
        this.vegan = vegan;
    }

    public String getName() {
        return name;
    }

    public List<String> getRequiredIngredients() {
        return requiredIngredients;
    }

    public List<String> getOptionalIngredients() {
        return optionalIngredients;
    }

    public int getTime() {
        return time;
    }

    public boolean isVegan() {
        return vegan;
    }
}
