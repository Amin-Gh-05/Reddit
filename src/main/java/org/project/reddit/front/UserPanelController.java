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
import org.project.reddit.user.User;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class UserPanelController implements Initializable {
    public static User user;

    @FXML
    private Label subredditCount;

    @FXML
    private Label postCount;

    @FXML
    private Label commentCount;

    @FXML
    private ListView<String> savedPostList;

    @FXML
    private TextField searchText;

    @FXML
    private Tab timeLine;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        subredditCount.setText(String.valueOf(user.getSubRedditList().size()));
        postCount.setText(String.valueOf(user.getPostList().size()));
        commentCount.setText(String.valueOf(user.getCommentList().size()));
        savedPostList.getItems().addAll(refreshSavedPosts());
    }

    @FXML
    void logOut(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Log Out");
        alert.setHeaderText("Returning to main panel");
        alert.setContentText("Are you sure you want to logout?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            user = null;
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/project/reddit/main-view.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            System.out.println("> redirect to main panel");
        }
    }

    @FXML
    void searchAll(ActionEvent event) {

    }

    @FXML
    void viewProfile(ActionEvent event) {

    }

    private String[] refreshSavedPosts() {
        int size = user.getSavedPostList().size();
        String[] savedPosts = new String[size];
        for (int i = 0; i < size; i++) {
            savedPosts[i] = user.getSavedPostList().get(i).getTitle();
        }
        return savedPosts;
    }
}
