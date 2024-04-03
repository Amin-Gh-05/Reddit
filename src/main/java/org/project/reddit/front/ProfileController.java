package org.project.reddit.front;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.project.reddit.content.SubReddit;
import org.project.reddit.user.User;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    public static User user;

    @FXML
    private Label emailText;

    @FXML
    private TextField newEmailText;

    @FXML
    private Label usernameText;

    @FXML
    private TextField newUsernameText;

    @FXML
    private PasswordField oldPasswordText;

    @FXML
    private PasswordField newPasswordText;

    @FXML
    private ListView<String> subredditList;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        emailText.setText(user.getEmail());
        usernameText.setText(user.getUsername());
        for (SubReddit subReddit : user.getSubRedditList()) {
            String label = "";
            label += subReddit.getTopic() + " ";
            if (subReddit.getAdminList().contains(user)) {
                label += "(admin) ";
            }
            label += subReddit.getCreateDateTime();
            subredditList.getItems().add(label);
        }
        System.out.println("> profile page was initialized");
    }

    @FXML
    void updateEmail(ActionEvent event) {
        if (!User.validateEmail(newEmailText.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid email");
            alert.setHeaderText("Email is already used or not valid");
            alert.setContentText("Change email address and try again");
            if (alert.showAndWait().get() == ButtonType.OK) {
                return;
            }
        }
        user.changeEmail(newEmailText.getText());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Update email");
        alert.setHeaderText("Updation successful");
        alert.setContentText("Your email was successfully changed");
        if (alert.showAndWait().get() == ButtonType.OK) {
            emailText.setText(newEmailText.getText());
            newEmailText.clear();
        }
        System.out.println("> email address was updated");
    }

    @FXML
    void updateUsername(ActionEvent event) {
        if (!User.validateUsername(newUsernameText.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid username");
            alert.setHeaderText("Username is already used or not valid");
            alert.setContentText("Change username and try again");
            if (alert.showAndWait().get() == ButtonType.OK) {
                return;
            }
        }
        user.changeUsername(newUsernameText.getText());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Update username");
        alert.setHeaderText("Updation successful");
        alert.setContentText("Your email was successfully changed");
        if (alert.showAndWait().get() == ButtonType.OK) {
            usernameText.setText(newUsernameText.getText());
            newUsernameText.clear();
        }
        System.out.println("> username was updated");
    }

    @FXML
    void updatePassword(ActionEvent event) {
        if (!user.checkPassword(oldPasswordText.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Wrong password");
            alert.setHeaderText("Old password wasn't correct");
            alert.setHeaderText("Re-enter the old password and try again");
            if (alert.showAndWait().get() == ButtonType.OK) {
                return;
            }
        }
        if (!User.validatePassword(newPasswordText.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid password");
            alert.setHeaderText("Password is not valid");
            alert.setContentText("Change password and try again (it should be at least 8 chars including letter and number)");
            if (alert.showAndWait().get() == ButtonType.OK) {
                return;
            }
        }
        user.changePassword(newPasswordText.getText());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Update password");
        alert.setHeaderText("Updation successful");
        alert.setContentText("Your password was successfully changed");
        if (alert.showAndWait().get() == ButtonType.OK) {
            oldPasswordText.clear();
            newPasswordText.clear();
        }
        System.out.println("> password was updated");
    }

    @FXML
    void backToPanel(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/project/reddit/user-view.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        System.out.println("> return to user panel");
    }
}
