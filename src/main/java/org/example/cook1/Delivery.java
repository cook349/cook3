package org.example.cook1;

import java.time.LocalDateTime;

public class Delivery {
  public String customer;
  public String item;
  public LocalDateTime time;

  public Delivery(String customer, String item, LocalDateTime time) {
    this.customer = customer;
    this.item = item;
    this.time = time;
  }



  public String getCustomer() { return
    customer;
  }
  public String getItem() {
    return item;
  }
  public LocalDateTime getTime() {
    return time;
  }
}
