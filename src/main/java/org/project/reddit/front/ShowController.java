package org.project.reddit.front;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import org.project.reddit.content.Post;
import org.project.reddit.content.SubReddit;
import org.project.reddit.user.User;

import java.io.IOException;
import java.util.Objects;

public class ShowController {
    public User user;
    // controllers
    public UserController userController;
    public MainController mainController;
    @FXML
    public VBox userPane;
    @FXML
    public Label usernameText;
    @FXML
    public Label karmaCount;
    @FXML
    public ListView<String> subredditList;

    // refresh all details in user show tab
    @FXML
    void refreshAll() {
        // refresh karma
        this.karmaCount.setText("Karma: " + this.user.getKarma());
        // refresh posts of user
        this.userPane.getChildren().remove(2, this.userPane.getChildren().size());
        for (Post post : this.user.getPostList()) {
            this.userPane.getChildren().add(getPostLayout(post));
        }
        refreshSubreddits();
        // refresh panels
        if (userController != null) {
            userController.refreshAll();
        }
        if (mainController != null) {
            mainController.refreshAll();
        }
    }

    // refresh subreddits of user
    void refreshSubreddits() {
        // clears list
        this.subredditList.getItems().clear();
        // adds all subreddits to list
        for (SubReddit subReddit : this.user.getSubRedditList()) {
            this.subredditList.getItems().add(subReddit.getTopic());
        }
        // set right click menu for every subreddit selected
        ContextMenu contextMenu = new ContextMenu();
        MenuItem viewItem = new MenuItem("View");
        contextMenu.getItems().add(viewItem);
        subredditList.setContextMenu(contextMenu);
        // action of every menu item
        subredditList.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            String topic = subredditList.getSelectionModel().getSelectedItem();
            viewItem.setOnAction(e -> viewItem(topic));
        });
    }

    private void viewItem(String topic) {
        SubReddit subReddit = SubReddit.findSubReddit(topic);
        // load subreddit panel
        if (subReddit != null) {
            if (UserController.user != null) {
                this.userController.loadSubreddit(subReddit);
            } else {
                this.mainController.loadSubreddit(subReddit);
            }
        }
    }

    // load post layout
    Node getPostLayout(Post post) {
        // load post layout from fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/project/reddit/post-view.fxml"));
        try {
            Node node = loader.load();
            PostController controller = loader.getController();
            controller.post = post;
            // only if in user show panel
            controller.showController = this;
            // set details of post
            controller.usernameText.setText(Objects.requireNonNull(User.findUserViaId(post.getUser())).getUsername());
            controller.karmaCount.setText("Karma: " + post.getKarma());
            controller.dateTimeText.setText(post.getCreateDateTime());
            controller.topicText.setText(post.getTitle());
            controller.textBody.setText(post.getText());
            // disable some actions if user show panel is opened in main panel
            if (UserController.user == null) {
                controller.likeButton.setVisible(false);
                controller.dislikeButton.setVisible(false);
                controller.deleteButton.setVisible(false);
                controller.editButton.setVisible(false);
                controller.saveButton.setVisible(false);
            }
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
            return node;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
