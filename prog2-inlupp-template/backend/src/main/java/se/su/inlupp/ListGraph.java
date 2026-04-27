package se.su.inlupp;

import java.util.*;

public class ListGraph<T> implements Graph<T> {
    Map<T, List<GraphEdge<T>>> adjecencyListMap = new HashMap<>();

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
          for(GraphEdge<T> edge: adjecencyListMap.get(t)){
              if(edge.getDestination()==node){
                  adjecencyListMap.remove(t);
              }
          }
      }
      adjecencyListMap.remove(node);
      //Make it so it removes itself from other nodes' AdjacencyLists too later
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
      //I do not know if this will work
      if(getEdgeBetween(node1,node2)!=null){
          throw new IllegalStateException("Edge already exists!");
      }
      adjecencyListMap.get(node1).add(new GraphEdge<>(weight,name,node2));
      adjecencyListMap.get(node2).add(new GraphEdge<>(weight,name,node1));
  }

  @Override
  public void disconnect(T node1, T node2) {
      if(!adjecencyListMap.containsKey(node1) || !adjecencyListMap.containsKey(node2)) {
          throw new NoSuchElementException("Object does not exist!");
      }
      for(GraphEdge<T> edge1: adjecencyListMap.get(node1)) {
          if(edge1.getDestination()==node2){
              adjecencyListMap.get(node1).remove(edge1);
              for(GraphEdge<T> edge2: adjecencyListMap.get(node2)) {
                  if(edge2.getDestination()==node1){
                      adjecencyListMap.get(node2).remove(edge2);
                      return;
                  }
              }
          }
      }
      throw new IllegalStateException("Found no edge between objects!");
  }

  @Override
  public void setConnectionWeight(T node1, T node2, int weight) {
      //Looks nearly identical to disconnect, could use a help method!
      if(!adjecencyListMap.containsKey(node1) || !adjecencyListMap.containsKey(node2)) {
          throw new NoSuchElementException("Object does not exist!");
      }
      for(GraphEdge<T> edge1: adjecencyListMap.get(node1)) {
          if(edge1.getDestination()==node2) {
              edge1.setWeight(weight);
              for(GraphEdge<T> edge2: adjecencyListMap.get(node2)) {
                  if (edge2.getDestination() == node1) {
                      edge2.setWeight(weight);
                      return;
                  }
              }
          }
      }
      throw new NoSuchElementException("Found no edge between objects!");
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
      for (GraphEdge<T> edge : adjecencyListMap.get(node1)) {
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
}

