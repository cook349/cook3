package org.example.cook1.AcceptanceTest;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import org.example.cook1.*;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
public class feature6 {


  private List<Delivery> deliveries = new ArrayList<>();
  private List<Task> tasks = new ArrayList<>();
  private List<InventoryIte> inventory = new ArrayList<>();
  private List<String> notifications = new ArrayList<>();
  private LocalDateTime currentTime;

  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


  @Given("the system clock is set to {string}")
  public void theSystemClockIsSetTo(String dateTime) {
    currentTime = LocalDateTime.parse(dateTime, formatter);

  }

  @Given("a customer {string} has an upcoming delivery {string} at {string}")
  public void aCustomerHasAnUpcomingDeliveryAt(String customer, String item, String time) {

    deliveries.add(new Delivery(customer, item, LocalDateTime.parse(time, formatter)));
  }


  @When("it is {string}")
  public void itIs(String now) {

    currentTime = LocalDateTime.parse(now, formatter);
    notifications.clear();



    for (Delivery d : deliveries) {
      if (d.getTime().minusHours(1).equals(currentTime)) {
        notifications.add(String.format(
                "To %s: Your order %s is arriving at %s today.",
                d.getCustomer(),
                d.getItem(),
                d.getTime().format(DateTimeFormatter.ofPattern("HH:mm"))
        ));
      }
    }


    for (Task t : tasks) {
      if (t.getTime().minusMinutes(15).equals(currentTime)) {
        notifications.add(String.format(
                "To %s: Task %s at %s",
                t.getChef(),
                t.getDescription(),
                t.getTime().format(formatter)
        ));
      }
    }

    for (InventoryIte itm : inventory) {
      if (itm.getStock() < itm.getCriticalThreshold()) {
        notifications.add("Alert: " + itm.getName() + " stock is critically low (" + itm.getStock() + " units). Please restock");
      } else if (itm.getStock() < itm.getMinThreshold()) {
        notifications.add("Alert: " + itm.getName() + " stock is low (" + itm.getStock() + " units). Please restock");
      }
    }


  }



  @Then("the system should send a reminder to {string} saying {string}")
  public void theSystemShouldSendAReminderToSaying(String recipient, String message) {
    String expected = "To " + recipient + ": " + message;
    assert notifications.contains(expected) : "Expected notification not found: " + expected;
  }



  @Given("a chef {string} has a task {string} at {string}")
  public void aChefHasATaskAt(String chef, String task, String time) {
    tasks.add(new Task(chef, task, LocalDateTime.parse(time, formatter)));


  }


  @Then("the system should notify chef {string}: {string}")
  public void theSystemShouldNotifyChef(String chef, String message) {
    String expected = String.format("To %s: %s", chef, message);
    notifications.forEach(notification -> {
      if (notification.equals(expected)) {
        return;
      }
    });
    assert notifications.contains(expected) : "Expected notification not found: " + expected;
  }



  @Given("the following inventory levels:")
  public void theFollowingInventoryLevels(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    for (Map<String, String> row : rows) {
      String name = row.get("Ingredient");
      int stock = Integer.parseInt(row.get("Stock"));
      int min = Integer.parseInt(row.get("Threshold"));

      int critical = 0;
      inventory.add(new InventoryIte(name, stock, min, critical));
    }
  }


  @When("the system checks inventory")
  public void theSystemChecksInventory() {
    for (InventoryIte item : inventory) {
      if (item.getStock() < item.getCriticalThreshold()) {
        notifications.add("Alert: " + item.getName() + " stock is critically low (" + item.getStock() + " units). Please restock");
      } else if (item.getStock() < item.getMinThreshold()) {
        notifications.add("Alert: " + item.getName() + " stock is low (" + item.getStock() + " units). Please restock");
      }
    }
  }

  @Then("it should send alerts:")
  public void itShouldSendAlerts(DataTable table) {
    List<Map<String, String>> rows = table.asMaps(String.class, String.class);
    for (Map<String, String> row : rows) {
      String message = row.get("Message");

      assert notifications.contains(message) :
              "Expected alert not found: " + message;
    }
  }



}
