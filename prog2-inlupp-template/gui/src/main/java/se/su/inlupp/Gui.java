package se.su.inlupp;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.Optional;


public class Gui extends Application {

    TravelModel travelModel = new TravelModel();
    Pane center = new Pane();
    Stage stage;

    public void start(Stage stage) {
        this.stage = stage;
        Graph<String> graph = new ListGraph<String>(); //ta bort någon gång
        BorderPane root = new BorderPane();
        FlowPane top = new FlowPane();
        root.setTop(top);
        root.setCenter(center);
        Scene scene = new Scene(root , 640 , 480);

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
            if(!travelModel.hasUnsavedChanges()) {
                stage.close();
            }
            else{
                if(closeConfirmation()) {
                    stage.close();
                }
            }
        });

        Menu menu = new Menu("Options...");
        menu.getItems().addAll(saveTravelApp,loadTravelApp,exitTravelApp);
        MenuBar menuBar = new MenuBar(menu);

        Button addMapButton = new Button("Välj kartbild");
        addMapButton.setOnAction(e ->{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Välj en fil");
            File file = fileChooser.showOpenDialog(stage);
            String imagePath = file.getAbsolutePath();
            travelModel.setImagePath(imagePath);
            addMapToGUI(imagePath);
        });

        Button addCityBtn = new Button("Lägg till stad");
        addCityBtn.setOnAction(e -> {
            center.setOnMouseClicked(mouseEvent -> {
                double x = mouseEvent.getSceneX();
                double y = mouseEvent.getSceneY();
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Lägg till stad");
                dialog.setHeaderText("Ange stadens namn");
                Optional<String> result = dialog.showAndWait();
                if(result.isEmpty()){
                    center.setOnMouseClicked(null);
                }
                if(result.isPresent()) {
                    String name = result.get();
                    City city = new City(name, x, y);
                    if (name.isEmpty() || travelModel.findCityByName(name) != null) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Fel");
                        alert.setContentText("Stad namn inte giltigt eller redan finns");
                        alert.showAndWait();
                        center.setOnMouseClicked(null);
                    } else {
                        travelModel.addCity(city);
                        addCityToGUI(travelModel, city);
                    }
                }
            });
        });

        top.getChildren().addAll(addCityBtn, addMapButton, menuBar);
        stage.setScene(scene);
        stage.show();
    }

    private void addMapToGUI(String imagePath) {
        Image map = new Image(new File(imagePath).toURI().toString());
        ImageView mapView = new ImageView(map);
        mapView.fitHeightProperty().bind(stage.heightProperty());
        mapView.fitWidthProperty().bind(stage.widthProperty());
        center.getChildren().addFirst(mapView); //Kommer inte att skriva över förra bild om man vill ändra! Fixa!!!
    }

    private void addCityToGUI(TravelModel travelModel, City city) {
        InteractionControl interactionControl = new InteractionControl();
        interactionControl.interactableCity(travelModel, city, center);
    }

    private void redrawGraph() {
        center.getChildren().clear();
        for (City city : travelModel.getCitys()) {
            addCityToGUI(travelModel,city);
        }
        addMapToGUI(travelModel.getImagePath());
    }
    private String getFilePath(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Välj en fil");
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            return file.getAbsolutePath();
        }
        return null;
    }

    private boolean closeConfirmation(){
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
