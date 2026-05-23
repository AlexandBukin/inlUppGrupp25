package se.su.inlupp;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;


public class Gui extends Application {

    Shape circle;
    double startX,startY;

  public void start(Stage stage) {
    TravelModel travelModel = new TravelModel();
    Graph<String> graph = new ListGraph<String>();
    BorderPane root = new BorderPane();
    FlowPane top = new FlowPane();
    root.setTop(top);
    Pane center = new Pane();
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
              travelModel.addCity(new City(name, x, y));

              Circle circle = new Circle(x, y, 10, Color.BLUE);

              Label cityName = new Label("   "+name);
              cityName.layoutXProperty().bind(circle.centerXProperty());
              cityName.layoutYProperty().bind(circle.centerYProperty());

              circle.setOnMousePressed(new StartDragHandler(circle));
              circle.setOnMouseDragged(new DragHandler(circle));

              center.getChildren().addAll(cityName,circle);
              center.setOnMouseClicked(null);
          });
      });
      top.getChildren().addAll(addCityBtn, fileButton);
    center.getChildren().addAll(label);
    stage.setScene(scene);
    stage.show();
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

      public DragHandler(Circle circle) {
          this.circle = circle;
      }

      @Override
      public void handle(MouseEvent event) {
          double newX = circle.getLayoutX() + event.getX() - startX;
          double newY = circle.getLayoutY() + event.getY() - startY;
          circle.setCenterX(newX);
          circle.setCenterY(newY);
      }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
