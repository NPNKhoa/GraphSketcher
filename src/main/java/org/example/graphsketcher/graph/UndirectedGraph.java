package org.example.graphsketcher.graph;

public abstract class UndirectedGraph extends Graph {
    @Override
    public Edge getEdgeByVert(Vertex vertex) {
        for (Edge edge : getEdges()) {
            if (edge.getBeginVert() == vertex || edge.getEndVert() == vertex) {
                return edge;
            }
        }
        return null;
    }

    @Override
    public Edge getEdgeByVert(Vertex beginVert, Vertex endVert) {
        for (Edge edge : getEdges()) {
            if ((edge.getBeginVert() == beginVert && edge.getEndVert() == endVert) ||
                    (edge.getBeginVert() == endVert || edge.getEndVert() == beginVert)) {
                return edge;
            }
        }
        return null;
    }
}