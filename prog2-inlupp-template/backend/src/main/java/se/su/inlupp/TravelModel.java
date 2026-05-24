package se.su.inlupp;


import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class TravelModel {
    private ListGraph<City> cityListGraph;
    private PathFinder<City> cityPathFinder;
    private boolean unsavedChanges;
    private String imagePath;

    public TravelModel() {
        cityListGraph = new ListGraph<>();
        unsavedChanges = false; // VIKTIGT ! behöver sätta till false varje gång vi sparar i Användargränssnittet.
        //Lägg till det i alla dessa metoder, plus en getter hasUnsavedChanges()! //Tror jag har fixat det! -Alex
    }

    public void saveToFile(String filePath) {
        try{
            PrintWriter pw = new PrintWriter(filePath);
            pw.println(imagePath);
                for (City city : getCitys()) {
                    pw.println(city.getName() + ";" + city.getX() + ";" + city.getY());

                }
                pw.println("---");
                for (City city : getCitys()) {
                    for (Edge<City> edge : getCitysFrom(city)) {
                        pw.println(city.getName() + ';' + edge.getDestination() + ';' + edge.getName() + ';' + edge.getWeight());
                    }
                    pw.close();
                    unsavedChanges = false;
                }
        }catch(FileNotFoundException e) {
            System.err.print("Kunde inte hitta filen! (se till att det är en text fil)");
        }
    }

    public void loadFromFile(String filePath) {
        try{ Scanner scanner = new Scanner(new File(filePath));
            this.imagePath = scanner.nextLine();
            cityListGraph = new ListGraph<>();
            String line = scanner.nextLine();
            while (!line.equals("---")) {
                String[] cityInfo;
                cityInfo = line.split(";");
                String cityName = cityInfo[0];
                double cityXpos = Double.parseDouble(cityInfo[1]);
                double cityYpos = Double.parseDouble(cityInfo[2]);
                City city = new City(cityName, cityXpos, cityYpos);
                cityListGraph.add(city);
                line = scanner.nextLine();
            }
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                String[] edgeInfo = line.split(";");
                String cityName = edgeInfo[0];
                String destination = edgeInfo[1];
                String routeName = edgeInfo[2];
                int routePrice = Integer.parseInt(edgeInfo[3]);

                connectCity(findCityByName(cityName), findCityByName(destination), routeName, routePrice);
            }

        }catch(FileNotFoundException e){
            System.err.print("Hittade inte filen!");
        }catch(IOException e){
        System.err.print("IO-fel upstod!");
    }

    }

    public City findCityByName(String name) {
        for (City city : getCitys()) {
            if (city.getName().equals(name)) {
                return city;
            }
        }
        return null; // null hantering kan behövas åtgärdas.
    }

    public void setCityPathFinder(PathFinder<City> pathFinder) {
        this.cityPathFinder = pathFinder;

    }
    public Path<City> findPath(City from , City to){
        return cityPathFinder.findPath(cityListGraph , from , to );
    }
    //image path , getters och setters
    public String getImagePath(){
        return imagePath;
    }
    public void setImagePath(String imagePath){
        this.imagePath = imagePath;
        unsavedChanges = true;
    }

    public boolean hasUnsavedChanges(){
        return unsavedChanges;
    }
    public void addCity(City city) {
        cityListGraph.add(city);
        unsavedChanges = true;
    }

    public void removeCity(City city) {
        cityListGraph.remove(city);
        unsavedChanges = true;
    }

    public void connectCity(City from, City to, String flightPathName, int price) {
        cityListGraph.connect(from, to, flightPathName, price);
        unsavedChanges = true;
    }

    public void disconnectCity(City from, City to) {

        cityListGraph.disconnect(from, to);
        unsavedChanges = true;
    }

    public void setConnectionWeight(City from, City to, int price) {
        cityListGraph.setConnectionWeight(from, to, price);
        unsavedChanges = true;
    }

    public Edge<City> getEdgeBetweenCity(City from, City to) {
        return cityListGraph.getEdgeBetween(from, to);
    }

    public Collection<Edge<City>> getCitysFrom(City from) {
        return cityListGraph.getEdgesFrom(from);
    }

    public Set<City> getCitys() {
        return cityListGraph.getNodes();
    }

    public Iterator<City> cityIterator() {
        return cityListGraph.iterator();
    }

    public String toString() {
        return cityListGraph.toString();
    }
}
