package org.project.reddit.front;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.project.reddit.content.Post;
import org.project.reddit.content.SubReddit;
import org.project.reddit.user.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubredditController {
    public SubReddit subReddit;

    @FXML
    public VBox subredditPane;

    @FXML
    public Label topicText;

    @FXML
    public Label memberCount;

    @FXML
    public Label dateText;

    @FXML
    public ListView<String> memberList;

    @FXML
    public AnchorPane createPostPane;

    @FXML
    public Button joinButton;

    @FXML
    public Button leaveButton;

    @FXML
    public TextField postTitle;

    @FXML
    public TextField tagsText;

    @FXML
    public TextArea postTextBody;

    @FXML
    public Label invalidAlert;

    @FXML
    void refreshSubreddit() {
        this.subredditPane.getChildren().remove(3, this.subredditPane.getChildren().size());
        for (Post post : this.subReddit.getPostList()) {
            this.subredditPane.getChildren().add(getPostLayout(post));
        }
        this.memberList.getItems().clear();
        for (User user : this.subReddit.getMemberList()) {
            String userText = user.getUsername();
            if (subReddit.getAdminList().contains(user)) {
                userText += " (admin)";
            }
            this.memberList.getItems().add(userText);
        }
    }

    @FXML
    void createPost() {
        this.invalidAlert.setText(null);
        if (this.postTitle.getText() == null || this.postTextBody.getText() == null) {
            this.invalidAlert.setText("Please fill all essential fields");
            return;
        }
        if (this.tagsText.getText() == null) {
            UserController.user.createPost(this.postTitle.getText(), this.postTextBody.getText(), this.subReddit);
        } else {
            List<String> tagList = new ArrayList<>(Arrays.asList(tagsText.getText().split(" ")));
            UserController.user.createPost(tagList, this.postTitle.getText(), this.postTextBody.getText(), this.subReddit);
            this.tagsText.clear();
        }
        this.postTitle.clear();
        this.postTextBody.clear();
        refreshSubreddit();
    }

    @FXML
    void joinSubreddit() {
        UserController.user.joinSubReddit(this.subReddit);
        this.joinButton.setVisible(false);
        this.leaveButton.setVisible(true);
        refreshSubreddit();
    }

    @FXML
    void leaveSubreddit() {
        UserController.user.leaveSubReddit(this.subReddit);
        this.joinButton.setVisible(true);
        this.leaveButton.setVisible(false);
        refreshSubreddit();
    }

    Node getPostLayout(Post post) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/project/reddit/post-view.fxml"));
        try {
            Node node = loader.load();
            PostController controller = loader.getController();
            controller.post = post;
            controller.usernameText.setText(post.getUser().getUsername());
            controller.karmaCount.setText("Karma: " + post.getKarma());
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
            return node;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
