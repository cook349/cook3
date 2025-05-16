package org.example.cook1;

public class RecipeScore {
    private AI recipe;
    private int score;

    public RecipeScore(AI recipe, int score) {
        this.recipe = recipe;
        this.score = score;
    }

    public AI getRecipe() {
        return recipe;
    }

    public int getScore() {
        return score;
    }
}
