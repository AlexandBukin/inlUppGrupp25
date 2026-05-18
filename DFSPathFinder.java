package se.su.inlupp;

import java.util.*;

public class DFSPathFinder<T> implements PathFinder<T> {


    @Override
    public Path<T> findPath(Graph<T> graph, T from, T to) {
        List<Edge<T>> savedEdges = new ArrayList<>();
        HashSet<T> visitedNodes = new HashSet<>();
        if(dfs(graph, from, to, visitedNodes, savedEdges)){
            return new PathImpl<>(from , savedEdges);
        }
        return null;
    }

    public boolean dfs(Graph<T> graph, T current, T target, Set<T> visitedNodes, List<Edge<T>> savedEdges) {
        visitedNodes.add(current);
        if (current.equals(target)) {
            return true;
        } else {

            for (Edge<T> edge : graph.getEdgesFrom(current)) {
                if (!visitedNodes.contains(edge.getDestination())) {
                    savedEdges.add(edge);
                    if (dfs(graph, edge.getDestination(), target, visitedNodes, savedEdges)) {
                        return true;
                    } else {
                        savedEdges.remove(edge);
                    }
                }
            }
        }
        return false;
    }
}

