package se.su.inlupp;

import java.util.*;

public class ListGraph<T> implements Graph<T>, Iterable<T> {
    Map<T, List<Edge<T>>> adjecencyListMap = new HashMap<>();

  @Override
  public void add(T node) {
    adjecencyListMap.putIfAbsent(node, new ArrayList<>());
  }

  @Override
  public void remove(T node) {
      if (!adjecencyListMap.containsKey(node)) {
          throw new NoSuchElementException("Graph has no such element!");
      }
      for(T t: adjecencyListMap.keySet()){
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
      if(!adjecencyListMap.containsKey(node1) || !adjecencyListMap.containsKey(node2)) {
          throw new NoSuchElementException("Graph does not contain this object!");
      }
      if(weight<0) {
          throw new IllegalArgumentException("Weight can not be negative!");
      }
      //Keep an eye on this below, might break?
      if(getEdgeBetween(node1,node2)!=null){
          throw new IllegalStateException("Edge already exists!");
      }
      adjecencyListMap.get(node1).add(new GraphEdge<>(weight,name,node2));
      adjecencyListMap.get(node2).add(new GraphEdge<>(weight,name,node1));
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
      //Looks nearly identical to disconnect, could use a help method!
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
      return new HashSet<>(adjecencyListMap.keySet());
  }

  @Override
  public Collection<Edge<T>> getEdgesFrom(T node) {
      if(!adjecencyListMap.containsKey(node)) {
          throw new NoSuchElementException("Object was not found!");
      }
      return new ArrayList<>(adjecencyListMap.get(node));
  }

  @Override
  public Edge<T> getEdgeBetween(T node1, T node2) {
      //Similar to setConnectionWeight and disconnect, could maybe use similar help method?
      if (!adjecencyListMap.containsKey(node1) || !adjecencyListMap.containsKey(node2)) {
          throw new NoSuchElementException("Object does not exist!");
      }
      for (Edge<T> edge : adjecencyListMap.get(node1)) {
          if (edge.getDestination() == node2) {
              return edge;
          }
      }
      return null;
  }

  @Override
  public Iterator<T> iterator() {
      return adjecencyListMap.keySet().iterator();
  }

    @Override
    public String toString() {
            StringBuilder sb = new StringBuilder();
    for(T n : adjacencyList.keySet()){
      sb.append(n).append(adjacencyList.get(n));
      sb.append('\n');
    }
    return sb.toString();
    }
}

