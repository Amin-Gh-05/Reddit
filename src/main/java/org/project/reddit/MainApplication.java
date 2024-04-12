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

    @Override
    public void init() throws Exception {
        try {
            FileInputStream fileIn = new FileInputStream("Users.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            User.userList = (ArrayList<User>) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("> user-list was deserialized");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileInputStream fileIn = new FileInputStream("Subreddits.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            SubReddit.subRedditList = (ArrayList<SubReddit>) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("> subreddit-list was deserialized");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileInputStream fileIn = new FileInputStream("Posts.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Post.postList = (ArrayList<Post>) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("> post-list was deserialized");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    @Override
    public void stop() {
        try {
            FileOutputStream fileOut = new FileOutputStream("Users.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(User.userList);
            out.close();
            fileOut.close();
            System.out.println("> user-list was serialized");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileOutputStream fileOut = new FileOutputStream("Subreddits.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(SubReddit.subRedditList);
            out.close();
            fileOut.close();
            System.out.println("> subreddit-list was serialized");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileOutputStream fileOut = new FileOutputStream("Posts.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(Post.postList);
            out.close();
            fileOut.close();
            System.out.println("> post-list was serialized");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
