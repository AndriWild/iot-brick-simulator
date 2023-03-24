import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class ApplicationUi extends Pane {

  private PresentationModel pm;
  private Circle circle;

  public ApplicationUi(PresentationModel pm) {
    this.pm = pm;
    initializeControls();
    layoutControls();

  }

  private void initializeControls() {

    this.setStyle("-fx-background-color: grey;");
    circle = new Circle(10);
    circle.relocate(100, 100);
  }

  private void layoutControls() {
    this.getChildren().add(circle);
  }
}
