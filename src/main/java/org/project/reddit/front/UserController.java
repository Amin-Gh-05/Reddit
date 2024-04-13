package org.project.reddit.front;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.project.reddit.content.Post;
import org.project.reddit.content.SubReddit;
import org.project.reddit.user.User;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class UserController implements Initializable {
    public static User user;
    @FXML
    private ListView<String> savedPostList;
    @FXML
    private Label subredditCount;
    @FXML
    private Label postCount;
    @FXML
    private Label commentCount;
    @FXML
    private TextField searchText;
    @FXML
    private VBox postBox;
    @FXML
    private TabPane tabsPane;
    @FXML
    private TextField topicText;
    @FXML
    private Label invalidAlert;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        refreshAll();
        System.out.println("> user panel got initialized");
    }

    // log out button
    @FXML
    void logOut(ActionEvent event) throws IOException {
        // show alert to let user confirm
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Log Out");
        alert.setHeaderText("Returning to main panel");
        alert.setContentText("Are you sure you want to logout?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            // set user of panels null
            user = null;
            ProfileController.user = null;
            // change scene
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/project/reddit/main-view.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            System.out.println("> redirect to main panel");
        }
    }

    // search among users and subreddits, then open a new tab and show details if possible
    @FXML
    void searchAll(ActionEvent event) {
        // search among subreddits
        if (this.searchText.getText().startsWith("r/")) {
            SubReddit subReddit = SubReddit.findSubReddit(this.searchText.getText().substring(2));
            // do nothing if not found
            if (subReddit == null) {
                System.out.println("> Invalid topic");
                return;
            }
            loadSubreddit(subReddit);
        }
        // search among users
        else if (this.searchText.getText().startsWith("u/")) {
            User user = User.findUserViaUsername(this.searchText.getText().substring(2));
            // do nothing if not found
            if (user == null) {
                System.out.println("> Invalid username");
                return;
            }
            loadUser(user);
        }
        // condition which user input is not valid
        else {
            System.out.println("> Invalid input");
        }
    }

    // open profile panel and change scene
    @FXML
    void viewProfile(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/project/reddit/profile-view.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        System.out.println("> redirect to profile panel");
    }

    // refresh all details of panel
    @FXML
    void refreshAll() {
        // refresh counts
        this.subredditCount.setText(String.valueOf(user.getSubRedditList().size()));
        this.postCount.setText(String.valueOf(user.getPostList().size()));
        this.commentCount.setText(String.valueOf(user.getCommentList().size()));
        refreshSavedPosts();
        // refresh timeline and alerts
        refreshTimeline();
        invalidAlert.setVisible(false);
        System.out.println("> user panel got refreshed");
    }

    // create a new subreddit button
    @FXML
    void createSubreddit() {
        // checks if subreddit topic is repeated
        for (SubReddit subReddit : SubReddit.subRedditList) {
            if (subReddit.getTopic().equals(topicText.getText())) {
                System.out.println("> subreddit already exists");
                invalidAlert.setVisible(true);
                return;
            }
        }
        user.createSubReddit(topicText.getText());
        topicText.clear();
        refreshAll();
    }

    // refresh saved-post list
    void refreshSavedPosts() {
        int size = user.getSavedPostList().size();
        String[] savedPosts = new String[size];
        for (int i = 0; i < size; i++) {
            savedPosts[i] = user.getSavedPostList().get(i).getTitle();
        }
        this.savedPostList.getItems().clear();
        this.savedPostList.getItems().addAll(savedPosts);
        // set right click menu for every post selected
        ContextMenu contextMenu = new ContextMenu();
        MenuItem viewItem = new MenuItem("View");
        contextMenu.getItems().add(viewItem);
        savedPostList.setContextMenu(contextMenu);
        // action of menu item
        savedPostList.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            Post post = user.getSavedPostList().get(savedPostList.getSelectionModel().getSelectedIndex());
            viewItem.setOnAction(e -> loadPost(post));
        });
    }

    // load post layout
    private void loadPost(Post post) {
        Tab postTab = new Tab(post.getTitle());
        postTab.setClosable(true);
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER);
        stackPane.getChildren().add(getPostLayout(post));
        postTab.setContent(stackPane);
        this.tabsPane.getTabs().add(postTab);
        this.tabsPane.getSelectionModel().select(postTab);
    }

    // refresh timeline
    void refreshTimeline() {
        // clear posts
        postBox.getChildren().remove(1, postBox.getChildren().size());
        int size = user.getTimelinePostList().size();
        Post[] timelinePosts = new Post[size];
        // add the 10 latest posts
        if (size >= 10) {
            for (int i = size - 1; i >= size - 10; i--) {
                timelinePosts[i] = user.getTimelinePostList().get(i);
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                timelinePosts[i] = user.getTimelinePostList().get(i);
            }
        }
        for (Post post : timelinePosts) {
            this.postBox.getChildren().add(getPostLayout(post));
        }
    }

    // get the layout of posts and return a node
    Node getPostLayout(Post post) {
        // load post layout from fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/project/reddit/post-view.fxml"));
        try {
            Node node = loader.load();
            PostController controller = loader.getController();
            controller.post = post;
            // only-if opened in userpanel
            controller.userController = this;
            // set post details
            controller.usernameText.setText(Objects.requireNonNull(User.findUserViaId(post.getUser())).getUsername());
            controller.subredditText.setText(post.getSubReddit().getTopic());
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
            if (!post.getUser().equals(user.getId())) {
                controller.editButton.setVisible(false);
            }
            if (!post.getUser().equals(user.getId()) && !post.getSubReddit().getAdminList().contains(user.getId())) {
                controller.deleteButton.setVisible(false);
            }
            return node;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // open a new tab for subreddit loaded
    void loadSubreddit(SubReddit subReddit) {
        // load subreddit layout from fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/project/reddit/subreddit-view.fxml"));
        try {
            Node node = loader.load();
            SubredditController controller = loader.getController();
            controller.subReddit = subReddit;
            // only-if opened in user panel
            controller.userController = this;
            // set subreddit details
            controller.topicText.setText(subReddit.getTopic());
            controller.memberCount.setText("Members: " + subReddit.getMemberCount());
            controller.dateText.setText(subReddit.getCreateDateTime().substring(0, 10));
            // disable some actions as needed
            if (controller.subReddit.getMemberList().contains(user.getId())) {
                controller.joinButton.setVisible(false);
            } else {
                controller.leaveButton.setVisible(false);
            }
            controller.refreshAll();
            // open a new tab
            Tab subredditTab = new Tab(subReddit.getTopic());
            subredditTab.setClosable(true);
            subredditTab.setContent(node);
            this.tabsPane.getTabs().add(subredditTab);
            this.tabsPane.getSelectionModel().select(subredditTab);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    // open a new tab for user show loaded
    void loadUser(User user) {
        // load user show layout from fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/project/reddit/show-view.fxml"));
        try {
            Node node = loader.load();
            ShowController controller = loader.getController();
            controller.user = user;
            // only-if opened in user panel
            controller.userController = this;
            // set user show details
            controller.usernameText.setText(user.getUsername());
            controller.karmaCount.setText("Karma: " + user.getKarma());
            controller.refreshAll();
            // open a new tab
            Tab userTab = new Tab(user.getUsername());
            userTab.setClosable(true);
            userTab.setContent(node);
            this.tabsPane.getTabs().add(userTab);
            this.tabsPane.getSelectionModel().select(userTab);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
