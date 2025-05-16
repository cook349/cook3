module org.example.cook1 {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.example.cook1 to javafx.fxml;
    exports org.example.cook1;
}
