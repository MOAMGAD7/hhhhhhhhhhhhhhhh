package com.banking;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.IOException;
import java.util.Properties;

public class VerifyEmailController {

    @FXML private TextField codeField;
    @FXML private Label codeError;

    // Email settings (SMTP)
    //smtp.gmail.com" هو عنوان السيرفر الذي يتعامل مع إرسال الرسائل للبريد الإلكتروني التابع لجوجل.
    private static final String SMTP_HOST = "smtp.gmail.com";
    //587: اتصال مشفر باستخدام STARTTLS ← الموصى به من Google.
    //
    //في هذه الحالة نستخدم 587 مع TLS encryption الذي يبدأ بعد الاتصال.
    private static final String SMTP_PORT = "587";
    private static final String SENDER_EMAIL = "mohamedamgad7777@gmail.com"; // Replace with your Gmail address
    private static final String SENDER_PASSWORD = "xnpvkxlplwtqscbg"; // Replace with your App Password

    @FXML
    protected void handleVerifyCode(ActionEvent event) throws IOException {
        // استخراج الكود المدخل من الحقل
        String code = codeField.getText();
        UserSession session = UserSession.getInstance();
        String username = session.getUsername();

        // التحقق من إن الكود مش فاضي
        if (code.isEmpty()) {
            codeError.setText("Please enter the verification code");
            return;
        }

        // التحقق من إن الـ username مش null
        if (username == null || username.isEmpty()) {
            codeError.setText("Error: User session is invalid. Please try again.");
            System.err.println("Error in VerifyEmailController: Username is null or empty");
            return;
        }

        // التحقق من صحة الكود باستخدام قاعدة البيانات
        if (database_BankSystem.verifyCode(username, code)) {
            // طباعة قيمة requestSource للتأكد من المصدر
            System.out.println("Request Source in VerifyEmailController: " + session.getRequestSource());

            // تحديد المكان اللي هنروح ليه بناءً على مصدر الطلب
            String fxmlPath = "/com/example/maged/ChangePassword.fxml"; // الافتراضي (Forget Password)
            String title = "Change Password";
            String emailRecipient = database_BankSystem.getEmailByUsername(username); // جلب الإيميل من قاعدة البيانات

            if (emailRecipient == null) {
                System.err.println("Error in VerifyEmailController: Could not retrieve email for username: " + username);
                codeError.setText("Error: Could not send confirmation email. Please try again.");
                return;
            }

            if ("Signup".equals(session.getRequestSource())) {
                fxmlPath = "/com/example/maged/Login.fxml";
                title = "Login";
                // إرسال إيميل تأكيد التسجيل
                boolean emailSent = sendSignupConfirmationEmail(emailRecipient, username);
                if (!emailSent) {
                    System.err.println("Failed to send signup confirmation email to: " + emailRecipient);
                }
            } else if ("ForgetPassword".equals(session.getRequestSource())) {
                fxmlPath = "/com/example/maged/ChangePassword.fxml";
                title = "Change Password";
                // إرسال إيميل تأكيد تغيير كلمة المرور
                boolean emailSent = sendPasswordChangeConfirmationEmail(emailRecipient, username);
                if (!emailSent) {
                    System.err.println("Failed to send password change confirmation email to: " + emailRecipient);
                }
            } else {
                // لو requestSource كان null أو قيمة غير متوقعة
                System.err.println("Unknown request source: " + session.getRequestSource() + ". Defaulting to Login.");
                fxmlPath = "/com/example/maged/Login.fxml";
                title = "Login";
            }

            // تحميل الصفحة المناسبة
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // تحميل خلفية الصورة
            Image backgroundImage = new Image(getClass().getResourceAsStream("/back.jpg"));
            ImageView backgroundView = new ImageView(backgroundImage);

            // الحصول على أبعاد الشاشة
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double screenWidth = screenBounds.getWidth();
            double screenHeight = screenBounds.getHeight();

            // ضبط الخلفية لتناسب الشاشة
            backgroundView.setFitWidth(screenWidth);
            backgroundView.setFitHeight(screenHeight);
            backgroundView.setPreserveRatio(false);
            backgroundView.setEffect(new GaussianBlur(20));

            // إنشاء طبقة زرقاء شفافة
            Region blueOverlay = new Region();
            blueOverlay.setBackground(new Background(new BackgroundFill(
                    Color.rgb(0, 120, 255, 0.2),
                    CornerRadii.EMPTY,
                    Insets.EMPTY
            )));
            blueOverlay.setEffect(new GaussianBlur(20));
            blueOverlay.setPrefSize(screenWidth, screenHeight);

            // تجميع الخلفية والطبقة الشفافة وواجهة المستخدم
            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(backgroundView, blueOverlay, root);

            // إنشاء المشهد
            Scene scene = new Scene(stackPane);
            scene.getStylesheets().clear();


            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();

            // سجل لتأكيد التنقل
            System.out.println("Successfully navigated to: " + fxmlPath);

            // تصفير المصدر بعد التنقل لتجنب التعارض في المستقبل
            session.setRequestSource(null);
        } else {
            codeError.setText("Invalid verification code");
        }
    }

    @FXML
    protected void switchToLogin(ActionEvent event) throws IOException {
        // التنقل مباشرة إلى صفحة الـ Login
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/maged/Login.fxml"));
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
        
        // Stack background, overlay, and FXML UI
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(backgroundView, blueOverlay, root);
        
        // Create the scene
        Scene scene = new Scene(stackPane);
        UserSession session = UserSession.getInstance();
        scene.getStylesheets().clear();

        
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();

        // تصفير المصدر بعد التنقل
        session.setRequestSource(null);

        // سجل لتأكيد التنقل
        System.out.println("Successfully navigated to: /com/example/maged/Login.fxml (via switchToLogin)");
    }

    // دالة لإرسال إيميل تأكيد التسجيل
    private boolean sendSignupConfirmationEmail(String recipientEmail, String username) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("🎉 Welcome to BMA Bank - Registration Successful!");

            // تصميم الإيميل بطريقة لطيفة باستخدام HTML
            String htmlContent = """
                <div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px; background-color: #f9f9f9;'>
                    <h2 style='color: #2c3e50; text-align: center;'>🎉 Welcome to BMA Bank, %s! 🎉</h2>
                    <p style='color: #34495e; font-size: 16px; text-align: center;'>You’ve successfully registered with us!</p>
                    <div style='text-align: center; margin: 20px 0;'>
                        <p style='color: #34495e; font-size: 16px;'>We’re thrilled to have you on board. Start exploring your account and enjoy seamless banking with BMA Bank!</p>
                    </div>
                    <p style='color: #34495e; font-size: 16px; text-align: center;'>Log in now to get started:</p>
                    <div style='text-align: center;'>
                        <a href='#' style='background-color: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; display: inline-block;'>
                            Log In to Your Account
                        </a>
                    </div>
                    <p style='color: #34495e; font-size: 16px; text-align: center; margin-top: 20px;'>
                        Need help? Feel free to reach out at 
                        <a href='mailto:support@bmabank.com' style='color: #4CAF50; text-decoration: none;'>support@bmabank.com</a>.
                    </p>
                    <p style='color: #34495e; font-size: 16px; text-align: center; margin-top: 30px;'>
                        Best regards,<br>
                        <strong style='color: #2c3e50;'>The BMA Bank Team</strong>
                    </p>
                </div>
                """.formatted(username);
            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("✅ Signup confirmation email sent successfully to " + recipientEmail);
            return true;
        } catch (MessagingException e) {
            System.out.println("❌ Error sending signup confirmation email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // دالة لإرسال إيميل تأكيد تغيير كلمة المرور
    private boolean sendPasswordChangeConfirmationEmail(String recipientEmail, String username) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("🔒 Password Changed Successfully!");

            // تصميم الإيميل بطريقة لطيفة باستخدام HTML
            String htmlContent = """
                <div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px; background-color: #f9f9f9;'>
                    <h2 style='color: #2c3e50; text-align: center;'>🔒 Password Changed Successfully, %s!</h2>
                    <p style='color: #34495e; font-size: 16px; text-align: center;'>Your password has been updated successfully.</p>
                    <div style='text-align: center; margin: 20px 0;'>
                        <p style='color: #34495e; font-size: 16px;'>You’re all set to continue banking with us securely. If you didn’t make this change, please contact us immediately.</p>
                    </div>
                    <p style='color: #34495e; font-size: 16px; text-align: center;'>Log in with your new password to continue:</p>
                    <div style='text-align: center;'>
                        <a href='#' style='background-color: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; display: inline-block;'>
                            Log In to Your Account
                        </a>
                    </div>
                    <p style='color: #34495e; font-size: 16px; text-align: center; margin-top: 20px;'>
                        Need help? Feel free to reach out at 
                        <a href='mailto:support@bmabank.com' style='color: #4CAF50; text-decoration: none;'>support@bmabank.com</a>.
                    </p>
                    <p style='color: #34495e; font-size: 16px; text-align: center; margin-top: 30px;'>
                        Best regards,<br>
                        <strong style='color: #2c3e50;'>The BMA Bank Team</strong>
                    </p>
                </div>
                """.formatted(username);
            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("✅ Password change confirmation email sent successfully to " + recipientEmail);
            return true;
        } catch (MessagingException e) {
            System.out.println("❌ Error sending password change confirmation email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}