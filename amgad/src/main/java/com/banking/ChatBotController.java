package com.banking;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
public class ChatBotController {
    @FXML private TextField questionField;
    @FXML private ScrollPane chatScrollPane;
    @FXML private VBox chatHistory;

    private String currentUsername;

    @FXML
    public void initialize() {
        // Get the username from the session
        UserSession session = UserSession.getInstance();
        currentUsername = session.getUsername();
        if (currentUsername == null) {
            addMessageToChat("System", "User session not found. Please log in again.", "message-system");
        } else {
            addMessageToChat("Assistant", "Hello! How can I assist you today with any questions you have about banking services?", "message-assistant");
        }
    }

    @FXML
    protected void handleAskChatbot(ActionEvent event) {
        String question = questionField.getText();
        if (question == null || question.trim().isEmpty()) {
            addMessageToChat("System", "Please enter a question.", "message-system");
            return;
        }

        if (currentUsername == null) {
            addMessageToChat("System", "User session not found. Please log in again.", "message-system");
            return;
        }

        // إضافة سؤال المستخدم لتاريخ المحادثة
        addMessageToChat("You", question, "message-user");
        questionField.clear();

        try {
            // Prompt أولي عشان نحدد إذا كان السؤال متعلق بالداتا بيز أو لا
            String initialPrompt = "You are a banking assistant. Determine if the following question is related to banking data (e.g., balance, transactions, transfers, or money transfer). " +
                    "If it is related to banking data, respond with 'BANKING_DATA'. If it is a general question, respond with 'GENERAL'. Question: " + question;

            String questionType = OpenAIChatbot.getChatResponse(initialPrompt).trim();

            if (questionType.equalsIgnoreCase("BANKING_DATA")) {
                // لو السؤال متعلق بالداتا بيز، نكمل زي ما كنا بنعمل
                String dataPrompt = "You are a banking assistant with access to a user's account information in a database. " +
                        "You can retrieve the user's balance, recent transactions, recent transfers, or process a money transfer. " +
                        "The user has already logged in, so you can access their data using their username: " + currentUsername + ". " +
                        "Respond to the following question with a keyword or phrase indicating what data to retrieve or action to perform (e.g., 'balance', 'transactions', 'transfers', 'transfer money'). " +
                        "Do not provide any sensitive information directly. Question: " + question;

                String aiResponse = OpenAIChatbot.getChatResponse(dataPrompt).toLowerCase().trim();

                // معالجة الرد بناءً على نوع السؤال
                if (aiResponse.contains("balance")) {
                    double balance = database_BankSystem.getBalance(currentUsername);
                    if (balance >= 0) {
                        addMessageToChat("Assistant", "Your current balance is: $" + balance, "message-assistant");
                    } else {
                        addMessageToChat("Assistant", "Unable to retrieve your balance at this time. Please try again later.", "message-assistant");
                    }
                } else if (aiResponse.contains("transactions")) {
                    List<database_BankSystem.Transaction> transactions = database_BankSystem.getRecentTransactions(currentUsername, 5);
                    if (transactions.isEmpty()) {
                        addMessageToChat("Assistant", "No recent transactions found.", "message-assistant");
                    } else {
                        StringBuilder transactionList = new StringBuilder("Your recent transactions:\n");
                        for (database_BankSystem.Transaction transaction : transactions) {
                            transactionList.append(transaction.toString()).append("\n");
                        }
                        addMessageToChat("Assistant", transactionList.toString(), "message-assistant");
                    }
                } else if (aiResponse.contains("transfers")) {
                    List<database_BankSystem.Transfer> transfers = database_BankSystem.getRecentTransfers(currentUsername, 5);
                    if (transfers.isEmpty()) {
                        addMessageToChat("Assistant", "No recent transfers found.", "message-assistant");
                    } else {
                        StringBuilder transferList = new StringBuilder("Your recent transfers:\n");
                        for (database_BankSystem.Transfer transfer : transfers) {
                            transferList.append(transfer.toString()).append("\n");
                        }
                        addMessageToChat("Assistant", transferList.toString(), "message-assistant");
                    }
                } else if (aiResponse.contains("transfer money")) {
                    if (question.toLowerCase().contains("transfer") && question.contains("to")) {
                        try {
                            String[] parts = question.split("to");
                            String amountStr = parts[0].replaceAll("[^0-9.]", "").trim();
                            String toUsername = parts[1].trim().split(" ")[0];
                            double amount = Double.parseDouble(amountStr);
                            boolean success = database_BankSystem.transfer(currentUsername, toUsername, amount);
                            if (success) {
                                addMessageToChat("Assistant", "Transfer of $" + amount + " to " + toUsername + " completed successfully!", "message-assistant");
                            } else {
                                addMessageToChat("Assistant", "Transfer failed. Please check the username or ensure you have sufficient funds.", "message-assistant");
                            }
                        } catch (Exception e) {
                            addMessageToChat("Assistant", "Error processing transfer. Please use the format 'Transfer 100 to user123'.", "message-assistant");
                        }
                    } else {
                        addMessageToChat("Assistant", "To transfer money, please provide the recipient's username and the amount. For example, 'Transfer 100 to user123'.", "message-assistant");
                    }
                } else {
                    addMessageToChat("Assistant", "Sorry, I didn't understand your banking request. You can ask about your balance, recent transactions, transfers, or transfer money to another user.", "message-assistant");
                }
            } else {
                // لو السؤال عام، نجاوب عليه بشكل مباشر باستخدام OpenAI API
                String generalPrompt = "You are a helpful assistant. Answer the following question: " + question;
                String generalResponse = OpenAIChatbot.getChatResponse(generalPrompt);
                addMessageToChat("Assistant", generalResponse, "message-assistant");
            }
        } catch (IOException e) {
            addMessageToChat("System", "Error communicating with chatbot: " + e.getMessage(), "message-system");
        }
    }

    @FXML
    protected void clearChat(ActionEvent event) {
        chatHistory.getChildren().clear();
        addMessageToChat("Assistant", "Chat cleared! How can I assist you now?", "message-assistant");
    }

    private void addMessageToChat(String sender, String message, String styleClass) {
        Label messageLabel = new Label(sender + ": " + message);
        messageLabel.setWrapText(true);
        messageLabel.getStyleClass().add(styleClass);
        chatHistory.getChildren().add(messageLabel);

        // Scroll to bottom
        chatScrollPane.layout();
        chatScrollPane.setVvalue(1.0);
    }

    @FXML
    protected void goToDashboard(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/maged/dashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load background image
        Image backgroundImage = new Image(getClass().getResourceAsStream("/back.jpg"));
        ImageView backgroundView = new ImageView(backgroundImage);

        // Get screen dimensions
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

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

        // Stack background, overlay, and UI content
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(backgroundView, blueOverlay, root);

        Scene scene = new Scene(stackPane);
        UserSession session = UserSession.getInstance();
        scene.getStylesheets().clear();

        stage.setScene(scene);
        stage.setTitle("Dashboard");
        stage.show();
    }
}