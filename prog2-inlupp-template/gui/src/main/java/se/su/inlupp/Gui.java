package se.su.inlupp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;


public class Gui extends Application {

    InteractionControl interactionControl;
    Stage stage;
    TravelModel travelModel = new TravelModel();
    Map center;

    public void start(Stage stage) {
        this.stage = stage;
        center = new Map(stage);
        interactionControl = new InteractionControl(travelModel, center);
        BorderPane root = new BorderPane();
        FlowPane top = new FlowPane();
        root.setTop(top);
        root.setCenter(center);
        Scene scene = new Scene(root, 640, 480);

        MenuItem saveTravelApp = new MenuItem("Spara");
        saveTravelApp.setOnAction(event -> {
            String filePath = getFilePath();
            travelModel.saveToFile(filePath);
        });

        MenuItem loadTravelApp = new MenuItem("Öppna");
        loadTravelApp.setOnAction(event -> {
            String filePath = getFilePath();
            travelModel.loadFromFile(filePath);
            redrawGraph();
        });

        MenuItem exitTravelApp = new MenuItem("Avsluta");
        exitTravelApp.setOnAction(event -> {
            if (!travelModel.hasUnsavedChanges()) {
                stage.close();
            } else {
                if (closeConfirmation()) {
                    stage.close();
                }
            }
        });

        Menu menu = new Menu("Options...");
        menu.getItems().addAll(saveTravelApp, loadTravelApp, exitTravelApp);
        MenuBar menuBar = new MenuBar(menu);

        Button addMapButton = new Button("Välj kartbild");
        addMapButton.setOnAction(e -> {
            center.promptImagePath(stage, travelModel);
        });

        Button addCityBtn = new Button("Lägg till stad");
        addCityBtn.setOnAction(e -> interactionControl.addCityClicked());

        Button removeCityBtn = new Button("Ta bort stad");
        removeCityBtn.setOnAction(e -> interactionControl.removeCityClicked());

        Button connectCitiesBtn = new Button("Koppla städer");
        connectCitiesBtn.setOnAction(e -> interactionControl.connectCitiesDialog());

        Button removeConnectionBtn = new Button("Ta bort koppling");
        removeConnectionBtn.setOnAction(e -> interactionControl.removeLineBetweenCitiesDialog());

        top.getChildren().addAll(addCityBtn, addMapButton, removeCityBtn, connectCitiesBtn, removeConnectionBtn, menuBar);
        stage.setScene(scene);
        stage.show();
    }

    private void addCityToGUI(TravelModel travelModel, City city) {
//        InteractionControl interactionControl = new InteractionControl(travelModel, center);
        interactionControl.interactableCity(travelModel, city, center);
    }

    private void redrawGraph() {
        center.getChildren().clear();
        interactionControl.clearMaps();
        center.hasImage = false;
        for (City city : travelModel.getCitys()) {
            addCityToGUI(travelModel, city);
            }
        for (City city : travelModel.getCitys()) {
            //vet ej om vi får använda Edge inom Gui mappen, om inte: lägg till en metod i TravelModel som  gör det. -Alex
            for (Edge<City> flight : travelModel.getFlightsFrom(city)) {
                if (city.getName().compareTo(flight.getDestination().getName()) < 0) {
                    interactionControl.drawLine(city, flight.getDestination(), flight.getName(), flight.getWeight());
                }
            }
        }
        center.addMapImage(stage, travelModel.getImagePath());
    }


    private String getFilePath() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Välj en fil");
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            return file.getAbsolutePath();
        }
        return null;
    }

    private boolean closeConfirmation() {
        Alert closeConfirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        closeConfirmationAlert.setTitle("Avsluta");
        closeConfirmationAlert.setHeaderText("Är du säker på att du vill avsluta?");
        closeConfirmationAlert.setContentText("Det finns osparade ändringar!");
        Optional<ButtonType> result = closeConfirmationAlert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    public static void main(String[] args) {
        launch(args);
    }
}