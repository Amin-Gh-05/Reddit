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

    @FXML
    void cancelSignUp(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/project/reddit/main-view.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        System.out.println("> returned to main panel");
    }

    @FXML
    void logIn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/project/reddit/login-view.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        System.out.println("> redirect to login page");
    }

    @FXML
    void signUp(ActionEvent event) throws IOException {
        this.emailValidity.setText("");
        this.usernameValidity.setText("");
        this.passwordValidity.setText("");
        if (!User.validateEmail(this.emailText.getText())) {
            this.emailValidity.setText("Invalid");
            return;
        }
        if (!User.validateUsername(this.usernameText.getText())) {
            this.usernameValidity.setText("Invalid");
            return;
        }
        if (!User.validatePassword(this.passwordText.getText())) {
            this.passwordValidity.setText("Invalid");
            return;
        }
        User.signUp(this.emailText.getText(), this.usernameText.getText(), this.passwordText.getText());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sign Up");
        alert.setHeaderText("You're signed up");
        alert.setContentText("Click ok to return to main panel");
        if (alert.showAndWait().get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/project/reddit/main-view.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            System.out.println("> returned to main panel");
        }
    }
}
