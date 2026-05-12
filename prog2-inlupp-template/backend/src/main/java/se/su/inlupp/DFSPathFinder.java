package se.su.inlupp;

import java.util.HashSet;
import java.util.LinkedList;

public class DFSPathFinder<T> implements PathFinder<T> {

    HashSet<T> visitedNodes = new HashSet<>();

    @Override
    public Path<T> findPath(Graph<T> graph, T from, T to) {
        visitedNodes.clear();
        LinkedList<Edge<T>> result = dfs(graph, from, to);
        if (result == null) {
            System.out.println("No Path Found!");
            return null;
        }
        return new GraphPath<>(from, to, result);
    }
    private LinkedList<Edge<T>> dfs(Graph<T> graph, T current, T target) {
        if (current.equals(target)) {
            return new LinkedList<>();
        }
        visitedNodes.add(current);
        for (Edge<T> edge : graph.getEdgesFrom(current)) {
            T next = edge.getDestination();
            if (!visitedNodes.contains(next)) {
                LinkedList<Edge<T>> path = dfs(graph, next, target);
                if (path != null) {
                    path.addFirst(edge);
                    return path;
                }
            }
        }
        return null;
    }
}

