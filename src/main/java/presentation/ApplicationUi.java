package main.java.presentation;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import main.java.view.Controls;
import main.java.view.Field;
import main.java.view.Grid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ApplicationUi extends Pane {

  private Grid     grid;
  private Field    field;
  private Controls controls;

  public ApplicationUi() throws IOException {
    initializeControls();
    layoutControls();
  }

  private void initializeControls() throws IOException {
    drawBackground();

    grid     = new Grid(PresentationModel.getInstance());
    field    = new Field();
    controls = new Controls();
  }

  private void layoutControls() {
    this.getChildren().add(grid);
    this.getChildren().add(field);
    this.getChildren().add(controls);
  }

  private void drawBackground() throws IOException {
    // https://map.geo.admin.ch/?lang=de&topic=swisstopo&bgLayer=ch.swisstopo.pixelkarte-farbe&catalogNodes=1392,1430&layers=ch.swisstopo.images-swissimage-dop10.metadata,ch.swisstopo.swissimage-product,KML%7C%7Chttps:%2F%2Fpublic.geo.admin.ch%2Fapi%2Fkml%2Ffiles%2FLAyAU_uYTCGngB4wfIo6tQ&E=2612657.47&N=1261467.03&zoom=11&layers_timestamp=,current,
    // images around 	2'612'660.5, 1'261'504.3
    // left bottom coordinate:  2'612'576, 1'261'364 => lat:47.502907, long: 7.60555
    // left top coordinate:      2'612'572, 1'261'560 => lat:47.50467, long: 7.605555
    // delta = 0.001765
    // Image Positions:
    // zoom level: 27
    // [ 0 1 2 ]
    // [ 3 4 5 ]
    // [ 6 7 8 ]

    List<Image> filesInFolder = Files.walk(Paths.get("src/res/img"))
        .filter(Files::isRegularFile)
        .map(Path::toFile)
        .sorted()
        .map(img -> new Image("res/img/" + img.getName()))
        .toList();

    List<BackgroundPosition> positions = new ArrayList<>();

    for (int left = 0; left < 3; left++) {
      for (int right = 0; right < 3; right++) {
        final int PIXEL_PER_PIECE = 256;
        positions.add(new BackgroundPosition(
            null,
            PIXEL_PER_PIECE * right,
            false,
            null,
            PIXEL_PER_PIECE * left,
            false
        ));
      }
    }

    List<BackgroundImage> bgImages = IntStream
        .range(0, filesInFolder.size())
        .mapToObj(
            idx -> new BackgroundImage(
                filesInFolder.get(idx),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                positions.get(idx),
                BackgroundSize.DEFAULT
            )).toList();

    this.setBackground(new Background(bgImages.toArray(new BackgroundImage[0])));
  }
}