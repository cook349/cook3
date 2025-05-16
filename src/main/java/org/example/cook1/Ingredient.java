package org.example.cook1;
public class Ingredient {
  private String name;
  private int stock;
  private int minThreshold;
  private int criticalThreshold;

  public Ingredient(String name, int stock, int minThreshold, int criticalThreshold) {
    this.name = name;
    this.stock = stock;
    this.minThreshold = minThreshold;
    this.criticalThreshold = criticalThreshold;
  }

  public int getStock() {

    return stock;
  }


  public int getMinThreshold() {
    return minThreshold;
  }

  public void setStock(int stock) {
    this.stock = stock;
  }

  public String getStockStatus() {
    if (stock < criticalThreshold) {
      return "Critical";
    } else if (stock <= minThreshold) {
      return "Low";
    } else {
      return "Sufficient";
    }
  }


}
