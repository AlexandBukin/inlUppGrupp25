package se.su.inlupp;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Map extends Pane {

    boolean hasImage = false;
    Stage stage;
    public Map(Stage stage){
        this.stage = stage;
    }

    public void promptImagePath(Stage stage, TravelModel travelModel){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Välj en fil");
        File file = fileChooser.showOpenDialog(stage);
        String imagePath = file.getAbsolutePath();
        travelModel.setImagePath(imagePath);
        addMapImage(stage, imagePath);
    }
    public void addMapImage(Stage stage, String imagePath) {
        if(hasImage){
            this.getChildren().removeFirst();
        }
        Image map = new Image(new File(imagePath).toURI().toString());
        ImageView mapView = new ImageView(map);
        mapView.fitHeightProperty().bind(this.heightProperty());
        mapView.fitWidthProperty().bind(this.widthProperty());
        this.getChildren().addFirst(mapView);
        hasImage = true;
    }
}
