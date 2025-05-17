Feature: AI Recipe Recommendation

  Scenario Outline: Get recipe recommendation based on constraints
    Given a user with dietary restriction "<Diet>"
    And available ingredients "<Ingredients>"
    And maximum cooking time of "<Time>" minutes
    When the AI recommends a recipe
    Then the recommendation should be "<Recipe>"


    Examples:
      | Diet   | Ingredients            | Time | Recipe                      |
      | Vegan  | Tomatoes, basil, pasta | 30   | Spaghetti with Tomato Sauce |
      | Vegan  | Tomatoes, basil        | 45   | Tomato Basil Soup           |
      | Vegan  | Basil, pasta           | 20   | Vegan Pesto Pasta           |
