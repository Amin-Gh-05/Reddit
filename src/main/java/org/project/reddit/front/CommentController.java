package org.project.reddit.front;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import org.project.reddit.content.Comment;

public class CommentController {
    public Comment comment;

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
    void voteDownComment() {
        UserController.user.downVote(this.comment);
        this.karmaCount.setText("Karma: " + this.comment.getKarma());
    }

    @FXML
    void voteUpComment() {
        UserController.user.upVote(this.comment);
        this.karmaCount.setText("Karma: " + this.comment.getKarma());
    }

    @FXML
    void deleteComment() {
        UserController.user.removeComment(this.comment, this.comment.getPost());
    }

    @FXML
    void editComment() {
        String oldText = this.textBody.getText();
        TextField newText = new TextField(oldText);
        this.commentPane.getChildren().remove(this.textBody);
        this.commentPane.getChildren().add(newText);
        newText.setLayoutX(14);
        newText.setLayoutY(37);
        newText.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                UserController.user.changeCommentText(this.comment, newText.getText());
                this.textBody.setText(newText.getText());
                this.commentPane.getChildren().remove(newText);
                this.commentPane.getChildren().add(this.textBody);
            }
        });
    }
}
