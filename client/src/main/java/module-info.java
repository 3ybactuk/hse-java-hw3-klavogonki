module ru.hse.javaprogramming.client {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.hse.javaprogramming.client to javafx.fxml;
    exports ru.hse.javaprogramming.client;
}