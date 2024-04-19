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
import javafx.stage.Stage;
import org.project.reddit.content.Post;
import org.project.reddit.content.SubReddit;
import org.project.reddit.user.User;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Label userCount;
    @FXML
    private Label subredditCount;
    @FXML
    private Label postCount;
    @FXML
    private TextField searchText;
    @FXML
    private ListView<String> trendingPostList;
    @FXML
    private TabPane tabsPane;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        refreshAll();
        System.out.println("> main panel got initialized");
    }

    // open sign up panel and change scene
    @FXML
    void openSignUpPage(ActionEvent event) throws IOException {
        // change scene
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/project/reddit/signup-view.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        System.out.println("> redirect to signup page");
    }

    // open log in panel and change scene
    @FXML
    void openLogInPage(ActionEvent event) throws IOException {
        // change scene
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/project/reddit/login-view.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        System.out.println("> redirect to login page");
    }

    // refresh counts and trending-post list
    @FXML
    void refreshAll() {
        this.userCount.setText(String.valueOf(User.userCount));
        this.subredditCount.setText(String.valueOf(SubReddit.subRedditCount));
        this.postCount.setText(String.valueOf(Post.postCount));
        try {
            refreshTrendingPosts();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("> " + e);
        }
        System.out.println("> main panel refreshed");
    }

    // search among users and subreddits, then open a new tab and show details if possible
    @FXML
    void searchAll() {
        // search among subreddits
        if (searchText.getText().startsWith("r/")) {
            SubReddit subReddit = SubReddit.findSubReddit(searchText.getText().substring(2));
            // do nothing if not found
            if (subReddit == null) {
                System.out.println("> Invalid topic");
                return;
            }
            loadSubreddit(subReddit);
        }
        // search among users
        else if (searchText.getText().startsWith("u/")) {
            User user = User.findUserViaUsername(searchText.getText().substring(2));
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

    // refresh trending-posts list
    void refreshTrendingPosts() throws IndexOutOfBoundsException {
        // clear and fill list
        this.trendingPostList.getItems().clear();
        this.trendingPostList.getItems().addAll(Post.getTrendingPosts());
        // add right click menu for every post
        ContextMenu contextMenu = new ContextMenu();
        MenuItem viewItem = new MenuItem("View");
        contextMenu.getItems().add(viewItem);
        this.trendingPostList.setContextMenu(contextMenu);
        // set action for menu item
        this.trendingPostList.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            Post post = Post.postList.get(trendingPostList.getSelectionModel().getSelectedIndex());
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

    Node getPostLayout(Post post) {
        // load post layout from fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/project/reddit/post-view.fxml"));
        try {
            Node node = loader.load();
            PostController controller = loader.getController();
            controller.post = post;
            // set details of post
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
            controller.likeButton.setVisible(false);
            controller.dislikeButton.setVisible(false);
            controller.saveButton.setVisible(false);
            controller.editButton.setVisible(false);
            controller.deleteButton.setVisible(false);
            return node;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    // open a new tab for subreddit
    void loadSubreddit(SubReddit subReddit) {
        // load fxml file of subreddit layout
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/project/reddit/subreddit-view.fxml"));
        try {
            Node node = loader.load();
            SubredditController controller = loader.getController();
            controller.subReddit = subReddit;
            // only-if loaded in main panel
            controller.mainController = this;
            // set details of subreddit
            controller.topicText.setText(subReddit.getTopic());
            controller.memberCount.setText("Members: " + subReddit.getMemberCount());
            controller.dateText.setText(subReddit.getCreateDateTime().substring(0, 10));
            // disable some actions
            controller.joinButton.setVisible(false);
            controller.leaveButton.setVisible(false);
            controller.createPostPane.setVisible(false);
            controller.refreshAll();
            // open a new tab
            Tab subredditTab = new Tab(subReddit.getTopic());
            subredditTab.setClosable(true);
            subredditTab.setContent(node);
            tabsPane.getTabs().add(subredditTab);
            tabsPane.getSelectionModel().select(subredditTab);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    void loadUser(User user) {
        // load fxml file of show user layout
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/project/reddit/show-view.fxml"));
        try {
            Node node = loader.load();
            ShowController controller = loader.getController();
            controller.user = user;
            // only if opened in main panel
            controller.mainController = this;
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
