package se.su.inlupp;

import java.util.*;

public class ListGraph<T> implements Graph<T> , Iterable<T>{
  private Map<T , List<Edge<T>>> adjacencyList;

  public ListGraph(){
    adjacencyList = new HashMap<>();
  }

  @Override
  public void add(T node) {
    adjacencyList.computeIfAbsent(node , k -> new ArrayList<>());
  }

  @Override
  public void remove(T node) {
    if(!adjacencyList.containsKey(node)){
      throw new NoSuchElementException("Denna nod finns inte.");
    }
    for(T n : adjacencyList.keySet()){
      adjacencyList.get(n).removeIf(edge -> edge.getDestination().equals(node));
    }
    adjacencyList.remove(node);
  }

  @Override
  public boolean hasNode(T node) {
    return adjacencyList.containsKey(node);
  }

  @Override
  public void connect(T node1, T node2, String name, int weight) {
    if(!adjacencyList.containsKey(node1) || !adjacencyList.containsKey(node2)){
      throw new NoSuchElementException("Denna nod finns inte.");
    }
    if (weight < 0) {
      throw new IllegalArgumentException("Vikt får inte vara negativt");
    }
    if(getEdgeBetween(node1 , node2) != null){
      throw new IllegalStateException("Kanten redan finns!");
    }
    adjacencyList.get(node1).add(new EdgeImpl<>(node2 , name , weight));
    adjacencyList.get(node2).add(new EdgeImpl<>(node1 , name , weight));
  }

  @Override
  public void disconnect(T node1, T node2) {
    if(!adjacencyList.containsKey(node1) || !adjacencyList.containsKey(node2)) {
    throw new NoSuchElementException("Nod existerar inte");
  }
    if(getEdgeBetween(node1 , node2) == null){
      throw new IllegalStateException("Det finns ingen kant mellan " + node1 + "och" + node2);
    }
    adjacencyList.get(node1).removeIf(edge -> edge.getDestination().equals(node2));
    adjacencyList.get(node2).removeIf(edge -> edge.getDestination().equals(node1));

  }

  @Override
  public void setConnectionWeight(T node1, T node2, int weight) {
    if(!adjacencyList.containsKey(node1) || !adjacencyList.containsKey(node2)) {
      throw new NoSuchElementException("Nod existerar inte");
    }
    if(weight < 0 ){
      throw new IllegalArgumentException("Vikten får inte vara negativ");
    }
    Edge<T> edgeTo = getEdgeBetween(node1 , node2);
    if(edgeTo == null){
      throw new NoSuchElementException("Finns ingen kant");
    }
    edgeTo.setWeight(weight);

    Edge<T> edgeBack = getEdgeBetween(node2 , node1);
    if(edgeBack == null){
      throw new NoSuchElementException("Finns ingen kant");
    }
    edgeBack.setWeight(weight);
  }

  @Override
  public Set<T> getNodes() {
    HashSet<T> kopior = new HashSet<>();
    kopior.addAll(adjacencyList.keySet());
    return kopior;
  }

  @Override
  public Collection<Edge<T>> getEdgesFrom(T node) {
    if(!adjacencyList.containsKey(node)){
      throw new NoSuchElementException("Denna nod finns inte :" + node);
    }
    return new ArrayList<>(adjacencyList.get(node));
  }

  @Override
  public Edge<T> getEdgeBetween(T node1, T node2) {
    if(!adjacencyList.containsKey(node1) || !adjacencyList.containsKey(node2)){
    throw new NoSuchElementException("Nod existerar inte");
    }
    for(Edge edge : adjacencyList.get(node1)){
      if(edge.getDestination().equals(node2)){
        return edge;
      }
    }
    return null;
  }

  @Override
  public Iterator<T> iterator() {
     return adjacencyList.keySet().iterator();
  }

  @Override
  public String toString(){
    StringBuilder sb = new StringBuilder();
    for(T n : adjacencyList.keySet()){
      sb.append(n).append(adjacencyList.get(n));
      sb.append('\n');
    }
    return sb.toString();
  }
}

