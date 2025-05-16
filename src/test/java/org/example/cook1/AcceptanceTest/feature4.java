package org.example.cook1.AcceptanceTest;

import io.cucumber.java.en.*;
import org.example.cook1.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class feature4 {
  private InventoryMang inventoryManager = new InventoryMang();
  private String currentIngredientName;
  private Supplie selectedSupplier;
  private List<Supplie> suppliers = new ArrayList<>();
  private double remainingBudget;
  private String systemResponse;

  @Given("there is an ingredient {string} with current stock {string}, minimum threshold {string} and critical threshold {string}")
  public void setupIngredient(String name, String stockStr, String minStr, String criticalStr) {
    int stock = Integer.parseInt(stockStr);
    int minThreshold = Integer.parseInt(minStr);
    int criticalThreshold = Integer.parseInt(criticalStr);

    inventoryManager.addIngredient(name, stock, minThreshold, criticalThreshold);
    this.currentIngredientName = name;
  }

  @When("{string} units are consumed")
  public void consumeUnits(String quantityStr) {
    int quantity = Integer.parseInt(quantityStr);
    inventoryManager.consume(currentIngredientName, quantity);
    systemResponse = null;
  }

  @Then("the stock status should be {string}")
  public void verifyStockStatus(String expectedStatus) {
    assertEquals(expectedStatus, inventoryManager.getStockStatus(currentIngredientName));
  }

  @Then("the system should {string}")
  public void verifySystemAction(String expectedAction) {
    String actualAction = (systemResponse != null)
      ? systemResponse
      : inventoryManager.getSuggestedAction(currentIngredientName);
    assertEquals(expectedAction, actualAction);
  }

  @Given("there is an ingredient {string} that needs to be reordered")
  public void setupReorder(String name) {
    this.currentIngredientName = name;
  }
  @Given("the following suppliers are available:")
  public void setupSuppliers(io.cucumber.datatable.DataTable dataTable) {
    suppliers = dataTable.asMaps().stream()
      .map(row -> {
        String name = row.get("Supplier");
        String priceStr = row.get("Price");
        if (name == null || priceStr == null) {
          throw new IllegalArgumentException("Supplier data is missing");
        }

        double price = Double.parseDouble(priceStr);
        return new Supplie(name, price);
      })
      .collect(Collectors.toList());
  }


  @When("the system fetches real-time prices")
  public void fetchPrices() {
    selectedSupplier = inventoryManager.selectBestSupplier(suppliers);
  }

  @Then("the system should select {string} offering the best price")
  public void verifyBestSupplier(String expectedSupplier) {
    assertNotNull("No supplier selected", selectedSupplier);
    assertEquals(expectedSupplier, selectedSupplier.getName());
  }

  @Then("the expected price should be {string}")
  public void verifyBestPrice(String expectedPriceStr) {
    double expectedPrice = Double.parseDouble(expectedPriceStr);
    assertEquals(expectedPrice, selectedSupplier.getPrice(), 0.01);
  }

  @Given("the ingredient {string} is critically low")
  public void setCriticalIngredient(String name) {
    this.currentIngredientName = name;
  }

  @Given("the system attempts to place an order with {string}")
  public void attemptOrder(String supplierName) {
    selectedSupplier = suppliers.stream()
      .filter(s -> s.getName().equals(supplierName))
      .findFirst()
      .orElse(null);
  }

  @When("the supplier responds with {string}")
  public void handleResponse(String response) {
    if (inventoryManager.getStockStatus(currentIngredientName).equals("Ingredient not found")) {
      inventoryManager.addIngredient(currentIngredientName, 2, 5, 3);
    }

    systemResponse = inventoryManager.handleOrderFailure(response);
    if (inventoryManager.getStockStatus(currentIngredientName).equals("Ingredient not found")) {
      inventoryManager.addIngredient(currentIngredientName, 2, 5, 3);
    }
    systemResponse = inventoryManager.handleOrderFailure(response);
  }

  @Given("the system has a remaining budget of {string}")
  public void setBudget(String budgetStr) {
    remainingBudget = Double.parseDouble(budgetStr);
    inventoryManager.setBudget(remainingBudget);
  }

  @When("the system evaluates the purchase")
  public void evaluatePurchase() {
    if (selectedSupplier != null) {
      systemResponse = inventoryManager.evaluatePurchase(selectedSupplier.getPrice());
    } else {
      systemResponse = "No supplier selected";
    }
  }
  @Given("the ingredient {string} needs reordering")
  public void theIngredientNeedsReordering(String ingredientName) {
    currentIngredientName = ingredientName;
    inventoryManager.setNeedsReordering(ingredientName, true);
  }

  @Given("the best available supplier offers it for {string}")
  public void theBestAvailableSupplierOffersItFor(String priceStr) {
    double price = Double.parseDouble(priceStr);
    selectedSupplier = new Supplie(currentIngredientName, price);
    suppliers.add(selectedSupplier);
  }

}




