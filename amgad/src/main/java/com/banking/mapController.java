package com.banking;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.List;

public class mapController {
    @FXML
    private ImageView worldMap;

    @FXML
    private Pane pinContainer;

    @FXML
    private Group mapGroup;

    @FXML
    private StackPane mapContainer;


    @FXML
    private Button zoomInBtn, zoomOutBtn;

    private double zoomFactor = 1.0;
    private Label locationLabel;

    private double dragStartX;
    private double dragStartY;
    private double initialTranslateX;
    private double initialTranslateY;

    @FXML
    public void initialize() {
        // تحميل صورة الخريطة
        worldMap.setImage(new Image(getClass().getResource("/MAP.png").toExternalForm()));
        worldMap.setOpacity(.85);
        worldMap.setStyle("-fx-background-color: linear-gradient(to bottom, white, blue);");

        List<PinData> pins = Arrays.asList(
                new PinData(80, 150, "Egypt",0),
                new PinData(100, 400, "Saudi Arabia", 0),
                new PinData(330, 270,  "Germany", 0),
                new PinData(500, 100, "USA", 0),
                new PinData(445, 270,  "Japan", 0)
        );

        for (PinData pin : pins) {
            addPin(pin);
        }

        zoomInBtn.setOnAction(e -> {
            mapGroup.setScaleX(mapGroup.getScaleX() * 1.1);
            mapGroup.setScaleY(mapGroup.getScaleY() * 1.1);
        });

        zoomOutBtn.setOnAction(e -> {
            mapGroup.setScaleX(mapGroup.getScaleX() / 1.1);
            mapGroup.setScaleY(mapGroup.getScaleY() / 1.1);
        });

        // إضافة خاصية السحب (Dragging)
        setupDragging();

        Platform.runLater(() -> {
            javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(mapContainer.getWidth(), mapContainer.getHeight());
            mapContainer.setClip(clip);
        });


    }

    private void addPin(PinData data) {
        ImageView pin = new ImageView(new Image(getClass().getResource("/Location-pin.png").toExternalForm()));
        pin.setFitHeight(30);
        pin.setFitWidth(30);
        pin.setTranslateX(data.getX());
        pin.setTranslateY(data.getY());
        pin.setRotate(data.getRotation());

        DropShadow shadow = new DropShadow();
        shadow.setRadius(5);
        shadow.setOffsetX(3);
        shadow.setOffsetY(3);
        pin.setEffect(shadow);

        TranslateTransition bounce = new TranslateTransition(Duration.millis(700), pin);
        bounce.setFromY(data.getY());
        bounce.setToY(data.getY() - 15);
        bounce.setAutoReverse(true);
        bounce.setCycleCount(TranslateTransition.INDEFINITE);
        bounce.play();

        pin.setOnMouseEntered(e -> showLocationMessage(data));
        pin.setOnMouseExited(e -> hideLocationMessage());
        pin.setOnMouseClicked(e -> showCountryName(data));

        pinContainer.getChildren().add(pin);
    }

    private void showCountryName(PinData data) {
        Label countryLabel = new Label();
        countryLabel.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-padding: 5; -fx-background-radius: 5; -fx-border-radius: 5;");

        countryLabel.setTranslateX(data.getX() - 10);
        countryLabel.setTranslateY(data.getY() - 40);

        pinContainer.getChildren().add(countryLabel);

        Timeline timeline = new Timeline();
        for (int i = 0; i < data.getCountry().length(); i++) {
            final int index = i;
            KeyFrame keyFrame = new KeyFrame(Duration.millis(100 * i), ev -> {
                countryLabel.setText(countryLabel.getText() + data.getCountry().charAt(index));
            });
            timeline.getKeyFrames().add(keyFrame);
        }

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.3), countryLabel);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        timeline.setOnFinished(finish -> {
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(finishPause -> {
                FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), countryLabel);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);
                fadeOut.setOnFinished(e -> pinContainer.getChildren().remove(countryLabel));
                fadeOut.play();
            });
            pause.play();
        });

        timeline.play();
    }

    private void showLocationMessage(PinData data) {
        if (locationLabel == null) {
            locationLabel = new Label();
            locationLabel.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-padding: 5; -fx-background-radius: 5; -fx-border-radius: 5;");
            pinContainer.getChildren().add(locationLabel);
        }


        locationLabel.setTranslateX(data.getX());
        locationLabel.setTranslateY(data.getY() - 30);
        locationLabel.setOpacity(0);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.3), locationLabel);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    private void hideLocationMessage() {
        if (locationLabel != null) {
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), locationLabel);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> pinContainer.getChildren().remove(locationLabel));
            fadeOut.play();
            locationLabel = null;
        }
    }

    private void zoom(double factor) {
        zoomFactor *= factor;
        double pivotX = mapContainer.getWidth() / 2;
        double pivotY = mapContainer.getHeight() / 2;

        mapGroup.setScaleX(zoomFactor);
        mapGroup.setScaleY(zoomFactor);

        mapGroup.setTranslateX((1 - zoomFactor) * pivotX);
        mapGroup.setTranslateY((1 - zoomFactor) * pivotY);
    }

    private void setupDragging() {
        mapContainer.setOnMousePressed(event -> {
            dragStartX = event.getSceneX();
            dragStartY = event.getSceneY();
            initialTranslateX = mapGroup.getTranslateX();
            initialTranslateY = mapGroup.getTranslateY();
        });

        mapContainer.setOnMouseDragged(event -> {
            double offsetX = event.getSceneX() - dragStartX;
            double offsetY = event.getSceneY() - dragStartY;

            mapGroup.setTranslateX(initialTranslateX + offsetX);
            mapGroup.setTranslateY(initialTranslateY + offsetY);
        });
    }


}

