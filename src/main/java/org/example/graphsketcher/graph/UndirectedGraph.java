package org.example.graphsketcher.graph;

import java.util.ArrayList;
import java.util.List;

public class UndirectedGraph extends Graph {
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
    public List<Edge> getAllEdgesByVert(Vertex vertex) {
        List<Edge> edgeList = new ArrayList<>();
        for (Edge edge : getEdges()) {
            if (edge.getBeginVert() == vertex || edge.getEndVert() == vertex) {
                edgeList.add(edge);
            }
        }
        return edgeList;
    }

    @Override
    public Edge getEdgeByVert(Vertex beginVert, Vertex endVert) {
        if (beginVert == endVert) return null;
        for (Edge edge : getEdges()) {
            if ((edge.getBeginVert() == beginVert && edge.getEndVert() == endVert) ||
                    (edge.getBeginVert() == endVert || edge.getEndVert() == beginVert)) {
                return edge;
            }
        }
        return null;
    }
}
