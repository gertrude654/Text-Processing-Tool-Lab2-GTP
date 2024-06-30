package org.example.textprocessingtool.dataManager;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.Map;


public class MainAppController {
    @FXML
    private TextField textInput;
    @FXML
    private TextField regexInput;
    @FXML
    private ComboBox<String> collectionComboBox;
    @FXML
    private ListView<String> listView;
    @FXML
    private TextArea resultArea;
    @FXML
    private Label currentCollectionLabel;

    private final GenericController<String> controller;

    public MainAppController() {
        GenericRepository<String> repository = new GenericRepositoryImpl<>();
        controller = new GenericControllerImpl<>(repository);
    }

    @FXML
    private void handleAdd(MouseEvent event) {
        String text = textInput.getText();
        if (!text.isEmpty()) {
            controller.addData(collectionComboBox.getValue(), text);
            refreshListView();
            textInput.clear();
        }
    }

    @FXML
    private void handleUpdate(MouseEvent event) {
        String selectedItem = listView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String newText = textInput.getText();
            if (!newText.isEmpty()) {
                controller.updateData(collectionComboBox.getValue(), selectedItem, newText);
                refreshListView();
                textInput.clear();
            }
        }
    }

    @FXML
    private void handleDelete(MouseEvent event) {
        String selectedItem = listView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            controller.deleteData(collectionComboBox.getValue(), selectedItem);
            refreshListView();
        }
    }

    @FXML
    private void handleSearch(MouseEvent event) {
        String regex = regexInput.getText();
        if (!regex.isEmpty()) {
            var matches = controller.searchData(collectionComboBox.getValue(), regex);
            resultArea.clear();
            matches.forEach(match -> resultArea.appendText(match + "\n"));
        }
    }

    @FXML
    private void handleCollectionSwitch(MouseEvent event) {
        refreshListView();
        updateCollectionLabel(collectionComboBox.getValue());
    }

    private void refreshListView() {
        listView.getItems().clear();
        switch (collectionComboBox.getValue()) {
            case "ArrayList":
                listView.getItems().addAll(controller.getDataCollection());
                break;
            case "HashSet":
                listView.getItems().addAll(controller.getDataSet());
                break;
            case "LinkedHashMap":
                for (Map.Entry<Integer, String> entry : controller.getDataMap().entrySet()) {
                    listView.getItems().add(entry.getKey() + ": " + entry.getValue());
                }
                break;
            case "HashMap":
                for (Map.Entry<Integer, String> entry : controller.getDataHashMap().entrySet()) {
                    listView.getItems().add(entry.getKey() + ": " + entry.getValue());
                }
                break;
        }
    }

    private void updateCollectionLabel(String collectionType) {
        currentCollectionLabel.setText("Current Collection: " + collectionType);
    }
}
