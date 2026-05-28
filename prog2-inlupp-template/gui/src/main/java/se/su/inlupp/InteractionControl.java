package se.su.inlupp;

import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.*;

public class InteractionControl {

    private final HashMap<City, Circle> cityCircleMap = new HashMap<>();
    private final HashMap<City, Label> cityToLabel = new HashMap<>();
    private final HashMap<City, Set<Line>> cityLines = new HashMap<>();
    double startX, startY;
    private final TravelModel travelModel;
    private final Pane center;

    public InteractionControl(TravelModel travelModel, Pane center) {
        this.travelModel = travelModel;
        this.center = center;
    }


    public void addCityClicked() {
        clearCityClickHandlers();
        center.setOnMouseClicked(MouseEvent -> {
            double x = MouseEvent.getX();
            double y = MouseEvent.getY();

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Lägg till stad");
            dialog.setHeaderText("Ange stadens namn");
            Optional<String> result = dialog.showAndWait();

            if (result.isPresent()) {
                String name = result.get().substring(0,1).toUpperCase()+result.get().substring(1).toLowerCase();
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
                removeLinesFromCity(city);
                System.out.println("Försöker ta bort: " + city.getName());
                cityCircleMap.remove(city);
                travelModel.removeCity(city);
                center.getChildren().removeAll(label, circle);
            });
            center.setOnMouseClicked(null);
        }
    }

    private void removeLinesFromCity(City city) {
        Set<Line> lines = cityLines.get(city);
        if (lines != null) {
            center.getChildren().removeAll(lines);
            cityLines.remove(city);
            System.out.println(center.getChildren());
        }
    }

    public void removeLineBetweenCitiesDialog() {
        clearCityClickHandlers();
        List<City> cities = getTwoCitiesFromDialog("Remove Line","Ta bort koppling");
        if(cities == null){
            return;
        }
        removeLineBetweenCities(cities.getFirst(), cities.getLast());
    }

    private List<City> getTwoCitiesFromDialog(String dialogName, String buttonText) {
        Dialog<ButtonType> getCitiesPrompt = new Dialog<>();
        getCitiesPrompt.setTitle(dialogName);
        GridPane gridPane = new GridPane();
        ButtonType disconnect = new ButtonType(buttonText);
        getCitiesPrompt.getDialogPane().getButtonTypes().addAll(disconnect);

        TextField from = new TextField();
        TextField to = new TextField();
        gridPane.add(new Label("Stad 1"), 0, 0);
        gridPane.add(from, 1, 0);
        gridPane.add(new Label("Stad 2"), 0, 1);
        gridPane.add(to, 1, 1);

        getCitiesPrompt.getDialogPane().setContent(gridPane);
        Optional<ButtonType> result = getCitiesPrompt.showAndWait();
        if (result.isPresent() && result.get() == disconnect) {
            String fromSanitized = from.getText().substring(0,1).toUpperCase()+from.getText().substring(1).toLowerCase();
            String toSanitized = to.getText().substring(0,1).toUpperCase()+to.getText().substring(1).toLowerCase();
            if (travelModel.findCityByName(fromSanitized) == null || travelModel.findCityByName(toSanitized) == null || toSanitized.equals(fromSanitized)) /* borde inte vara här */ {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("City error");
                alert.setContentText("Kunde inte hitta en av städerna");
                alert.showAndWait();
            }else {
                List<City> cities = new ArrayList<>();
                cities.add(travelModel.findCityByName(fromSanitized));
                cities.add(travelModel.findCityByName(toSanitized));

                return cities;
            }
        }
        return null;
    }

    private void removeLineBetweenCities(City cityOne, City cityTwo) {
        if((cityLines.get(cityOne) == null || cityLines.get(cityTwo) == null)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("City error");
            alert.setContentText("En av givna städerna har inga flyg");
            alert.showAndWait();
        }else{
            for(Line lineOne: cityLines.get(cityOne)) {
                for(Line lineTwo: cityLines.get(cityTwo)){
                    if(lineOne.equals(lineTwo)) {
                        center.getChildren().remove(lineOne);
                        cityLines.get(cityOne).remove(lineOne);
                        travelModel.disconnectCity(cityOne,cityTwo);
                        return;
                    }
                }
            }
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Edge error");
            alert.setContentText("Finns inga flyg mellan städerna!");
            alert.showAndWait();
        }
    }

    public void clearMaps() {
        cityCircleMap.clear();
        cityToLabel.clear();
        cityLines.clear();
    }


    public void interactableCity(TravelModel travelModel, City city, Pane center) {
        Circle circle = new Circle(city.getX(), city.getY(), 5, Color.RED);
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

        clearCityClickHandlers();
        Dialog<ButtonType> createEdgeprompt = new Dialog<>();
        GridPane pane = new GridPane();
        ButtonType connect = new ButtonType("Koppla");
        createEdgeprompt.getDialogPane().getButtonTypes().addAll(connect);

        TextField from = new TextField();
        TextField to = new TextField();
        TextField flyglinje = new TextField();
        TextField pris = new TextField();
        pane.add(new Label("Från"), 0, 0);
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
            if (from.getText().isEmpty() || to.getText().isEmpty() || flyglinje.getText().isEmpty() || pris.getText().isEmpty() || from.getText().equals(to.getText())) /*Borde flyttas till ny alert men vill inte ta plats.*/ {
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
                        drawLine(cityFrom, cityTo, flyglinje.getText(), Integer.parseInt(pris.getText()));
                    } catch (NumberFormatException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Pris error");
                        alert.setContentText("Priset måste vara ett heltal");
                        alert.showAndWait();
                    } catch (IllegalStateException e){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Kant error");
                        alert.setContentText("Kanten finns redan");
                        alert.showAndWait();
                    }
                }

            }
        }
    }

    public void drawLine(City cityFrom, City cityTo, String flyglinje, int pris) {
        Line line = new Line(cityFrom.getX(), cityFrom.getY(), cityTo.getX(), cityTo.getY());
        cityLines.computeIfAbsent(cityFrom, k -> new HashSet<>()).add(line);
        cityLines.computeIfAbsent(cityTo, k -> new HashSet<>()).add(line);
        line.endXProperty().bind(cityCircleMap.get(cityFrom).centerXProperty());
        line.endYProperty().bind(cityCircleMap.get(cityFrom).centerYProperty());
        line.startXProperty().bind(cityCircleMap.get(cityTo).centerXProperty());
        line.startYProperty().bind(cityCircleMap.get(cityTo).centerYProperty());
        center.getChildren().add(1, line);
    }

    public void runDFS() {
        travelModel.setCityPathFinderDFS();
        run("Välj städer för DFS sökning");
    }
    public void runBFS(){
        travelModel.setCityPathFinderBFS();
        run("Välj städer för BFS sökning");
    }

    private void run(String dialogTitle) {
        List<City> cities = getTwoCitiesFromDialog(dialogTitle, "Hitta Väg");
        if (cities == null) {
            //Lägg till alert funktionalitet sen (Bryt ut alla alerts till en ny metod).
            return;
        }
        Path<City> path = travelModel.findPath(cities.getFirst(), cities.getLast());
        clearCityClickHandlers();
        Dialog<ButtonType> showPath = new Dialog<>();
        ButtonType close = new ButtonType("Stäng");
        showPath.getDialogPane().getButtonTypes().addAll(close);
        showPath.setTitle("Resultat");
        GridPane pane = new GridPane();
        showPath.getDialogPane().setContent(pane);
        pane.minHeight(400);
        pane.minWidth(500);
        int i = 0;
        pane.add(new Label("Start: " + cities.getFirst().getName()), 0, i);
        for (Edge<City> edge : path.getEdges()) {
            i++;
            pane.add(new Label(" |"), 0, i);
            i++;
            pane.add(new Label("V"), 0, i);
            i += 2;
            pane.add(new Label("Stop: " + edge.getDestination().getName() + " Flyglinje: " + edge.getName() + " Pris: " + edge.getWeight()), 0, i);
        }
        Optional<ButtonType> result = showPath.showAndWait();
        if (result.isPresent() && result.get() == close) {
            showPath.close();
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

    public void clearCityClickHandlers() {
        for (Circle circle : cityCircleMap.values()) {
            circle.setOnMouseClicked(null);
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
