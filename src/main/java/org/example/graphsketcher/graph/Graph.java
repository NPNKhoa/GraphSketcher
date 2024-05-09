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
    private final int INFINITY = Integer.MAX_VALUE;

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

        for (int i = 100; i >= 1; i--) {
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

    public List<Vertex> dijsktra(Vertex startVert, Vertex endVert) {
        Map<Vertex, Integer> distance = new HashMap<>();
        Map<Vertex, Vertex> previous = new HashMap<>();
        Queue<Vertex> unvisitedVert = getUnvisitedVert();

        if (Integer.parseInt(startVert.getName()) > Integer.parseInt(endVert.getName())) {
            unvisitedVert = getUnvisitedVertReverse();
        }

        // Initialize distances
        for (Vertex vertex : vertexes) {
            distance.put(vertex, vertex == startVert ? 0 : INFINITY);
            previous.put(vertex, null);
        }

        while (!unvisitedVert.isEmpty()) {
            Vertex currentVert = unvisitedVert.poll();
            List<Edge> neighbors = getAllEdgesByVert(currentVert);

            for (Edge edge : neighbors) {
                Vertex neighbor = edge.getBeginVert() == currentVert ? edge.getEndVert() : edge.getBeginVert();
                int newDistance = distance.get(currentVert) + edge.getWeight();

                if (newDistance < distance.get(neighbor)) {
                    distance.put(neighbor, newDistance);
                    previous.put(neighbor, currentVert);
                }
            }
        }

        return reconstructPath(previous, endVert);
    }

    /**
     * find the minimum weight cycle that passes through all the vertexes in the graph
     * @param startVert start vertex
     * @return the minimum weight cycle
     */
    public List<Vertex> findMinimumWeightCycle(Vertex startVert) {
        Vertex endVert = vertexes.getLast();

        List<Edge> copyEdges = new ArrayList<>(edges);

        List<Vertex> pathFromStartToEnd = dijsktra(startVert, endVert);


        List<Edge> traveledEdges = getEdgesFromShortestPath(pathFromStartToEnd);
        edges.removeAll(traveledEdges);

        List<Vertex> pathFromEndToStart = dijsktra(endVert, startVert);

        List<Vertex> cycle = new ArrayList<>(pathFromStartToEnd);
        cycle.addAll(pathFromEndToStart.subList(1, pathFromEndToStart.size()));

        edges.clear();
        edges.addAll(copyEdges);
        return cycle;
    }

    /**
     * Prim algorithm to find minimum spanning tree
     * @param startVert start vertex
     * @return minimum spanning tree
     */
    public List<Edge> prim(Vertex startVert) {
        // Initialize
        List<Edge> mst = new ArrayList<>();
        Set<Vertex> visited = new HashSet<>();

        // Add start vertex the set representing the minimum spanning tree.
        visited.add(startVert);

        // Create a priority queue to store the edges to be processed, sorted by weight
        PriorityQueue<Edge> edgesQueue = new PriorityQueue<>(Comparator.comparingInt(Edge::getWeight));

        // Add all edges of the starting vertex to the queue
        edgesQueue.addAll(getAllEdgesByVert(startVert));

        while (!edgesQueue.isEmpty()) {
            // Find the minimum-weight edge that connects a vertex in the tree to a vertex not in the tree.
            Edge minEdge = edgesQueue.poll();
            Vertex u = minEdge.getBeginVert();
            Vertex v = minEdge.getEndVert();

            // If the edge connects a vertex in the tree to a vertex not in the tree
            if (visited.contains(u) && visited.contains(v)) {
                continue;
            }

            // Add that edge to the minimum spanning tree and mark the vertex as added.
            mst.add(minEdge);
            if (!visited.contains(u)) {
                visited.add(u);
                edgesQueue.addAll(getAllEdgesByVert(u));
            }
            if (!visited.contains(v)) {
                visited.add(v);
                edgesQueue.addAll(getAllEdgesByVert(v));
            }
        }

        // Step 4: Finish when all vertices have been added to the minimum spanning tree.
        return mst;
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
            if (edge.getBeginVert() != vertex && !edge.getBeginVert().isVisited()) {
                neighbors.add(edge.getBeginVert());
            }
            else if (edge.getEndVert() != vertex && !edge.getEndVert().isVisited()) {
                neighbors.add(edge.getEndVert());
            }
        }
        return neighbors;
    }

    private Queue<Vertex> getUnvisitedVert() {
        Queue<Vertex> result = new LinkedList<>();
        for (Vertex vertex : vertexes) {
            if (!vertex.isVisited()) {
                result.add(vertex);
            }
        }
        return result;
    }

    private Queue<Vertex> getUnvisitedVertReverse() {
        Queue<Vertex> result = new LinkedList<>();
        for (int i = vertexes.size() - 1; i >= 0; i--) {
            if (!vertexes.get(i).isVisited()) {
                result.add(vertexes.get(i));
            }
        }
        return result;
    }

    private List<Vertex> reconstructPath(Map<Vertex, Vertex> previous, Vertex endVert) {
        List<Vertex> path = new ArrayList<>();
        Vertex currentVertex = endVert;
        while (currentVertex != null) {
            path.add(currentVertex);
            currentVertex = previous.get(currentVertex);
        }
        Collections.reverse(path);
        return path;
    }

    private List<Edge> getEdgesFromShortestPath(List<Vertex> shortestPath) {
        List<Edge> result = new ArrayList<>();

        for (Edge edge : edges) {
            for (int i = 0; i < shortestPath.size() - 1; i++) {
                if (edge == getExactlyEdge(shortestPath.get(i), shortestPath.get(i + 1))) {
                    result.add(edge);
                }
            }
        }

        return result;
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

    /**
     * Tìm đỉnh dựa trên tên của đỉnh
     * @param name tên
     * @return đỉnh tuơng ứng
     */
    public Vertex findVertByName(String name) {
        for(int i = 0; i < vertexes.size(); i++)
            if(vertexes.get(i).getName().compareTo(name) == 0)
                return vertexes.get(i);
        return null;
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

    public Edge getExactlyEdge(Vertex beginVert, Vertex endVert) {
        for (Edge edge : edges) {
            if (edge.getBeginVert() == beginVert && edge.getEndVert() == endVert) {
                return edge;
            }
        }
        return null;
    }

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
