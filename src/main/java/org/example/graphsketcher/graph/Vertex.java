package org.example.graphsketcher.graph;

import javafx.scene.control.Label;

public class Vertex {
    private Label vertLabel;
    private boolean isVisted;

    // Default Constructor
    public Vertex() {
        this.vertLabel = new Label();
        this.isVisted = false;
    }

    // Copy Constructor
    public Vertex(Vertex vertex) {
        this.vertLabel = vertex.vertLabel;
        this.isVisted = vertex.isVisted;
    }

    /**
     * getter: Get vert label
     * @return vert label
     */
    public Label getVertLabel() {
        return vertLabel;
    }

    /**
     * Check if a vertex is visited or not
     * @return true if visited vertex
     */
    public boolean isVisted() {
        return isVisted;
    }

    /**
     * setter: Set vert label
     * @param vertLabel bert label
     */
    public void setVertLabel(Label vertLabel) {
        this.vertLabel = vertLabel;
    }

    /**
     * setter: set visited vertex
     * @param visted is visited or not
     */
    public void setVisted(boolean visted) {
        isVisted = visted;
    }

    /**
     * Get vertex name in string
     * @return vertex name
     */
    public String getName() {
        return vertLabel.getText().trim();
    }
}
