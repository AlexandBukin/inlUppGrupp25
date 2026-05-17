package se.su.inlupp;

public class GraphEdge<T> implements Edge<T> {
    
    int weight;
    String name;
    T destination;

    GraphEdge(int weight, String name, T destination) {
        this.weight = weight;
        this.name = name;
        this.destination = destination;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public void setWeight(int weight) {
        if (weight < 0) {
            throw new IllegalArgumentException("The weight cannot be negative");
        }
        this.weight = weight; 
    }

    @Override
    public T getDestination() {
        return destination;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "till " + destination + "via " + name + "(vikt: " + weight + ")";
    }


}
