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
    @FXML
    private Label karmaCount;

    // initialize the profile panel
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // set the user details
        this.emailText.setText(user.getEmail());
        this.usernameText.setText(user.getUsername());
        this.karmaCount.setText("Karma: " + user.getKarma());
        // show all subreddits joined
        for (SubReddit subReddit : user.getSubRedditList()) {
            String label = "";
            label += subReddit.getTopic() + " ";
            // add admin label as needed
            if (subReddit.getAdminList().contains(user)) {
                label += "(admin)  ";
            }
            label += subReddit.getCreateDateTime();
            this.subredditList.getItems().add(label);
        }
        System.out.println("> profile page was initialized");
    }

    // change email address button
    @FXML
    void updateEmail() {
        // check the new email validity and show alerts as needed
        if (!User.validateEmail(this.newEmailText.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid email");
            alert.setHeaderText("Email is already used or not valid");
            alert.setContentText("Change email address and try again");
            if (alert.showAndWait().get() == ButtonType.OK) {
                return;
            }
        }
        user.changeEmail(this.newEmailText.getText());
        // show alert for succession of progress
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Update email");
        alert.setHeaderText("Updation successful");
        alert.setContentText("Your email was successfully changed");
        if (alert.showAndWait().get() == ButtonType.OK) {
            this.emailText.setText(this.newEmailText.getText());
            this.newEmailText.clear();
        }
        System.out.println("> email address was updated");
    }

    // change username button
    @FXML
    void updateUsername() {
        // check the new username validity and show alert as needed
        if (!User.validateUsername(this.newUsernameText.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid username");
            alert.setHeaderText("Username is already used or not valid");
            alert.setContentText("Change username and try again");
            if (alert.showAndWait().get() == ButtonType.OK) {
                return;
            }
        }
        user.changeUsername(this.newUsernameText.getText());
        // show alert for succession of progress
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Update username");
        alert.setHeaderText("Updation successful");
        alert.setContentText("Your email was successfully changed");
        if (alert.showAndWait().get() == ButtonType.OK) {
            this.usernameText.setText(this.newUsernameText.getText());
            this.newUsernameText.clear();
        }
        System.out.println("> username was updated");
    }

    @FXML
    void updatePassword() {
        // check the old password validity and show alert as needed
        if (!user.checkPassword(this.oldPasswordText.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Wrong password");
            alert.setHeaderText("Old password wasn't correct");
            alert.setHeaderText("Re-enter the old password and try again");
            if (alert.showAndWait().get() == ButtonType.OK) {
                return;
            }
        }
        // check the new password validity and show alert as needed
        if (!User.validatePassword(this.newPasswordText.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid password");
            alert.setHeaderText("Password is not valid");
            alert.setContentText("Change password and try again (it should be at least 8 chars including letter and number)");
            if (alert.showAndWait().get() == ButtonType.OK) {
                return;
            }
        }
        user.changePassword(this.newPasswordText.getText());
        // show alert for succession of progress
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Update password");
        alert.setHeaderText("Updation successful");
        alert.setContentText("Your password was successfully changed");
        if (alert.showAndWait().get() == ButtonType.OK) {
            this.oldPasswordText.clear();
            this.newPasswordText.clear();
        }
        System.out.println("> password was updated");
    }

    // return to main panel button
    @FXML
    void backToPanel(ActionEvent event) throws IOException {
        // change scene
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/project/reddit/user-view.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        System.out.println("> return to user panel");
    }
}
