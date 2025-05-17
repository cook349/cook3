Feature: Inventory and Supplier Management
  Scenario Outline: Monitor stock levels and suggest restocking
    Given there is an ingredient "<Ingredient>" with current stock "<CurrentStock>", minimum threshold "<MinThreshold>" and critical threshold "<CriticalThreshold>"
    When "<QuantityConsumed>" units are consumed
    Then the stock status should be "<Status>"
    And the system should "<ExpectedAction>"

    Examples:
      | Ingredient | CurrentStock | MinThreshold | CriticalThreshold | QuantityConsumed | Status    | ExpectedAction                          |
      | Tomatoes   | 15           | 10           | 5                 | 4                | Sufficient| No action                               |
      | Olive Oil  | 8            | 5            | 2                 | 6                | Low       | Send restocking alert                   |
      | Flour      | 4            | 10           | 3                 | 2                | Critical  | Generate automatic purchase order       |

  Scenario Outline: Select most cost-effective supplier
    Given there is an ingredient "<Ingredient>" that needs to be reordered
    And the following suppliers are available:
      | Supplier    | Price |
      | Supplier 1  | <Price1> |
      | Supplier 2  | <Price2> |
    When the system fetches real-time prices
    Then the system should select "<ExpectedSupplier>" offering the best price
    And the expected price should be "<ExpectedPrice>"

    Examples:
      | Ingredient | Price1 | Price2 | ExpectedSupplier | ExpectedPrice |
      | Meat       | 10.5   | 9.8    | Supplier 2       | 9.8           |
      | Fish       | 12.0   | 13.2   | Supplier 1       | 12.0          |

  Scenario Outline: Handle failure in contacting supplier or order rejection
    Given the ingredient "<Ingredient>" is critically low
    And the system attempts to place an order with "<PreferredSupplier>"
    When the supplier responds with "<SupplierResponse>"
    Then the system should "<SystemAction>"

    Examples:
      | Ingredient | PreferredSupplier | SupplierResponse              | SystemAction                                   |
      | Milk       | Supplier A        | Connection Timeout            | Retry with next available supplier             |
      | Cheese     | Supplier B        | Order Rejected (Out of Stock) | Notify kitchen manager and suggest alternative |

  Scenario Outline: Ensure order stays within budget
    Given the ingredient "<Ingredient>" needs reordering
    And the system has a remaining budget of "<Budget>"
    And the best available supplier offers it for "<Price>"
    When the system evaluates the purchase
    Then the system should "<Decision>"

    Examples:
      | Ingredient | Budget | Price | Decision                        |
      | Butter     | 20.0   | 18.5  | Approve order                   |
      | Vanilla    | 5.0    | 6.3   | Defer order and notify manager |
