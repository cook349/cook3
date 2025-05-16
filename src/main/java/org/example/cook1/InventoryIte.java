package org.example.cook1;

public class InventoryIte {
  public String name;
  public int stock;
  public int minThreshold;
  public int criticalThreshold;

  public InventoryIte(String name, int stock, int min, int critical) {
    this.name = name;
    this.stock = stock;
    this.minThreshold = min;
    this.criticalThreshold = critical;
  }
  public String getName() {
    return name;
  }
  public int getStock() {
    return stock;
  }
  public int getMinThreshold() {
    return minThreshold;
  }
  public int getCriticalThreshold() {
    return criticalThreshold;
  }
}
