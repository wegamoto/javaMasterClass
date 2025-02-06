module sample.todolist {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.wewe.todolist to javafx.fxml;
    exports com.wewe.todolist;
}