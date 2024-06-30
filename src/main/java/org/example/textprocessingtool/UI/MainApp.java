package org.example.textprocessingtool.UI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.textprocessingtool.regex.RegexExpression;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MainApp extends Application {

    // Collection data
    private List<String> arrayList = new ArrayList<>();
    private Set<String> hashSet = new HashSet<>();
    private Map<Integer, String> linkedHashMap = new LinkedHashMap<>();
    private Map<Integer, String> hashMap = new HashMap<>();
    private ListView<String> listView = new ListView<>();
    private TextArea resultArea = new TextArea();
    private Label currentCollectionLabel = new Label("Current Collection: ArrayList");

    private int nextMapKey = 1;

    private ComboBox<String> collectionComboBox;

    // Regex UI components
    private TextField emailTextField;
    private TextField patternTextField;
    private TextField replacementTextField;
    private TextArea regexResultTextArea;
    private RegexExpression regexExpression;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Text Processing Tool");

        // Initialize RegexExpressions instance
        regexExpression = new RegexExpression();

        // Tab for main data collection functionalities
        Tab mainTab = new Tab("Data Collection");
        mainTab.setContent(createMainLayout());

        // Tab for regex functionalities
        Tab regexTab = new Tab("Regex Operations");
        regexTab.setContent(createRegexLayout());

        // TabPane to hold both tabs
        TabPane tabPane = new TabPane(mainTab, regexTab);

        // Scene
        Scene scene = new Scene(tabPane, 400, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createMainLayout() {
        // Input fields
        TextField textInput = new TextField();
        textInput.setPromptText("Enter text");

        TextField regexInput = new TextField();
        regexInput.setPromptText("Enter regex pattern");

        // ComboBox for collection selection
        collectionComboBox = new ComboBox<>();
        collectionComboBox.getItems().addAll("ArrayList", "HashSet", "LinkedHashMap", "HashMap");
        collectionComboBox.setValue("ArrayList");

        // Buttons
        Button addButton = new Button("Add");
        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");
        Button searchButton = new Button("Search");

        // ListView to display data
        listView.getItems().addAll(arrayList);

        // TextArea to display search results
        resultArea.setEditable(false);

        // Event handlers
        addButton.setOnAction(e -> {
            String text = textInput.getText();
            if (!text.isEmpty()) {
                switch (collectionComboBox.getValue()) {
                    case "ArrayList":
                        arrayList.add(text);
                        break;
                    case "HashSet":
                        hashSet.add(text);
                        break;
                    case "LinkedHashMap":
                        linkedHashMap.put(nextMapKey++, text);
                        break;
                    case "HashMap":
                        hashMap.put(nextMapKey++, text);
                        break;
                }
                refreshListView();
                textInput.clear();
            }
        });

        updateButton.setOnAction(e -> {
            String selectedItem = listView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                String newText = textInput.getText();
                if (!newText.isEmpty()) {
                    switch (collectionComboBox.getValue()) {
                        case "ArrayList":
                            arrayList.set(arrayList.indexOf(selectedItem), newText);
                            break;
                        case "HashSet":
                            hashSet.remove(selectedItem);
                            hashSet.add(newText);
                            break;
                        case "LinkedHashMap":
                            for (Map.Entry<Integer, String> entry : linkedHashMap.entrySet()) {
                                if (entry.getValue().equals(selectedItem)) {
                                    linkedHashMap.put(entry.getKey(), newText);
                                    break;
                                }
                            }
                            break;
                        case "HashMap":
                            for (Map.Entry<Integer, String> entry : hashMap.entrySet()) {
                                if (entry.getValue().equals(selectedItem)) {
                                    hashMap.put(entry.getKey(), newText);
                                    break;
                                }
                            }
                            break;
                    }
                    refreshListView();
                    textInput.clear();
                }
            }
        });

        deleteButton.setOnAction(e -> {
            String selectedItem = listView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                switch (collectionComboBox.getValue()) {
                    case "ArrayList":
                        arrayList.remove(selectedItem);
                        break;
                    case "HashSet":
                        hashSet.remove(selectedItem);
                        break;
                    case "LinkedHashMap":
                        linkedHashMap.values().removeIf(value -> value.equals(selectedItem));
                        break;
                    case "HashMap":
                        hashMap.values().removeIf(value -> value.equals(selectedItem));
                        break;
                }
                refreshListView();
            }
        });

        searchButton.setOnAction(e -> {
            String regex = regexInput.getText();
            if (!regex.isEmpty()) {
                Pattern pattern = Pattern.compile(regex);
                List<String> matches;
                switch (collectionComboBox.getValue()) {
                    case "ArrayList":
                        matches = arrayList.stream()
                                .filter(data -> pattern.matcher(data).find())
                                .collect(Collectors.toList());
                        break;
                    case "HashSet":
                        matches = hashSet.stream()
                                .filter(data -> pattern.matcher(data).find())
                                .collect(Collectors.toList());
                        break;
                    case "LinkedHashMap":
                        matches = linkedHashMap.values().stream()
                                .filter(data -> pattern.matcher(data).find())
                                .collect(Collectors.toList());
                        break;
                    case "HashMap":
                        matches = hashMap.values().stream()
                                .filter(data -> pattern.matcher(data).find())
                                .collect(Collectors.toList());
                        break;
                    default:
                        matches = new ArrayList<>();
                        break;
                }
                resultArea.clear();
                matches.forEach(match -> resultArea.appendText(match + "\n"));
            }
        });

        collectionComboBox.setOnAction(e -> {
            String selectedCollection = collectionComboBox.getValue();
            currentCollectionLabel.setText("Current Collection: " + selectedCollection);
            refreshListView();
        });

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(
                new Label("Text Data"), textInput, addButton, updateButton, deleteButton,
                new Label("Regex Pattern"), regexInput, searchButton,
                currentCollectionLabel,
                new Label("Data List"), listView, new Label("Search Results"), resultArea,
                new Label("Switch Collection"), collectionComboBox
        );

        return layout;
    }

    private VBox createRegexLayout() {
        // UI Components
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

        regexResultTextArea = new TextArea();
        regexResultTextArea.setEditable(false);
        regexResultTextArea.setWrapText(true);
        regexResultTextArea.setPrefHeight(200);

        // Layout
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        vbox.getChildren().addAll(
                emailLabel, emailTextField,
                patternLabel, patternTextField,
                replacementLabel, replacementTextField,
                validateButton, searchButton, matchButton, replaceButton,
                regexResultTextArea
        );

        return vbox;
    }

    private void validateEmail() {
        String email = emailTextField.getText();
        if (!email.isEmpty()) {
            boolean isValid = regexExpression.isValidEmail(email);
            displayRegexResult("Email validity: " + isValid);
        } else {
            displayRegexResult("Please enter valid email address.");
        }
    }

    private void searchPattern() {
        String email = emailTextField.getText();
        String pattern = patternTextField.getText();
        if (!email.isEmpty() && !pattern.isEmpty()) {
            boolean found = regexExpression.searchPatternInText(email, pattern);
            displayRegexResult("Pattern found: " + found);
        } else {
            displayRegexResult("Please enter both email and pattern.");
        }
    }

    private void matchPattern() {
        String email = emailTextField.getText();
        String pattern = patternTextField.getText();
        if (!email.isEmpty() && !pattern.isEmpty()) {
            boolean matched = regexExpression.matchPatternInText(email, pattern);
            displayRegexResult("Pattern matched: " + matched);
        } else {
            displayRegexResult("Please enter both email and pattern.");
        }
    }

    private void replacePattern() {
        String email = emailTextField.getText();
        String pattern = patternTextField.getText();
        String replacement = replacementTextField.getText();
        if (!email.isEmpty() && !pattern.isEmpty() && !replacement.isEmpty()) {
            String result = regexExpression.replacePatternInText(email, pattern, replacement);
            displayRegexResult("Replacement result: " + result);
        } else {
            displayRegexResult("Please enter email, pattern, and replacement.");
        }
    }

    private void displayRegexResult(String result) {
        regexResultTextArea.setText(result);
    }

    private void refreshListView() {
        listView.getItems().clear();
        switch (collectionComboBox.getValue()) {
            case "ArrayList":
                listView.getItems().addAll(arrayList);
                break;
            case "HashSet":
                listView.getItems().addAll(hashSet);
                break;
            case "LinkedHashMap":
                listView.getItems().addAll(linkedHashMap.values());
                break;
            case "HashMap":
                listView.getItems().addAll(hashMap.values());
                break;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
