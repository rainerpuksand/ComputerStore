package com.example.computerstore.scenes;

import com.example.computerstore.service.NavigationService;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

public class HomeScene implements Navigatable {

    private final List<Node> nodes = new ArrayList<>();
    private final NavigationService navigationService = NavigationService.getInstance();
    ObservableList<Node> observableList = FXCollections.observableArrayList();

    public HomeScene() {
//        HBox hBox = new HBox();
//        Bindings.bindContent(hBox.getChildren(), observableList);
//        Button button = new Button("ADD ITEM");
//        button.setOnAction(event->{
//            observableList.add(new Button("Reactively added button"));
//        });
//        this.nodes.add(hBox);
//        this.nodes.add(button);
    }



    public String getTitle() {
        return "Home Scene";
    }

    @Override
    public List<Node> getNodes() {
        return this.nodes;
    }
}
