package org.example.graphsketcher.graph;

import javafx.scene.control.Label;

public class Vertex {
    private Label vertLabel;
    private boolean isVisited;

    /**
     * Default constructor
     */
    public Vertex() {
        this.vertLabel = new Label();
        this.isVisited = false;
    }

    /**
     * Copy constructor
     */
    public Vertex(Vertex vertex) {
        this.vertLabel = vertex.vertLabel;
        this.isVisited = vertex.isVisited;
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
    public boolean isVisited() {
        return isVisited;
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
     * @param visited is visited or not
     */
    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    /**
     * Get vertex name in string
     * @return vertex name
     */
    public String getName() {
        return vertLabel.getText();
    }
}
