package org.project.reddit.front;

import javafx.event.ActionEvent;
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
    void voteDownComment(ActionEvent event) {
        UserPanelController.user.downVote(comment);
        karmaCount.setText("Karma: " + comment.getKarma());
    }

    @FXML
    void voteUpComment(ActionEvent event) {
        UserPanelController.user.upVote(comment);
        karmaCount.setText("Karma: " + comment.getKarma());
    }

    @FXML
    void deleteComment(ActionEvent event) {
        UserPanelController.user.removeComment(comment, comment.getPost());
    }

    @FXML
    void editComment(ActionEvent event) {
        String oldText = textBody.getText();
        TextField newText = new TextField(oldText);
        commentPane.getChildren().remove(textBody);
        commentPane.getChildren().add(newText);
        newText.setLayoutX(14);
        newText.setLayoutY(37);
        newText.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                UserPanelController.user.changeCommentText(comment, newText.getText());
                textBody.setText(newText.getText());
                commentPane.getChildren().remove(newText);
                commentPane.getChildren().add(textBody);
            }
        });
    }
}
