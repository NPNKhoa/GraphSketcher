package org.example.graphsketcher.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.controlsfx.control.ListSelectionView;
import org.example.graphsketcher.Main;
import org.example.graphsketcher.graph.Edge;
import org.example.graphsketcher.graph.Graph;
import org.example.graphsketcher.graph.UndirectedGraph;
import org.example.graphsketcher.graph.Vertex;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class HomeController implements Initializable {
    @FXML
    public Pane mainPane;
    @FXML
    public Canvas canvas;
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
    private Label selectedVertLabel = null;
    private Line temporaryLine = null;
    private Graph graph;
    private final int RADIUS = 30;

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

    // ================================ HANDLE EVENTS ======================================

    // *********************************** BUTTON ******************************************
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
        graph.resetGraph();
        mainPane.getChildren().clear();
        mainPane.getChildren().add(canvas);
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

    // ******************************* GRAPH'S ELEMENT *************************************

    public void vertOnClick(MouseEvent mouseEvent) {
        if (isEnableDelete) {
            Label clickedVertLabel = (Label) mouseEvent.getSource();
            Vertex clickedVertex = graph.findVertByLabel(clickedVertLabel);
            List<Edge> edgeList = graph.getAllEdgesByVert(clickedVertex);
            List<Line> edgeLineList = new ArrayList<>();
            List<Label> edgeWeightLabelList = new ArrayList<>();
            for (Edge edge : edgeList) {
                edgeLineList.add(edge.getLineEdge());
                edgeWeightLabelList.add(edge.getWeightLabel());
            }

            graph.getEdges().removeAll(edgeList);
            graph.deleteVert(clickedVertLabel);

            mainPane.getChildren().remove(clickedVertLabel);
            mainPane.getChildren().removeAll(edgeLineList);
            mainPane.getChildren().removeAll(edgeWeightLabelList);
        }
    }

    public void edgeOnClick(MouseEvent mouseEvent) {

    }

    public void weightOnClick(MouseEvent mouseEvent) {

    }

    public void vertOnPress(MouseEvent mouseEvent) {
        if (isEnableAddEdge) {
            startDrawEdge(mouseEvent);
        }

        if (isEnableMove) {
            startMoveVert(mouseEvent);
        }
    }

    public void vertOnDrag(MouseEvent mouseEvent) {
        if (isEnableAddEdge) {
            drawEdge(mouseEvent);
        }

        if (isEnableMove) {
            moveVert(mouseEvent);
        }
    }

    public void vertOnRelease(MouseEvent mouseEvent) {
        if (isEnableAddEdge) {
            endDrawEdge(mouseEvent);
        }

        if (isEnableMove) {
            endMoveVert();
        }
    }

    // *********************************** OTHERS ******************************************

    public void canvasOnClick(MouseEvent mouseEvent) {
        if (isEnableAddVert) {
            Label vertLabel = graph.addVert(mouseEvent);
            mainPane.getChildren().add(vertLabel);
            addEventToVert(vertLabel);
        }
    }

    // ================================ LOGICAL CODE ========================================

    /**
     * Start drawing a line that represent for an edge
     */
    private void startDrawEdge(MouseEvent mouseEvent) {
        selectedVertLabel = (Label) mouseEvent.getSource();

        double startLineX = selectedVertLabel.getLayoutX() + selectedVertLabel.getWidth() / 2;
        double startLineY = selectedVertLabel.getLayoutY() + selectedVertLabel.getHeight() / 2;

        temporaryLine = new Line(startLineX, startLineY, startLineX, startLineY);

        mainPane.getChildren().add(temporaryLine);
    }

    private void drawEdge(MouseEvent mouseEvent) {
        temporaryLine.setStartX(selectedVertLabel.getLayoutX() + selectedVertLabel.getWidth() / 2);
        temporaryLine.setStartY(selectedVertLabel.getLayoutY() + selectedVertLabel.getHeight() / 2);
        temporaryLine.setEndX(selectedVertLabel.getLayoutX() + mouseEvent.getX());
        temporaryLine.setEndY(selectedVertLabel.getLayoutY() + mouseEvent.getY());
    }

    private void endDrawEdge(MouseEvent mouseEvent) {
        Label releaseVertexLabel = getReleaseVertexLabel(mouseEvent);

        if (releaseVertexLabel != null && releaseVertexLabel != selectedVertLabel) {
            Vertex beginVert = graph.findVertByLabel(selectedVertLabel);
            Vertex endVert = graph.findVertByLabel(releaseVertexLabel);

            showInputWeightDialog(beginVert, endVert);
        }

        selectedVertLabel = null;
        mainPane.getChildren().remove(temporaryLine);
        temporaryLine = null;
    }

    private void startMoveVert(MouseEvent mouseEvent) {
        selectedVertLabel = (Label) mouseEvent.getSource();
    }

    private void moveVert(MouseEvent mouseEvent) {
        selectedVertLabel = (Label) mouseEvent.getSource();

        selectedVertLabel.setLayoutX(mouseEvent.getX() + selectedVertLabel.getLayoutX() - RADIUS);
        selectedVertLabel.setLayoutY(mouseEvent.getY() + selectedVertLabel.getLayoutY() - RADIUS);

        Vertex selectedVertex = graph.findVertByLabel(selectedVertLabel);

        List<Edge> edgeList = graph.getAllEdgesByVert(selectedVertex);

        for (Edge edge : edgeList) {
            Label startVertLabel = edge.getBeginVert().getVertLabel();
            Label endVertLabel = edge.getEndVert().getVertLabel();

            double[] edgeCoordinates = calculateCoordinates(startVertLabel, endVertLabel);
            edge.getLineEdge().setStartX(edgeCoordinates[0]);
            edge.getLineEdge().setStartY(edgeCoordinates[1]);
            edge.getLineEdge().setEndX(edgeCoordinates[2]);
            edge.getLineEdge().setEndY(edgeCoordinates[3]);

            edge.getWeightLabel().setLayoutX((edgeCoordinates[0] + edgeCoordinates[2]) / 2);
            edge.getWeightLabel().setLayoutY((edgeCoordinates[1] + edgeCoordinates[3]) / 2);
        }
    }

    private void endMoveVert() {
        selectedVertLabel = null;
    }

    /**
     * Show dialog allowing user to input weight and draw edge.
     * After user entered the weight, create edge, edge weight and add them to main pane
     * @param beginVert begin vertex
     * @param endVert end vertex
     */
    private void showInputWeightDialog(Vertex beginVert, Vertex endVert) {
        /*
            Create a dialog and set its properties
         */
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Nhập trọng số cung!");
        dialog.setHeaderText(null);
        dialog.setContentText("Xin mời nhập trọng số cung: ");

        dialog.showAndWait().ifPresent(value -> {
            try {
                int intValue = Integer.parseInt(value); // the entered value
                if (intValue > 0) {
                    /*
                        Create an edge and set their properties
                     */
                    Edge edge = new Edge();
                    edge.setLineEdge(addEdge(beginVert.getVertLabel(), endVert.getVertLabel()));
                    edge.setBeginVert(beginVert);
                    edge.setEndVert(endVert);
                    edge.setWeight(intValue);

                    graph.getEdges().add(edge);

                    double[] edgeCoordinates = calculateCoordinates(beginVert.getVertLabel(), endVert.getVertLabel());

                    /*
                        Create a label representing for edge weight
                     */
                    Label weightLabel = new Label();
                    weightLabel.getStyleClass().add("weightLabel");
                    weightLabel.setText(Integer.toString(intValue));
                    weightLabel.setLayoutX((edgeCoordinates[0] + edgeCoordinates[2]) / 2);
                    weightLabel.setLayoutY((edgeCoordinates[1] + edgeCoordinates[3]) / 2);

                    edge.setWeightLabel(weightLabel);

                    mainPane.getChildren().add(weightLabel);
                }
                else {
                    showAlert("Đầu vào không hợp lệ", "Hãy nhập một số nguyên lớn hơn 0!");
                }
            }
            catch (NumberFormatException e) {
                showAlert("Đầu vào không hợp lệ", "Hãy nhập một số nguyên hợp lệ!");
            }
        });
    }

    /**
     * Add edges to the graph and display it on the UI
     * @param startVertLabel begin vertex label
     * @param endVertLabel end vertex label
     * @return an edge that have start, end vertex label and its weight
     */
    private Line addEdge(Label startVertLabel, Label endVertLabel) {
        double[] coordinates = calculateCoordinates(startVertLabel, endVertLabel);

        Line lineEdge = new Line();
        lineEdge.setStartX(coordinates[0]);
        lineEdge.setStartY(coordinates[1]);
        lineEdge.setEndX(coordinates[2]);
        lineEdge.setEndY(coordinates[3]);
        mainPane.getChildren().add(lineEdge);

        return lineEdge;
    }

    /**
     * Calculate the coordinates so that the distance of the line connecting two vertices is shortest
     * @return an array with 4 coordinates in order startX, startY, endX, endY
     */
    private double[] calculateCoordinates(Label startVertLabel, Label endVertLabel) {

        // coordinates variable is considered as coordinates of a line in order startX, startY, endX, endY
        double[] coordinates = new double[4];

        double angle = Math.atan2(endVertLabel.getLayoutY() - startVertLabel.getLayoutY(),
                endVertLabel.getLayoutX() - startVertLabel.getLayoutX());
        
        coordinates[0] = (startVertLabel.getLayoutX() + RADIUS) + (RADIUS * Math.cos(angle)); // startX
        coordinates[1] = (startVertLabel.getLayoutY() + RADIUS) + (RADIUS * Math.sin(angle)); //startY
        coordinates[2] = (endVertLabel.getLayoutX() + RADIUS) - (RADIUS * Math.cos(angle)); // endX
        coordinates[3] = (endVertLabel.getLayoutY() + RADIUS) - (RADIUS * Math.sin(angle)); // endY

        return coordinates;
    }

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

    private void addEventToVert(Label vertLabel) {
        vertLabel.setOnMouseClicked(this::vertOnClick);
        vertLabel.setOnMousePressed(this::vertOnPress);
        vertLabel.setOnMouseDragged(this::vertOnDrag);
        vertLabel.setOnMouseReleased(this::vertOnRelease);
    }

    private Label getReleaseVertexLabel(MouseEvent mouseEvent) {
        Label vertLabel;
        Bounds bounds;
        for (Vertex vertex : graph.getVertexes()) {
            vertLabel = vertex.getVertLabel();
            bounds = vertLabel.getBoundsInParent();

            if (bounds.contains(selectedVertLabel.getLayoutX() + mouseEvent.getX(),
                    selectedVertLabel.getLayoutY() + mouseEvent.getY())) {
                return vertLabel;
            }
        }
        return null;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
