module com.example.computerstore {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.graphics;
    requires java.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires java.sql;

    opens com.example.computerstore to javafx.fxml;
    opens com.example.computerstore.entity to org.hibernate.orm.core;
    exports com.example.computerstore;
}