package se.su.inlupp;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.HashMap;
import java.util.Optional;

public class InteractionControl {


    private final HashMap<City , Circle> cityCircleMap = new  HashMap<>();
    private final HashMap<City , Label > cityToLabel = new HashMap<>();
    double startX,startY;
    private TravelModel travelModel;
    private Pane center;

    public InteractionControl(TravelModel travelModel, Pane center) {
        this.travelModel = travelModel;
        this.center = center;
    }


    public void addCityClicked() {
        center.setOnMouseClicked(MouseEvent -> {
            double x = MouseEvent.getX();
            double y = MouseEvent.getY();

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Lägg till stad");
            dialog.setHeaderText("Ange stadens namn");
            Optional<String> result = dialog.showAndWait();

            if (result.isPresent()) {
                String name = result.get();
                City city = new City(name, x, y);

                if (name.isEmpty() || travelModel.findCityByName(name) != null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Fel");
                    alert.setContentText("Stad namn inte giltigt eller redan finns");
                    alert.showAndWait();
                } else {
                    travelModel.addCity(city);
                    interactableCity(travelModel, city, center);
                }
            }
            center.setOnMouseClicked(null);
        });
    }

    public void removeCityClicked(){
        System.out.println("cityCircleMap storlek: " + cityCircleMap.size());
        System.out.println("removeCityClicked this: " + this.hashCode());
        for(City city : cityCircleMap.keySet()){
            Circle circle = cityCircleMap.get(city);
            Label label = cityToLabel.get(city);
            circle.setOnMouseClicked(e->{
                System.out.println("Försöker ta bort: " + city.getName());
                cityCircleMap.remove(city);
                travelModel.removeCity(city);
                center.getChildren().removeAll( label , circle);
            });

        }
        center.setOnMouseClicked(null);
    }

    public void clearMaps(){
        cityCircleMap.clear();
        cityToLabel.clear();
    }


    public void interactableCity(TravelModel travelModel, City city, Pane center) {
        Circle circle = new Circle(city.getX(), city.getY(), 5, Color.BLUE);
        Label cityName = new Label(city.getName());
        cityName.setLayoutX(city.getX() + 20);
        cityName.setLayoutY(city.getY() - 10);
        cityName.layoutXProperty().bind(circle.centerXProperty());
        cityName.layoutYProperty().bind(circle.centerYProperty());
        System.out.println("Lägger till i map: " + city.getName()); // ALLA SYSTEM.OUT.PRINTS ÄR TILL DEBUGGING, KAN TA BORT SEN
        System.out.println("interactableCity this: " + this.hashCode());
        cityCircleMap.put(city, circle);
        System.out.println("Efter put, storlek: " + cityCircleMap.size());
        cityToLabel.put(city , cityName);
        circle.setOnMousePressed(new StartDragHandler(circle));
        circle.setOnMouseDragged(new DragHandler(circle,city));

        center.getChildren().addAll(cityName, circle);
        center.setOnMouseClicked(null);
    }

    class StartDragHandler implements EventHandler<MouseEvent> {

        private Circle circle;

        public StartDragHandler(Circle circle) {
            this.circle = circle;
        }

        @Override
        public void handle(MouseEvent event) {
            startX = circle.getLayoutX();
            startY = circle.getLayoutY();
        }
    }

    class DragHandler implements EventHandler<MouseEvent> {

        private Circle circle;
        private City city;

        public DragHandler(Circle circle, City city) {
            this.circle = circle;
            this.city = city;
        }

        @Override
        public void handle(MouseEvent event) {
            double newX = circle.getLayoutX() + event.getX() - startX;
            double newY = circle.getLayoutY() + event.getY() - startY;
            circle.setCenterX(newX);
            circle.setCenterY(newY);
            city.setX(newX);
            city.setY(newY);
        }
    }
}
