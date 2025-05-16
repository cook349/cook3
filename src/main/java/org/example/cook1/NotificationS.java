package org.example.cook1;

public class NotificationS {

  public void sendNotification(Chef chef, CookingTask task) {

    System.out.printf("Notification sent to Chef %s: %s (%s) at %s%n",
      chef.getName(),

      task.getTaskName(),
      task.getDietType(),
      task.getScheduledTime());
  }
}
