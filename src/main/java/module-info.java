module org.project.reddit {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.codec;


    opens org.project.reddit to javafx.fxml;
    exports org.project.reddit;
    exports org.project.reddit.front;
    exports org.project.reddit.user;
    exports org.project.reddit.content;
    opens org.project.reddit.front to javafx.fxml;
}