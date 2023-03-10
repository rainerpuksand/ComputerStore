package com.example.computerstore.scenes;

import com.example.computerstore.entity.CartItem;
import com.example.computerstore.entity.Computer;
import com.example.computerstore.repository.CartRepository;
import com.example.computerstore.service.AuthorizationService;
import com.example.computerstore.service.NavigationService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CartScene implements Navigatable {
    private final List<Node> nodes = new ArrayList<>();
    private final CartRepository cartRepository = new CartRepository();
    private final AuthorizationService authorizationService = AuthorizationService.getInstance();
    private final NavigationService navigationService = NavigationService.getInstance();
    Alert clearAlert = new Alert(Alert.AlertType.CONFIRMATION, "Your cart will be cleared. Proceed?", ButtonType.OK, ButtonType.CANCEL);
    private final BooleanProperty actionsDisabledProperty = new SimpleBooleanProperty();

    public CartScene() {
        List<Computer> computers = cartRepository.findItemsByUserId(this.authorizationService.getUserId());
        actionsDisabledProperty.set(computers.size() == 0);
        double totalCartPrice = computers.stream().mapToDouble(Computer::getPrice).sum();
        Label totalPriceLabel = new Label(String.format("Total cart price: %.2f",totalCartPrice));
        totalPriceLabel.setStyle("-fx-font-weight: bold");
        computers.forEach(System.out::println);
        VBox cartItems = new VBox();
        if (computers.size() > 0) {
            cartItems.getChildren().add(generateCart(computers));
        } else {
            cartItems.setMinWidth(390);
            cartItems.setAlignment(Pos.CENTER);
            Text text = new Text("You have no items in cart.");
            Button goToShopButton = new Button("Go to shop");
            goToShopButton.setOnAction(event -> this.navigationService.navigateTo("shop"));
            cartItems.getChildren().addAll(text, goToShopButton);
        }
        Label label = new Label("Shopping cart");
        label.setFont(new Font("Verdana", 25));
        HBox titleBox = new HBox(label);
        titleBox.setAlignment(Pos.CENTER);
        ScrollPane scrollPane = new ScrollPane(cartItems);
        scrollPane.setMaxHeight(200);
        scrollPane.setMinHeight(160);

        GridPane actionButtons = new GridPane();
        actionButtons.setPadding(new Insets(10));
        actionButtons.setHgap(10);
        Button checkoutButton = new Button("Checkout");
        Button clearButton = new Button("Clear");
        checkoutButton.disableProperty().bind(actionsDisabledProperty);
        clearButton.disableProperty().bind(actionsDisabledProperty);
        clearButton.setOnAction(event -> {
            clearAlert.showAndWait().ifPresent(
                    response -> {
                        if (response == ButtonType.OK) {
                            this.cartRepository.clearCart(this.authorizationService.getUserId());
                            this.navigationService.navigateTo("cart");
                        }
                    });
        });
        actionButtons.addRow(0,totalPriceLabel, checkoutButton, clearButton);
        this.nodes.addAll(List.of(titleBox, scrollPane, actionButtons));
    }

    private GridPane generateCart(List<Computer> computerList) {
        Map<Computer, Long> computerMap =
                computerList.stream().collect(
                        Collectors.groupingBy(
                                Function.identity(), Collectors.counting()
                        )
                );

        GridPane cartItems = new GridPane();
        cartItems.setPadding(new Insets(5));
        cartItems.setHgap(10);
        cartItems.setVgap(10);

        List<Computer> sortedList = computerMap.keySet().stream().sorted(Comparator.comparing(Computer::getManufacturer)).toList();
        for(int i = 0; i<sortedList.size();i++){
            Computer computer = sortedList.get(i);
            long count = computerMap.get(computer);
            double price = count * computer.getPrice();

            Text priceLabel = new Text(String.format("%.2f$",computer.getPrice()));
            Text totalPriceLabel = new Text(String.format("%.2f$", price));

            Button plusButton = new Button("+");
            Text computerName = new Text(computer.getManufacturer() + " " + computer.getModelName());
            Button minusButton = new Button("-");
            minusButton.setOnAction(event -> {
                this.cartRepository.decrementCount(computer.getId(), this.authorizationService.getUserId());
                this.navigationService.navigateTo("cart");
            });

            plusButton.setOnAction(event -> {
                this.cartRepository.save(new CartItem(computer.getId(), this.authorizationService.getUserId()));
                this.navigationService.navigateTo("cart");
            });
            Text quantity = new Text(String.valueOf(count));

            cartItems.addRow(i,computerName, priceLabel, minusButton, quantity, plusButton, totalPriceLabel);
        }
        return cartItems;
    }


    public String getTitle() {
        return "Shop Scene";
    }

    @Override
    public List<Node> getNodes() {
        return this.nodes;
    }
}
