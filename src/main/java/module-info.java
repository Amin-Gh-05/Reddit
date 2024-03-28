module org.project.reddit {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.project.reddit to javafx.fxml;
    exports org.project.reddit;
}