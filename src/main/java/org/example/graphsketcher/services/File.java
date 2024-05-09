package org.example.graphsketcher.services;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import org.example.graphsketcher.controller.HomeController;
import org.example.graphsketcher.graph.Edge;
import org.example.graphsketcher.graph.Graph;
import org.example.graphsketcher.graph.UndirectedGraph;
import org.example.graphsketcher.graph.Vertex;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;


import java.io.*;

public class File {
    public static HomeController homeController;
    public static Pane mainPane;

    /**
     * Show window allowing user to save graph
     * @param event event that triggered the method
     */
    public static void saveGraph(MouseEvent event, Graph graph) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save graph");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text (*.txt)", "*.txt"));

        java.io.File selectedFile;
        selectedFile = fileChooser.showSaveDialog(((Node)event.getSource()).getScene().getWindow());

        if (selectedFile != null) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile));

                writer.write(String.valueOf(graph.getVertexes().size()) + "\t");
                writer.write(String.valueOf(graph.getEdges().size()));
                writer.write("\n");

                for (Vertex vertex : graph.getVertexes()) {
                    writer.write(vertex.getName() + "\t");
                    writer.write(String.valueOf(vertex.getVertLabel().getLayoutX())
                            + "\t" + String.valueOf(vertex.getVertLabel().getLayoutY()));
                    writer.write("\n");
                }

                for (Edge edge : graph.getEdges()) {
                    writer.write(edge.getBeginVert().getName() + "\t");
                    writer.write(edge.getEndVert().getName() + "\t");
                    writer.write(Integer.toString(edge.getWeight()));
                    writer.write("\n");
                }

                showDialog(Alert.AlertType.INFORMATION, "Save graph",
                        null, "Graph has been saved successfully");
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Load graph from file
     * @param event event that triggered the method
     */
    public static void loadGraph(MouseEvent event, Graph graph) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open graph");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text (*.txt)", "*.txt"));

        java.io.File selectedFile;
        selectedFile = fileChooser.showOpenDialog(((Node)event.getSource()).getScene().getWindow());

        if (selectedFile != null) {
            try {
                graph.resetGraph();

                BufferedReader reader = new BufferedReader(new FileReader(selectedFile));

                String line; // The line is currently reading
                int vertNumber = 0;
                int count = 0; // The number of the line is currently reading, the line 0 presents the number of vertexes and edges

                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\t"); // "part" is a part of the line, separated by a tab character

                    if (count == 0) {
                        vertNumber = Integer.parseInt(parts[0].trim());
                    }
                    else if (count >= 1 && count <= vertNumber) {
                        // invoke the method to add a vertex to the graph
                        graph.getVertexes().add(initVertFromFile(parts));
                        Label vertLabel = graph.getVertexes().getLast().getVertLabel();
                        mainPane.getChildren().add(vertLabel);
                        homeController.addEventToVert(vertLabel);
                        graph.getVertName().remove(parts[0]);
                    }
                    else {
                        // invoke the method to add an edge to the graph
                        addEdge(graph, parts);
                    }

                    count++;
                }

                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /************************************************************************************************
     *                                          SUB-METHOD
     ************************************************************************************************/

    /**
     * Show dialog
     * @param type        notification type
     * @param title       title
     * @param header      header
     * @param contentText content text
     */
    private static void showDialog(Alert.AlertType type, String title, String header, String contentText) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(contentText);

        alert.showAndWait();
    }

    /**
     * Initialize a vertex from data reading from file
     * @param parts parts of the line, separated by a tab character
     * @return a vertex
     */
    private static Vertex initVertFromFile(String[] parts) {

        // Initialize a label for the vertex
        Label vertLabel = new Label(parts[0]);
        vertLabel.setFont(new Font("System Bold", 24));
        vertLabel.setId("vertLabel");
        vertLabel.setLayoutX(Double.parseDouble(parts[1]));
        vertLabel.setLayoutY(Double.parseDouble(parts[2]));

        // Initialize a vertex and add the vertLabel to it
        Vertex vertex = new Vertex();
        vertex.setVertLabel(vertLabel);
        vertex.setVisited(false);

        return vertex;
    }


    /**
     * Initialize an edge from data reading from file
     * @param parts parts of the line, separated by a tab character
     */
    private static void addEdge(Graph graph, String[] parts) {
        Vertex beginVert = graph.findVertByName(parts[0]);
        Vertex endVert = graph.findVertByName(parts[1]);

        if (beginVert != null && endVert != null) {
            int iWeight = Integer.parseInt(parts[2]);

            double[] edgeCoordinates = homeController.calculateCoordinates(beginVert.getVertLabel(), endVert.getVertLabel(), 30);

            Line lineEdge = new Line();

            lineEdge.setStartX(edgeCoordinates[0]);
            lineEdge.setStartY(edgeCoordinates[1]);
            lineEdge.setEndX(edgeCoordinates[2]);
            lineEdge.setEndY(edgeCoordinates[3]);
            lineEdge.setStyle("-fx-stroke-width: 2px;");

            Edge edge = new Edge();

            edge.setLineEdge(lineEdge);
            edge.setBeginVert(beginVert);
            edge.setEndVert(endVert);
            edge.setWeight(iWeight);

            /*
                Create a label representing for edge weight
             */
            Label weightLabel = new Label();
            weightLabel.getStyleClass().add("weightLabel");
            weightLabel.setText(Integer.toString(iWeight));
            weightLabel.setLayoutX((edgeCoordinates[0] + edgeCoordinates[2]) / 2);
            weightLabel.setLayoutY((edgeCoordinates[1] + edgeCoordinates[3]) / 2);

            edge.setWeightLabel(weightLabel);

            graph.getEdges().add(edge);

            mainPane.getChildren().add(lineEdge);
            homeController.addEventToEdge(lineEdge);
            mainPane.getChildren().add(weightLabel);
            homeController.addEventToWeight(weightLabel);
        }
    }

}