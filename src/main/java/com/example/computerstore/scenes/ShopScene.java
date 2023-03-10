package com.example.computerstore.scenes;

import com.example.computerstore.entity.CartItem;
import com.example.computerstore.entity.Computer;
import com.example.computerstore.repository.CartRepository;
import com.example.computerstore.repository.ComputerRepository;
import com.example.computerstore.service.AuthorizationService;
import com.example.computerstore.service.NavigationService;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShopScene implements Navigatable {

    private final List<Node> nodes = new ArrayList<>();
    private final ComputerRepository computerRepository = new ComputerRepository();
    private final AuthorizationService authorizationService = AuthorizationService.getInstance();
    private final CartRepository cartRepository = new CartRepository();
    private final NavigationService navigationService = NavigationService.getInstance();
    Alert removeAlert = new Alert(Alert.AlertType.CONFIRMATION, "Computer will be removed from shop, proceed?", ButtonType.OK, ButtonType.CANCEL);
    VBox computerBox;
    List<Computer> computerList = new ArrayList<>();

    public ShopScene() {
        fetchComputers();
        Label label = new Label("Our Computers");
        label.setFont(new Font("Verdana", 25));
        HBox titleBox = new HBox(label);
        titleBox.setAlignment(Pos.CENTER);
        VBox content = new VBox();
        content.getChildren().add(computerBox);
        ScrollPane scrollPane = new ScrollPane(content);
        BorderPane borderPane = new BorderPane(scrollPane);
        this.nodes.addAll(List.of(titleBox, borderPane));
    }


    @Override
    public String getTitle() {
        return "Shop Scene";
    }

    @Override
    public List<Node> getNodes() {
        return this.nodes;
    }

    private void fetchComputers() {
        if (this.authorizationService.isAdmin()) {
            this.computerList = computerRepository.findAll();
        } else {
            this.computerList = computerRepository.findAllInStock();
        }
        computerBox = getComputers(computerList);
    }


    private VBox getComputers(List<Computer> computerList) {
        VBox root = new VBox();
        root.setPadding(new Insets(5));
        if(computerList.size() == 0){
            Label label1 = new Label("Unfortunately we don't have any computers in stock at the moment.");
            Label label2 = new Label("Please check back later.");
            root.setAlignment(Pos.BASELINE_CENTER);
            root.getChildren().addAll(label1,label2);
            return root;
        }
        computerList.forEach(computer -> {
//            Computer = getComputer(computer);
            boolean inStock = computer.getInStock().equalsIgnoreCase("Y");
            String imagePath = (computer.getImagePath() == null || computer.getImagePath().isBlank()) ? "no_image.jpg" : computer.getImagePath();
            Image image = new Image(imagePath);
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(70);
            imageView.setFitWidth(110);

            GridPane computerBox = new GridPane();
            computerBox.setPadding(new Insets(5));
            Region r = new Region();
            r.setMinWidth(80);

            VBox imageBox = new VBox(imageView);
            VBox productBox = new VBox();
            productBox.setMinWidth(170);

            computerBox.addRow(0, productBox, r, imageBox);
            GridPane.setHalignment(imageBox, HPos.RIGHT);
            GridPane.setHalignment(computerBox, HPos.LEFT);

            HBox productManufacturerBox = new HBox();
            HBox productNameBox = new HBox();
            HBox productPriceBox = new HBox();

            Label nameLabel = new Label("Model Name: ");
            Label manufacturerLabel = new Label("Manufacturer: ");
            Label priceLabel = new Label("Price: ");

            Label productName = new Label(computer.getModelName());
            Label productManufacturer = new Label(computer.getManufacturer());
            Label price = new Label(String.valueOf(computer.getPrice()));

            productNameBox.getChildren().addAll(nameLabel, productName);
            productManufacturerBox.getChildren().addAll(manufacturerLabel, productManufacturer);
            productPriceBox.getChildren().addAll(priceLabel, price);

            VBox buttons = new VBox();
            GridPane userActions = new GridPane();
            userActions.setHgap(5);

            if(inStock){
                Button addToCartButton = new Button("Add to cart");

                addToCartButton.setOnAction(event -> {
                    ButtonType cart = new ButtonType("Go to cart");
                    ButtonType ok = new ButtonType("OK");
                    Alert addToCartAlert = new Alert(Alert.AlertType.INFORMATION, "Item added to cart successfully?");
                    addToCartAlert.setTitle("Success!");
                    addToCartAlert.setHeaderText("Success!");
                    addToCartAlert.getButtonTypes().setAll(cart, ok);

                    this.cartRepository.save(new CartItem(computer.getId(), this.authorizationService.getUserId()));
                    // learn something about java.util.Optional;
                    Optional<ButtonType> result =  addToCartAlert.showAndWait();
                    if(result.get() == cart){
                        this.navigationService.navigateTo("cart");
                    }
                });
                userActions.addRow(0, addToCartButton);
            }


            buttons.getChildren().add(userActions);

            if (this.authorizationService.isAdmin()) {
                Label stockLabel = new Label();
                stockLabel.setText(inStock ? "In stock" : "Not in stock");
                stockLabel.setStyle("-fx-font-weight: bold");
                stockLabel.setTextFill(inStock ? Color.GREEN : Color.RED);
                userActions.add(stockLabel, 1, 0);
                GridPane adminActions = new GridPane();
                Label label = new Label("Admin actions:");
                label.setStyle("-fx-font-weight: bold");
                Separator separator = new Separator();
                separator.setPadding(new Insets(5));
                separator.setOrientation(Orientation.HORIZONTAL);
                buttons.getChildren().add(separator);

                Button removeStock = new Button(inStock ? "Remove from stock" : "Add to stock");
                Button removeItem = new Button("Remove from shop");

                removeStock.setOnAction(event -> {
                    this.computerRepository.updateStockStatus(computer.getId(), inStock ? "N" : "Y");
                    this.navigationService.navigateTo("shop");
                });
                removeItem.setOnAction(event -> {
                    removeAlert.showAndWait().ifPresent(
                            response -> {
                                if (response == ButtonType.OK) {
                                    this.computerRepository.delete(computer);
                                    this.navigationService.navigateTo("shop");
                                }
                            }
                    );
                });
                adminActions.setHgap(5);
                adminActions.addRow(0, label, removeItem, removeStock);
                buttons.getChildren().add(adminActions);
            }

            productBox.getChildren().addAll(productManufacturerBox, productNameBox, productPriceBox);
            Separator productSeparator = new Separator();
            productSeparator.setPadding(new Insets(5));
            productSeparator.setOrientation(Orientation.HORIZONTAL);
            root.getChildren().addAll(computerBox, buttons, productSeparator);
        });
        return root;
    }
}

