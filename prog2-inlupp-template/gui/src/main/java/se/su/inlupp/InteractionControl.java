package se.su.inlupp;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class InteractionControl {

    Circle circle;
    double startX,startY;

    public void interactableCity(TravelModel travelModel, City city, Pane center) {
        Circle circle = new Circle(city.getX(), city.getY(), 5, Color.BLUE);
        Label cityName = new Label(city.getName());
        cityName.setLayoutX(city.getX() + 20);
        cityName.setLayoutY(city.getY() - 10);
        cityName.layoutXProperty().bind(circle.centerXProperty());
        cityName.layoutYProperty().bind(circle.centerYProperty());

        circle.setOnMousePressed(new StartDragHandler(circle));
        circle.setOnMouseDragged(new DragHandler(circle));

        center.getChildren().addAll(cityName, circle);
        center.setOnMouseClicked(null);
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
}
