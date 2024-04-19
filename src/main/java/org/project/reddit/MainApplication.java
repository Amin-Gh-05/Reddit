package org.project.reddit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.project.reddit.content.Post;
import org.project.reddit.content.SubReddit;
import org.project.reddit.user.User;

import java.io.*;
import java.util.ArrayList;

public class MainApplication extends Application {
    public static void main(String[] args) {
        System.out.println("> app started");
        launch(args);
    }

    // load data before launching app
    @Override
    public void init() throws Exception {
        // load users from User.ser if possible
        try {
            FileInputStream fileIn = new FileInputStream("Users.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            User.userList = (ArrayList<User>) in.readObject();
            in.close();
            fileIn.close();
            User.userCount = User.userList.size();
            System.out.println("> user-list was deserialized");
        } catch (IOException e) {
            System.out.println("> " + e);
        }
        // load subreddits from Subreddits.ser if possible
        try {
            FileInputStream fileIn = new FileInputStream("Subreddits.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            SubReddit.subRedditList = (ArrayList<SubReddit>) in.readObject();
            in.close();
            fileIn.close();
            SubReddit.subRedditCount = SubReddit.subRedditList.size();
            System.out.println("> subreddit-list was deserialized");
        } catch (IOException e) {
            System.out.println("> " + e);
        }
        // load posts from Posts.ser if possible
        try {
            FileInputStream fileIn = new FileInputStream("Posts.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Post.postList = (ArrayList<Post>) in.readObject();
            in.close();
            fileIn.close();
            Post.postCount = Post.postList.size();
            System.out.println("> post-list was deserialized");
        } catch (IOException e) {
            System.out.println("> " + e);
        }
    }

    // start app with initializing the main panel
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 540);
        stage.getIcons().add(new Image("org/project/reddit/pics/icon.png"));
        stage.setTitle("Reddit");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    // save data while closing the app
    @Override
    public void stop() {
        // save users to Users.ser if possible
        try {
            FileOutputStream fileOut = new FileOutputStream("Users.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(User.userList);
            out.close();
            fileOut.close();
            System.out.println("> user-list was serialized");
        } catch (IOException e) {
            System.out.println("> " + e);
        }
        // save subreddits to Subreddits.ser if possible
        try {
            FileOutputStream fileOut = new FileOutputStream("Subreddits.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(SubReddit.subRedditList);
            out.close();
            fileOut.close();
            System.out.println("> subreddit-list was serialized");
        } catch (IOException e) {
            System.out.println("> " + e);
        }
        // save posts to Posts.ser if possible
        try {
            FileOutputStream fileOut = new FileOutputStream("Posts.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(Post.postList);
            out.close();
            fileOut.close();
            System.out.println("> post-list was serialized");
        } catch (IOException e) {
            System.out.println("> " + e);
        }
    }
}
