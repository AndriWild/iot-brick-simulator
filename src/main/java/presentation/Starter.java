package main.java.presentation;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Starter extends Application {
  @Override
  public void start(Stage stage) throws IOException {
    PresentationModel pm = PresentationModel.getInstance();
    Pane pane = new ApplicationUi();

   // https://map.geo.admin.ch/?lang=de&topic=swisstopo&bgLayer=ch.swisstopo.pixelkarte-farbe&catalogNodes=1392,1430&layers=ch.swisstopo.images-swissimage-dop10.metadata,ch.swisstopo.swissimage-product,KML%7C%7Chttps:%2F%2Fpublic.geo.admin.ch%2Fapi%2Fkml%2Ffiles%2FLAyAU_uYTCGngB4wfIo6tQ&E=2612657.47&N=1261467.03&zoom=11&layers_timestamp=,current,
    // images around 	2'612'660.5, 1'261'504.3
    // left bottom coordinate:  2'612'576, 1'261'364 => lat:47.502907, long: 7.60555
    // left top coordinate:      2'612'572, 1'261'560 => lat:47.50467, long: 7.605555
    // Image Positions:
    // zoom level: 27
    // [ 0 1 2 ]
    // [ 3 4 5 ]
    // [ 6 7 8 ]

    int imageSize = 256;

      List<Image> filesInFolder = Files.walk(Paths.get("src/res/img"))
          .filter(Files::isRegularFile)
          .map(Path::toFile)
          .sorted()
          .map(img -> new Image("res/img/" + img.getName()))
          .toList();

    List<BackgroundPosition> bgs2 = new ArrayList<>();

    for (int left = 0; left < 3; left++) {
      for (int right = 0; right < 3; right++) {
        bgs2.add(new BackgroundPosition(null, imageSize * right, false, null, imageSize * left, false));
      }
    }

    List<BackgroundImage> bgImages = IntStream
        .range(0, filesInFolder.size())
        .mapToObj(
            idx -> new BackgroundImage(
                filesInFolder.get(idx),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                bgs2.get(idx),
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