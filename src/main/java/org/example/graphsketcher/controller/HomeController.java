package org.example.graphsketcher.controller;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.effect.Light;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import org.example.graphsketcher.Main;
import org.example.graphsketcher.graph.Edge;
import org.example.graphsketcher.graph.Graph;
import org.example.graphsketcher.graph.UndirectedGraph;
import org.example.graphsketcher.graph.Vertex;
import org.example.graphsketcher.services.File;

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
    public  Button undoBtn;
    @FXML
    public  Button travelBtn;
    @FXML
    public  Button pathBtn;
    @FXML
    public  Button cycleBtn;
    @FXML
    public  Button treeBtn;
    @FXML
    public  Button saveBtn;
    @FXML
    public  Button openBtn;
    private boolean isEnableAddVert;
    private boolean isEnableAddEdge;
    private boolean isEnableMove;
    private boolean isEnableDelete;
    private Label selectedVertLabel = null;
    private Line temporaryLine = null;
    private Graph graph;
    private Stack<Graph> history;
    private final int RADIUS = 30;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        graph = new UndirectedGraph();
        history = new Stack<>();
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
        history.push(createCopyGraph());
        graph.resetGraph();
        mainPane.getChildren().clear();
        mainPane.getChildren().add(canvas);
    }

    public void undoBtnOnClick() {
        graph = history.pop();
        loadGraphFromHistory(graph);
        notiField.clear();
        notiField.appendText("Undo successfully!");
    }

    /**
     * Handle the click event on travel button
     */
    public void travelBtnOnClick() {
        resetBackgroundColor();
        resetLineColor();
        String beginVertString = showInputVertDialog();
        Vertex beginVert = getVertByStringName(beginVertString);
        List<Vertex> traveledVert = graph.depthFirstSearch(beginVert);

        for (Vertex vert : traveledVert) {
            vert.getVertLabel().setBackground(generateBackgroundColor(new Color(0.32, 1.0, 0.1, 1.0)));
        }
        notiField.setText("Kết quả duyệt đồ thị: ");
        for (Vertex vert : traveledVert) {
            notiField.appendText(vert.getName() + ", ");
        }
        resetVisitedVert();
    }

    /**
     * Handle the click event on path button
     */
    public void pathBtnOnClick() {
        resetBackgroundColor();
        resetLineColor();
        List<String> inputVertexes = showInputVertexesDialog();
        Vertex beginVert = getVertByStringName(inputVertexes.getFirst());
        Vertex endVert = getVertByStringName(inputVertexes.getLast());

        notiField.clear();
        List<Vertex> path = graph.dijsktra(beginVert, endVert);
        notiField.appendText("Đường đi ngắn nhất tìm được: ");
        for (Vertex vertex : path) {
            if (vertex != null) {
                vertex.getVertLabel().setBackground(generateBackgroundColor(new Color(0.32, 1.0, 0.1, 1.0)));
                notiField.appendText(vertex.getName() + " ");
            }
        }
        resetVisitedVert();
    }

    /**
     * Handle the click event on cycle button
     */
    public void cycleBtnOnClick() {
        resetBackgroundColor();
        resetLineColor();
        String beginVertString = showInputVertDialog();
        Vertex beginVert = getVertByStringName(beginVertString);

        notiField.clear();
        List<Vertex> cycle = graph.minimumWeightGreedy(beginVert);
        notiField.appendText("Chu trình với trọng số nhỏ nhất tìm được: ");
        for (Vertex vertex : cycle) {
            if (vertex != null) {
                vertex.getVertLabel().setBackground(generateBackgroundColor(new Color(0.32, 1.0, 0.1, 1.0)));
                notiField.appendText(vertex.getName() + " ");
            }
        }
        resetVisitedVert();
    }

    /**
     * Handle the click event on tree button
     */
    public void treeBtnOnClick() {
        resetBackgroundColor();
        resetLineColor();
        String beginVertString = showInputVertDialog();
        Vertex beginVert = getVertByStringName(beginVertString);

        notiField.clear();
        List<Edge> mst = graph.prim(beginVert);

        for (Edge edge : mst) {
            edge.getLineEdge().setStrokeWidth(3.0);
            edge.getLineEdge().setStroke(Color.RED);
        }

        notiField.appendText("Cây khung nhỏ nhất trong đồ thị: ");
        for (Edge edge : mst) {
            notiField.appendText( "Cung (" + edge.getBeginVert().getName()
                    + ", " + edge.getEndVert().getName() + ") ");
        }
        resetVisitedVert();
    }

    /**
     * Handle the click event on save button
     */
    public void saveBtnOnClick(MouseEvent mouseEvent) {
        File.saveGraph(mouseEvent, this.graph);
    }

    /**
     * Handle the click event on open button
     */
    public void openBtnOnClick(MouseEvent mouseEvent) {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(canvas);

        for (Vertex vertex : graph.getVertexes()) {
            graph.getVertName().add(vertex.getName());
        }

        File.mainPane = this.mainPane;
        File.homeController = this;
        File.loadGraph(mouseEvent, graph);
        resetVisitedVert();
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

    /**
     * Handle the click event on vertex
     */
    public void vertOnClick(MouseEvent mouseEvent) {
        if (isEnableDelete) {
            history.push(this.graph);
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
            deleteVert(clickedVertLabel);

            mainPane.getChildren().remove(clickedVertLabel);
            mainPane.getChildren().removeAll(edgeLineList);
            mainPane.getChildren().removeAll(edgeWeightLabelList);
        }
    }

    /**
     * Handle the press event on vertex
     */
    public void vertOnPress(MouseEvent mouseEvent) {
        if (isEnableAddEdge) {
            startDrawEdge(mouseEvent);
        }

        if (isEnableMove) {
            startMoveVert(mouseEvent);
        }
    }

    /**
     * Handle the drag event on vertex
     */
    public void vertOnDrag(MouseEvent mouseEvent) {
        if (isEnableAddEdge) {
            drawEdge(mouseEvent);
        }

        if (isEnableMove) {
            moveVert(mouseEvent);
        }
    }

    /**
     * Handle the release event on vertex
     */
    public void vertOnRelease(MouseEvent mouseEvent) {
        if (isEnableAddEdge) {
            endDrawEdge(mouseEvent);
        }

        if (isEnableMove) {
            endMoveVert();
        }
    }

    // *********************************** OTHERS ******************************************

    /**
     * Handle the click event on canvas
     */
    public void canvasOnClick(MouseEvent mouseEvent) {
        if (isEnableAddVert) {
            Label vertLabel = addVert(mouseEvent);
            mainPane.getChildren().add(vertLabel);
            addEventToVert(vertLabel);
        }
    }

    // ================================ LOGICAL CODE ========================================

    /**
     * Start drawing edge
     */
    private void startDrawEdge(MouseEvent mouseEvent) {
        selectedVertLabel = (Label) mouseEvent.getSource();

        double startLineX = selectedVertLabel.getLayoutX() + selectedVertLabel.getWidth() / 2;
        double startLineY = selectedVertLabel.getLayoutY() + selectedVertLabel.getHeight() / 2;

        temporaryLine = new Line(startLineX, startLineY, startLineX, startLineY);

        mainPane.getChildren().add(temporaryLine);
    }

    /**
     * On drawing edge
     */
    private void drawEdge(MouseEvent mouseEvent) {
        temporaryLine.setStartX(selectedVertLabel.getLayoutX() + selectedVertLabel.getWidth() / 2);
        temporaryLine.setStartY(selectedVertLabel.getLayoutY() + selectedVertLabel.getHeight() / 2);
        temporaryLine.setEndX(selectedVertLabel.getLayoutX() + mouseEvent.getX());
        temporaryLine.setEndY(selectedVertLabel.getLayoutY() + mouseEvent.getY());
    }

    /**
     * End drawing edge
     */
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

    /**
     * Start moving vertex
     */
    private void startMoveVert(MouseEvent mouseEvent) {
        selectedVertLabel = (Label) mouseEvent.getSource();
    }

    /**
     * On moving vertex
     */
    private void moveVert(MouseEvent mouseEvent) {
        selectedVertLabel = (Label) mouseEvent.getSource();

        double vertLayoutX = mouseEvent.getX() + selectedVertLabel.getLayoutX() - RADIUS;
        double vertLayoutY = mouseEvent.getY() + selectedVertLabel.getLayoutY() - RADIUS;

        double[] vertLayout = getVertCoordinates(vertLayoutX, vertLayoutY);

        selectedVertLabel.setLayoutX(vertLayout[0]);
        selectedVertLabel.setLayoutY(vertLayout[1]);

        Vertex selectedVertex = graph.findVertByLabel(selectedVertLabel);

        List<Edge> edgeList = graph.getAllEdgesByVert(selectedVertex);

        for (Edge edge : edgeList) {
            Label startVertLabel = edge.getBeginVert().getVertLabel();
            Label endVertLabel = edge.getEndVert().getVertLabel();

            double[] edgeCoordinates = calculateCoordinates(startVertLabel, endVertLabel, RADIUS);
            edge.getLineEdge().setStartX(edgeCoordinates[0]);
            edge.getLineEdge().setStartY(edgeCoordinates[1]);
            edge.getLineEdge().setEndX(edgeCoordinates[2]);
            edge.getLineEdge().setEndY(edgeCoordinates[3]);

            edge.getWeightLabel().setLayoutX((edgeCoordinates[0] + edgeCoordinates[2]) / 2);
            edge.getWeightLabel().setLayoutY((edgeCoordinates[1] + edgeCoordinates[3]) / 2);
        }
    }

    /**
     * End moving vertex
     */
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

        dialog.setOnShown(event -> {
            Platform.runLater(() -> dialog.getEditor().selectAll());
        });

        while (true) {
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String value = result.get();
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

                        double[] edgeCoordinates = calculateCoordinates(beginVert.getVertLabel(), endVert.getVertLabel(), RADIUS);

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
                        addEventToWeight(weightLabel);

                        // Break the loop if input is valid
                        break;
                    } else {
                        showAlert("Đầu vào không hợp lệ", "Hãy nhập một số nguyên lớn hơn 0!");
                    }
                } catch (NumberFormatException e) {
                    showAlert("Đầu vào không hợp lệ", "Hãy nhập một số nguyên hợp lệ!");
                }
            } else {
                // Break the loop if dialog is cancelled
                break;
            }
        }
    }


    /**
     * Show dialog allowing user to input the specified vertex.
     * @return vertex name
     */
    private String showInputVertDialog() {
        List<Vertex> vertexes = graph.getVertexes();
        List<String> vertexString = new ArrayList<>();
        for (Vertex v : vertexes) {
            vertexString.add(v.getName());
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(vertexes.getFirst().getName(), vertexString);
        dialog.setTitle("Chọn đỉnh bắt đầu");
        dialog.setHeaderText(null);

        return dialog.showAndWait().get();
    }

    /**
     * Show dialog allowing user to input the specified vertexes.
     * @return vertex name
     */
    private List<String> showInputVertexesDialog() {
        List<Vertex> vertexes = graph.getVertexes();
        List<String> vertexNames = new ArrayList<>();
        for (Vertex v : vertexes) {
            vertexNames.add(v.getName());
        }

        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Chọn đỉnh bắt đầu và kết thúc");
        dialog.setHeaderText(null);

        // Tạo các label
        Label startLabel = new Label("Đỉnh bắt đầu:\t");
        Label endLabel = new Label("Đỉnh kết thúc:\t");

        // Tạo choice box cho đỉnh bắt đầu và kết thúc
        ChoiceBox<String> startChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList(vertexNames));
        ChoiceBox<String> endChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList(vertexNames));

        // Thêm listener để loại bỏ đỉnh đã chọn ở đỉnh bắt đầu khỏi đỉnh kết thúc
        startChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                endChoiceBox.getItems().remove(newValue);
            }
        });

        // Thêm các control vào grid pane
        GridPane grid = new GridPane();
        grid.add(startLabel, 0, 0);
        grid.add(startChoiceBox, 1, 0);
        grid.add(endLabel, 0, 1);
        grid.add(endChoiceBox, 1, 1);

        // Đảm bảo các control sẽ tự động mở rộng để đồng bộ với kích thước cửa sổ
        GridPane.setHgrow(startChoiceBox, Priority.ALWAYS);
        GridPane.setHgrow(endChoiceBox, Priority.ALWAYS);

        dialog.getDialogPane().setContent(grid);

        // Tạo button cho dialog
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Xác nhận khi người dùng chọn OK
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                String startVertex = startChoiceBox.getValue();
                String endVertex = endChoiceBox.getValue();

                if (startVertex == null || endVertex == null) {
                    // Hiển thị cảnh báo nếu người dùng không chọn cả hai đỉnh
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Vui lòng chọn cả hai đỉnh.");
                    alert.showAndWait();
                    return null;
                }

                List<String> result = new ArrayList<>();
                result.add(startVertex);
                result.add(endVertex);
                return result;
            }
            return null;
        });

        // Hiển thị dialog và lấy kết quả
        Optional<List<String>> result = dialog.showAndWait();
        return result.orElse(null);
    }


    /**
     * Add vertex to vertexes list and create vertex label
     * @return vertex label
     */
    public Label addVert(MouseEvent mouseEvent) {
        Label vertLabel = createVertLabel(mouseEvent);

        /*
        Create a vertex and set its properties
         */
        Vertex vertex = new Vertex();
        vertex.setVertLabel(vertLabel);
        vertex.setVisited(false);

        // Add vertex to vertexes list in graph
        graph.getVertexes().add(vertex);

        return vertLabel;
    }

    /**
     * Create vertex label and set its properties
     * @return vertex label
     */
    public Label createVertLabel(MouseEvent mouseEvent) {
        double vertLayoutX = Math.min(mouseEvent.getX() - RADIUS, mainPane.getWidth() - RADIUS * 2);
        double vertLayoutY = Math.min(mouseEvent.getY() - RADIUS, mainPane.getHeight() - RADIUS * 2);

        double[] vertLayout = getVertCoordinates(vertLayoutX, vertLayoutY);

        Label vertLabel = new Label(graph.getVertName().getFirst());
        vertLabel.setId("vertLabel");
        vertLabel.setLayoutX(vertLayout[0]);
        vertLabel.setLayoutY(vertLayout[1]);
        vertLabel.setText(graph.getVertName().getLast());
        graph.getVertName().removeLast();
        vertLabel.setFont(new Font("System Bold", 24));

        return vertLabel;
    }

    /**
     * Remove vertex by vertex label
     * @param vertLabel vertex label
     */
    public void deleteVert(Label vertLabel) {
        history.push(createCopyGraph());
        Vertex vertex = graph.findVertByLabel(vertLabel);
        graph.getVertName().add(vertex.getName());
        // Lambda expression to sort vertName list
        graph.getVertName().sort((s1, s2) -> Integer.compare(Integer.parseInt(s2), Integer.parseInt(s1)));
        graph.getVertexes().remove(vertex);
    }

    /**
     * Add edges to the graph and display it on the UI
     * @param startVertLabel begin vertex label
     * @param endVertLabel end vertex label
     * @return an edge that have start, end vertex label and its weight
     */
    public Line addEdge(Label startVertLabel, Label endVertLabel) {
        double[] coordinates = calculateCoordinates(startVertLabel, endVertLabel, RADIUS);

        Line lineEdge = new Line();
        lineEdge.setStartX(coordinates[0]);
        lineEdge.setStartY(coordinates[1]);
        lineEdge.setEndX(coordinates[2]);
        lineEdge.setEndY(coordinates[3]);
        lineEdge.setStyle("-fx-stroke-width: 2px;");
        mainPane.getChildren().add(lineEdge);

        addEventToEdge(lineEdge);

        return lineEdge;
    }

    /**
     * Delete edge by edge line
     * @param edgeLine edge line
     */
    public void deleteEdge(Line edgeLine) {
        history.push(createCopyGraph());
        Edge edge = graph.getEdgeByEdgeLine(edgeLine);
        graph.getEdges().remove(edge);
        mainPane.getChildren().remove(edgeLine);
        mainPane.getChildren().remove(edge.getWeightLabel());
    }

    /**
     * Calculate the coordinates so that the distance of the line connecting two vertices is shortest
     * @return an array with 4 coordinates in order startX, startY, endX, endY
     */
    public double[] calculateCoordinates(Label startVertLabel, Label endVertLabel, int radius) {

        // coordinates variable is considered as coordinates of a line in order startX, startY, endX, endY
        double[] coordinates = new double[4];

        double angle = Math.atan2(endVertLabel.getLayoutY() - startVertLabel.getLayoutY(),
                endVertLabel.getLayoutX() - startVertLabel.getLayoutX());

        coordinates[0] = (startVertLabel.getLayoutX() + radius) + (radius * Math.cos(angle)); // startX
        coordinates[1] = (startVertLabel.getLayoutY() + radius) + (radius * Math.sin(angle)); //startY
        coordinates[2] = (endVertLabel.getLayoutX() + radius) - (radius * Math.cos(angle)); // endX
        coordinates[3] = (endVertLabel.getLayoutY() + radius) - (radius * Math.sin(angle)); // endY

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

    /**
     * Change edge weight
     * @param weight the clicked weight
     */
    private void changeWeight(Label weight) {
        Edge edge = graph.getEdgeByWeightLabel(weight);
        mainPane.getChildren().remove(edge.getWeightLabel());
        showInputWeightDialog(edge.getBeginVert(), edge.getEndVert());
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
        saveBtn.setFocusTraversable(false);
        openBtn.setFocusTraversable(false);

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

    /**
     * Add event to the specified vertex
     * @param vertLabel vertex label
     */
    public void addEventToVert(Label vertLabel) {
        vertLabel.setOnMouseClicked(this::vertOnClick);
        vertLabel.setOnMousePressed(this::vertOnPress);
        vertLabel.setOnMouseDragged(this::vertOnDrag);
        vertLabel.setOnMouseReleased(this::vertOnRelease);
    }

    /**
     * Add event to the specified edge
     * @param edgeLine edge line
     */
    public void addEventToEdge(Line edgeLine) {
        edgeLine.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1 && isEnableDelete) {
                history.push(this.graph);
                deleteEdge(edgeLine);
            }
        });
    }

    /**
     * Add event to the specified weight
     * @param weight weight label
     */
    public void addEventToWeight(Label weight) {
        weight.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                changeWeight(weight);
            }
        });
    }

    /**
     * Get the vertex released
     * @param mouseEvent mouse event, where the vertex released
     * @return vertex label if the pointer inside a vertex, else null
     */
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

    /**
     * Show alert that can modify title and content
     * @param title title
     * @param content content
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Get the vertex coordinates, which have x layout is vertLayoutX and y layout is vertLayoutY
     * @return vertex coordinates in layout (x, y)
     */
    private double[] getVertCoordinates(double vertLayoutX, double vertLayoutY) {
        if (vertLayoutX <= 0) {
            vertLayoutX = 0;
        }
        if (vertLayoutX >= mainPane.getWidth() - RADIUS * 2) {
            vertLayoutX = mainPane.getWidth() - RADIUS * 2;
        }
        if (vertLayoutY <= 0) {
            vertLayoutY = 0;
        }
        if (vertLayoutY >= mainPane.getHeight() - RADIUS * 2) {
            vertLayoutY = mainPane.getHeight() - RADIUS * 2;
        }

        return new double[]{vertLayoutX, vertLayoutY};
    }

    /**
     * Reset all vertexes' visited state
     */
    private void resetVisitedVert() {
        for (Vertex vertex : graph.getVertexes()) {
            vertex.setVisited(false);
        }
    }

    /**
     * get vertex by name of vertex
     * @param name name of vertex
     * @return vertex
     */
    public Vertex getVertByStringName(String name) {
        Vertex resultVert = new Vertex();
        for (Vertex vertex : graph.getVertexes()) {
            if (Objects.equals(vertex.getName(), name)) {
                resultVert = vertex;
            }
        }
        return resultVert;
    }

    private Graph createCopyGraph() {
        Graph copyGraph = new UndirectedGraph();
        List<Vertex> vertexes = new ArrayList<>(graph.getVertexes());
        List<Edge> edges = new ArrayList<>(graph.getEdges());

        for (Vertex vertex : vertexes) {
            copyGraph.getVertexes().add(vertex);
            copyGraph.getVertName().removeLast();
        }

        for (Edge edge : edges) {
            copyGraph.getEdges().add(edge);
        }

        return copyGraph;
    }

    private void loadGraphFromHistory(Graph loadedGraph) {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(canvas);

        List<Vertex> vertexes = loadedGraph.getVertexes();
        List<Edge> edges = loadedGraph.getEdges();

        for (Vertex vertex : vertexes) {
            Label vertLabel = vertex.getVertLabel();
            mainPane.getChildren().add(vertLabel);
            addEventToVert(vertLabel);
        }

        for (Edge edge : edges) {
            Line edgeLine = edge.getLineEdge();
            Label weightLabel = edge.getWeightLabel();
            mainPane.getChildren().add(edgeLine);
            mainPane.getChildren().add(weightLabel);
            addEventToEdge(edgeLine);
            addEventToWeight(weightLabel);
        }
    }

    private Background generateBackgroundColor(Color color) {
        BackgroundFill backgroundFill = new BackgroundFill(color, new CornerRadii(10.0, true), null);
        return new Background(backgroundFill);
    }

    private void resetBackgroundColor() {
        Background background = new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10.0, true), null));
        for (Vertex vertex : graph.getVertexes()) {
            vertex.getVertLabel().setBackground(background);
        }
    }

    private void resetLineColor() {
        for (Edge edge : graph.getEdges()) {
            edge.getLineEdge().setStroke(Color.BLACK);
            edge.getLineEdge().setStrokeWidth(1.0);
        }
    }
}
