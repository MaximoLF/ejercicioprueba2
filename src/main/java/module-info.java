module com.maximo.pokedex {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires com.google.gson;

    opens com.maximo.pokedex to javafx.fxml;
    exports com.maximo.pokedex;
    exports com.maximo.pokedex.controller;
    opens com.maximo.pokedex.controller to javafx.fxml;
}