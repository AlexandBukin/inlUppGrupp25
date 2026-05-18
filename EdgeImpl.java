package se.su.inlupp;

public class EdgeImpl <T> implements Edge<T> {
    private T destination;
    private String name ;
    private int weight;

    public EdgeImpl(T destination , String name , int weight){
        this.destination = destination;
        this.name = name ;
        this.weight = weight;
    }
    public int getWeight(){
        return weight;
    }
    public void setWeight(int weight){
        if(weight < 0){
            throw new IllegalArgumentException("Vikt kan inte vara negativt");
        }
    this.weight = weight;
    }
    public T getDestination(){
        return destination;
    }
    public String getName(){
        return name;
    }

    public String toString(){
        return "till "+destination+" med "+name+" tar "+weight;
    }

}
