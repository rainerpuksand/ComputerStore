package com.example.computerstore.service;

import com.example.computerstore.entity.User;
import com.example.computerstore.scenes.*;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class NavigationService {

    private static NavigationService instance = null;
    private AuthorizationService auth = AuthorizationService.getInstance();
    private Stage stage;


    public static NavigationService getInstance() {
        if (instance == null) {
            instance = new NavigationService();
        }
        return instance;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void navigateTo(String name) {
        Navigatable scene = createScene(name);
        List<Node> nodes = scene.getNodes();
        VBox root = new VBox();
        if(!name.equals("login")){
            User user = this.auth.getAuthState();
            BorderPane userProfilePane = new BorderPane();
            userProfilePane.setMinWidth(400);
            userProfilePane.setPadding(new Insets(5));

            String labelsStyle = "-fx-font-weight: bold";
            Label nameLabel = new Label(String.format("Logged in as: %s %s", user.getFirstName(), user.getLastName()));
            Label roleLabel = new Label(String.format("Role: %s", user.getRole()));
            nameLabel.setStyle(labelsStyle);
            roleLabel.setStyle(labelsStyle);
            HBox userNameBox = new HBox(nameLabel, roleLabel);
            userNameBox.setSpacing(10);


            Button logoutButton = new Button("Logout");
            logoutButton.setOnAction(event-> this.logoutUser());
            Button editProfileButton = new Button("Edit profile");
            HBox buttonsBox = new HBox(logoutButton,editProfileButton);
            buttonsBox.setSpacing(5);

            userProfilePane.setRight(buttonsBox);
            userProfilePane.setLeft(userNameBox);


            MenuBar menuBar = getMenuBar();
            root.getChildren().addAll(menuBar, userProfilePane);
        }
        root.getChildren().addAll(nodes);
        stage.setTitle(scene.getTitle());
        stage.setScene(new Scene(root, 400,300));
    }

    private Navigatable createScene(String scene) {
        return switch (scene) {
            case "cart" -> new CartScene();
            case "admin" -> new AdminScene();
            case "login" -> new LoginScene();
            case "shop" -> new ShopScene();
            case "home" -> new HomeScene();
            default -> new HomeScene();
        };
    }

    public MenuBar getMenuBar() {
        Menu menu = new Menu("Menu");

        MenuItem shop = new MenuItem("Shop");
        shop.setOnAction(event -> this.navigateTo("shop"));

        MenuItem shoppingCart = new MenuItem("My cart");
        shoppingCart.setOnAction(event -> this.navigateTo("cart"));

        MenuItem bills = new MenuItem("Bills");

        MenuItem admin = new MenuItem("Admin Panel");
        admin.setOnAction(event -> this.navigateTo("admin"));

        List<MenuItem> menuItemList = new ArrayList<>(List.of(shop, shoppingCart, bills));
        if (this.auth.isAdmin()) {
            menuItemList.add(admin);
        }
        menu.getItems().addAll(menuItemList);
        return new MenuBar(menu);
    }

    private void logoutUser(){
        this.auth.logout();
        this.navigateTo("login");
    }

    private NavigationService() {
    }

}
