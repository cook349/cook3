Feature: Scheduling and Task Management

  Scenario Outline: Smart task assignment and notification for chefs
    Given the kitchen manager adds a new cooking task '<Task>' with dietary type '<DietType>' scheduled at '<ScheduledTime>'
    And the system identifies chef '<ChefName>' with expertise in '<Expertise>' and current workload is '<Workload>'
    When the system assigns the task to '<ChefName>'
    Then a notification should be sent to '<ChefName>' with task details: '<Task>', scheduled at '<ScheduledTime>'
    And the system should log the task assignment and update the chef's workload
    And ensure that chefs are assigned fairly based on expertise and current load

    Examples:
      | Task               | DietType        | ScheduledTime | ChefName | Expertise           | Workload |
      | Vegan Lunch        | Vegan           | 12:00 PM      | Ali      | Vegan               | Low      |
      | Gluten-Free Pizza  | Gluten-Free     | 06:00 PM      | Sara     | Gluten-Free         | Medium   |
      | Sugar-Free Cake    | Diabetic        | 03:00 PM      | Hadi     | Sugar-Free Desserts | Low      |

