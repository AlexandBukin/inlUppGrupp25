package se.su.inlupp;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;


public class Gui extends Application {

    TravelModel travelModel = new TravelModel();
    Pane center = new Pane();

  public void start(Stage stage) {
    Graph<String> graph = new ListGraph<String>();
    BorderPane root = new BorderPane();
    FlowPane top = new FlowPane();
    root.setTop(top);
    root.setCenter(center);
    Scene scene = new Scene(root , 640 , 480);

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
      Button addCityBtn = new Button("Lägg till stad");
      addCityBtn.setOnAction(e -> {
          center.setOnMouseClicked(mouseEvent -> {
              double x = mouseEvent.getSceneX();
              double y = mouseEvent.getSceneY();
              TextInputDialog dialog = new TextInputDialog();
              dialog.setTitle("Lägg till stad");
              dialog.setHeaderText("Ange stadens namn");
              dialog.showAndWait();
              String name = dialog.getEditor().getText();
              City city = new City(name, x, y);
              travelModel.addCity(city);
              addCityToGUI(travelModel, city, center);
          });
      });
      top.getChildren().addAll(addCityBtn, fileButton);
    center.getChildren().addAll(label);
    stage.setScene(scene);
    stage.show();
  }

    private void addCityToGUI(TravelModel travelModel, City city, Pane center) {

      InteractionControl interactionControl = new InteractionControl();
      interactionControl.interactableCity(travelModel, city, center);
    }

    public void redrawGraph() {
        for (City city : travelModel.getCitys()) {
            addCityToGUI(travelModel,city,center);
        }
    }

  public static void main(String[] args) {
    launch(args);
  }
}
