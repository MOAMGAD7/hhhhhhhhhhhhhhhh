package com.banking;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
public class Payment {
    public Label TrToLb;
    public Label AmLb;
    public Label StLb;
    public Label DtLb;
    public Label CtgLb;
    public Label NoHsLb;// Balance description/heading label
    public AnchorPane HsPn;
    public AnchorPane payback;
    public VBox MyHs;
    public AnchorPane HsPn1;
    public Label TrToLb1;
    public Label AmLb1;
    public Label StLb1;
    public Label CtgLb1;
    public Label DtLb1;
    public AnchorPane HsPn11;
    public Label TrToLb11;
    public Label AmLb11;
    public Label StLb11;
    public Label DtLb11;
    public Label CtgLb11;
    public AnchorPane HsPn111;
    public Label TrToLb111;
    public Label AmLb111;
    public Label StLb111;
    public Label DtLb111;
    public Label CtgLb111;
    public AnchorPane HsPn1111;
    public Label TrToLb1111;
    public Label AmLb1111;
    public Label StLb1111;
    public Label CtgLb1111;
    public Label DtLb1111;
    public AnchorPane HsPn11111;
    public Label TrToLb11111;
    public Label AmLb11111;
    public Label StLb11111;
    public Label DtLb11111;
    public Label CtgLb11111;
    public int c=0;
    public ImageView BlUp;
    public ImageView BlDn;
    public Label BlINPy;
    public double bln=1000;
    public Button ToBills;
    public Button ToMobile;
    public Button ToCard;
    public Button ToGov;
    public Button ToDon;
    public Button ToEdu;
    public Button ToIns;
    public Button ToOthr;
    @FXML
    private Button toTransfer;
    @FXML
    public void initialize() {
        // الحصول على اسم المستخدم الحالي من UserSession
        UserSession session = UserSession.getInstance();
        String username = session.getUsername();

        // الحصول على رصيد المستخدم من قاعدة البيانات
        if (username != null) {
            double balance = database_BankSystem.getBalance(username);
            if (balance >= 0) {
                // عرض الرصيد في اللافتة BlINPy
                BlINPy.setText(String.format("%.2f EGP", balance));

                // تحميل آخر ٦ عمليات تحويل من قاعدة البيانات
                loadTransactionHistory(username, 6);
            } else {
                // إذا حدث خطأ في استرجاع الرصيد
                BlINPy.setText("N/A");
                System.out.println("❌ تعذر استرجاع رصيد المستخدم: " + username);
            }
        } else {
            // إذا لم يتم تسجيل الدخول
            BlINPy.setText("N/A");
            System.out.println("❌ لم يتم تسجيل الدخول، لا يمكن عرض الرصيد");
        }
    }

    /**
     * تحميل آخر عمليات التحويل من قاعدة البيانات وعرضها في واجهة المستخدم
     * @param username اسم المستخدم
     * @param limit عدد العمليات الأقصى للعرض
     */
    private void loadTransactionHistory(String username, int limit) {
        // جلب آخر التحويلات من قاعدة البيانات
        List<database_BankSystem.Transfer> transfers = database_BankSystem.getRecentTransfers(username, limit);

        if (transfers.isEmpty()) {
            // إظهار رسالة عدم وجود تحويلات
            NoHsLb.setVisible(true);
            return;
        }

        NoHsLb.setVisible(false);
        BlDn.setVisible(true);

        // مصفوفات للعناصر المختلفة في واجهة المستخدم
        Label[] toLabels = {TrToLb, TrToLb1, TrToLb11, TrToLb111, TrToLb1111, TrToLb11111};
        Label[] amountLabels = {AmLb, AmLb1, AmLb11, AmLb111, AmLb1111, AmLb11111};
        Label[] statusLabels = {StLb, StLb1, StLb11, StLb111, StLb1111, StLb11111};
        Label[] dateLabels = {DtLb, DtLb1, DtLb11, DtLb111, DtLb1111, DtLb11111};
        Label[] categoryLabels = {CtgLb, CtgLb1, CtgLb11, CtgLb111, CtgLb1111, CtgLb11111};
        AnchorPane[] historyPanes = {HsPn, HsPn1, HsPn11, HsPn111, HsPn1111, HsPn11111};

        // إظهار عدد من العناصر يساوي عدد التحويلات
        int size = Math.min(transfers.size(), limit);

        for (int i = 0; i < size; i++) {
            database_BankSystem.Transfer transfer = transfers.get(i);

            // تحديد ما إذا كان المستخدم هو المرسل أو المستقبل
            boolean isOutgoing = username.equals(transfer.getFromUser());
            String otherUser = isOutgoing ? transfer.getToUser() : transfer.getFromUser();

            // تعيين النص في العناصر
            toLabels[i].setText(otherUser);
            amountLabels[i].setText(isOutgoing ? "- " + transfer.getAmount() + " EGP" : "+ " + transfer.getAmount() + " EGP");
            amountLabels[i].setStyle("-fx-text-fill: " + (isOutgoing ? "rgba(255, 0, 0)" : "rgba(0, 204, 0)") + ";");
            statusLabels[i].setText(transfer.getStatus());
            dateLabels[i].setText(transfer.getDate());
            categoryLabels[i].setText("Transfer");

            // تعيين ألوان النص
            categoryLabels[i].setTextFill(javafx.scene.paint.Paint.valueOf("#008cff"));
            historyPanes[i].setStyle("-fx-background-color: rgba(0, 140, 255, 0.3);");

            // إظهار العنصر
            historyPanes[i].setVisible(true);
        }

        // إخفاء العناصر المتبقية
        for (int i = size; i < limit; i++) {
            historyPanes[i].setVisible(false);
        }
    }

    @FXML
    public void onBillPaymentClicked(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/maged/PaymentPage.fxml"));
        Parent paymentPage = fxmlLoader.load();
        PaymentPage paymentPage1 =fxmlLoader.getController();
        paymentPage1.setPaymentCategory("Bills");
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
        Stage stage = (Stage) ToBills.getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    public void switchToPage2(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/maged/Transfer.fxml"));
        Parent page2 = fxmlLoader.load();

        // Load background image - validate resource path
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
        stackPane.getChildren().add(page2);

        // Create the scene
        Scene scene = new Scene(stackPane, 1200, 700);

        Stage stage = (Stage) toTransfer.getScene().getWindow();
        stage.setScene(scene);
    }
    // Declare these as class variables to access in setColors method


    /**
     * تحديث تاريخ العمليات بإضافة عملية جديدة ونقل العمليات القديمة للأسفل
     * مع الاحتفاظ بآخر 6 عمليات فقط
     */
    public void addHistory(String recipient, String amount, String category) {
        // 1. تحديث الرصيد
        try {
            String currentBalanceText = BlINPy.getText();
            String numericText = currentBalanceText.replaceAll("[^0-9.]", "");
            double currentBalance = Double.parseDouble(numericText);
            double transferAmount = Double.parseDouble(amount.replaceAll("[^0-9.]", ""));
            double newBalance = currentBalance - transferAmount;

            // تحديث عرض الرصيد
            BlINPy.setText(String.format("%.2f EGP", newBalance));
            System.out.println("تم تحديث الرصيد إلى: " + newBalance);
        } catch (Exception e) {
            System.err.println("خطأ في تحديث الرصيد: " + e.getMessage());
            e.printStackTrace();
        }

        // 2. تحريك جميع العمليات القديمة للأسفل بعملية واحدة
        shiftHistoryDown();

        // 3. إضافة العملية الجديدة في الأعلى
        NoHsLb.setVisible(false);
        BlDn.setVisible(true);

        // إعداد العملية الجديدة
        TrToLb.setText(recipient);
        AmLb.setText("- " + amount);
        StLb.setText("Success");
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM dd, hh:mm a");
        String formatted = now.format(formatter);
        DtLb.setText(formatted);
        CtgLb.setText(category);

        // 4. تعيين النمط والألوان المناسبة
        updateHistoryItemStyle(HsPn, CtgLb, AmLb, category);

        // 5. إضافة العملية لقاعدة البيانات
        UserSession session = UserSession.getInstance();
        String username = session.getUsername();
        if (username != null) {
            // تسجيل العملية في قاعدة البيانات لتظهر في تاريخ المعاملات لاحقاً
            if (category.equals("Transfer")) {
                database_BankSystem.recordTransfer(username, recipient,
                        Double.parseDouble(amount.replaceAll("[^0-9.]", "")), "completed");
            }
        }
    }

    /**
     * تحريك جميع عناصر التاريخ للأسفل لإفساح المجال للعملية الجديدة
     */
    private void shiftHistoryDown() {
        // نقل من علبة ٥ إلى علبة ٦ (إزالة أقدم عملية)
        if (HsPn1111.isVisible()) {
            TrToLb11111.setText(TrToLb1111.getText());
            AmLb11111.setText(AmLb1111.getText());
            StLb11111.setText(StLb1111.getText());
            DtLb11111.setText(DtLb1111.getText());
            CtgLb11111.setText(CtgLb1111.getText());
            updateHistoryItemStyle(HsPn11111, CtgLb11111, AmLb11111, CtgLb1111.getText());
            HsPn11111.setVisible(true);
        }

        // نقل من علبة ٤ إلى علبة ٥
        if (HsPn111.isVisible()) {
            TrToLb1111.setText(TrToLb111.getText());
            AmLb1111.setText(AmLb111.getText());
            StLb1111.setText(StLb111.getText());
            DtLb1111.setText(DtLb111.getText());
            CtgLb1111.setText(CtgLb111.getText());
            updateHistoryItemStyle(HsPn1111, CtgLb1111, AmLb1111, CtgLb111.getText());
            HsPn1111.setVisible(true);
        }

        // نقل من علبة ٣ إلى علبة ٤
        if (HsPn11.isVisible()) {
            TrToLb111.setText(TrToLb11.getText());
            AmLb111.setText(AmLb11.getText());
            StLb111.setText(StLb11.getText());
            DtLb111.setText(DtLb11.getText());
            CtgLb111.setText(CtgLb11.getText());
            updateHistoryItemStyle(HsPn111, CtgLb111, AmLb111, CtgLb11.getText());
            HsPn111.setVisible(true);
        }

        // نقل من علبة ٢ إلى علبة ٣
        if (HsPn1.isVisible()) {
            TrToLb11.setText(TrToLb1.getText());
            AmLb11.setText(AmLb1.getText());
            StLb11.setText(StLb1.getText());
            DtLb11.setText(DtLb1.getText());
            CtgLb11.setText(CtgLb1.getText());
            updateHistoryItemStyle(HsPn11, CtgLb11, AmLb11, CtgLb1.getText());
            HsPn11.setVisible(true);
        }

        // نقل من علبة ١ إلى علبة ٢
        if (HsPn.isVisible()) {
            TrToLb1.setText(TrToLb.getText());
            AmLb1.setText(AmLb.getText());
            StLb1.setText(StLb.getText());
            DtLb1.setText(DtLb.getText());
            CtgLb1.setText(CtgLb.getText());
            updateHistoryItemStyle(HsPn1, CtgLb1, AmLb1, CtgLb.getText());
            HsPn1.setVisible(true);
        }
    }

    /**
     * تحديث نمط عنصر التاريخ بناءً على نوع العملية
     */
    private void updateHistoryItemStyle(AnchorPane pane, Label categoryLabel, Label amountLabel, String category) {
        if (category.equals("Transfer")) {
            categoryLabel.setTextFill(javafx.scene.paint.Paint.valueOf("#008cff"));
            pane.setStyle("-fx-background-color: rgba(0, 140, 255, 0.3);");
        } else {
            categoryLabel.setTextFill(javafx.scene.paint.Paint.valueOf("#FF3232"));
            pane.setStyle("-fx-background-color: rgba(255, 50, 50, 0.3);");
        }
        amountLabel.setStyle("-fx-text-fill: rgba(255, 0, 0);");
    }

    /**
     * هذه الدوال معطلة الآن وتم استبدالها بدالة واحدة معممة
     * يتم الاحتفاظ بها مؤقتاً للتوافق مع الكود القديم
     */
    public void addHistory1(String s1, String s2, String s3) {
        addHistory(s1, s2, s3);
    }

    public void addHistory2(String s1, String s2, String s3) {
        addHistory(s1, s2, s3);
    }

    public void addHistory3(String s1, String s2, String s3) {
        addHistory(s1, s2, s3);
    }

    public void addHistory4(String s1, String s2, String s3) {
        addHistory(s1, s2, s3);
    }

    public void addHistory5(String s1, String s2, String s3) {
        addHistory(s1, s2, s3);
    }
    public void setColors(){
        if(Objects.equals(CtgLb.getText(), "Transfer")){
            CtgLb.setTextFill(Paint.valueOf("#008cff"));
            HsPn.setStyle("-fx-background-color: rgba(0, 140, 255, 0.3);");
        }
        else{
            CtgLb.setTextFill(Paint.valueOf("#FF3232"));
            HsPn.setStyle("-fx-background-color: rgba(255, 50, 50, 0.3);");
        }
        AmLb.setStyle("-fx-text-fill: rgba(255, 0, 0);");
    }
    public void setColors1(){
        if(Objects.equals(CtgLb1.getText(), "Transfer")){
            CtgLb1.setTextFill(Paint.valueOf("#008cff"));
            HsPn1.setStyle("-fx-background-color: rgba(0, 140, 255, 0.3);");
        }
        else{
            CtgLb1.setTextFill(Paint.valueOf("#FF3232"));
            HsPn1.setStyle("-fx-background-color: rgba(255, 50, 50, 0.3);");
        }
        AmLb1.setStyle("-fx-text-fill: rgba(255, 0, 0);");
    }
    public void setColors2(){
        if(Objects.equals(CtgLb11.getText(), "Transfer")){
            CtgLb11.setTextFill(Paint.valueOf("#008cff"));
            HsPn11.setStyle("-fx-background-color: rgba(0, 140, 255, 0.3);");
        }
        else{
            CtgLb11.setTextFill(Paint.valueOf("#FF3232"));
            HsPn11.setStyle("-fx-background-color: rgba(255, 50, 50, 0.3);");
        }
        AmLb11.setStyle("-fx-text-fill: rgba(255, 0, 0);");
    }
    public void setColors3(){
        if(Objects.equals(CtgLb111.getText(), "Transfer")){
            CtgLb111.setTextFill(Paint.valueOf("#008cff"));
            HsPn111.setStyle("-fx-background-color: rgba(0, 140, 255, 0.3);");
        }
        else{
            CtgLb111.setTextFill(Paint.valueOf("#FF3232"));
            HsPn111.setStyle("-fx-background-color: rgba(255, 50, 50, 0.3);");
        }
        AmLb111.setStyle("-fx-text-fill: rgba(255, 0, 0);");
    }
    public void setColors4(){
        if(Objects.equals(CtgLb1111.getText(), "Transfer")){
            CtgLb1111.setTextFill(Paint.valueOf("#008cff"));
            HsPn1111.setStyle("-fx-background-color: rgba(0, 140, 255, 0.3);");
        }
        else{
            CtgLb1111.setTextFill(Paint.valueOf("#FF3232"));
            HsPn1111.setStyle("-fx-background-color: rgba(255, 50, 50, 0.3);");
        }
        AmLb1111.setStyle("-fx-text-fill: rgba(255, 0, 0);");
    }
    public void setColors5(){
        if(Objects.equals(CtgLb11111.getText(), "Transfer")){
            CtgLb11111.setTextFill(Paint.valueOf("#008cff"));
            HsPn11111.setStyle("-fx-background-color: rgba(0, 140, 255, 0.3);");
        }
        else{
            CtgLb11111.setTextFill(Paint.valueOf("#FF3232"));
            HsPn11111.setStyle("-fx-background-color: rgba(255, 50, 50, 0.3);");
        }
        AmLb11111.setStyle("-fx-text-fill: rgba(255, 0, 0);");
    }

    public void onMobileClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/maged/PaymentPage.fxml"));
        Parent paymentPage = fxmlLoader.load();
        PaymentPage paymentPage1 =fxmlLoader.getController();
        paymentPage1.setPaymentCategory("Mobile Top-Up");
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
        Stage stage = (Stage) ToBills.getScene().getWindow();
        stage.setScene(scene);
    }

    public void onCardClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/maged/PaymentPage.fxml"));
        Parent paymentPage = fxmlLoader.load();
        PaymentPage paymentPage1 =fxmlLoader.getController();
        paymentPage1.setPaymentCategory("Credit Card");
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
        Stage stage = (Stage) ToBills.getScene().getWindow();
        stage.setScene(scene);
    }

    public void onGovClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/maged/PaymentPage.fxml"));
        Parent paymentPage = fxmlLoader.load();
        PaymentPage paymentPage1 =fxmlLoader.getController();
        paymentPage1.setPaymentCategory("Government Service");
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
        Stage stage = (Stage) ToBills.getScene().getWindow();
        stage.setScene(scene);
    }

    public void onDonClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/maged/PaymentPage.fxml"));
        Parent paymentPage = fxmlLoader.load();
        PaymentPage paymentPage1 =fxmlLoader.getController();
        paymentPage1.setPaymentCategory("Donation");
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
        Stage stage = (Stage) ToBills.getScene().getWindow();
        stage.setScene(scene);
    }

    public void onEduClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/maged/PaymentPage.fxml"));
        Parent paymentPage = fxmlLoader.load();
        PaymentPage paymentPage1 =fxmlLoader.getController();
        paymentPage1.setPaymentCategory("Education Payments");
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
        Stage stage = (Stage) ToBills.getScene().getWindow();
        stage.setScene(scene);
    }

    public void onInsClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/maged/PaymentPage.fxml"));
        Parent paymentPage = fxmlLoader.load();
        PaymentPage paymentPage1 =fxmlLoader.getController();
        paymentPage1.setPaymentCategory("Insurance Payments");
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
        Stage stage = (Stage) ToBills.getScene().getWindow();
        stage.setScene(scene);
    }

    public void OnOtherClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/maged/PaymentPage.fxml"));
        Parent paymentPage = fxmlLoader.load();
        PaymentPage paymentPage1 =fxmlLoader.getController();
        paymentPage1.setPaymentCategory("Other Payments");
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
        Stage stage = (Stage) ToBills.getScene().getWindow();
        stage.setScene(scene);
    }
}