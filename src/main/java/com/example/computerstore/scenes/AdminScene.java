package com.example.computerstore.scenes;

import com.example.computerstore.entity.Computer;
import com.example.computerstore.repository.ComputerRepository;
import com.example.computerstore.service.NavigationService;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class AdminScene implements Navigatable {

    private final NavigationService navigationService = NavigationService.getInstance();
    private final ComputerRepository computerRepository = new ComputerRepository();
    private final StringProperty enteredManufacturer = new SimpleStringProperty();
    private final StringProperty enteredModelName = new SimpleStringProperty();
    private final DoubleProperty enteredPrice = new SimpleDoubleProperty(0);
    private final StringProperty enteredImagePath = new SimpleStringProperty();
    private final StringProperty inStockSelected = new SimpleStringProperty();
    private final List<Node> nodes = new ArrayList<>();

    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Invalid file url.", ButtonType.OK);
    Alert successAlert = new Alert(Alert.AlertType.CONFIRMATION, "Computer will be created, proceed?", ButtonType.OK, ButtonType.CANCEL);

    public AdminScene() {
        Label label = new Label("Create a computer");
        label.setFont(new Font("Verdana", 25));
        HBox titleBox = new HBox(label);
        titleBox.setAlignment(Pos.CENTER);

        GridPane gridPane = new GridPane();
//      gridPane.setGridLinesVisible(true);
        gridPane.setPadding(new Insets(30, 60, 20, 45));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        TextField manufacturer = new TextField();
        enteredManufacturer.bind(manufacturer.textProperty());
        Text manufacturerLabel = new Text("Manufacturer");
        manufacturerLabel.setStyle("-fx-font-weight: bold");

        TextField modelName = new TextField();
        enteredModelName.bind(modelName.textProperty());
        Text modelNameLabel = new Text("Model");
        modelNameLabel.setStyle("-fx-font-weight: bold");

        Spinner<Double> priceInput = new Spinner<>(0, 5000, 0, 0.1);
        priceInput.setEditable(true);
        enteredPrice.bind(priceInput.valueProperty());
        Text priceLabel = new Text("Price");
        priceLabel.setStyle("-fx-font-weight: bold");

        TextField imagePathInput = new TextField();
        enteredImagePath.bind(imagePathInput.textProperty());
        Text imagePathLabel = new Text("Image path");
        imagePathLabel.setStyle("-fx-font-weight: bold");

        Text toggleLabel = new Text("Add to stock?");
        toggleLabel.setStyle("-fx-font-weight: bold");

        ToggleGroup toggles = new ToggleGroup();
        ToggleButton stock = new ToggleButton("Yes");
        stock.setUserData("Y");
        ToggleButton noStock = new ToggleButton("No");
        noStock.setUserData("N");
        HBox togglesBox = new HBox(stock, noStock);
        toggles.getToggles().addAll(stock, noStock);

        Button submitButton = new Button("Submit");


        submitButton.setOnAction(event -> {
            try {
                getClass().getClassLoader().getResource(enteredImagePath.get()).getFile();
                successAlert.showAndWait().ifPresent(
                        response -> {
                            if (response == ButtonType.OK) {
                                Computer computer = new Computer(
                                        enteredManufacturer.get(),
                                        enteredModelName.get(),
                                        enteredPrice.getValue(),
                                        enteredImagePath.get(),
                                        inStockSelected.get());
                                this.computerRepository.save(computer);
                                this.navigationService.navigateTo("shop");
                            }
                        }
                );

            } catch (Exception e) {
                errorAlert.showAndWait();
            }
        });

        StringBinding inStockBinding = new StringBinding() {
            {
                super.bind(toggles.selectedToggleProperty());
            }

            @Override
            protected String computeValue() {
                Toggle toggle = toggles.getSelectedToggle();
                if (toggle == null) {
                    return "";
                }
                return (String) toggle.getUserData();
            }
        };
        this.inStockSelected.bind(inStockBinding);

        submitButton.
                disableProperty().
                bind(enteredManufacturer.isEmpty().
                        or(enteredModelName.isEmpty()).
                        or(enteredPrice.isEqualTo(0)).
                        or(enteredImagePath.isEmpty()).
                        or(inStockSelected.isEmpty()));

        submitButton.setStyle("-fx-pref-width: 100px");
        submitButton.setPadding(new Insets(5));
        GridPane.setMargin(submitButton, new Insets(0, 0, 0, 50));

        gridPane.add(manufacturerLabel, 0, 0);
        gridPane.add(manufacturer, 1, 0);

        gridPane.add(modelNameLabel, 0, 1);
        gridPane.add(modelName, 1, 1);

        gridPane.add(priceLabel, 0, 2);
        gridPane.add(priceInput, 1, 2);

        gridPane.add(imagePathLabel, 0, 3);
        gridPane.add(imagePathInput, 1, 3);

        gridPane.add(toggleLabel, 0, 4);
        gridPane.add(togglesBox, 1, 4);

        gridPane.add(submitButton, 1, 5);
        this.nodes.addAll(List.of(titleBox, gridPane));
    }

    @Override
    public String getTitle() {
        return "Admin Scene";
    }

    @Override
    public List<Node> getNodes() {
        return this.nodes;
    }
}
