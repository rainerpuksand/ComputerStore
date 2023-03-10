package com.example.computerstore;

import com.example.computerstore.entity.User;
import com.example.computerstore.repository.CartRepository;
import com.example.computerstore.repository.UserRepository;
import com.example.computerstore.scenes.LoginScene;
import com.example.computerstore.service.NavigationService;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class HelloApplication extends Application {

    private final UserRepository userRepository = new UserRepository();
    private final CartRepository cartRepository = new CartRepository();
    private final NavigationService navigationService =  NavigationService.getInstance();

    @Override
    public void start(Stage stage) {
        this.navigationService.setStage(stage);
        System.out.println("List of existing users:");
        List<User> users = userRepository.findAll();
        users.forEach(System.out::println);
        LoginScene loginScene = new LoginScene();
        List<Node> nodes = loginScene.getNodes();
        VBox root = new VBox();
        root.getChildren().addAll(nodes);
        stage.setTitle(loginScene.getTitle());
        stage.setScene(new Scene(root, 400, 300));
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
//        Session session = ConnectionService.getSessionFactory().openSession();
//        session.beginTransaction();
//        User user = new User();
//        user.setFirstName("First name");
//        user.setLastName("Last name");
//        user.setEmailAddress("Email address");
//        user.setPassword("Password");
//        session.save(user);
//        user.setEmailAddress(null);
//        session.save(user);
//        session.getTransaction().commit();
//        session.clear();
        launch();
    }
}