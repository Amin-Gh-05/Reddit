package org.project.reddit.front;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.project.reddit.user.User;

import java.io.IOException;
import java.util.Objects;

public class SignupController {
    @FXML
    private TextField emailText;
    @FXML
    private PasswordField passwordText;
    @FXML
    private TextField usernameText;
    @FXML
    private Label emailValidity;
    @FXML
    private Label usernameValidity;
    @FXML
    private Label passwordValidity;

    // cancel progress and return to main panel button
    @FXML
    void cancelSignUp(ActionEvent event) throws IOException {
        // change scene
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/project/reddit/main-view.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        System.out.println("> returned to main panel");
    }

    // switch to login page button
    @FXML
    void logIn(ActionEvent event) throws IOException {
        // change scene
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/project/reddit/login-view.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        System.out.println("> redirect to login page");
    }

    // sign in button
    @FXML
    void signUp(ActionEvent event) throws IOException {
        // clear test fields
        this.emailValidity.setText("");
        this.usernameValidity.setText("");
        this.passwordValidity.setText("");
        // check validity of email adress
        if (!User.validateEmail(this.emailText.getText())) {
            this.emailValidity.setText("Invalid");
            return;
        }
        // check validity of username
        if (!User.validateUsername(this.usernameText.getText())) {
            this.usernameValidity.setText("Invalid");
            return;
        }
        // check validity of password
        if (!User.validatePassword(this.passwordText.getText())) {
            this.passwordValidity.setText("Invalid");
            return;
        }
        User.signUp(this.emailText.getText(), this.usernameText.getText(), this.passwordText.getText());
        // show alert for succession of progress
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sign Up");
        alert.setHeaderText("You're signed up");
        alert.setContentText("Click ok to return to main panel");
        if (alert.showAndWait().get() == ButtonType.OK) {
            // change scene and return to main panel
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/project/reddit/main-view.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            System.out.println("> returned to main panel");
        }
    }
}
