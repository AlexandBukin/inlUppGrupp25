package se.su.inlupp;

import java.util.HashSet;
import java.util.LinkedList;

public class DFSPathFinder<T> implements PathFinder<T> {

    @Override
    public Path<T> findPath(Graph<T> graph, T from, T to) {
        HashSet<T> visited = new HashSet<>();
        LinkedList<Edge<T>> result = dfs(graph, from, to, visited);
        if (result == null) {
            return null;
        }
        return new GraphPath<>(from, to, result);
    } 

    private LinkedList<Edge<T>> dfs(Graph<T> graph, T current, T target, HashSet<T> visited) {
      if (current.equals(target)) {
        return new LinkedList<>();
      }
      visited.add(current);

    for (Edge<T> edge : graph.getEdgesFrom(current)) {
      T next = edge.getDestination();
      if (!visited.contains(next)) {
        LinkedList<Edge<T>> path = dfs(graph, next, target, visited);
        if (path != null) {
          path.addFirst(edge);
          return path;
        }
      }
    }
    return null;
  }
} 

