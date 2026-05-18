package se.su.inlupp;

import java.util.*;

public final class PathImpl<T> implements Path<T> {


    private final T startNode;
    private final List<Edge<T>> edges;
    private final List<T> nodes = new ArrayList<>();

    public PathImpl(T startNode, List<Edge<T>> edges) {
        this.startNode = startNode;
        this.edges = new ArrayList<>(edges);
        nodes.add(startNode);
        for (Edge<T> edge : edges) {
            T nextNode = edge.getDestination();
            nodes.add(nextNode);
        }
    }

    public T getStart() {
        return startNode;
    }

    public T getEnd() {
        return nodes.get(nodes.size() - 1);
    }

    public int getTotalWeight() {
        int totalWeight = 0;
        for (Edge<T> edge : edges) {
            totalWeight += edge.getWeight();
        }
        return totalWeight;
    }

    public List<Edge<T>> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    public List<T> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    @Override
    public Iterator<Edge<T>> iterator() {
        return getEdges().iterator();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getStart()).append('-').append(getEnd());
        sb.append('\n');
        for (Edge<T> edge : edges) {
            sb.append(edge);
            sb.append('\n');
        }
        sb.append(getTotalWeight());
        return sb.toString();
    }
}
