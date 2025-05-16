package org.example.cook1;


public class Chef {
  private String name;
  private String expertise;
  private int currentTasks;
  private int maxTasks;
  public Chef(String name, String expertise, int currentTasks, int maxTasks) {
    this.name = name;
    this.expertise = expertise;
    this.currentTasks = currentTasks;
    this.maxTasks = maxTasks;
  }
  public String getName() {
    return name;
  }
  public String getExpertise() {
    return expertise;
  }
  public int getCurrentTasks() {
    return currentTasks;
  }
  public int getMaxTasks() {
    return maxTasks;
  }
  public void setCurrentTasks(int tasks) {
    currentTasks = tasks;
  }


}

