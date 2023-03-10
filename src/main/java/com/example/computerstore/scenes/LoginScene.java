package com.example.computerstore.scenes;

import com.example.computerstore.entity.User;
import com.example.computerstore.repository.UserRepository;
import com.example.computerstore.service.AuthorizationService;
import com.example.computerstore.service.NavigationService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class LoginScene implements Navigatable {

    private final List<Node> nodes = new ArrayList<>();
    private final Text emailStatusText;
    private final Text passwordStatusText;
    private final StringProperty emailStatus = new SimpleStringProperty("");
    private final StringProperty passwordStatus = new SimpleStringProperty("");
    private final StringProperty enteredEmail = new SimpleStringProperty();
    private final StringProperty enteredPassword = new SimpleStringProperty();

    private final UserRepository userRepository = new UserRepository();
    private final AuthorizationService authorizationService = AuthorizationService.getInstance();
    private final NavigationService navigationService = NavigationService.getInstance();

    public LoginScene() {
        VBox content = new VBox();

        Label label = new Label("Login Page");
        label.setFont(new Font("Verdana", 25));
        HBox titleBox = new HBox(label);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(12, 12, 12, 12));
        Image image = new Image("store.jpg");
        ImageView imageView = new ImageView(image);
        imageView.setOpacity(0.3);

        GridPane loginPane = new GridPane();
//        loginPane.setGridLinesVisible(true);
        loginPane.setPadding(new Insets(45));
        loginPane.setVgap(10);
        loginPane.setHgap(10);

        TextField emailField = new TextField();
        enteredEmail.bind(emailField.textProperty());
        Text emailLabel = new Text("Email");
        emailLabel.setStyle("-fx-font-weight: bold");

        PasswordField passwordField = new PasswordField();
        enteredPassword.bind(passwordField.textProperty());
        Text passwordLabel = new Text("Password");
        passwordLabel.setStyle("-fx-font-weight: bold");

        emailStatusText = new Text("OK");
        emailStatusText.textProperty().bind(emailStatus);
        emailStatusText.setStyle("-fx-font-weight: bold; -fx-font-size: 20");
        passwordStatusText = new Text("NOT");
        passwordStatusText.textProperty().bind(passwordStatus);
        passwordStatusText.setStyle("-fx-font-weight: bold; -fx-font-size: 20");

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-pref-width: 100px");
        loginButton.setPadding(new Insets(5));
        GridPane.setMargin(loginButton, new Insets(0, 0, 0, 50));

        loginPane.add(emailLabel, 0, 0);
        loginPane.add(emailField, 1, 0);
        loginPane.add(emailStatusText, 2, 0);

        loginPane.add(passwordLabel, 0, 1);
        loginPane.add(passwordField, 1, 1);
        loginPane.add(passwordStatusText, 2, 1);

        loginPane.add(loginButton, 1, 2);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(imageView, loginPane);

        content.getChildren().addAll(titleBox, stackPane);

        loginButton.setOnAction((event) -> {
            boolean loginSuccessful = validateInput();
            if (loginSuccessful) {
                this.navigationService.navigateTo("home");
            }
        });
        this.nodes.add(new BorderPane(content));
    }

    @Override
    public String getTitle() {
        return "Computer Store";
    }

    @Override
    public List<Node> getNodes() {
        return this.nodes;
    }

    public boolean validateInput() {
        String email = enteredEmail.get();
        String password = enteredPassword.get();
        boolean userExists = userRepository.emailExists(email);
        if (userExists) {
            emailStatus.set("✔");
            emailStatusText.setFill(Color.GREEN);
        } else {
            emailStatus.set("❌");
            emailStatusText.setFill(Color.RED);
            return false;
        }

        User user = userRepository.loginUser(email, password);
        if (user == null) {
            passwordStatus.set("❌");
            passwordStatusText.setFill(Color.RED);
            return false;
        } else {
            passwordStatus.set("✔");
            passwordStatusText.setFill(Color.GREEN);
        }
        this.authorizationService.login(user);
        return true;
    }
}
