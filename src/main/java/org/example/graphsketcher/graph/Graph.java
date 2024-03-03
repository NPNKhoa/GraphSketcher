package org.example.graphsketcher.graph;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
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

    /**
     * Get vertex name
     * @return vert name list
     */
    public List<String> getVertName() {
        return vertName;
    }

    /**
     * Get color set
     * @return colors set
     */
    public Set<Color> getColors() {
        return colors;
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

            if (!currentVert.isVisited()) {
                result.add(currentVert);

                List<Vertex> neighbors = getUnvisitedNeighbors(currentVert);
                for (Vertex neighbor : neighbors) {
                    vertexStack.push(neighbor);
                }
                currentVert.setVisited(true);
            }
        }
        return result;
    }

    // =================================== SUB-METHODS ===========================================

    /**
     * Generate random color and make it unique
     * @return a set of unique color
     */
    public Set<Color> generateRandomUniqueColor(int n) {
        Set<Color> colors = new HashSet<>();

        for (int i = 0; i < n; i++) {
            double red = Math.random();
            double green = Math.random();
            double blue = Math.random();

            Color randomColor = new Color(red, green, blue, 1.0);
            colors.add(randomColor);
        }

        return colors;
    }

    /**
     * Get vertex neighbors if it unvisited
     * @param vertex current vertex
     * @return vertexes list
     */
    private List<Vertex> getUnvisitedNeighbors(Vertex vertex) {
        List<Vertex> neighbors = new ArrayList<>();
        List<Edge> edgeList = getAllEdgesByVert(vertex);
        for (Edge edge : edgeList) {
            if (edge.getBeginVert() != vertex) {
                neighbors.add(edge.getBeginVert());
            }
            else if (edge.getEndVert() != vertex) {
                neighbors.add(edge.getEndVert());
            }
        }
        return neighbors;
    }

    // ============================== EVENT HANDLER METHODS ======================================

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
     * @return edge
     */
    public abstract Edge getEdgeByVert(Vertex vertex);

    /**
     * Find all edges that connect to specified vertex
     * @param vertex vertex
     * @return edges list
     */
    public abstract List<Edge> getAllEdgesByVert(Vertex vertex);

    /**
     * Find edge by begin vertex and end vertex
     * @return an edge connecting 2 vertex passed into
     */
    public abstract Edge getEdgeByVert(Vertex beginVert, Vertex endVert);

    /**
     * Find edge by edge line
     * @param edgeLine edge line
     * @return edge if edge line exist, else null
     */
    public Edge getEdgeByEdgeLine(Line edgeLine) {
        for (Edge edge : edges) {
            if (edgeLine.equals(edge.getLineEdge())) {
                return edge;
            }
        }
        return null;
    }

    /**
     * Find edge by its weight label
     * @param weightLabel weight label
     * @return edge if the weight is existing else null
     */
    public Edge getEdgeByWeightLabel(Label weightLabel) {
        for (Edge edge : edges) {
            if (edge.getWeightLabel().equals(weightLabel)) {
                return edge;
            }
        }
        return null;
    }
}
