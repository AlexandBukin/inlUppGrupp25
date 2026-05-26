package se.su.inlupp;

import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Optional;

public class InteractionControl {


    private final HashMap<City, Circle> cityCircleMap = new HashMap<>();
    private final HashMap<City, Label> cityToLabel = new HashMap<>();
    double startX, startY;
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

    public void removeCityClicked() {
        System.out.println("cityCircleMap storlek: " + cityCircleMap.size());
        System.out.println("removeCityClicked this: " + this.hashCode());
        for (City city : cityCircleMap.keySet()) {
            Circle circle = cityCircleMap.get(city);
            Label label = cityToLabel.get(city);
            circle.setOnMouseClicked(e -> {
                System.out.println("Försöker ta bort: " + city.getName());
                cityCircleMap.remove(city);
                travelModel.removeCity(city);
                center.getChildren().removeAll(label, circle);
            });

        }
        center.setOnMouseClicked(null);
    }

    public void clearMaps() {
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
        cityToLabel.put(city, cityName);
        circle.setOnMousePressed(new StartDragHandler(circle));
        circle.setOnMouseDragged(new DragHandler(circle, city));

        center.getChildren().addAll(cityName, circle);
        center.setOnMouseClicked(null);
    }

    public void connectCitiesDialog() {

        Dialog<ButtonType> createEdgeprompt = new Dialog<>();
        GridPane pane = new GridPane();
        ButtonType connect = new ButtonType("Koppla");
        createEdgeprompt.getDialogPane().getButtonTypes().addAll(connect);

        TextField from = new TextField();
        TextField to = new TextField();
        TextField flyglinje = new TextField();
        TextField pris = new TextField();
        pane.add(new Label("From"), 0, 0);
        pane.add(from, 1, 0);
        pane.add(new Label("Till"), 0, 1);
        pane.add(to, 1, 1);
        pane.add(new Label("Flyglinje"), 0, 2);
        pane.add(flyglinje, 1, 2);
        pane.add(new Label("Pris"), 0, 3);
        pane.add(pris, 1, 3);

        createEdgeprompt.getDialogPane().setContent(pane);

        Optional<ButtonType> result = createEdgeprompt.showAndWait();
        if (result.isPresent() && result.get() == connect) {
            if (from.getText().isEmpty() || to.getText().isEmpty() || flyglinje.getText().isEmpty() || pris.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Kant error");
                alert.setContentText("Kant kan inte skapas, kontrollera textfälten");
                alert.showAndWait();
            } else {
                City cityFrom = travelModel.findCityByName(from.getText());
                City cityTo = travelModel.findCityByName(to.getText());
                if (cityTo == null || cityFrom == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Kant error");
                    alert.setContentText("Stad eller städer du försökte ange, finns ej med");
                    alert.showAndWait();
                } else {
                    try {
                        travelModel.connectCity(cityFrom, cityTo, flyglinje.getText(), Integer.parseInt(pris.getText()));
                    } catch (NumberFormatException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Pris error");
                        alert.setContentText("Priset måste vara ett heltal");
                        alert.showAndWait();
                    }
                }
                Line line = new Line(cityFrom.getX() , cityFrom.getY() , cityTo.getX() , cityTo.getY());
                line.endXProperty().bind(cityCircleMap.get(cityFrom).centerXProperty());
                line.endYProperty().bind(cityCircleMap.get(cityFrom).centerYProperty());
                line.startXProperty().bind(cityCircleMap.get(cityTo).centerXProperty());
                line.startYProperty().bind(cityCircleMap.get(cityTo).centerYProperty());
                center.getChildren().add(line);

            }

        }


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
