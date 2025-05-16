package org.example.cook1;

public class CookingTask {
  private String taskName;
  private String dietType;
  private String scheduledTime;


  public CookingTask(String taskName, String dietType, String scheduledTime) {
      this.taskName = taskName;
    this.dietType = dietType;
    this.scheduledTime = scheduledTime;
  }

  public String getTaskName() {
    return taskName;
  }
  public String getDietType() {
    return dietType;
  }
  public String getScheduledTime() {
    return scheduledTime;
  }


}
