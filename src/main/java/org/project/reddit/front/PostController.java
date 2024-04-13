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
import org.project.reddit.user.User;

import java.io.IOException;
import java.util.Objects;

public class PostController {
    public Post post;
    // controller (depends on the panel which post is shown)
    public UserController userController;
    public ShowController showController;
    public SubredditController subredditController;
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
    // times the edit button is clicked (to prevent several edits at the same time)
    int editClick = 0;

    // save or unsave post if possible
    @FXML
    void savePost(ActionEvent event) {
        // save post if it's not saved
        if (!UserController.user.getSavedPostList().contains(this.post)) {
            UserController.user.savePost(this.post);
        }
        // unsave post if it's already saved
        else {
            UserController.user.unsavePost(this.post);
        }
        // refresh panels
        if (userController != null) {
            userController.refreshAll();
        }
        if (showController != null) {
            showController.refreshAll();
        }
        if (subredditController != null) {
            subredditController.refreshAll();
        }
    }

    // upvote post button
    @FXML
    void upVotePost() {
        UserController.user.upVote(this.post);
        karmaCount.setText("Karma: " + this.post.getKarma());
        // refresh karma if in the user show panel
        if (showController != null) {
            showController.karmaCount.setText("Karma: " + showController.user.getKarma());
        }
    }

    // downvote post button
    @FXML
    void downVotePost() {
        UserController.user.downVote(this.post);
        karmaCount.setText("Karma: " + this.post.getKarma());
        // refresh karma if in the user show panel
        if (showController != null) {
            showController.karmaCount.setText("Karma: " + showController.user.getKarma());
        }
    }

    // create a new comment for the post
    @FXML
    void sendComment() {
        if (UserController.user == null) {
            System.out.println("> access not granted");
            return;
        }
        UserController.user.createComment(this.newCommentText.getText(), this.post);
        // refresh comments panel
        this.newCommentText.clear();
        this.refreshComments();
    }

    // delete a post if possible
    @FXML
    void deletePost() {
        UserController.user.removePost(this.post, this.post.getSubReddit());
        // refresh panels
        if (userController != null) {
            userController.refreshAll();
        }
        if (showController != null) {
            showController.refreshAll();
        }
        if (subredditController != null) {
            subredditController.refreshAll();
        }
    }

    // edit post text if possible
    @FXML
    void editPost() {
        // checks if edit button is already pressed
        if (editClick > 0) {
            System.out.println("> still in edit progress");
            return;
        }
        editClick++;
        String oldText = this.textBody.getText();
        // create a text field to edit text
        TextArea newText = new TextArea(oldText);
        this.postPane.getChildren().remove(this.textBody);
        this.postPane.getChildren().add(newText);
        newText.setLayoutX(22);
        newText.setLayoutY(65);
        newText.setPrefWidth(516);
        newText.setPrefHeight(20);
        // confirm editation progress if enter is pressed
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

    // refresh comments panel
    @FXML
    public void refreshComments() {
        // clear all comments
        this.commentBox.getChildren().remove(1, this.commentBox.getChildren().size());
        int size = this.post.getCommentList().size();
        Comment[] postComments = new Comment[size];
        for (int i = size - 1; i >= 0; i--) {
            postComments[i] = this.post.getCommentList().get(i);
        }
        // add all comments to screen
        for (Comment comment : postComments) {
            this.commentBox.getChildren().add(getCommentLayout(comment));
        }
    }

    // load comment layout and return a node
    Node getCommentLayout(Comment comment) {
        // load fxml file of comment layout
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/project/reddit/comment-view.fxml"));
        try {
            Node node = loader.load();
            CommentController controller = loader.getController();
            // set comment details
            controller.comment = comment;
            controller.usernameText.setText(Objects.requireNonNull(User.findUserViaId(comment.getUser())).getUsername());
            controller.postController = this;
            controller.textBody.setText(comment.getText());
            controller.dateTimeText.setText(comment.getCreateDateTime());
            controller.karmaCount.setText("Karma: " + comment.getKarma());
            // disable some actions as needed
            if (UserController.user != null) {
                if (!comment.getUser().equals(UserController.user.getId())) {
                    controller.editButton.setVisible(false);
                }
                if (!comment.getUser().equals(UserController.user.getId()) && !comment.getPost().getSubReddit().getAdminList().contains(UserController.user.getId())) {
                    controller.deleteButton.setVisible(false);
                }
            } else {
                controller.editButton.setVisible(false);
                controller.deleteButton.setVisible(false);
                controller.likeButton.setVisible(false);
                controller.dislikeButton.setVisible(false);
            }
            return node;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
