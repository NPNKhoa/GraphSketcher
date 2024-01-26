package org.example.graphsketcher.graph;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.util.*;

public abstract class Graph {
    protected List<Vertex> vertexes;
    protected List<Edge> edges;

    // A list that save vert name
    protected  List<String> vertName;
    protected Set<Color> colors;

    // Default constuctor
    public Graph() {
        this.vertexes = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.vertName = new ArrayList<>();
        this.colors = new HashSet<>();

        for (int i = 100; i >= 1; i --) {
            vertName.add(i - 1, String.valueOf(i));
        }

        generateRandomUniqueColor(colors);
    }

    /**
     * Reset graph to initial state
     */
    protected void resetGraph() {
        vertexes.clear();
        edges.clear();
        vertName.clear();
        colors.clear();

        for (int i = 100; i >= 1; i --) {
            vertName.add(i - 1, String.valueOf(i));
        }

        generateRandomUniqueColor(colors);
    }

    /**
     * Get vertexes list
     * @return Vertexes list
     */
    List<Vertex> getVertexes() {
        return this.vertexes;
    }

    /**
     * Get edges list
     * @return edges list
     */
    List<Edge> getEdges() {
        return this.edges;
    }


    // =================================== PRIMARY ALGORITHMS =======================================

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

            if (currentVert.isVisted()) {
                result.add(currentVert);

                List<Vertex> neighbors = getUnvisitedNeighbors(currentVert);
                for (Vertex neighbor : neighbors) {
                    vertexStack.push(neighbor);
                }
            }
        }
        return  result;
    }


    // =================================== SUB METHODS ===========================================

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
            if (edge.getBeginVert().equals(vertex) && !edge.getBeginVert().isVisted()) {
                neighbors.add(edge.getBeginVert());
            }
            else if (edge.getEndVert().equals(vertex) && !edge.getEndVert().isVisted()) {
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
        Label vertLabel = createVertLabel(mouseEvent);

        /*
        Create a vertex and set its property
         */
        Vertex vertex = new Vertex();
        vertex.setVertLabel(vertLabel);
        vertex.setVisted(false);

        // Add vertex to vertexes list in graph
        vertexes.add(vertex);

        return vertLabel;
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
