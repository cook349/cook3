Feature: Customer Profile Management

  Scenario Outline: Add customer dietary preference and allergies with validation
    Given a customer '<Name>'
    When they enter '<Preference>' as a dietary preference and allergies '<Allergy>'
    Then the system should store the dietary preferences as '<ExpectedPreference>'
    And the system should store the allergies as '<ExpectedAllergy>'
    And recommend appropriate meals like '<RecommendedMeal>' and prevent unwanted ingredients '<AvoidedIngredient>'

    Examples:
      | Name | Preference | Allergy | ExpectedPreference | ExpectedAllergy | RecommendedMeal | AvoidedIngredient |
      | Ali  | Vegan      | honey   | Vegan              | honey           | Tofu Bowl       | honey             |
      | Noor | Vegetarian | nuts    | Vegetarian         | nuts            | Veggie Pasta    | nuts              |



  Scenario Outline: Chef views preferences
    Given the customer '<Name>' has dietary preferences saved
    When the chef opens the customer '<Name>' profile
    Then the preferences '<Preference>' are displayed to the chef

    Examples:
      | Name | Preference |
      | Ali  | vegan      |
      | Noor | vegetarian |
      | Sami | gluten-free|


  Scenario Outline:  customer to views  past meal orders
  Given the customer '<Name>' has previous orders
    When they visit the order history page
  Then they should see a list of their past orders as '<PastOrders>'

    Examples:
      | Name   | PastOrders                             |
      | Ali    | Tofu Bowl, Vegan Burger               |
      | Noor   | Veggie Pasta, Vegetable Stir Fry       |
      | Sami   | Grilled Chicken Salad, Quinoa Bowl     |

  Scenario Outline: Chef accesses customers' order history for personalized meal suggestions
    Given the chef opens the customer '<Name>' profile
    When the chef reviews the order history of customer '<Name>'
    Then the chef should suggest personalized meal plans based on past orders like '<SuggestedMeals>'

    Examples:
      | Name   | SuggestedMeals                        |
      | Ali    | Tofu Bowl, Vegan Burger               |
      | Noor   | Veggie Pasta, Vegetable Stir Fry       |
      | Sami   | Grilled Chicken Salad, Quinoa Bowl     |



  Scenario Outline: System stores and retrieves customer order history for trend analysis
    Given the customer '<Name>' has placed orders '<Orders>'
    When the system stores the order history
    And the system retrieves the order history for analysis
    Then the retrieved order history for customer '<Name>' should be '<ExpectedHistory>'
    And the system should make the data available for service improvement reports

    Examples:
      | Name   | Orders                                  | ExpectedHistory                       |
      | Ali    | Tofu Bowl, Vegan Burger                 | Tofu Bowl, Vegan Burger               |
      | Noor   | Veggie Pasta, Vegetable Stir Fry        | Veggie Pasta, Vegetable Stir Fry      |
      | Sami   | Grilled Chicken Salad, Quinoa Bowl      | Grilled Chicken Salad, Quinoa Bowl    |


