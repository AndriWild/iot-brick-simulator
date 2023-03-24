import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Starter extends Application {
  @Override
  public void start(Stage stage) throws IOException {
    PresentationModel pm = new PresentationModel();
    Pane pane = new ApplicationUi(pm);
    Scene scene = new Scene(pane, 200, 200);
    stage.titleProperty().set("Brick Simulator");
    stage.setScene(scene);
    stage.show();

  }

  public static void main(String[] args) {
    Application.launch();
  }
}