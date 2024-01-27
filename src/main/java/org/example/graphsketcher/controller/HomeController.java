package org.example.graphsketcher.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.graphsketcher.Main;

import java.io.IOException;
import java.util.Objects;

public class HomeController {
    @FXML
    public Pane mainPane;
    @FXML
    public TextField notiField;
    @FXML
    public VBox firstBox;
    @FXML
    public VBox secondBox;
    @FXML
    public Button helpButton;

    // ================================ HANDLE EVENT =======================================
    public void helpButtonOnClick(ActionEvent event) {
        try {
            switchToHelpView(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ================================ LOGICAL CODE ========================================
    public void switchToHelpView(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        GridPane layout = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("views/help-view.fxml")));
        Scene scene = new Scene(layout);
        stage.sizeToScene();
        stage.setScene(scene);
    }

    // ================================ SUB PROCESS ==========================================
}
