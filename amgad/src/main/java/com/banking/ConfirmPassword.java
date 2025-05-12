package com.banking;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ConfirmPassword {
    public PasswordField p1;
    public PasswordField p2;
    public PasswordField p3;
    public PasswordField p4;
    public PasswordField p5;
    public PasswordField p6;
    public ImageView im1;
    public ImageView im2;
    public ImageView im3;
    public ImageView im4;
    public ImageView im5;
    public ImageView im6;
    public Button ShwPs;
    public Label lp1;
    public Label lp2;
    public Label lp3;
    public Label lp4;
    public Label lp5;
    public Label lp6;
    public Label WrPs;
    public Button CnPsBn;
    int arr[]=new int[6];
    int[] password = {1, 2, 3, 4, 5,6};
    String sr1,sr2,sr3;
    public int c=0;
    public void initialize() {
        im1.setVisible(false);
        im2.setVisible(false);
        im3.setVisible(false);
        im4.setVisible(false);
        im5.setVisible(false);
        im6.setVisible(false);
        lp1.setVisible(false);
        lp2.setVisible(false);
        lp3.setVisible(false);
        lp4.setVisible(false);
        lp5.setVisible(false);
        lp6.setVisible(false);
        WrPs.setVisible(false);
        setupField(p1, p2);
        setupField(p2, p3);
        setupField(p3, p4);
        setupField(p4, p5);
        setupField(p5, p6);
        setupField(p6, null);
        p1.textProperty().addListener((observable, oldValue, newValue1) -> {
            im1.setVisible(!newValue1.isEmpty());
            arr[0]=Integer.parseInt(newValue1);
        });
        p2.textProperty().addListener((observable, oldValue, newValue2) -> {
            im2.setVisible(!newValue2.isEmpty());
            arr[1]=Integer.parseInt(newValue2);
        });
        p3.textProperty().addListener((observable, oldValue, newValue3) -> {
            im3.setVisible(!newValue3.isEmpty());
            arr[2]=Integer.parseInt(newValue3);
        });
        p4.textProperty().addListener((observable, oldValue, newValue4) -> {
            im4.setVisible(!newValue4.isEmpty());
            arr[3]=Integer.parseInt(newValue4);
        });
        p5.textProperty().addListener((observable, oldValue, newValue5) -> {
            im5.setVisible(!newValue5.isEmpty());
            arr[4]=Integer.parseInt(newValue5);
        });
        p6.textProperty().addListener((observable, oldValue, newValue6) -> {
            im6.setVisible(!newValue6.isEmpty());
            arr[5]=Integer.parseInt(newValue6);
        });
    }

    private void setupField(PasswordField current, PasswordField next) {
        current.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.length() == 1 && next != null) {
                next.requestFocus();
            }
            if (newVal.length() > 1) {
                current.setText(newVal.substring(0, 1)); // Limit to one character
            }
        });
    }
    public void getTxt(String s1,String s2,String s3){
        this.sr1=s1;
        this.sr2=s2;
        this.sr3=s3;
    }
    int cnt=0;
    public void Clicking4(ActionEvent actionEvent) {
        if(cnt%2==0){
            im1.setVisible(true);
            im2.setVisible(true);
            im3.setVisible(true);
            im4.setVisible(true);
            im5.setVisible(true);
            im6.setVisible(true);
            lp1.setVisible(false);
            lp2.setVisible(false);
            lp3.setVisible(false);
            lp4.setVisible(false);
            lp5.setVisible(false);
            lp6.setVisible(false);
            p1.setVisible(true);
            p2.setVisible(true);
            p3.setVisible(true);
            p4.setVisible(true);
            p5.setVisible(true);
            p6.setVisible(true);
        }
        else{
            im1.setVisible(false);
            im2.setVisible(false);
            im3.setVisible(false);
            im4.setVisible(false);
            im5.setVisible(false);
            im6.setVisible(false);
            lp1.setVisible(true);
            lp2.setVisible(true);
            lp3.setVisible(true);
            lp4.setVisible(true);
            lp5.setVisible(true);
            lp6.setVisible(true);
            lp1.setText(String.valueOf(arr[0]));
            lp2.setText(String.valueOf(arr[1]));
            lp3.setText(String.valueOf(arr[2]));
            lp4.setText(String.valueOf(arr[3]));
            lp5.setText(String.valueOf(arr[4]));
            lp6.setText(String.valueOf(arr[5]));
            p1.setVisible(false);
            p2.setVisible(false);
            p3.setVisible(false);
            p4.setVisible(false);
            p5.setVisible(false);
            p6.setVisible(false);
        }
        cnt++;
    }
    public void Clicking5(ActionEvent actionEvent) {
        boolean flag=true;
        for(int i=0;i<6;i++){
            if(arr[i]!=password[i]){
                flag=false;
            }
        }
        if(!flag){
            WrPs.setVisible(true);
            p1.setVisible(true);
            p2.setVisible(true);
            p3.setVisible(true);
            p4.setVisible(true);
            p5.setVisible(true);
            p6.setVisible(true);
            p1.clear();
            p2.clear();
            p3.clear();
            p4.clear();
            p5.clear();
            p6.clear();
            p1.requestFocus();
            im1.setVisible(false);
            im2.setVisible(false);
            im3.setVisible(false);
            im4.setVisible(false);
            im5.setVisible(false);
            im6.setVisible(false);
            lp1.setVisible(false);
            lp2.setVisible(false);
            lp3.setVisible(false);
            lp4.setVisible(false);
            lp5.setVisible(false);
            lp6.setVisible(false);
        }
        else{
            WrPs.setVisible(false);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/maged/OperationSucceeded.fxml"));
            Parent root = null;
            try {
                root = fxmlLoader.load();
                OperationSucceeded operationSucceeded =fxmlLoader.getController();
                operationSucceeded.getT(sr1,sr2,sr3);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Stage stage = new Stage();
            stage.setTitle("Success!");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            ((Stage)((Node)actionEvent.getSource()).getScene().getWindow()).close();
        }
    }
}
