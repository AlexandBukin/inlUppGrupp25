package se.su.inlupp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

public class BFSPathFinder<T> implements PathFinder<T> {

  @Override
  public Path<T> findPath(Graph<T> graph, T from, T to) {
    
    HashSet<T> visited = new HashSet<>();
    LinkedList<T> queue = new LinkedList<>();
    Map<T, T> cameFrom = new HashMap<>();

    queue.add(from);
    visited.add(from);

    while (!queue.isEmpty()) {
        T current = queue.poll();
        if (current.equals(to)) {
          LinkedList<Edge<T>> path = new LinkedList<>();

          T node = to;

          while (!node.equals(from)) {
              T previous = cameFrom.get(node);
              Edge<T> edge = graph.getEdgeBetween(previous, node);
              path.addFirst(edge);
              node = previous;
          }

          return new GraphPath<>(from, to, path);
        }

        for (Edge<T> edge : graph.getEdgesFrom(current)) {
          if (!visited.contains(edge.getDestination())) {
              queue.add(edge.getDestination());
              cameFrom.put(edge.getDestination(), current);
              visited.add(edge.getDestination());
          }
        }
    }
    return null;
  }
}

