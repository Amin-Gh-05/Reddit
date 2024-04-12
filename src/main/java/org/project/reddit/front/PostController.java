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

    public UserController userController;

    public ShowController showController;

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
    public Button saveButton;

    @FXML
    public Button likeButton;

    @FXML
    public Button dislikeButton;

    @FXML
    public VBox commentBox;

    @FXML
    public TextArea newCommentText;

    int editClick = 0;

    @FXML
    void savePost(ActionEvent event) {
        if (!UserController.user.getSavedPostList().contains(this.post)) {
            UserController.user.savePost(this.post);
        } else {
            UserController.user.unsavePost(this.post);
        }
        if (userController != null) {
            userController.refreshAll();
        }
        if (showController != null) {
            showController.refreshUser();
        }

    }

    @FXML
    void upVotePost() {
        UserController.user.upVote(this.post);
        karmaCount.setText("Karma: " + this.post.getKarma());
        if (showController != null) {
            showController.karmaCount.setText("Karma: " + showController.user.getKarma());
        }
    }

    @FXML
    void downVotePost() {
        UserController.user.downVote(this.post);
        karmaCount.setText("Karma: " + this.post.getKarma());
        if (showController != null) {
            showController.karmaCount.setText("Karma: " + showController.user.getKarma());
        }
    }

    @FXML
    void sendComment() {
        UserController.user.createComment(this.newCommentText.getText(), this.post);
        this.newCommentText.clear();
        this.refreshComments();
    }

    @FXML
    void deletePost() {
        UserController.user.removePost(this.post, this.post.getSubReddit());
        if (userController != null) {
            userController.refreshAll();
        }
        if (showController != null) {
            showController.refreshUser();
        }
    }

    @FXML
    void editPost() {
        if (editClick > 0) {
            System.out.println("> still in edit progress");
            return;
        }
        editClick++;
        String oldText = this.textBody.getText();
        TextArea newText = new TextArea(oldText);
        this.postPane.getChildren().remove(this.textBody);
        this.postPane.getChildren().add(newText);
        newText.setLayoutX(22);
        newText.setLayoutY(65);
        newText.setPrefWidth(516);
        newText.setPrefHeight(20);
        newText.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                UserController.user.changePostText(this.post, newText.getText());
                this.textBody.setText(newText.getText());
                this.postPane.getChildren().remove(newText);
                this.postPane.getChildren().add(this.textBody);
                editClick--;
            }
        });
    }

    @FXML
    public void refreshComments() {
        this.commentBox.getChildren().remove(1, this.commentBox.getChildren().size());
        int size = this.post.getCommentList().size();
        Comment[] postComments = new Comment[size];
        for (int i = size - 1; i >= 0; i--) {
            postComments[i] = this.post.getCommentList().get(i);
        }
        for (Comment comment : postComments) {
            this.commentBox.getChildren().add(getCommentLayout(comment));
        }
    }


    Node getCommentLayout(Comment comment) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/project/reddit/comment-view.fxml"));
        try {
            Node node = loader.load();
            CommentController controller = loader.getController();
            controller.comment = comment;
            controller.usernameText.setText(comment.getUser().getUsername());
            controller.controller = this;
            controller.textBody.setText(comment.getText());
            controller.dateTimeText.setText(comment.getCreateDateTime());
            controller.karmaCount.setText("Karma: " + comment.getKarma());
            if (!comment.getUser().equals(UserController.user)) {
                controller.editButton.setVisible(false);
            }
            if (!comment.getUser().equals(UserController.user) && !comment.getPost().getSubReddit().getAdminList().contains(UserController.user)) {
                controller.deleteButton.setVisible(false);
            }
            return node;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
