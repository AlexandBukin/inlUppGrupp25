package se.su.inlupp;

import java.io.File;

import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class InteractionControl {
    private TravelModel travelModel;
    private Pane center;

    public InteractionControl(TravelModel travelModel, Pane center) {
        this.travelModel = travelModel;
        this.center = center;
    }

    public void loadFromFile(Stage stage, Label label) {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Välj en fil");
      File file = fileChooser.showOpenDialog(stage);
      if(file != null) {
        label.setText("Vald fil: " + file.getName());
      }
    }

    public void addCityClicked() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Lägg till stad");
        dialog.setHeaderText("Ange stadens namn");
        dialog.showAndWait();
        String name = dialog.getEditor().getText();

        center.setOnMouseClicked(mouseEvent -> {
          double x = mouseEvent.getX();
          double y = mouseEvent.getY();
          
          travelModel.addCity(new City(name, x, y));
        
          Circle circle = new Circle(x, y, 10, Color.BLUE);
          Label label = new Label(name);
          label.setLayoutX(x + 15);
          label.setLayoutY(y - 10);

          center.getChildren().addAll(circle, label);
          center.setOnMouseClicked(null);

        });
    }
}


