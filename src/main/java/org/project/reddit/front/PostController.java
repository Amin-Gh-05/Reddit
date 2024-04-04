package org.project.reddit.front;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import org.project.reddit.content.Comment;
import org.project.reddit.content.Post;

import java.io.IOException;

public class PostController {
    public Post post;

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
    private VBox commentBox;

    @FXML
    private TextArea newCommentText;

    @FXML
    void savePost(ActionEvent event) {
        UserPanelController.user.savePost(this.post);
    }

    @FXML
    void upVotePost(ActionEvent event) {
        UserPanelController.user.upVote(this.post);
        karmaCount.setText(String.valueOf(this.post.getKarma()));
    }

    @FXML
    void downVotePost(ActionEvent event) {
        UserPanelController.user.downVote(this.post);
        karmaCount.setText(String.valueOf(this.post.getKarma()));
    }
    @FXML
    void sendComment(ActionEvent event) {
        UserPanelController.user.createComment(newCommentText.getText(), this.post);
    }

    @FXML
    void showComments(ActionEvent event) throws IOException {
        commentBox.getChildren().remove(1, commentBox.getChildren().size());
        refreshComments();
    }

    public void refreshComments() throws IOException {
        int size = post.getCommentList().size();
        Comment[] postComments = new Comment[size];
        for (int i = size - 1; i >= 0; i--) {
            postComments[i] = post.getCommentList().get(i);
        }
        for (Comment comment : postComments) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/project/reddit/comment-view.fxml"));
            Node node = loader.load();
            CommentController controller = loader.getController();
            controller.comment = comment;
            controller.usernameText.setText(comment.getUser().getUsername());
            controller.textBody.setText(comment.getText());
            controller.dateTimeText.setText(comment.getCreateDateTime());
            controller.karmaCount.setText(String.valueOf(comment.getKarma()));
            commentBox.getChildren().add(node);
        }
    }
}
