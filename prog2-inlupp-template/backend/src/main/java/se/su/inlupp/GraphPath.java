package se.su.inlupp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class GraphPath<T> implements Path<T> {
    
    T startNode;
    T endNode;
    List<Edge<T>> edges = new ArrayList<>();
    List<T> nodes = new ArrayList<>();

    public GraphPath(T startNode, T endNode, List<Edge<T>> path) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.edges = path;

        nodes.add(startNode);
        for (Edge<T> edge : path) {
            nodes.add(edge.getDestination());
        }
    }

    @Override
    public T getStart() {
        return startNode;
    }

    @Override
    public T getEnd() {
        return endNode;
    }

    @Override
    public int getTotalWeight() {
        int totalWeight = 0;
        for (Edge<T> edge : getEdges()) {
            totalWeight += edge.getWeight();
        }
        return totalWeight;
    }

    @Override
    public List<Edge<T>> getEdges() {
        return Collections.unmodifiableList(edges);
    }


    @Override
    public List<T> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    @Override
    public Iterator<Edge<T>> iterator() {
        return edges.iterator();
    }

    @Override
    public String toString() {
        return "Start: " + startNode + " End: " + endNode + " (Path: " + getNodes() + ") " + "(Total Weight: " + getTotalWeight() + ")";
    }

}
