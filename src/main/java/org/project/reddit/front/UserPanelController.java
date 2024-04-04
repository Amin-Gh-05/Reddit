package org.project.reddit.front;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.project.reddit.content.Post;
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
    public ListView<String> savedPostList;

    @FXML
    private TextField searchText;

    @FXML
    private VBox postBox;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        subredditCount.setText(String.valueOf(user.getSubRedditList().size()));
        postCount.setText(String.valueOf(user.getPostList().size()));
        commentCount.setText(String.valueOf(user.getCommentList().size()));
        refreshSavedPosts();
        refreshTimeline();
    }

    @FXML
    void logOut(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Log Out");
        alert.setHeaderText("Returning to main panel");
        alert.setContentText("Are you sure you want to logout?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            user = null;
            ProfileController.user = null;
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
    void viewProfile(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/project/reddit/profile-view.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        System.out.println("> redirect to profile panel");
    }

    public void refreshSavedPosts() {
        int size = user.getSavedPostList().size();
        String[] savedPosts = new String[size];
        for (int i = 0; i < size; i++) {
            savedPosts[i] = user.getSavedPostList().get(i).getTitle();
        }
        savedPostList.getItems().addAll(savedPosts);
    }

    public void refreshTimeline() {
        int size = user.getTimelinePostList().size();
        Post[] timelinePosts = new Post[size];
        if (size >= 10) {
            for (int i = size - 1; i >= size - 10; i--) {
                timelinePosts[i] = user.getTimelinePostList().get(i);
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                timelinePosts[i] = user.getTimelinePostList().get(i);
            }
        }
        for (Post post : timelinePosts) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/project/reddit/post-view.fxml"));
            try {
                Node node = loader.load();
                PostController controller = loader.getController();
                controller.post = post;
                controller.usernameText.setText(post.getUser().getUsername());
                controller.karmaCount.setText(String.valueOf(post.getKarma()));
                controller.dateTimeText.setText(post.getCreateDateTime());
                controller.topicText.setText(post.getTitle());
                controller.textBody.setText(post.getText());
                if (!post.getTagList().isEmpty()) {
                    String tags = "";
                    for (String tag : post.getTagList()) {
                        tags += "#" + tag;
                    }
                    controller.tagsText.setText(tags);
                } else {
                    controller.tagsText.setVisible(false);
                }
                postBox.getChildren().add(node);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
