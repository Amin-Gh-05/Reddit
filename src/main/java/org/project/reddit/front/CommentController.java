package org.project.reddit.front;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import org.project.reddit.content.Comment;

public class CommentController {
    // comment of this controller
    public Comment comment;
    // post of this comment
    public PostController postController;
    @FXML
    public AnchorPane commentPane;
    @FXML
    public Label usernameText;
    @FXML
    public Label textBody;
    @FXML
    public Label dateTimeText;
    @FXML
    public Label karmaCount;
    @FXML
    public Button editButton;
    @FXML
    public Button deleteButton;
    @FXML
    public Button likeButton;
    @FXML
    public Button dislikeButton;
    // times the edit button is clicked (to prevent several edits at the same time)
    int editClick = 0;

    // voteup/votedown comment buttons
    @FXML
    void voteDownComment() {
        UserController.user.downVote(this.comment);
        this.karmaCount.setText("Karma: " + this.comment.getKarma());
    }

    @FXML
    void voteUpComment() {
        UserController.user.upVote(this.comment);
        this.karmaCount.setText("Karma: " + this.comment.getKarma());
    }

    // delete comment button
    @FXML
    void deleteComment() {
        UserController.user.removeComment(this.comment, this.comment.getPost());
        postController.refreshComments();
    }

    // edit text of comment button
    @FXML
    void editComment() {
        // checks if edit button is already pressed and process is going on
        if (editClick > 0) {
            System.out.println("> still in edit progress");
            return;
        }
        editClick++;
        String oldText = this.textBody.getText();
        // add a new text field for editation
        TextField newText = new TextField(oldText);
        this.commentPane.getChildren().remove(this.textBody);
        this.commentPane.getChildren().add(newText);
        newText.setLayoutX(14);
        newText.setLayoutY(37);
        // if enter is pressed, editation is confirmed
        newText.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                UserController.user.changeCommentText(this.comment, newText.getText());
                this.textBody.setText(newText.getText());
                this.commentPane.getChildren().remove(newText);
                this.commentPane.getChildren().add(this.textBody);
                editClick--;
            }
        });
    }
}
