module com.exam.mcq {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.exam.mcq to javafx.fxml;
    exports com.exam.mcq;
}