package org.example.graphsketcher.graph;

import javafx.scene.control.Label;
import javafx.scene.shape.Line;

public class Edge {
    private Vertex beginVert;
    private Vertex endVert;
    private int weight;
    private Label weightLabel;
    private Line lineEdge;

    /**
     * Default constructor
     */
    public Edge() {
        this.beginVert = new Vertex();
        this.endVert = new Vertex();
        this.weightLabel = new Label();
        this.lineEdge = new Line();
    }

    /**
     * Constructor with 3 parameters
     */
    public Edge(Vertex beginVert, Vertex endVert, int weight) {
        this.beginVert = beginVert;
        this.endVert = endVert;
        this.weight = weight;
        this.weightLabel = new Label(String.valueOf(weight));
    }

    public Edge(Edge edge) {
        this.beginVert = edge.beginVert;
        this.endVert = edge.endVert;
        this.weight = edge.weight;
        this.weightLabel = edge.weightLabel;
        this.lineEdge = edge.lineEdge;
    }

    /**
     * getter: Get begin vertex
     */
    public Vertex getBeginVert() {
        return beginVert;
    }

    /**
     * setter: set begin vertex
     */
    public void setBeginVert(Vertex beginVert) {
        this.beginVert = beginVert;
    }

    /**
     * getter: Get end vertex
     */
    public Vertex getEndVert() {
        return endVert;
    }

    /**
     * setter: set end vertex
     */
    public void setEndVert(Vertex endVert) {
        this.endVert = endVert;
    }

    /**
     * getter: Get weight
     */
    public int getWeight() {
        return weight;
    }

    /**
     * setter: Set weight
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * getter: Get weight label
     */
    public Label getWeightLabel() {
        return weightLabel;
    }

    /**
     * setter: Set weight label
     */
    public void setWeightLabel(Label weightLabel) {
        this.weightLabel = weightLabel;
    }

    /**
     * getter: Get line edge
     */
    public Line getLineEdge() {
        return lineEdge;
    }

    /**
     * setter: Set line edge
     */
    public void setLineEdge(Line lineEdge) {
        this.lineEdge = lineEdge;
    }

    public Vertex getOppositeVert(Vertex vertex) {
        if (vertex == beginVert) {
            return endVert;
        } else if (vertex == endVert) {
            return beginVert;
        }
        return null;
    }
}
