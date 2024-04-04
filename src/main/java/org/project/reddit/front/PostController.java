package org.project.reddit.front;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.project.reddit.content.Comment;
import org.project.reddit.content.Post;

import java.io.IOException;

public class PostController {
    public Post post;

    @FXML
    public AnchorPane postPane;

    @FXML
    public Label usernameText;

    @FXML
    public Label karmaCount;

    @FXML
    public Label dateTimeText;

    @FXML
    public Label topicText;

    @FXML
    public Label textBody;

    @FXML
    public Label tagsText;
    @FXML
    public Button deleteButton;
    @FXML
    public Button editButton;
    @FXML
    public VBox commentBox;
    @FXML
    public TextArea newCommentText;

    @FXML
    void savePost(ActionEvent event) {
        UserPanelController.user.savePost(this.post);
    }

    @FXML
    void upVotePost(ActionEvent event) {
        UserPanelController.user.upVote(this.post);
        karmaCount.setText("Karma: " + this.post.getKarma());
    }

    @FXML
    void downVotePost(ActionEvent event) {
        UserPanelController.user.downVote(this.post);
        karmaCount.setText("Karma: " + this.post.getKarma());
    }

    @FXML
    void sendComment(ActionEvent event) {
        UserPanelController.user.createComment(newCommentText.getText(), this.post);
        newCommentText.clear();
    }

    @FXML
    void showComments(ActionEvent event) {
        commentBox.getChildren().remove(1, commentBox.getChildren().size());
        refreshComments();
    }

    @FXML
    void deletePost(ActionEvent event) {
        UserPanelController.user.removePost(this.post, this.post.getSubReddit());
    }

    @FXML
    void editPost(ActionEvent event) {
        String oldText = textBody.getText();
        TextArea newText = new TextArea(oldText);
        postPane.getChildren().remove(textBody);
        postPane.getChildren().add(newText);
        newText.setLayoutX(22);
        newText.setLayoutY(65);
        newText.setPrefWidth(516);
        newText.setPrefHeight(70);
        newText.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                UserPanelController.user.changePostText(this.post, newText.getText());
                textBody.setText(newText.getText());
                postPane.getChildren().remove(newText);
                postPane.getChildren().add(textBody);
            }
        });
    }

    void refreshComments() {
        int size = post.getCommentList().size();
        Comment[] postComments = new Comment[size];
        for (int i = size - 1; i >= 0; i--) {
            postComments[i] = post.getCommentList().get(i);
        }
        for (Comment comment : postComments) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/project/reddit/comment-view.fxml"));
            try {
                Node node = loader.load();
                CommentController controller = loader.getController();
                controller.comment = comment;
                controller.usernameText.setText(comment.getUser().getUsername());
                controller.textBody.setText(comment.getText());
                controller.dateTimeText.setText(comment.getCreateDateTime());
                controller.karmaCount.setText("Karma: " + comment.getKarma());
                if (!comment.getUser().equals(UserPanelController.user)) {
                    controller.editButton.setVisible(false);
                }
                if (!comment.getUser().equals(UserPanelController.user) && !comment.getPost().getSubReddit().getAdminList().contains(UserPanelController.user)) {
                    controller.deleteButton.setVisible(false);
                }
                commentBox.getChildren().add(node);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
