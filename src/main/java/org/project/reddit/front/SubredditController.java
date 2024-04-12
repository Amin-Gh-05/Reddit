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
    void refreshAll() {
        this.memberCount.setText("Members: " + subReddit.getMemberCount());
        if (UserController.user == null) {
            this.subredditPane.getChildren().remove(2, this.subredditPane.getChildren().size());
        } else {
            this.subredditPane.getChildren().remove(3, this.subredditPane.getChildren().size());
        }
        for (Post post : this.subReddit.getPostList()) {
            this.subredditPane.getChildren().add(getPostLayout(post));
        }
        refreshMembers();
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
        refreshAll();
    }

    @FXML
    void joinSubreddit() {
        UserController.user.joinSubReddit(this.subReddit);
        this.joinButton.setVisible(false);
        this.leaveButton.setVisible(true);
        refreshAll();
    }

    @FXML
    void leaveSubreddit() {
        UserController.user.leaveSubReddit(this.subReddit);
        this.joinButton.setVisible(true);
        this.leaveButton.setVisible(false);
        refreshAll();
    }

    void refreshMembers() {
        this.memberList.getItems().clear();
        for (User user : this.subReddit.getMemberList()) {
            String userText = user.getUsername();
            if (subReddit.getAdminList().contains(user)) {
                userText += " (admin)";
            }
            this.memberList.getItems().add(userText);
        }
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Remove");
        MenuItem adminItem = new MenuItem("Set Admin");
        contextMenu.getItems().add(deleteItem);
        contextMenu.getItems().add(adminItem);
        memberList.setContextMenu(contextMenu);
        memberList.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            String username = memberList.getSelectionModel().getSelectedItem();
            deleteItem.setOnAction(e -> deleteItem(username));
            adminItem.setOnAction(e -> setAdmin(username));
        });
    }

    private void setAdmin(String username) {
        if (username.endsWith(" (admin)")) {
            System.out.println("> user is already an admin");
            return;
        }
        User user = User.findUserViaUsername(username);
        UserController.user.addAdmin(user, this.subReddit);
        refreshAll();
    }

    private void deleteItem(String item) {
        if (item.endsWith(" (admin)")) {
            item = item.replaceAll(" \\(admin\\)", "");
        }
        User user = User.findUserViaUsername(item);
        UserController.user.removeMember(user, this.subReddit);
        refreshAll();
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
            controller.refreshComments();
            return node;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
