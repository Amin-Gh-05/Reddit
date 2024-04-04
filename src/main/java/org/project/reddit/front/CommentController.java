package org.project.reddit.front;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.project.reddit.content.Comment;

public class CommentController {
    public Comment comment;

    @FXML
    public Label usernameText;

    @FXML
    public Label textBody;

    @FXML
    public Label dateTimeText;

    @FXML
    public Label karmaCount;

    @FXML
    void voteDownComment(ActionEvent event) {
        UserPanelController.user.downVote(comment);
        karmaCount.setText(String.valueOf(comment.getKarma()));
    }

    @FXML
    void voteUpComment(ActionEvent event) {
        UserPanelController.user.upVote(comment);
        karmaCount.setText(String.valueOf(comment.getKarma()));
    }
}
