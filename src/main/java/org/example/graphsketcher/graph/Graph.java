package org.example.graphsketcher.graph;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import java.util.*;

public abstract class Graph {
    protected List<Vertex> vertexes;
    protected List<Edge> edges;

    // A list that save vert name
    protected  List<String> vertName;
    protected Set<Color> colors;

    // Default constructor
    public Graph() {
        this.vertexes = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.vertName = new ArrayList<>();
        this.colors = new HashSet<>();

        for (int i = 100; i >= 1; i --) {
            vertName.add(String.valueOf(i));
        }

        generateRandomUniqueColor(colors);
    }

    /**
     * Reset graph to initial state
     */
     public void resetGraph() {
        vertexes.clear();
        edges.clear();
        vertName.clear();
        colors.clear();

        for (int i = 100; i >= 1; i --) {
            vertName.add(String.valueOf(i));
        }

        generateRandomUniqueColor(colors);
    }

    /**
     * Get vertexes list
     * @return Vertexes list
     */
    public List<Vertex> getVertexes() {
        return this.vertexes;
    }

    /**
     * Get edges list
     * @return edges list
     */
    public List<Edge> getEdges() {
        return this.edges;
    }

    public List<String> getVertName() {
        return vertName;
    }

    // =================================== ALGORITHMS =======================================

    /**
     * Depth first search algorithm, travel form any vertex to all vertexes
     * @return A list of vertex traveled
     */
    public List<Vertex> depthFirstSearch(Vertex startVert) {
        List<Vertex> result = new ArrayList<>();
        Stack<Vertex> vertexStack = new Stack<>();

        if (!vertexes.isEmpty()) {
            vertexStack.push(startVert);
        }

        while (!vertexStack.isEmpty()) {
            Vertex currentVert = vertexStack.pop();

            if (currentVert.isVisited()) {
                result.add(currentVert);

                List<Vertex> neighbors = getUnvisitedNeighbors(currentVert);
                for (Vertex neighbor : neighbors) {
                    vertexStack.push(neighbor);
                }
            }
        }
        return  result;
    }

    // =================================== SUB-METHODS ===========================================

    /**
     * Generate random color and make it unique
     */
    private void generateRandomUniqueColor(Set<Color> colors) {
        double red = Math.random();
        double green = Math.random();
        double blue = Math.random();

        Color randomColor = new Color(red, green, blue, 1.0);

        colors.add(randomColor);
    }

    /**
     * Get vertex neighbors if it unvisited
     * @param vertex current vertex
     * @return vertexes list
     */
    private List<Vertex> getUnvisitedNeighbors(Vertex vertex) {
        List<Vertex> neighbors = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.getBeginVert().equals(vertex) && !edge.getBeginVert().isVisited()) {
                neighbors.add(edge.getBeginVert());
            }
            else if (edge.getEndVert().equals(vertex) && !edge.getEndVert().isVisited()) {
                neighbors.add(edge.getEndVert());
            }
        }
        return neighbors;
    }

    // ============================== EVENT HANDLER METHODS ======================================

    /**
     * Add vertex to vertexes list and create vertex label
     * @return vertex label
     */
    public Label addVert(MouseEvent mouseEvent) {
        /*
        Create vertex label and set its properties
         */
        Label vertLabel = createVertLabel(mouseEvent);
        vertLabel.setLayoutX(mouseEvent.getX());
        vertLabel.setLayoutY(mouseEvent.getY());
        vertLabel.setText(getVertName().getLast());
        getVertName().removeLast();
        vertLabel.getStyleClass().add("vertLabel");

        /*
        Create a vertex and set its properties
         */
        Vertex vertex = new Vertex();
        vertex.setVertLabel(vertLabel);
        vertex.setVisited(false);

        // Add vertex to vertexes list in graph
        vertexes.add(vertex);

        return vertLabel;
    }

    /**
     * Remove vertex by vertex label
     * @param vertLabel vertex label
     */
    public void deleteVert(Label vertLabel) {
        Vertex vertex = findVertByLabel(vertLabel);
        vertName.add(vertex.getName());
        // Lambda expression to sort vertName list
        vertName.sort((s1, s2) -> Integer.compare(Integer.parseInt(s2), Integer.parseInt(s1)));
        vertexes.remove(vertex);
    }

    /**
     * Create vertex label
     * @return vertex label
     */
    public Label createVertLabel(MouseEvent mouseEvent) {
        Label vertLabel = new Label(vertName.getFirst());
        vertLabel.setFont(new Font("System Bold", 24));
        vertLabel.setId("vertLabel");
        vertLabel.setLayoutX(mouseEvent.getX() - (vertLabel.getWidth() / 2));
        vertLabel.setLayoutY(mouseEvent.getY() - (vertLabel.getHeight() / 2));
        return vertLabel;
    }

    /**
     * Find vertex by vertex label
     * @param vertLabel vertex label
     * @return vertex where vertex label equal to vertLabel parameter
     */
    public Vertex findVertByLabel(Label vertLabel) {
        Vertex result = new Vertex();
        for (Vertex v : vertexes) {
            if (v.getVertLabel() == vertLabel) {
                result = v;
            }
        }
        return result;
    }

    // ============================== ABSTRACT METHODS ======================================

    /**
     * Find edge by a vertex
     */
    public abstract Edge getEdgeByVert(Vertex vertex);

    /**
     * Find edge by begin vertex and end vertex
     * @return an edge connecting 2 vertex passed into
     */
    public abstract Edge getEdgeByVert(Vertex beginVert, Vertex endVert);
}
