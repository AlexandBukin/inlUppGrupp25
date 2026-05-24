package se.su.inlupp;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;


public class Gui extends Application {

    TravelModel travelModel = new TravelModel();
    Pane center = new Pane();

  public void start(Stage stage) {
    BorderPane root = new BorderPane();
    FlowPane top = new FlowPane();
    root.setTop(top);
    root.setCenter(center);
    top.setBackground(Background.fill(Color.BLUE));
    InteractionControl interactionControl = new InteractionControl(travelModel, center);

    Scene scene = new Scene(root , 640 , 480);

    Label label = new Label();
    Button fileButton = new Button("Välj fil");
    fileButton.setOnAction(e -> interactionControl.loadFile(stage, label));

    Button addCityBtn = new Button("Lägg till stad");
    addCityBtn.setOnAction(e -> interactionControl.addCityClicked());
      
    top.getChildren().addAll(addCityBtn, fileButton);
    center.getChildren().addAll(label);
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
} 
