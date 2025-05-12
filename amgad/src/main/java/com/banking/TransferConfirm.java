package com.banking;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
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
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
public class TransferConfirm {
    public Label AmCn;
    public Label AmFeCn;
    public Button TrCnf;
    public Label Lb1;
    public Label Lb2;
    public Label lb3;
    @FXML
    Label TransToCn;
    String str1,str2,str3;
    public void LabelText(String s1,String s2,String s3,String s4){
        TransToCn.setText(s1);
        AmCn.setText(s2);
        AmFeCn.setText(s3);
        if(s4.equals("Transfer")){
            this.str1=s1;
            this.str2=s3;
            this.str3=s4;
        }
        else if(s4.equals("Bills")){
            Lb1.setText("Bill Type:");
            Lb2.setText("Customer ID:");
            lb3.setText("Bill Amount:");
            this.str1=s1;
            this.str2=s3;
            this.str3=s4;
        }
        else if(s4.equals("Mobile Top-Up")){
            Lb1.setText("Network:");
            Lb2.setText("Mobile Number:");
            lb3.setText("Top-Up Amount:");
            this.str1=s1;
            this.str2=s3;
            this.str3=s4;
        }
        else if(s4.equals("Credit Card")){
            Lb1.setText("Chosen Card:");
            Lb2.setText("Amount:");
            lb3.setText("Amount with Fees:");
            this.str1=s1;
            this.str2=s3;
            this.str3=s4;
        }
        else if(s4.equals("Government Service")){
            Lb1.setText("Chosen Service:");
            Lb2.setText("Amount:");
            lb3.setText("Amount with Fees:");
            this.str1=s1;
            this.str2=s3;
            this.str3=s4;
        }
        else if(s4.equals("Donation")){
            Lb1.setText("Chosen Charity:");
            Lb2.setText("Donated Amount:");
            lb3.setText("Amount with Fees:");
            this.str1=s1;
            this.str2=s3;
            this.str3=s4;
        }
        else if(s4.equals("Education Payments")){
            Lb1.setText("Chosen Facility:");
            Lb2.setText("Student ID:");
            lb3.setText("Amount with Fees:");
            this.str1=s1;
            this.str2=s3;
            this.str3=s4;
        }
        else if(s4.equals("Insurance Payments")){
            Lb1.setText("Insurance Provider:");
            Lb2.setText("Policy Number:");
            lb3.setText("Amount with Fees:");
            this.str1=s1;
            this.str2=s3;
            this.str3=s4;
        }
        else if(s4.equals("Other Payments")){
            Lb1.setText("Payment Category:");
            Lb2.setText("Payee Name:");
            lb3.setText("Amount with Fees:");
            this.str1=s1;
            this.str2=s3;
            this.str3=s4;
        }
    }
    public void Clicking3(ActionEvent actionEvent) {
        UserSession session = UserSession.getInstance();
        String username = session.getUsername();

        // ✅ التحقق من str1 (المعرف أو البيانات الأخرى)
        if (str1 == null || str1.isEmpty()) {
            System.out.println("❌ المعرف أو البيانات فارغة!");
            return;
        }

        // ✅ التحقق من str2 (المبلغ)
        if (str2 == null || str2.isEmpty()) {
            System.out.println("❌ المبلغ فارغ!");
            return;
        }

        String amountString;

        if (str2.contains("EGP")) {
            amountString = str2.split("EGP")[0].trim();  // ياخد اللي قبل EGP ويشيل المسافات
        } else {
            amountString = str2.trim();  // لو مفيش EGP يتعامل مع الرقم على طول
        }

        double amount;

        try {
            amount = Double.parseDouble(amountString);
            if (amount <= 0) {
                System.out.println("❌ المبلغ يجب أن يكون أكبر من صفر!");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ خطأ في تنسيق المبلغ: " + str2);
            return;
        }

        boolean operationSuccess = false;

        // تنفيذ العملية حسب النوع المطلوب (str3)
        switch (str3) {
            case "Transfer":
                // ✅ الحصول على اسم المستخدم من المعرف
                String recipientUsername = database_BankSystem.getusernamebyid(str1);

                if (recipientUsername == null) {
                    System.out.println("❌ لم يتم العثور على مستخدم برقم معرف: " + str1);
                    return;
                }

                System.out.println("✅ تم العثور على المستخدم: " + recipientUsername);

                // ✅ تنفيذ التحويل
                operationSuccess = database_BankSystem.transfer(username, recipientUsername, amount);
                if (operationSuccess) {
                    System.out.println("✅ تمت عملية التحويل بنجاح! المبلغ: " + amount + " للمستخدم: " + recipientUsername);
                }
                break;

            case "Bills":
                // تنفيذ عملية دفع الفواتير
                String billType = TransToCn.getText();
                String customerId = str1;
                operationSuccess = database_BankSystem.transferBill(username, billType, customerId, amount);
                if (operationSuccess) {
                    System.out.println("✅ تمت عملية دفع الفاتورة بنجاح! النوع: " + billType + " للعميل: " + customerId);
                }
                break;

            case "Mobile Top-Up":
                // تنفيذ عملية شحن الرصيد
                String network = TransToCn.getText();
                String mobileNumber = str1;
                operationSuccess = database_BankSystem.transferMobileTopUp(username, network, mobileNumber, amount);
                if (operationSuccess) {
                    System.out.println("✅ تمت عملية شحن الرصيد بنجاح! الشبكة: " + network + " للرقم: " + mobileNumber);
                }
                break;

            case "Credit Card":
                // تنفيذ عملية دفع بطاقة الائتمان
                String cardType = TransToCn.getText();
                operationSuccess = database_BankSystem.transferCreditCard(username, cardType, amount);
                if (operationSuccess) {
                    System.out.println("✅ تمت عملية دفع بطاقة الائتمان بنجاح! النوع: " + cardType);
                }
                break;

            case "Government Service":
                // تنفيذ عملية دفع خدمات حكومية
                String serviceType = TransToCn.getText();
                String serviceNumber = str1;
                operationSuccess = database_BankSystem.transferGovernmentService(username, serviceType, serviceNumber, amount);
                if (operationSuccess) {
                    System.out.println("✅ تمت عملية دفع الخدمة الحكومية بنجاح! النوع: " + serviceType);
                }
                break;

            case "Donation":
                // تنفيذ عملية التبرع
                String charity = TransToCn.getText();
                operationSuccess = database_BankSystem.transferDonation(username, charity, amount);
                if (operationSuccess) {
                    System.out.println("✅ تمت عملية التبرع بنجاح! الجهة: " + charity);
                }
                break;

            case "Education Payments":
                // تنفيذ عملية دفع مصاريف تعليمية
                String facility = TransToCn.getText();
                String studentId = str1;
                operationSuccess = database_BankSystem.transferEducationPayment(username, facility, studentId, amount);
                if (operationSuccess) {
                    System.out.println("✅ تمت عملية دفع المصاريف التعليمية بنجاح! المؤسسة: " + facility);
                }
                break;

            case "Insurance Payments":
                // تنفيذ عملية دفع تأمين
                String provider = TransToCn.getText();
                String policyNumber = str1;
                operationSuccess = database_BankSystem.transferInsurancePayment(username, provider, policyNumber, amount);
                if (operationSuccess) {
                    System.out.println("✅ تمت عملية دفع التأمين بنجاح! الشركة: " + provider);
                }
                break;

            case "Other Payments":
                // تنفيذ عملية دفع أخرى
                String category = TransToCn.getText();
                String payeeName = str1;
                operationSuccess = database_BankSystem.transferOtherPayment(username, category, payeeName, amount);
                if (operationSuccess) {
                    System.out.println("✅ تمت عملية الدفع بنجاح! الفئة: " + category);
                }
                break;

            default:
                System.out.println("❌ نوع العملية غير معروف: " + str3);
                return;
        }

        if (!operationSuccess) {
            System.out.println("❌ فشلت العملية!");
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/maged/ConfirmPassword.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
            ConfirmPassword confirmPassword = fxmlLoader.getController();
            confirmPassword.getTxt(str1, str2, str3);
            System.out.println("✅ تم تحميل نافذة تأكيد كلمة المرور بنجاح");
        } catch (IOException e) {
            System.out.println("❌ خطأ في تحميل نافذة التأكيد: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        Stage stage = new Stage();
        stage.setTitle("Payment Confirmation");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        ((Stage)((Node)actionEvent.getSource()).getScene().getWindow()).close();
    }
}