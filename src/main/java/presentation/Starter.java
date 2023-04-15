package main.java.presentation;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Starter extends Application {
  @Override
  public void start(Stage stage) {
    PresentationModel pm = PresentationModel.getInstance();
    Pane pane = new ApplicationUi();

   // https://map.geo.admin.ch/?lang=de&topic=swisstopo&bgLayer=ch.swisstopo.pixelkarte-farbe&catalogNodes=1392,1430&layers=ch.swisstopo.images-swissimage-dop10.metadata,ch.swisstopo.swissimage-product,KML%7C%7Chttps:%2F%2Fpublic.geo.admin.ch%2Fapi%2Fkml%2Ffiles%2FLAyAU_uYTCGngB4wfIo6tQ&E=2612657.47&N=1261467.03&zoom=11&layers_timestamp=,current,
    // images around 	2'612'660.5, 1'261'504.3
    int imageSize = 256;
    Image[] images = {
         new Image("./res/img/ol.jpeg"), new Image("./res/img/or.jpeg"),
         new Image("./res/img/ul.jpeg"), new Image("./res/img/ur.jpeg"),
    };

    BackgroundPosition[] bgs = {
        new BackgroundPosition(null, 0.0, false, null, 0.0,false),        new BackgroundPosition(null, imageSize, false, null, 0.0,false),
        new BackgroundPosition(null, 0.0, false, null, imageSize, false), new BackgroundPosition(null, imageSize, false, null, imageSize ,false),
    };

    List<BackgroundImage> bgImages = IntStream
        .range(0, images.length)
        .mapToObj(
            idx -> new BackgroundImage(
                images[idx],
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                bgs[idx],
                BackgroundSize.DEFAULT
            )).toList();

    pane.setBackground(new Background(bgImages.toArray(new BackgroundImage[0])));

    Scene scene = new Scene(pane, pm.getWindowSize().getWidth(), pm.getWindowSize().getHeight());
    stage.titleProperty().bind(pm.windowTitleProperty());
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    Application.launch();
  }
}