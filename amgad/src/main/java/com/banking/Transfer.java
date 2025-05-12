package com.banking;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;

public class Transfer {
    @FXML
    TextField TransIn,TransAm;
    public String TrTo;
    public String TrAm;
    String TrAmDS;
    double TrAmD;
    @FXML
    Label TransAmF;
    @FXML
    Button OpTrans;
    @FXML
    Button BkToPy;

    public void Writing(ActionEvent actionEvent) {
        TransIn.setOnAction(e -> {
            String input = TransIn.getText();
            TrTo=input;
        });
    }
    public void Writing2(ActionEvent actionEvent) {
        TransAm.setOnAction(e -> {
            String input = TransAm.getText();
            TrAm=input;
            TrAmD = Double.parseDouble(TrAm);
            TrAmD+=(TrAmD*3.0/100.0);
            TrAmDS=String.valueOf(TrAmD+" EGP");
            TransAmF.setText(String.valueOf(TrAmD+" EGP"));
        });
    }
    public String getTrTo(){
        return TrTo;
    }
    public String getTrAmDS(){
        return TrAmDS;
    }
    public void Clicking2(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/maged/TransferConfirm.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
            TransferConfirm transferConfirm=fxmlLoader.getController();
            transferConfirm.LabelText(TrTo,TrAm+" EGP",String.valueOf(TrAmD+" EGP"),"Transfer");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.setTitle("Transfer Confirmation");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    public void goBackToPayment(ActionEvent event) throws IOException {
        // Load the Payment.fxml file
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/maged/Payment.fxml"));
        Parent paymentPage = fxmlLoader.load();

        // Load background image
        Image backgroundImage;
        try {
            backgroundImage = new Image(getClass().getResourceAsStream("/back.jpg"));
            if (backgroundImage.isError()) {
                System.err.println("Error loading background image: " + backgroundImage.getException().getMessage());
                // Use a fallback color if image fails to load
                backgroundImage = null;
            }
        } catch (Exception e) {
            System.err.println("Failed to load background image: " + e.getMessage());
            backgroundImage = null;
        }

        // Get screen dimensions
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        // Create stack pane for layering content
        StackPane stackPane = new StackPane();

        // Add background and overlay if image loaded successfully
        if (backgroundImage != null) {
            ImageView backgroundView = new ImageView(backgroundImage);
            // Fit background to screen
            backgroundView.setFitWidth(screenWidth);
            backgroundView.setFitHeight(screenHeight);
            backgroundView.setPreserveRatio(false);
            backgroundView.setEffect(new GaussianBlur(20));

            // Create a transparent blue overlay
            Region blueOverlay = new Region();
            blueOverlay.setBackground(new Background(new BackgroundFill(
                    Color.rgb(0, 120, 255, 0.2),
                    CornerRadii.EMPTY,
                    Insets.EMPTY
            )));
            blueOverlay.setEffect(new GaussianBlur(20));
            blueOverlay.setPrefSize(screenWidth, screenHeight);

            // Add background and overlay to stack
            stackPane.getChildren().addAll(backgroundView, blueOverlay);
        } else {
            // Create a fallback blue gradient background if image failed to load
            Region fallbackBackground = new Region();
            fallbackBackground.setBackground(new Background(new BackgroundFill(
                    Color.rgb(10, 60, 120, 1.0),
                    CornerRadii.EMPTY,
                    Insets.EMPTY
            )));
            fallbackBackground.setPrefSize(screenWidth, screenHeight);
            stackPane.getChildren().add(fallbackBackground);
        }

        // Add the UI content on top
        stackPane.getChildren().add(paymentPage);

        // Create the scene
        Scene scene = new Scene(stackPane, 1200, 700);

        // Get the current stage and set the new scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
}