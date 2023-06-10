module ru.hse.javaprogramming.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;


    opens ru.hse.javaprogramming.client to javafx.fxml;
    exports ru.hse.javaprogramming.client;
}