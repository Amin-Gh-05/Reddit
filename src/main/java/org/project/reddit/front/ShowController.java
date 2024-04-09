package org.project.reddit.front;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import org.project.reddit.content.Post;
import org.project.reddit.content.SubReddit;
import org.project.reddit.user.User;

import java.io.IOException;

public class ShowController {
    public User user;

    @FXML
    public VBox userPane;

    @FXML
    public Label usernameText;

    @FXML
    public Label karmaCount;

    @FXML
    public ListView<String> subredditList;

    @FXML
    void refreshUser() {
        this.karmaCount.setText("Karma: " + this.user.getKarma());
        this.userPane.getChildren().remove(2, this.userPane.getChildren().size());
        for (Post post : this.user.getPostList()) {
            this.userPane.getChildren().add(getPostLayout(post));
        }
        this.subredditList.getItems().clear();
        for (SubReddit subReddit : this.user.getSubRedditList()) {
            this.subredditList.getItems().add(subReddit.getTopic());
        }
    }

    Node getPostLayout(Post post) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/project/reddit/post-view.fxml"));
        try {
            Node node = loader.load();
            PostController controller = loader.getController();
            controller.post = post;
            controller.showController = this;
            controller.usernameText.setText(post.getUser().getUsername());
            controller.karmaCount.setText("Karma: " + post.getKarma());
            controller.dateTimeText.setText(post.getCreateDateTime());
            controller.topicText.setText(post.getTitle());
            controller.textBody.setText(post.getText());
            if (UserController.user == null) {
                controller.likeButton.setVisible(false);
                controller.dislikeButton.setVisible(false);
                controller.deleteButton.setVisible(false);
                controller.editButton.setVisible(false);
                controller.saveButton.setVisible(false);
            }
            if (!post.getTagList().isEmpty()) {
                String tags = "";
                for (String tag : post.getTagList()) {
                    tags += "#" + tag;
                }
                controller.tagsText.setText(tags);
            } else {
                controller.tagsText.setVisible(false);
            }
            return node;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
