package org.example.graphsketcher.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.example.graphsketcher.Main;

import java.io.IOException;
import java.util.Objects;

public class HelpController {
    @FXML
    public Button backButton;

    // ================================ HANDLE EVENT =======================================
    public void backButtonOnClick(ActionEvent event) {
        try {
            switchToHomeView(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ================================ LOGICAL CODE ========================================
    public void switchToHomeView(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        GridPane layout = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("views/home-view.fxml")));
        Scene scene = new Scene(layout);
        stage.sizeToScene();
        stage.setScene(scene);
    }

    // ================================ SUB PROCESS ==========================================
}
