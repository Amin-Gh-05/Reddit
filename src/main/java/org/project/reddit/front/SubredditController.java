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
import java.util.*;

public class SubredditController {
    public SubReddit subReddit;
    // controllers
    public UserController userController;
    public MainController mainController;
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

    // refresh all details button
    @FXML
    void refreshAll() {
        // refresh member count of subreddit
        this.memberCount.setText("Members: " + subReddit.getMemberCount());
        // refresh posts
        if (UserController.user == null) {
            this.subredditPane.getChildren().remove(2, this.subredditPane.getChildren().size());
        } else {
            this.subredditPane.getChildren().remove(3, this.subredditPane.getChildren().size());
        }
        for (Post post : this.subReddit.getPostList()) {
            this.subredditPane.getChildren().add(getPostLayout(post));
        }
        // refresh members
        refreshMembers();
        // refresh panels
        if (userController != null) {
            userController.refreshAll();
        }
        if (mainController != null) {
            mainController.refreshAll();
        }
    }

    // create new post button
    @FXML
    void createPost() {
        this.invalidAlert.setText(null);
        // checks if all needed info is provided by user
        if (this.postTitle.getText() == null || this.postTextBody.getText() == null) {
            this.invalidAlert.setText("Please fill all essential fields");
            return;
        }
        // create new post based on having or not having tags
        if (this.tagsText.getText() == null) {
            UserController.user.createPost(this.postTitle.getText(), this.postTextBody.getText(), this.subReddit);
        } else {
            List<String> tagList = new ArrayList<>(Arrays.asList(tagsText.getText().split(" ")));
            UserController.user.createPost(tagList, this.postTitle.getText(), this.postTextBody.getText(), this.subReddit);
            this.tagsText.clear();
        }
        // clear fields
        this.postTitle.clear();
        this.postTextBody.clear();
        refreshAll();
    }

    // join subreddit button
    @FXML
    void joinSubreddit() {
        UserController.user.joinSubReddit(this.subReddit);
        this.joinButton.setVisible(false);
        this.leaveButton.setVisible(true);
        refreshAll();
    }

    // leave subreddit button
    @FXML
    void leaveSubreddit() {
        UserController.user.leaveSubReddit(this.subReddit);
        this.joinButton.setVisible(true);
        this.leaveButton.setVisible(false);
        refreshAll();
    }

    // refresh members list
    void refreshMembers() {
        // clears list
        this.memberList.getItems().clear();
        // add all members to list
        for (UUID id : this.subReddit.getMemberList()) {
            User user = User.findUserViaId(id);
            assert user != null;
            String userText = user.getUsername();
            if (subReddit.getAdminList().contains(user.getId())) {
                userText += " (admin)";
            }
            this.memberList.getItems().add(userText);
        }
        // set right click menu for every member selected
        ContextMenu contextMenu = new ContextMenu();
        MenuItem viewItem = new MenuItem("View");
        MenuItem deleteItem = new MenuItem("Remove");
        MenuItem adminItem = new MenuItem("Set Admin");
        contextMenu.getItems().add(viewItem);
        contextMenu.getItems().add(deleteItem);
        contextMenu.getItems().add(adminItem);
        memberList.setContextMenu(contextMenu);
        // action of every menu item
        memberList.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            String username = memberList.getSelectionModel().getSelectedItem();
            viewItem.setOnAction(e -> viewItem(username));
            deleteItem.setOnAction(e -> deleteItem(username));
            adminItem.setOnAction(e -> setAdmin(username));
        });
    }

    // view member selected
    private void viewItem(String username) {
        // alter username
        if (username.endsWith(" (admin)")) {
            username = username.replaceAll(" \\(admin\\)", "");
        }
        User user = User.findUserViaUsername(username);
        // load user show panel
        if (user != null) {
            if (UserController.user != null) {
                this.userController.loadUser(user);
            } else {
                this.mainController.loadUser(user);
            }
        }
    }

    // remove member selected
    private void deleteItem(String item) {
        // alter username
        if (item.endsWith(" (admin)")) {
            item = item.replaceAll(" \\(admin\\)", "");
        }
        User user = User.findUserViaUsername(item);
        assert user != null;
        UserController.user.removeMember(user, this.subReddit);
        refreshAll();
    }

    // make the member selected admin
    private void setAdmin(String username) {
        // checks if user is admin
        if (username.endsWith(" (admin)")) {
            System.out.println("> user is already an admin");
            return;
        }
        User user = User.findUserViaUsername(username);
        assert user != null;
        UserController.user.addAdmin(user, this.subReddit);
        refreshAll();
    }

    Node getPostLayout(Post post) {
        // load post layout from fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/project/reddit/post-view.fxml"));
        try {
            Node node = loader.load();
            PostController controller = loader.getController();
            controller.post = post;
            // only-if opened from subreddit panel
            controller.subredditController = this;
            // set details of post
            controller.usernameText.setText(Objects.requireNonNull(User.findUserViaId(post.getUser())).getUsername());
            controller.karmaCount.setText("Karma: " + post.getKarma());
            controller.dateTimeText.setText(post.getCreateDateTime());
            controller.topicText.setText(post.getTitle());
            controller.textBody.setText(post.getText());
            // tags
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
            // disable some actions as needed
            if (UserController.user == null) {
                controller.likeButton.setVisible(false);
                controller.dislikeButton.setVisible(false);
                controller.saveButton.setVisible(false);
                controller.deleteButton.setVisible(false);
                controller.editButton.setVisible(false);
            } else {
                if (!post.getUser().equals(UserController.user.getId())) {
                    controller.editButton.setVisible(false);
                }
                if (!post.getUser().equals(UserController.user.getId()) && !post.getSubReddit().getAdminList().contains(UserController.user.getId())) {
                    controller.deleteButton.setVisible(false);
                }
            }
            return node;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
