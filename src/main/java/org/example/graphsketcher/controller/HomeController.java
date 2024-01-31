package org.example.graphsketcher.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.graphsketcher.Main;
import org.example.graphsketcher.graph.Graph;
import org.example.graphsketcher.graph.UndirectedGraph;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    public Pane mainPane;
    @FXML
    public TextField notiField;
    @FXML
    public Button helpButton;
    @FXML
    public VBox firstBox;
    @FXML
    public VBox secondBox;
    @FXML
    public Button addVertBtn;
    @FXML
    public  Button addEdgeBtn;
    @FXML
    public  Button moveBtn;
    @FXML
    public  Button deleteBtn;
    @FXML
    public  Button resetBtn;
    @FXML
    public  Button travelBtn;
    @FXML
    public  Button pathBtn;
    @FXML
    public  Button cycleBtn;
    @FXML
    public  Button treeBtn;
    @FXML
    public  Button connectBtn;
    private boolean isEnableAddVert;
    private boolean isEnableAddEdge;
    private boolean isEnableMove;
    private boolean isEnableDelete;
    Graph graph;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        graph = new UndirectedGraph();
        setDefaultBtnState();

        /*
        Avoid user input into text field and reset default focus
         */
        if (notiField != null) {
            notiField.setEditable(false);
            notiField.setFocusTraversable(false);
        }

        resetDefaultBtnFocus();
    }

    // ================================ HANDLE EVENT =======================================

    /**
     * Handle the click event on add vertex button
     */
    public void addVertBtnOnClick() {
        isEnableAddVert = !isEnableAddVert;
        if (isEnableAddVert) {
            setEnabledBtnColor(addVertBtn);
        }
        else {
            setDefaultBtnColor(addVertBtn);
        }
    }

    /**
     * Handle the click event on add edge button
     */
    public void addEdgeBtnOnClick() {
        isEnableAddEdge = !isEnableAddEdge;
        if (isEnableAddEdge) {
            setEnabledBtnColor(addEdgeBtn);
        }
        else {
            setDefaultBtnColor(addEdgeBtn);
        }
    }

    /**
     * Handle the click event on move button
     */
    public void moveBtnOnClick() {
        isEnableMove = !isEnableMove;
        if (isEnableMove) {
            setEnabledBtnColor(moveBtn);
        }
        else {
            setDefaultBtnColor(moveBtn);
        }
    }

    /**
     * Handle the click event on delete button
     */
    public void deleteBtnOnClick() {
        isEnableDelete = !isEnableDelete;
        if (isEnableDelete) {
            setEnabledBtnColor(deleteBtn);
        }
        else {
            setDefaultBtnColor(moveBtn);
        }
    }

    /**
     * Handle the click event on reset button
     */
    public void resetBtnOnClick() {
//        graph.resetGraph();
    }

    /**
     * Handle the click event on travel button
     */
    public void travelBtnOnClick() {

    }

    /**
     * Handle the click event on path button
     */
    public void pathBtnOnClick() {

    }

    /**
     * Handle the click event on cycle button
     */
    public void cycleBtnOnClick() {

    }

    /**
     * Handle the click event on tree button
     */
    public void treeBtnOnClick() {

    }

    /**
     * Handle the click event on connect button
     */
    public void connectBtnOnClick() {

    }

    /**
     * Handle the click event on help button
     */
    public void helpButtonOnClick(ActionEvent event) {
        try {
            switchToHelpView(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ================================ LOGICAL CODE ========================================

    /**
     * Switching to Help scene
     */
    public void switchToHelpView(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        GridPane layout = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("views/help-view.fxml")));
        Scene scene = new Scene(layout);
        stage.sizeToScene();
        stage.setScene(scene);
    }

    // ================================ SUB-HANDLER ==========================================

    /**
     * Reset default the focus on button
     */
    private void resetDefaultBtnFocus() {
        addVertBtn.setFocusTraversable(false);
        addEdgeBtn.setFocusTraversable(false);
        moveBtn.setFocusTraversable(false);
        deleteBtn.setFocusTraversable(false);
        resetBtn.setFocusTraversable(false);

        travelBtn.setFocusTraversable(false);
        pathBtn.setFocusTraversable(false);
        cycleBtn.setFocusTraversable(false);
        treeBtn.setFocusTraversable(false);
        connectBtn.setFocusTraversable(false);

        helpButton.setFocusTraversable(false);
    }

    /**
     * Set default button color and its state
     * @param btn button
     */
    private void setDefaultBtnColor(Button btn) {
        /*
        Set all buttons' state excepting button passed
         */
        if (Objects.equals(btn, addVertBtn)) {
            isEnableAddEdge = false;
            isEnableMove = false;
            isEnableDelete = false;
        }

        if (Objects.equals(btn, addEdgeBtn)) {
            isEnableAddVert = false;
            isEnableMove = false;
            isEnableDelete = false;
        }

        if (Objects.equals(btn, moveBtn)) {
            isEnableAddVert = false;
            isEnableAddEdge = false;
            isEnableDelete = false;
        }

        if (Objects.equals(btn, deleteBtn)) {
            isEnableAddVert = false;
            isEnableAddEdge = false;
            isEnableMove = false;
        }

        /*
        Set buttons style
         */
        addVertBtn.setStyle("-fx-background-color: #F2F2F2;");
        addEdgeBtn.setStyle("-fx-background-color: #F2F2F2;");
        moveBtn.setStyle("-fx-background-color: #F2F2F2;");
        deleteBtn.setStyle("-fx-background-color: #F2F2F2;");
    }

    /**
     * Set enabled button color
     * @param btn button
     */
    private void setEnabledBtnColor(Button btn) {
        setDefaultBtnColor(btn);
        btn.setStyle("-fx-background-color: #A2A2A2;");
    }

    /**
     * Set default button state (is enabled or not)
     */
    private void setDefaultBtnState() {
        isEnableAddVert = false;
        isEnableAddEdge = false;
        isEnableMove = false;
        isEnableDelete = false;
    }
}
