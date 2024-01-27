module org.example.graphsketcher {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens org.example.graphsketcher to javafx.fxml;
    exports org.example.graphsketcher;
    exports org.example.graphsketcher.graph;
    exports org.example.graphsketcher.controller;
}