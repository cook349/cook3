package org.example.cook1;
import java.time.LocalDateTime;

public class Task {

  public String chef;
  public String description;
  public LocalDateTime time;

  public Task(String chef, String description, LocalDateTime time) {
    this.chef = chef;
    this.description = description;
    this.time = time;
  }

  public String getChef() {

    return chef;
  }
  public String getDescription() {



    return description;
  }
  public LocalDateTime getTime() {

    return time; }
}
