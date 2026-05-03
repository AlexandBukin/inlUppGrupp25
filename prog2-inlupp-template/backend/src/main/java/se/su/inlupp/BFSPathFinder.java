package se.su.inlupp;

import java.util.*;

public class BFSPathFinder<T> implements PathFinder<T> {

  @Override
  public Path<T> findPath(Graph<T> graph, T from, T to) {

      HashSet<T> visitedNodes = new HashSet<>();
      LinkedList<T> queue = new LinkedList<>();
      Map<T, T> parent = new HashMap<>();

      queue.add(from);
      visitedNodes.add(from);
      while(!queue.isEmpty()) {

          T current = queue.poll();
          if(current.equals(to)){
              LinkedList<Edge<T>> path = new LinkedList<>();
              T node = to;

              while (!node.equals(from)) {
                  T prev = parent.get(node);
                  Edge<T> edge = graph.getEdgeBetween(prev, node);
                  path.addFirst(edge);
                  node = prev;
              }

              return new GraphPath<>(from, to, path);
          }

          for(Edge<T> edge: graph.getEdgesFrom(current)) {
              if(!visitedNodes.contains(edge.getDestination())){
                  queue.add(edge.getDestination());
                  parent.put(edge.getDestination(), current);
                  visitedNodes.add(edge.getDestination());
              }
          }
      }
      return null;
  }
}

