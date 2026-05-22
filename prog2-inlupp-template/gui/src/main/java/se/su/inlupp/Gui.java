package se.su.inlupp;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


public class Gui extends Application {

  public void start(Stage stage) {
    Graph<String> graph = new ListGraph<String>();
    BorderPane root = new BorderPane();
    Pane center = new Pane();
    root.setCenter(center);
    Scene scene = new Scene(root , 640 , 480);


    String javaVersion = System.getProperty("java.version");
    String javafxVersion = System.getProperty("javafx.version");
    Label label = new Label();
    Button fileButton = new Button("Välj fil");
    fileButton.setOnAction(e ->{
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Välj en fil");
      File file = fileChooser.showOpenDialog(stage);
      if(file != null) {
        label.setText("Vald fil: " + file.getName());
      }
    });
    center.getChildren().addAll(fileButton , label);
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
