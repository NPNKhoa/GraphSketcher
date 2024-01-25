package org.example.graphsketcher.graph;

import javafx.scene.control.Label;
import javafx.scene.shape.Line;

public class Edge {
    private Vertex beginVert;
    private Vertex endVert;
    private int weight;
    private Label weightLabel;
    private Line edgeLine;

    /**
     * Default constructor
     */
    public Edge() {
        this.beginVert = new Vertex();
        this.endVert = new Vertex();
        this.weightLabel = new Label();
        this.edgeLine = new Line();
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


    public Vertex getBeginVert() {
        return beginVert;
    }

    public void setBeginVert(Vertex beginVert) {
        this.beginVert = beginVert;
    }

    public Vertex getEndVert() {
        return endVert;
    }

    public void setEndVert(Vertex endVert) {
        this.endVert = endVert;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Label getWeightLabel() {
        return weightLabel;
    }

    public void setWeightLabel(Label weightLabel) {
        this.weightLabel = weightLabel;
    }

    public Line getEdgeLine() {
        return edgeLine;
    }

    public void setEdgeLine(Line edgeLine) {
        this.edgeLine = edgeLine;
    }
}
