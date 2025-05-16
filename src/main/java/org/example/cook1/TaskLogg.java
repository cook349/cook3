package org.example.cook1;

public class TaskLogg {

  public void logAssignment(Chef chef, CookingTask task) {
    System.out.printf("Task logged: %s assigned to %s at %s%n",
      task.getTaskName(),
      chef.getName(),
      java.time.LocalDateTime.now());
  }
}
