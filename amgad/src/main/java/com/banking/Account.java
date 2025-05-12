package com.banking;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.geometry.Insets;
import javafx.scene.shape.Rectangle;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.BarChart;

public class Account {

    @FXML
    private Button addCardButton;

    @FXML
    private HBox cardContainer;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private VBox cardInputBox;

    private int cardCount = 0;
    private final int maxCards = 4;

    @FXML
    private VBox transactionList;

    //------------------------------------------------------------------------------------------------------------------------------------------//
    //sidebar
    @FXML
    private FontAwesomeIconView homeIcon;

    @FXML
    private Label homeLabel;

    @FXML
    private FontAwesomeIconView userIcon;
    @FXML
    private Label userLabel;

    @FXML
    private FontAwesomeIconView exchangeIcon;
    @FXML
    private Label exchangeLabel;

    @FXML
    private FontAwesomeIconView moneyIcon;
    @FXML
    private Label moneyLabel;

    @FXML
    private FontAwesomeIconView chartIcon;
    @FXML
    private Label chartLabel;

    @FXML
    private FontAwesomeIconView mapIcon;
    @FXML
    private Label mapLabel;

    @FXML
    private FontAwesomeIconView cogIcon;
    @FXML
    private Label cogLabel;

    @FXML
    private FontAwesomeIconView helpIcon;
    @FXML
    private Label helpLabel;

    @FXML
    private FontAwesomeIconView commentIcon;
    @FXML
    private Label commentLabel;

    @FXML
    private FontAwesomeIconView searchIcon;

    @FXML
    private FontAwesomeIconView bellIcon;

    @FXML
    private ImageView homeGif;
    //-------------------------------------------------------------------------------------------------------------//

    @FXML
    public void initialize() {

        Object[][] transactions = {
                {"Ahmed", "Received", "+$120", "/s1.png", Color.LIMEGREEN},
                {"Laila", "Shopping", "-$60", "/s2.png", Color.RED},
                {"Tarek", "Transfer", "-$300", "/s3.png", Color.ORANGE},
                {"Mona", "Refund", "+$75", "/s4.png", Color.LIMEGREEN},
                {"Ahmed", "Received", "+$120", "/s1.png", Color.LIMEGREEN},
                {"Laila", "Shopping", "-$60", "/s2.png", Color.RED},
                {"Tarek", "Transfer", "-$300", "/s3.png", Color.ORANGE},
                {"Mona", "Refund", "+$75", "/s4.png", Color.LIMEGREEN},
                {"Ahmed", "Received", "+$120", "/s1.png", Color.LIMEGREEN},
                {"Laila", "Shopping", "-$60", "/s2.png", Color.RED},
                {"Tarek", "Transfer", "-$300", "/s3.png", Color.ORANGE},
                {"Mona", "Refund", "+$75", "/s4.png", Color.LIMEGREEN},
                {"Ahmed", "Received", "+$120", "/s1.png", Color.LIMEGREEN},
                {"Laila", "Shopping", "-$60", "/s2.png", Color.RED},
                {"Tarek", "Transfer", "-$300", "/s3.png", Color.ORANGE},
                {"Mona", "Refund", "+$75", "/s4.png", Color.LIMEGREEN}
        };

        for (int i = 0; i < transactions.length; i += 2) {
            HBox row = new HBox(20); // مسافة بين الكروت
            row.setAlignment(Pos.CENTER);
            row.setPrefWidth(Region.USE_COMPUTED_SIZE);

            HBox card1 = createTransactionCard(transactions[i]);
            HBox.setHgrow(card1, Priority.ALWAYS);
            card1.setMaxWidth(Double.MAX_VALUE);

            row.getChildren().add(card1);

            if (i + 1 < transactions.length) {
                HBox card2 = createTransactionCard(transactions[i + 1]);
                HBox.setHgrow(card2, Priority.ALWAYS);
                card2.setMaxWidth(Double.MAX_VALUE);
                row.getChildren().add(card2);
            } else {
                Region filler = new Region(); // لو مش فيه عنصر ثاني
                HBox.setHgrow(filler, Priority.ALWAYS);
                row.getChildren().add(filler);
            }

            transactionList.getChildren().add(row);

        }


        // اجعل خلفية الرسم البياني الشفافة
        barChart.setLegendVisible(false);
        barChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent;");
// تغيير لون عنوان الرسم
        Node chartTitle = barChart.lookup(".chart-title");
        if (chartTitle != null) {
            chartTitle.setStyle("-fx-text-fill: white;");
        }

        cardInputBox.setVisible(false);
        cardInputBox.setManaged(false);

        // Add default card
        Image img = new Image(getClass().getResourceAsStream("/s2.png"));
        ImageView newCard = new ImageView(img);
        newCard.setFitWidth(250);
        newCard.setFitHeight(170);
        newCard.setOnMouseClicked(event -> showFloatingCardWindow(img, "Default Name", "0000 0000 0000 0000", "Debit Card", "1000 EGP"));
        cardContainer.getChildren().add(newCard);
        cardCount++;

        XYChart.Series<String, Number> incomes = new XYChart.Series<>();
        incomes.setName("Incomes");

        XYChart.Series<String, Number> expenses = new XYChart.Series<>();
        expenses.setName("Expenses");

        // Data for the last 6 months
        String[] months = {"Jul", "Aug", "Sep", "Nov", "Dec", "Jan"};
        int[] incomeValues = {8000, 7500, 4000, 12000, 6500, 7000};
        int[] expenseValues = {6000, 7000, 3900, 9000, 6000, 7100};

        for (int i = 0; i < months.length; i++) {
            incomes.getData().add(new XYChart.Data<>(months[i], incomeValues[i]));
            expenses.getData().add(new XYChart.Data<>(months[i], expenseValues[i]));
        }

        barChart.getData().addAll(incomes, expenses);
        barChart.setBarGap(1); // Gap between bars in the same group
        barChart.widthProperty().addListener((obs, oldVal, newVal) -> {
            double width = newVal.doubleValue();
            if (width > 800) {
                barChart.setCategoryGap(90);
            } else if (width > 600) {
                barChart.setCategoryGap(70);
            } else {
                barChart.setCategoryGap(20);
            }
        });

        barChart.lookup(".chart-horizontal-grid-lines").setStyle("-fx-stroke: transparent;");
        barChart.lookup(".chart-vertical-grid-lines").setStyle("-fx-stroke: transparent;");

        //-------------------------------------------------------------------------------------------------------------------------------------------//
        //sidebar
        setupHomeAnimation(homeIcon, homeLabel);
        setupUserAnimation(userIcon, userLabel);
        setupExchangeAnimation(exchangeIcon, exchangeLabel);
        setupMoneyAnimation(moneyIcon, moneyLabel);
        setupChartAnimation(chartIcon, chartLabel);
        setupMapAnimation(mapIcon, mapLabel);
        setupCogAnimation(cogIcon, cogLabel);
        setupHelpAnimation(helpIcon, helpLabel);
        setupCommentAnimation(commentIcon, commentLabel);
        // Top bar icons
        if (searchIcon != null) {
            setupSearchAnimation(searchIcon);
        } else {
            System.out.println("Warning: searchIcon is null");
        }
        if (bellIcon != null) {
            setupBellAnimation(bellIcon);
        } else {
            System.out.println("Warning: bellIcon is null");
        }
        // GIF animation
        if (homeGif != null) {
            setupGifAnimation(homeGif);
        } else {
            System.out.println("Warning: homeGif is null");
        }
        //---------------------------------------------------------------------------------------------------------------------------------------------//

    }

    public void print(MouseEvent event) {
        System.out.println("Hello World");
    }

    private void showFloatingCardWindow(Image cardImage, String name, String number, String type, String amount) {
        Stage popupStage = new Stage();
        popupStage.initStyle(StageStyle.TRANSPARENT);

        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);
        content.setStyle("""
            -fx-padding: 20;
            -fx-background-color: rgba(50, 50, 50, 0.2);
            -fx-border-radius: 20;
            -fx-background-radius: 20;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0), 20, 0.5, 0, 4);
        """);

        content.setEffect(new BoxBlur(15, 15, 3));

        ImageView imgView = new ImageView(cardImage);
        imgView.setFitWidth(250);
        imgView.setFitHeight(150);

        Label nameLabel = new Label("Name: " + name);
        Label numberLabel = new Label("Card Number: " + number);
        Label typeLabel = new Label("Type: " + type);
        Label amountLabel = new Label("Amount: " + amount);

        nameLabel.setFont(Font.font(16));
        nameLabel.setStyle("-fx-text-fill: #ffffff;");
        numberLabel.setFont(Font.font(16));
        numberLabel.setStyle("-fx-text-fill: #ffffff;");
        typeLabel.setFont(Font.font(16));
        typeLabel.setStyle("-fx-text-fill: #ffffff;");
        amountLabel.setFont(Font.font(16));
        amountLabel.setStyle("-fx-text-fill: #ffffff;");

        Button closeBtn = new Button("❌");
        closeBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 18; -fx-text-fill: #555;");
        closeBtn.setOnAction(e -> popupStage.close());

        VBox header = new VBox(closeBtn);
        header.setAlignment(Pos.TOP_RIGHT);

        content.getChildren().addAll(header, imgView, nameLabel, numberLabel, typeLabel, amountLabel);

        StackPane root = new StackPane(content);
        root.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");

        Rectangle clip = new Rectangle(400, 450);
        clip.setArcWidth(40);
        clip.setArcHeight(40);
        root.setClip(clip);

        Scene scene = new Scene(root, 400, 450);
        scene.setFill(Color.TRANSPARENT);

        popupStage.setScene(scene);
        popupStage.setAlwaysOnTop(true);
        popupStage.show();

        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), content);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(600), content);
        scaleIn.setFromX(0.85);
        scaleIn.setFromY(0.85);
        scaleIn.setToX(1);
        scaleIn.setToY(1);

        fadeIn.play();
        scaleIn.play();
    }

    @FXML
    private void onAddCardClicked() {
        if (cardCount >= maxCards) return;

        Stage dialog = new Stage();
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.setTitle("Add New Card");

        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.setAlignment(Pos.CENTER);
        form.setStyle("""
                    -fx-background-color: rgba(255, 255, 255, 0.2);
                    -fx-border-radius: 30;
                    -fx-background-radius: 30;
                    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 30, 0.2, 0, 8);
                """);
        form.setEffect(new BoxBlur(10, 10, 3));

        StackPane root = new StackPane(form);
        root.setStyle("-fx-background-color: rgba(0, 140, 255, 0.5);");

        Rectangle clip = new Rectangle(320, 300);
        clip.setArcWidth(50);
        clip.setArcHeight(50);
        root.setClip(clip);

        // Card Type
        ComboBox<String> cardTypeComboBox = new ComboBox<>();
        cardTypeComboBox.getItems().addAll("Debit Card", "Credit Card", "Prepaid Card", "Virtual Card");
        cardTypeComboBox.setPromptText("Select Card Type");
        cardTypeComboBox.setPrefWidth(250);

        // Amount Field
        TextField amountField = new TextField();
        amountField.setPromptText("Enter Amount");
        amountField.setPrefWidth(250);

        // Style fields
        cardTypeComboBox.setStyle("-fx-background-radius: 12; -fx-font-size: 14px;");
        amountField.setStyle("-fx-background-radius: 12; -fx-font-size: 14px;");

        // Submit Button
        Button addBtn = new Button("Add Card");
        addBtn.setStyle("""
                    -fx-background-color: linear-gradient(to right, #008cff,#6DD5FA);
                    -fx-text-fill: white;
                    -fx-font-size: 15px;
                    -fx-font-weight: bold;
                    -fx-background-radius: 30;
                    -fx-padding: 8 20 8 20;
                    -fx-cursor: hand;
                    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0.3, 0, 4);
                """);

        // Close Button
        Button closeBtn = new Button("❌");
        closeBtn.setStyle("""
                    -fx-background-color: transparent;
                    -fx-font-size: 18;
                    -fx-text-fill: #333;
                """);
        closeBtn.setOnAction(e -> dialog.close());

        HBox topBar = new HBox(closeBtn);
        topBar.setAlignment(Pos.TOP_RIGHT);
        topBar.setPrefWidth(Double.MAX_VALUE);

        Label title = new Label("Enter card info:");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");

        form.getChildren().addAll(topBar, title, cardTypeComboBox, amountField, addBtn);

        Scene dialogScene = new Scene(root, 320, 300);
        dialogScene.setFill(Color.TRANSPARENT);
        dialog.setScene(dialogScene);
        dialog.show();

        // Handle "Add Card" action
        addBtn.setOnAction(e -> {
            String cardType = cardTypeComboBox.getValue();
            String amount = amountField.getText();

            if (cardType == null || amount.isEmpty()) return;

            // Define card details for showFloatingCardWindow
            String name = cardType; // Use card type as name
            String number = "**** **** **** ****"; // Placeholder for card number
            String type = cardType; // Card type
            String amountValue = amount + " EGP"; // Amount with currency

            // Add card based on cardCount
            if (cardCount == 0) {
                Image img = new Image(getClass().getResourceAsStream("/s2.png"));
                ImageView newCard = new ImageView(img);
                newCard.setFitWidth(250);
                newCard.setFitHeight(170);
                HBox.setMargin(newCard, new Insets(0, 0, 0, 0));
                newCard.setOnMouseClicked(event -> showFloatingCardWindow(img, name, number, type, amountValue));
                cardContainer.getChildren().add(newCard);
                cardCount++;
            } else if (cardCount == 1) {
                Image img = new Image(getClass().getResourceAsStream("/s4.png"));
                ImageView newCard = new ImageView(img);
                newCard.setFitWidth(250);
                newCard.setFitHeight(170);
                HBox.setMargin(newCard, new Insets(0, 0, 0, -170));
                newCard.setOnMouseClicked(event -> showFloatingCardWindow(img, name, number, type, amountValue));
                cardContainer.getChildren().add(newCard);
                cardCount++;
            } else if (cardCount == 2) {
                Image img = new Image(getClass().getResourceAsStream("/s3.png"));
                ImageView newCard = new ImageView(img);
                newCard.setFitWidth(250);
                newCard.setFitHeight(170);
                HBox.setMargin(newCard, new Insets(0, 0, 0, -170));
                newCard.setOnMouseClicked(event -> showFloatingCardWindow(img, name, number, type, amountValue));
                cardContainer.getChildren().add(newCard);
                cardCount++;
            } else if (cardCount == 3) {
                Image img = new Image(getClass().getResourceAsStream("/s1.png"));
                ImageView newCard = new ImageView(img);
                newCard.setFitWidth(250);
                newCard.setFitHeight(170);
                HBox.setMargin(newCard, new Insets(0, 0, 0, -170));
                newCard.setOnMouseClicked(event -> showFloatingCardWindow(img, name, number, type, amountValue));
                cardContainer.getChildren().add(newCard);
                cardCount++;
                addCardButton.setVisible(false);
                TranslateTransition moveLeft = new TranslateTransition(Duration.millis(500), cardContainer);
                moveLeft.setByX(-60); // Move the HBox left by 60 pixels
                moveLeft.play();
            }

            dialog.close();
        });
    }

    private HBox createTransactionCard(Object[] t) {
        String name = (String) t[0];
        String detail = (String) t[1];
        String amount = (String) t[2];
        String imgPath = (String) t[3];
        Color color = (Color) t[4];

        HBox box = new HBox(10);
        box.setStyle("-fx-background-color: rgba(255,255,255,0.05); -fx-background-radius: 12;");
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPrefWidth(280);

        // 👇 هنا تحط الأسطر دي
        box.setPrefWidth(Region.USE_COMPUTED_SIZE);
        box.setMaxWidth(Double.MAX_VALUE);


        ImageView img = new ImageView(new Image(getClass().getResourceAsStream(imgPath)));
        img.setFitWidth(40);
        img.setFitHeight(40);
        img.setClip(new Circle(20, 20, 20));

        VBox texts = new VBox(3);
        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14;");
        Label detailLabel = new Label(detail);
        detailLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 12;");
        texts.getChildren().addAll(nameLabel, detailLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label amountLabel = new Label(amount);
        amountLabel.setTextFill(color);
        amountLabel.setStyle("-fx-font-size: 14;");

        box.getChildren().addAll(img, texts, spacer, amountLabel);
        return box;
    }
    //---------------------------------------------------------------------------------------------------------------------------------------------//
    //sidebar
    private void setupHomeAnimation(FontAwesomeIconView icon, Label label) {
        if (icon == null || label == null) {
            System.out.println("Warning: homeIcon or homeLabel is null");
            return;
        }
        Rotate rotate = new Rotate(0, 0, icon.getLayoutY(), 0, Rotate.Y_AXIS);
        icon.getTransforms().add(rotate);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(rotate.angleProperty(), 0)),
                new KeyFrame(Duration.millis(300), new KeyValue(rotate.angleProperty(), 60)),
                new KeyFrame(Duration.millis(600), new KeyValue(rotate.angleProperty(), 60)),
                new KeyFrame(Duration.millis(900), new KeyValue(rotate.angleProperty(), 0))
        );
        timeline.setCycleCount(1);

        label.setOnMouseEntered(event -> {
            icon.setEffect(new DropShadow(10, Color.GRAY));
            timeline.playFromStart();
        });
        label.setOnMouseExited(event -> {
            icon.setEffect(null);
            rotate.setAngle(0);
        });
    }

    private void setupUserAnimation(FontAwesomeIconView icon, Label label) {
        if (icon == null || label == null) {
            System.out.println("Warning: userIcon or userLabel is null");
            return;
        }
        Scale scale = new Scale(1, 1);
        icon.getTransforms().add(scale);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(scale.xProperty(), 1), new KeyValue(scale.yProperty(), 1)),
                new KeyFrame(Duration.millis(300), new KeyValue(scale.xProperty(), 1.3), new KeyValue(scale.yProperty(), 1.3)),
                new KeyFrame(Duration.millis(600), new KeyValue(scale.xProperty(), 1.3), new KeyValue(scale.yProperty(), 1.3)),
                new KeyFrame(Duration.millis(900), new KeyValue(scale.xProperty(), 1), new KeyValue(scale.yProperty(), 1))
        );
        timeline.setCycleCount(1);

        label.setOnMouseEntered(event -> {
            icon.setEffect(new DropShadow(10, Color.GRAY));
            timeline.playFromStart();
        });
        label.setOnMouseExited(event -> {
            icon.setEffect(null);
            scale.setX(1);
            scale.setY(1);
        });
    }

    private void setupExchangeAnimation(FontAwesomeIconView icon, Label label) {
        if (icon == null || label == null) {
            System.out.println("Warning: exchangeIcon or exchangeLabel is null");
            return;
        }
        Translate translate = new Translate(0, 0);
        icon.getTransforms().add(translate);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(translate.xProperty(), 0)),
                new KeyFrame(Duration.millis(300), new KeyValue(translate.xProperty(), 10)),
                new KeyFrame(Duration.millis(600), new KeyValue(translate.xProperty(), 10)),
                new KeyFrame(Duration.millis(900), new KeyValue(translate.xProperty(), 0))
        );
        timeline.setCycleCount(1);

        label.setOnMouseEntered(event -> {
            icon.setEffect(new DropShadow(10, Color.GRAY));
            timeline.playFromStart();
        });
        label.setOnMouseExited(event -> {
            icon.setEffect(null);
            translate.setX(0);
        });
    }

    private void setupMoneyAnimation(FontAwesomeIconView icon, Label label) {
        if (icon == null || label == null) {
            System.out.println("Warning: moneyIcon or moneyLabel is null");
            return;
        }
        Rotate rotate = new Rotate(0, Rotate.Z_AXIS);
        icon.getTransforms().add(rotate);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(rotate.angleProperty(), 0)),
                new KeyFrame(Duration.millis(300), new KeyValue(rotate.angleProperty(), 180)),
                new KeyFrame(Duration.millis(600), new KeyValue(rotate.angleProperty(), 180)),
                new KeyFrame(Duration.millis(900), new KeyValue(rotate.angleProperty(), 360))
        );
        timeline.setCycleCount(1);

        label.setOnMouseEntered(event -> {
            icon.setEffect(new DropShadow(10, Color.GRAY));
            timeline.playFromStart();
        });
        label.setOnMouseExited(event -> {
            icon.setEffect(null);
            rotate.setAngle(0);
        });
    }

    private void setupChartAnimation(FontAwesomeIconView icon, Label label) {
        if (icon == null || label == null) {
            System.out.println("Warning: chartIcon or chartLabel is null");
            return;
        }
        Translate translate = new Translate(0, 0);
        icon.getTransforms().add(translate);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(translate.yProperty(), 0)),
                new KeyFrame(Duration.millis(300), new KeyValue(translate.yProperty(), -10)),
                new KeyFrame(Duration.millis(600), new KeyValue(translate.yProperty(), -10)),
                new KeyFrame(Duration.millis(900), new KeyValue(translate.yProperty(), 0))
        );
        timeline.setCycleCount(1);

        label.setOnMouseEntered(event -> {
            icon.setEffect(new DropShadow(10, Color.GRAY));
            timeline.playFromStart();
        });
        label.setOnMouseExited(event -> {
            icon.setEffect(null);
            translate.setY(0);
        });
    }

    private void setupMapAnimation(FontAwesomeIconView icon, Label label) {
        if (icon == null || label == null) {
            System.out.println("Warning: mapIcon or mapLabel is null");
            return;
        }
        Scale scale = new Scale(1, 1);
        icon.getTransforms().add(scale);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(scale.xProperty(), 1),
                        new KeyValue(scale.yProperty(), 1),
                        new KeyValue(icon.opacityProperty(), 1.0)),
                new KeyFrame(Duration.millis(300),
                        new KeyValue(scale.xProperty(), 1.2),
                        new KeyValue(scale.yProperty(), 1.2),
                        new KeyValue(icon.opacityProperty(), 0.7)),
                new KeyFrame(Duration.millis(600),
                        new KeyValue(scale.xProperty(), 1.2),
                        new KeyValue(scale.yProperty(), 1.2),
                        new KeyValue(icon.opacityProperty(), 0.7)),
                new KeyFrame(Duration.millis(900),
                        new KeyValue(scale.xProperty(), 1),
                        new KeyValue(scale.yProperty(), 1),
                        new KeyValue(icon.opacityProperty(), 1.0))
        );
        timeline.setCycleCount(1);

        label.setOnMouseEntered(event -> {
            icon.setEffect(new DropShadow(10, Color.GRAY));
            timeline.playFromStart();
        });
        label.setOnMouseExited(event -> {
            icon.setEffect(null);
            scale.setX(1);
            scale.setY(1);
            icon.setOpacity(1.0);
        });
    }

    private void setupCogAnimation(FontAwesomeIconView icon, Label label) {
        if (icon == null || label == null) {
            System.out.println("Warning: cogIcon or cogLabel is null");
            return;
        }
        Rotate rotate = new Rotate(0, Rotate.Z_AXIS);
        icon.getTransforms().add(rotate);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(rotate.angleProperty(), 0)),
                new KeyFrame(Duration.millis(300), new KeyValue(rotate.angleProperty(), 180)),
                new KeyFrame(Duration.millis(600), new KeyValue(rotate.angleProperty(), 180)),
                new KeyFrame(Duration.millis(900), new KeyValue(rotate.angleProperty(), 360))
        );
        timeline.setCycleCount(1);

        label.setOnMouseEntered(event -> {
            icon.setEffect(new DropShadow(10, Color.GRAY));
            timeline.playFromStart();
        });
        label.setOnMouseExited(event -> {
            icon.setEffect(null);
            rotate.setAngle(0);
        });
    }

    private void setupHelpAnimation(FontAwesomeIconView icon, Label label) {
        if (icon == null || label == null) {
            System.out.println("Warning: helpIcon or helpLabel is null");
            return;
        }
        Translate translate = new Translate(0, 0);
        icon.getTransforms().add(translate);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(translate.yProperty(), 0)),
                new KeyFrame(Duration.millis(150), new KeyValue(translate.yProperty(), -8)),
                new KeyFrame(Duration.millis(300), new KeyValue(translate.yProperty(), 0)),
                new KeyFrame(Duration.millis(450), new KeyValue(translate.yProperty(), -8)),
                new KeyFrame(Duration.millis(600), new KeyValue(translate.yProperty(), 0)),
                new KeyFrame(Duration.millis(750), new KeyValue(translate.yProperty(), -8)),
                new KeyFrame(Duration.millis(900), new KeyValue(translate.yProperty(), 0))
        );
        timeline.setCycleCount(1);

        label.setOnMouseEntered(event -> {
            icon.setEffect(new DropShadow(10, Color.GRAY));
            timeline.playFromStart();
        });
        label.setOnMouseExited(event -> {
            icon.setEffect(null);
            translate.setY(0);
        });
    }

    private void setupCommentAnimation(FontAwesomeIconView icon, Label label) {
        if (icon == null || label == null) {
            System.out.println("Warning: commentIcon or commentLabel is null");
            return;
        }
        Translate translate = new Translate(0, 0);
        Scale scale = new Scale(1, 1);
        icon.getTransforms().addAll(translate, scale);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(translate.yProperty(), 0),
                        new KeyValue(scale.xProperty(), 1),
                        new KeyValue(scale.yProperty(), 1)),
                new KeyFrame(Duration.millis(150),
                        new KeyValue(translate.yProperty(), -6),
                        new KeyValue(scale.xProperty(), 1.1),
                        new KeyValue(scale.yProperty(), 1.1)),
                new KeyFrame(Duration.millis(300),
                        new KeyValue(translate.yProperty(), 0),
                        new KeyValue(scale.xProperty(), 1.0),
                        new KeyValue(scale.yProperty(), 1.0)),
                new KeyFrame(Duration.millis(450),
                        new KeyValue(translate.yProperty(), -6),
                        new KeyValue(scale.xProperty(), 1.1),
                        new KeyValue(scale.yProperty(), 1.1)),
                new KeyFrame(Duration.millis(600),
                        new KeyValue(translate.yProperty(), 0),
                        new KeyValue(scale.xProperty(), 1.0),
                        new KeyValue(scale.yProperty(), 1.0)),
                new KeyFrame(Duration.millis(750),
                        new KeyValue(translate.yProperty(), -6),
                        new KeyValue(scale.xProperty(), 1.1),
                        new KeyValue(scale.yProperty(), 1.1)),
                new KeyFrame(Duration.millis(900),
                        new KeyValue(translate.yProperty(), 0),
                        new KeyValue(scale.xProperty(), 1.0),
                        new KeyValue(scale.yProperty(), 1.0))
        );
        timeline.setCycleCount(1);

        label.setOnMouseEntered(event -> {
            icon.setEffect(new DropShadow(10, Color.GRAY));
            timeline.playFromStart();
        });
        label.setOnMouseExited(event -> {
            icon.setEffect(null);
            translate.setY(0);
            scale.setX(1);
            scale.setY(1);
        });
    }

    private void setupSearchAnimation(FontAwesomeIconView icon) {
        if (icon == null) {
            System.out.println("Warning: searchIcon is null");
            return;
        }
        Scale scale = new Scale(1, 1);
        Rotate rotate = new Rotate(0, Rotate.Z_AXIS);
        icon.getTransforms().addAll(scale, rotate);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(scale.xProperty(), 1),
                        new KeyValue(scale.yProperty(), 1),
                        new KeyValue(rotate.angleProperty(), 0)),
                new KeyFrame(Duration.millis(300),
                        new KeyValue(scale.xProperty(), 1.5),
                        new KeyValue(scale.yProperty(), 1.5),
                        new KeyValue(rotate.angleProperty(), 15)),
                new KeyFrame(Duration.millis(600),
                        new KeyValue(scale.xProperty(), 1.5),
                        new KeyValue(scale.yProperty(), 1.5),
                        new KeyValue(rotate.angleProperty(), 15)),
                new KeyFrame(Duration.millis(900),
                        new KeyValue(scale.xProperty(), 1),
                        new KeyValue(scale.yProperty(), 1),
                        new KeyValue(rotate.angleProperty(), 0))
        );
        timeline.setCycleCount(1);

        icon.setOnMouseEntered(event -> {
            icon.setEffect(new DropShadow(10, Color.GRAY));
            timeline.playFromStart();
        });
        icon.setOnMouseExited(event -> {
            icon.setEffect(null);
            scale.setX(1);
            scale.setY(1);
            rotate.setAngle(0);
        });
    }

    private void setupBellAnimation(FontAwesomeIconView icon) {
        if (icon == null) {
            System.out.println("Warning: bellIcon is null");
            return;
        }
        Rotate rotate = new Rotate(0, Rotate.Z_AXIS);
        icon.getTransforms().add(rotate);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(rotate.angleProperty(), 0)),
                new KeyFrame(Duration.millis(150), new KeyValue(rotate.angleProperty(), 15)),
                new KeyFrame(Duration.millis(300), new KeyValue(rotate.angleProperty(), -15)),
                new KeyFrame(Duration.millis(450), new KeyValue(rotate.angleProperty(), 10)),
                new KeyFrame(Duration.millis(600), new KeyValue(rotate.angleProperty(), -10)),
                new KeyFrame(Duration.millis(750), new KeyValue(rotate.angleProperty(), 5)),
                new KeyFrame(Duration.millis(900), new KeyValue(rotate.angleProperty(), 0))
        );
        timeline.setCycleCount(1);

        icon.setOnMouseEntered(event -> {
            icon.setEffect(new DropShadow(10, Color.GRAY));
            timeline.playFromStart();
        });
        icon.setOnMouseExited(event -> {
            icon.setEffect(null);
            rotate.setAngle(0);
        });
    }

    private void setupGifAnimation(ImageView gif) {
        if (gif == null) {
            System.out.println("Warning: homeGif is null");
            return;
        }
        Scale scale = new Scale(1, 1);
        gif.getTransforms().add(scale);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(scale.xProperty(), 1),
                        new KeyValue(scale.yProperty(), 1)),
                new KeyFrame(Duration.millis(300),
                        new KeyValue(scale.xProperty(), 1.1),
                        new KeyValue(scale.yProperty(), 1.1)),
                new KeyFrame(Duration.millis(600),
                        new KeyValue(scale.xProperty(), 1.1),
                        new KeyValue(scale.yProperty(), 1.1)),
                new KeyFrame(Duration.millis(900),
                        new KeyValue(scale.xProperty(), 1),
                        new KeyValue(scale.yProperty(), 1))
        );
        timeline.setCycleCount(1);

        gif.setOnMouseEntered(event -> {
            gif.setEffect(new DropShadow(10, Color.GRAY));
            timeline.playFromStart();
        });
        gif.setOnMouseExited(event -> {
            gif.setEffect(null);
            scale.setX(1);
            scale.setY(1);
        });
    }
    //---------------------------------------------------------------------------------------------------------------------------------------------//

}

