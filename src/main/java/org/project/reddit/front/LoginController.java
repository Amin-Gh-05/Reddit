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

    @FXML
    void cancelLogIn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/project/reddit/main-view.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        System.out.println("> returned to main panel");
    }

    @FXML
    void logIn(ActionEvent event) throws IOException {
        User user = User.findUserViaUsername(usernameText.getText());
        if (user == null) {
            credValidity.setText("Invalid Username");
            return;
        }
        if (!user.checkPassword(passwordText.getText())) {
            credValidity.setText("Incorrect Password");
            return;
        }
        UserPanelController.user = user;
        ProfileController.user = user;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/project/reddit/user-view.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        System.out.println("> redirect to user panel");
    }

    @FXML
    void signUp(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/project/reddit/signup-view.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        System.out.println("> redirect to signup page");
    }
}
