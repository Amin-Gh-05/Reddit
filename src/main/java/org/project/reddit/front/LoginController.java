package org.project.reddit.front;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.project.reddit.user.User;

import java.io.IOException;
import java.util.Objects;

public class LoginController {
    @FXML
    private Label credValidity;
    @FXML
    private PasswordField passwordText;
    @FXML
    private TextField usernameText;

    // return to main panel and cancel logging in
    @FXML
    void cancelLogIn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/project/reddit/main-view.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        System.out.println("> returned to main panel");
    }

    // log in if possible and redirect to user panel
    @FXML
    void logIn(ActionEvent event) throws IOException {
        // checks if username is found
        User user = User.findUserViaUsername(this.usernameText.getText());
        if (user == null) {
            this.credValidity.setText("Invalid Username");
            return;
        }
        // checks if password matches the one in database
        if (!user.checkPassword(this.passwordText.getText())) {
            this.credValidity.setText("Incorrect Password");
            return;
        }
        // set user for profile panel and user panel
        UserController.user = user;
        ProfileController.user = user;
        // change scene
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/project/reddit/user-view.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        System.out.println("> redirect to user panel");
    }

    // switch to sign up panel
    @FXML
    void signUp(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/project/reddit/signup-view.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        System.out.println("> redirect to signup page");
    }
}
