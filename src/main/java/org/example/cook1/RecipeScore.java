package org.example.cook1;

public class RecipeScore {
    private AITest recipe;
    private int score;

    public RecipeScore(AITest recipe, int score) {
        this.recipe = recipe;
        this.score = score;
    }

    public AITest getRecipe() {
        return recipe;
    }

    public int getScore() {
        return score;
    }
}
