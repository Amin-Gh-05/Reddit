package org.project.reddit.front;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.project.reddit.content.Post;

public class PostController {
    public static Post post;

    @FXML
    public Label usernameText;

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
    void commentPost(ActionEvent event) {

    }

    @FXML
    void savePost(ActionEvent event) {

    }
}
