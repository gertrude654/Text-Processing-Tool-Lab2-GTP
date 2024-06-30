package org.example.textprocessingtool.UI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.textprocessingtool.dataManager.GenericController;
import org.example.textprocessingtool.dataManager.GenericControllerImpl;
import org.example.textprocessingtool.dataManager.GenericRepository;
import org.example.textprocessingtool.dataManager.GenericRepositoryImpl;
import org.example.textprocessingtool.regex.RegexExpression;

import java.util.Map;

public class CombinedUI extends Application {

    private TextField emailTextField;
    private TextField patternTextField;
    private TextField replacementTextField;
    private TextArea resultTextArea;
    private RegexExpression regexExpression; // Instance for handling regex operations

    private TextField textInput;
    private TextField regexInput;
    private ComboBox<String> collectionComboBox;
    private ListView<String> listView;
    private TextArea resultArea;
    private Label currentCollectionLabel;

    private final GenericController<String> controller;

    public CombinedUI() {
        GenericRepository<String> repository = new GenericRepositoryImpl<>();
        controller = new GenericControllerImpl<>(repository);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Text Processing and Data Management Tool");

        // Initialize RegexExpressions instance
        regexExpression = new RegexExpression();

        // UI Components for Regex functionality
        Label emailLabel = new Label("Email Address:");
        emailTextField = new TextField();

        Label patternLabel = new Label("Pattern to Search:");
        patternTextField = new TextField();

        Label replacementLabel = new Label("Replacement Text:");
        replacementTextField = new TextField();

        Button validateButton = new Button("Validate Email");
        validateButton.setOnAction(e -> validateEmail());

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> searchPattern());

        Button matchButton = new Button("Match");
        matchButton.setOnAction(e -> matchPattern());

        Button replaceButton = new Button("Replace");
        replaceButton.setOnAction(e -> replacePattern());

        resultTextArea = new TextArea();
        resultTextArea.setEditable(false);
        resultTextArea.setWrapText(true);
        resultTextArea.setPrefHeight(100);

        VBox regexVBox = new VBox(10);
        regexVBox.setAlignment(Pos.CENTER);
        regexVBox.setPadding(new Insets(20));
        regexVBox.getChildren().addAll(
                emailLabel, emailTextField,
                patternLabel, patternTextField,
                replacementLabel, replacementTextField,
                validateButton, searchButton, matchButton, replaceButton,
                resultTextArea
        );

        Tab regexTab = new Tab("Regex Operations", regexVBox);

        // UI Components for Data Management functionality
        Label textInputLabel = new Label("Text Input:");
        textInput = new TextField();

        Label regexInputLabel = new Label("Regex Input:");
        regexInput = new TextField();

        collectionComboBox = new ComboBox<>();
        collectionComboBox.getItems().addAll("ArrayList", "HashSet", "LinkedHashMap", "HashMap");
        collectionComboBox.setValue("ArrayList");

        collectionComboBox.valueProperty().addListener((obs, oldVal, newVal) -> handleCollectionSwitch(null));

        listView = new ListView<>();

        resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setWrapText(true);
        resultArea.setPrefHeight(100);

        currentCollectionLabel = new Label("Current Collection: ArrayList");

        Button addButton = new Button("Add");
        addButton.setOnMouseClicked(this::handleAdd);

        Button updateButton = new Button("Update");
        updateButton.setOnMouseClicked(this::handleUpdate);

        Button deleteButton = new Button("Delete");
        deleteButton.setOnMouseClicked(this::handleDelete);

        Button collectionSearchButton = new Button("Search");
        collectionSearchButton.setOnMouseClicked(this::handleSearch);

        VBox dataVBox = new VBox(10);
        dataVBox.setAlignment(Pos.CENTER);
        dataVBox.setPadding(new Insets(20));
        dataVBox.getChildren().addAll(
                textInputLabel, textInput,
                regexInputLabel, regexInput,
                new Label("Collection Type:"), collectionComboBox,
                addButton, updateButton, deleteButton, collectionSearchButton,
                listView, currentCollectionLabel, resultArea
        );

        Tab dataTab = new Tab("Data Management", dataVBox);

        // TabPane
        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(regexTab, dataTab);

        // Scene
        Scene scene = new Scene(tabPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void validateEmail() {
        String email = emailTextField.getText();
        if (!email.isEmpty()) {
            boolean isValid = regexExpression.isValidEmail(email);
            displayResult("Email validity: " + isValid);
        } else {
            displayResult("Please enter a valid email address.");
        }
    }

    private void searchPattern() {
        String email = emailTextField.getText();
        String pattern = patternTextField.getText();
        if (!email.isEmpty() && !pattern.isEmpty()) {
            boolean found = regexExpression.searchPatternInText(email, pattern);
            displayResult("Pattern '" + pattern + "' found in text: " + found);
        } else {
            displayResult("Please enter text and a pattern to search.");
        }
    }

    private void matchPattern() {
        String email = emailTextField.getText();
        String pattern = patternTextField.getText();
        if (!email.isEmpty() && !pattern.isEmpty()) {
            boolean matched = regexExpression.matchPatternInText(email, pattern);
            displayResult("Pattern '" + pattern + "' matched with text: " + matched);
        } else {
            displayResult("Please enter text and a pattern to match.");
        }
    }

    private void replacePattern() {
        String email = emailTextField.getText();
        String pattern = patternTextField.getText();
        String replacement = replacementTextField.getText();
        if (!email.isEmpty() && !pattern.isEmpty() && !replacement.isEmpty()) {
            String modifiedEmail = regexExpression.replacePatternInText(email, pattern, replacement);
            displayResult("Modified email address: " + modifiedEmail);
        } else {
            displayResult("Please enter text, a pattern to replace, and replacement text.");
        }
    }

    private void displayResult(String message) {
        resultTextArea.setText(message);
    }

    private void handleAdd(MouseEvent event) {
        String text = textInput.getText();
        if (!text.isEmpty()) {
            controller.addData(collectionComboBox.getValue(), text);
            refreshListView();
            textInput.clear();
        }
    }

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

    private void handleDelete(MouseEvent event) {
        String selectedItem = listView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            controller.deleteData(collectionComboBox.getValue(), selectedItem);
            refreshListView();
        }
    }

    private void handleSearch(MouseEvent event) {
        String regex = regexInput.getText();
        if (!regex.isEmpty()) {
            var matches = controller.searchData(collectionComboBox.getValue(), regex);
            resultArea.clear();
            matches.forEach(match -> resultArea.appendText(match + "\n"));
        }
    }

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

    public static void main(String[] args) {
        launch(args);
    }
}
