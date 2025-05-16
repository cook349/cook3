package org.example.cook1.AcceptanceTest;

import io.cucumber.java.en.*;
import org.example.cook1.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class feature3 {
  private List<Chef> chefs = new ArrayList<>();
  private CookingTask currentTask;
  private Chef assignedChef;
  private NotificationS notificationService = new NotificationS();
  private TaskLogg taskLogger = new TaskLogg();
  @Given("the kitchen manager adds a new cooking task {string} with dietary type {string} scheduled at {string}")
  public void addNewTask(String taskName, String dietType, String scheduledTime) {
    currentTask = new CookingTask(taskName, dietType, scheduledTime);
    System.out.println("New task created: " + taskName + " (" + dietType + ") at " + scheduledTime);
  }

  @Given("the system identifies chef {string} with expertise in {string} and current workload is {string}")
  public void identifyChef(String chefName, String expertise, String workload) {
    int currentTasks;
    switch (workload.toLowerCase()) {
      case "high": currentTasks = 4; break;
      case "medium": currentTasks = 2; break;
      default: currentTasks = 0;
    }
    int maxTasks = expertise.equals("Sugar-Free Desserts") ? 6 :
      expertise.equals("Vegan") ? 5 : 4;

    Chef chef = new Chef(chefName, expertise, currentTasks, maxTasks);
    chefs.add(chef);
    System.out.println("Chef added: " + chefName + " (" + expertise + ") - " +
      currentTasks + "/" + maxTasks + " tasks");
  }

  @When("the system assigns the task to {string}")
  public void assignTaskToChef(String expectedChefName) {
    System.out.println("\nAttempting to assign: " + currentTask.getTaskName() +
      " (Diet: " + currentTask.getDietType() + ")");
    System.out.println("Available Chefs:");
    chefs.forEach(c -> System.out.println(
      "- " + c.getName() + ": " + c.getExpertise() +
        " (" + c.getCurrentTasks() + "/" + c.getMaxTasks() + ")"
    ));

    assignedChef = assignTask(currentTask);

    if (assignedChef == null || !assignedChef.getName().equals(expectedChefName)) {
      throw new RuntimeException("Failed to assign task to " + expectedChefName +
        ". Actual assignment: " + (assignedChef != null ? assignedChef.getName() : "null"));
    }

    System.out.println("Successfully assigned to: " + assignedChef.getName());
  }

  @Then("a notification should be sent to {string} with task details: {string}, scheduled at {string}")
  public void verifyNotification(String chefName, String taskName, String scheduledTime) {
    notificationService.sendNotification(assignedChef, currentTask);
  }

  @Then("the system should log the task assignment and update the chef's workload")
  public void verifyLoggingAndWorkload() {
    taskLogger.logAssignment(assignedChef, currentTask);
    assert assignedChef.getCurrentTasks() > 0 : "Chef's workload not updated";
  }

  @Then("ensure that chefs are assigned fairly based on expertise and current load")
  public void verifyFairAssignment() {
    boolean expertiseMatch = assignedChef.getExpertise().equals(currentTask.getDietType()) ||
      isRelatedExpertise(assignedChef.getExpertise(), currentTask.getDietType());

    assert expertiseMatch : "Expertise mismatch: Chef has " + assignedChef.getExpertise() +
      " but task requires " + currentTask.getDietType();

    assert assignedChef.getCurrentTasks() <= assignedChef.getMaxTasks() :
      "Chef is overloaded: " + assignedChef.getCurrentTasks() + "/" + assignedChef.getMaxTasks();
  }

  private Chef assignTask(CookingTask task) {
    List<Chef> eligibleChefs = chefs.stream()
      .filter(chef -> chef.getExpertise().equals(task.getDietType()))
      .filter(chef -> chef.getCurrentTasks() < chef.getMaxTasks())
      .sorted(Comparator.comparingInt(Chef::getCurrentTasks))
      .collect(Collectors.toList());

    if (eligibleChefs.isEmpty()) {
      eligibleChefs = chefs.stream()
        .filter(chef -> isRelatedExpertise(chef.getExpertise(), task.getDietType()))
        .filter(chef -> chef.getCurrentTasks() < chef.getMaxTasks())
        .sorted(Comparator.comparingInt(Chef::getCurrentTasks))
        .collect(Collectors.toList());
    }

    if (!eligibleChefs.isEmpty()) {
      Chef chef = eligibleChefs.get(0);
      chef.setCurrentTasks(chef.getCurrentTasks() + 1);
      return chef;
    }
    return null;
  }

  private boolean isRelatedExpertise(String chefExpertise, String taskDietType) {
    Map<String, List<String>> expertiseMapping = Map.of(
      "Sugar-Free Desserts", List.of("Diabetic", "Sugar-Free"),
      "Vegan", List.of("Vegetarian"),
      "Gluten-Free", List.of("Celiac")
    );

    return expertiseMapping.getOrDefault(chefExpertise, List.of())
      .contains(taskDietType);
  }
}

