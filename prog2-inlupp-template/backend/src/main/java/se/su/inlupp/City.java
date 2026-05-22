package se.su.inlupp;

import java.util.Objects;

public class City {
    private String name;
    private double x;
    private double y;

    public City(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.equals(obj)) {
            return true;
        }
        if (!(obj instanceof City)) {
            return false;
        }

        City other = (City) obj;
        if (other.name.equals(this.getName())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
