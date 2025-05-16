Feature: Order and Menu Customization

  Scenario Outline: Customer customizes a meal by choosing ingredients
    Given customer "<Name>" with dietary restriction "<Restriction>" is creating a custom meal
    When they select "<SelectedIngredients>"
    Then the system should <ValidationResult> the combination
    And if accepted, calculate nutrition information
    And if rejected, suggest "<SuggestedAlternatives>"

    Examples:
      | Name  | Restriction   | SelectedIngredients           | ValidationResult | SuggestedAlternatives          |
      | Ali   | Vegan         | tofu, broccoli, brown rice    | Accept           | -                              |
      | Sara  | Halal         | chicken, rice, vegetables     | Accept           | -                              |
      | Noor  | Nut Allergy   | almonds, yogurt, honey        | Reject           | sunflower seeds, coconut yogurt|
      | Tom   | Dairy-Free    | cheese, pasta, tomato sauce   | Reject           | vegan cheese, nutritional yeast|

  Scenario Outline: Ingredient substitution suggestion
    Given customer "<Name>" has restriction "<Restriction>"
    And selected ingredient "<OriginalIngredient>" is unavailable or restricted
    When the system checks for alternatives
    Then it should suggest "<RecommendedSubstitute>"

    Examples:
      | Name  | Restriction   | OriginalIngredient | RecommendedSubstitute   |
      | Emma  | Vegan         | eggs              | flaxseed                |
      | Liam  | Gluten-Free   | wheat flour       | almond flour           |
      | Aya   | Shellfish All.| shrimp            | mushrooms              |
      | Omar  | Low-Sodium    | soy sauce        | coconut aminos        |

  Scenario Outline: Chef approval for substitutions
    Given system proposes replacing "<OriginalItem>" with "<ProposedSubstitute>"
    When chef "<ChefName>" <ApprovalDecision> the change
    Then the system should <SystemAction>

    Examples:
      | OriginalItem | ProposedSubstitute | ChefName | ApprovalDecision | SystemAction       |
      | dairy milk   | oat milk           | Chef Mei | approves        | update recipe      |
      | peanuts      | sunflower seeds    | Chef Raj | rejects         | request new option |
      | gluten pasta | zucchini noodles   | Chef Alex| approves       | update recipe      |

  Scenario Outline: Ingredient availability check
    Given "<Ingredient>" has stock status "<StockStatus>"
    When customer selects it
    Then display message "<AvailabilityMessage>"
    And suggest "<AlternativeIfUnavailable>"

    Examples:
      | Ingredient    | StockStatus | AvailabilityMessage    | AlternativeIfUnavailable |
      | organic basil | low         | Limited stock warning  | dried basil              |
      | salmon        | out         | Unavailable alert      | trout or sea bass        |
      | quinoa        | ample       | Available             | -                        |
      | almond flour  | arriving    | Available tomorrow    | coconut flour            |
