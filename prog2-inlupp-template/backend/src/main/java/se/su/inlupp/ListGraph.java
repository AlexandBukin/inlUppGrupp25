package se.su.inlupp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class ListGraph<T> implements Graph<T> {
  Map<T, List<Edge<T>>> adjecencyListMap = new HashMap<>();

  @Override
  public void add(T node) {
    adjecencyListMap.putIfAbsent(node, new ArrayList<>());
  }

  @Override
  public void remove(T node) {
    if (!adjecencyListMap.containsKey(node)) {
      throw new NoSuchElementException("The graph has no such element");
    }

    for (T t : adjecencyListMap.keySet()) {
      adjecencyListMap.get(t).removeIf(edge -> edge.getDestination().equals(node));
    }
    adjecencyListMap.remove(node);
  }

  @Override
  public boolean hasNode(T node) {
    return adjecencyListMap.containsKey(node);
  }

  @Override
  public void connect(T node1, T node2, String name, int weight) {
    if (!adjecencyListMap.containsKey(node1) || !adjecencyListMap.containsKey(node2)) {
      throw new NoSuchElementException("The graph doesn't contain this object");
    }

    if (weight < 0) {
      throw new IllegalArgumentException("Weight can't be negative");
    }

    if (getEdgeBetween(node1, node2) != null) {
      throw new IllegalStateException("Edge already exsists");
    }

    adjecencyListMap.get(node1).add(new GraphEdge<>(weight, name, node2));
    adjecencyListMap.get(node2).add(new GraphEdge<>(weight, name, node1));
  }

  @Override
  public void disconnect(T node1, T node2) {
    if (!adjecencyListMap.containsKey(node1) || !adjecencyListMap.containsKey(node2)) {
      throw new NoSuchElementException("The object does not exsist");
    }

    if (getEdgeBetween(node1, node2) == null) {
      throw new IllegalStateException("There is no edge between these nodes");
    }

    adjecencyListMap.get(node1).removeIf(edge -> edge.getDestination().equals(node2));
    adjecencyListMap.get(node2).removeIf(edge -> edge.getDestination().equals(node1));
  }

  @Override
  public void setConnectionWeight(T node1, T node2, int weight) {
    if (!adjecencyListMap.containsKey(node1) || !adjecencyListMap.containsKey(node2)) {
      throw new NoSuchElementException("The object does not exsist");
    }

    if (weight < 0) {
      throw new IllegalArgumentException("Weight can't be negative");
    }

    Edge<T> edge1 = getEdgeBetween(node1, node2);
    if (edge1 == null) {
      throw new NoSuchElementException("No edges exsist between these nodes");
    }
    edge1.setWeight(weight);
    
    Edge<T> edge2 = getEdgeBetween(node2, node1);
    if (edge2 == null) {
      throw new NoSuchElementException("No edges exsist between these nodes");
    }
    edge2.setWeight(weight);
  }

  @Override
  public Set<T> getNodes() {
    return new HashSet<>(adjecencyListMap.keySet());
  }

  @Override
  public Collection<Edge<T>> getEdgesFrom(T node) {
    if (!adjecencyListMap.containsKey(node)) {
      throw new NoSuchElementException("Node not found");
    }
    return new ArrayList<>(adjecencyListMap.get(node));
  }

  @Override
  public Edge<T> getEdgeBetween(T node1, T node2) {
    if (!adjecencyListMap.containsKey(node1) || !adjecencyListMap.containsKey(node2)) {
      throw new NoSuchElementException("Object not found");
    }

    for (Edge<T> edge : adjecencyListMap.get(node1)) {
      if (edge.getDestination().equals(node2)) {
        return edge;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return adjecencyListMap.toString();
  }

  @Override
  public Iterator<T> iterator() {
    return adjecencyListMap.keySet().iterator();
  }
}

