package org.example.cook1;

import java.util.*;
import java.util.stream.*;

public class InventoryMang {
  private Map<String, Ingredient> inventory = new HashMap<>();
  private double budget;

  public void setBudget(double budget) {
    this.budget = budget;
  }

  public void addIngredient(String name, int stock, int minThreshold, int criticalThreshold) {
    inventory.put(name, new Ingredient(name, stock, minThreshold, criticalThreshold));
  }

  public String consume(String ingredientName, int quantity) {
    Ingredient ing = inventory.get(ingredientName);
    if (ing == null) return "Ingredient not found";
    ing.setStock(ing.getStock() - quantity);
    return ing.getStockStatus();
  }

  public String getSuggestedAction(String ingredientName) {
    Ingredient ing = inventory.get(ingredientName);
    if (ing == null) return "Ingredient not found";
    String status = ing.getStockStatus();
    return switch (status) {
      case "Critical" -> "Generate automatic purchase order";
      case "Low"      -> "Send restocking alert";
      default         -> "No action";
    };
  }

  public Supplie selectBestSupplier(List<Supplie> suppliers) {
    return suppliers.stream()
      .min(Comparator.comparingDouble(Supplie::getPrice))
      .orElse(null);
  }

  public void setNeedsReordering(String ingredientName, boolean needsReorder) {
    Ingredient ing = inventory.get(ingredientName);
    if (ing != null) {
      if (needsReorder) {
        ing.setStock(ing.getMinThreshold() - 1);
      }
    }
  }

  public String handleOrderFailure(String response) {
    if (response.equalsIgnoreCase("Connection Timeout")) {
      return "Retry with next available supplier";
    } else if (response.contains("Rejected")) {
      return "Notify kitchen manager and suggest alternative";
    }
    return "Unknown response";
  }

  public String evaluatePurchase(double price) {
    if (price <= budget) {
      budget -= price;
      return "Approve order";
    } else {
      return "Defer order and notify manager";
    }
  }

  public String getStockStatus(String ingredientName) {
    Ingredient ing = inventory.get(ingredientName);
    return ing == null ? "Ingredient not found" : ing.getStockStatus();
  }

}
