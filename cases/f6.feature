Feature: Notifications and Alerts

  Background:
    Given the system clock is set to "2025-05-10 09:00"

  Scenario Outline: Customer reminder for delivery
    Given a customer "<Customer>" has an upcoming delivery "<OrderID>" at "<DeliveryTime>"
    When it is "<ReminderTime>"
    Then the system should send a reminder to "<Customer>" saying "<ExpectedMessage>"

    Examples:
      | Customer | OrderID   | DeliveryTime     | ReminderTime     | ExpectedMessage                                    |
      | Alice    | ORD-1001  | 2025-05-10 14:00 | 2025-05-10 13:00 | Your order ORD-1001 is arriving at 14:00 today.    |

  Scenario Outline: Chef notification for task
    Given a chef "<ChefName>" has a task "<TaskID>" at "<TaskTime>"
    When it is "<NotificationTime>"
    Then the system should notify chef "<ChefName>": "Task <TaskID> at <TaskTime>"

    Examples:
      | ChefName | TaskID      | TaskTime        | NotificationTime |
      | Ali      | TASK-VEG-01 | 2025-05-10 11:00| 2025-05-10 10:45 |

  Scenario: Low-stock alerts for multiple items
    Given the following inventory levels:
      | Ingredient | Stock | Threshold |
      | Tomatoes   | 8     | 10        |
      | Flour      | 2     | 5         |
    When the system checks inventory
    Then it should send alerts:
      | Recipient       | Message                                               |
      | kitchen_manager | Alert: Tomatoes stock is low (8 units). Please restock |
      | kitchen_manager | Alert: Flour stock is low (2 units). Please restock    |
