module org.project.reddit {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.codec;


    opens org.project.reddit to javafx.fxml;
    exports org.project.reddit;
}