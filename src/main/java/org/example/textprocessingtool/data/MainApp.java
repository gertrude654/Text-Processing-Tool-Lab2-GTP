package org.example.textprocessingtool.data;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MainApp extends Application {

    // Default to ArrayList
    private Collection<String> dataCollection = new ArrayList<>();
    private Set<String> dataSet = new HashSet<>();
    private Map<Integer, String> dataMap = new LinkedHashMap<>();
    private Map<Integer, String> dataHashMap = new HashMap<>();
    private ListView<String> listView = new ListView<>();
    private TextArea resultArea = new TextArea();
    private Label currentCollectionLabel = new Label("Current Collection: ArrayList");

    private int nextMapKey = 1;
    private int nextHashMapKey = 1;

    private ComboBox<String> collectionComboBox; // Declaring the ComboBox as a class-level variable

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Text Processing Tool");

        // Input fields
        TextField textInput = new TextField();
        textInput.setPromptText("Enter text");

        TextField regexInput = new TextField();
        regexInput.setPromptText("Enter regex pattern");

        // ComboBox for collection selection
        collectionComboBox = new ComboBox<>(); // Initialize the ComboBox here
        collectionComboBox.getItems().addAll("ArrayList", "HashSet", "LinkedHashMap", "HashMap");
        collectionComboBox.setValue("ArrayList"); // Default selection

        // Buttons
        Button addButton = new Button("Add");
        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");
        Button searchButton = new Button("Search");

        // ListView to display data
        listView.getItems().addAll(dataCollection);

        // TextArea to display search results
        resultArea.setEditable(false);

        // Event handlers
        addButton.setOnAction(e -> {
            String text = textInput.getText();
            if (!text.isEmpty()) {
                switch (collectionComboBox.getValue()) {
                    case "ArrayList":
                        dataCollection.add(text);
                        break;
                    case "HashSet":
                        dataSet.add(text);
                        break;
                    case "LinkedHashMap":
                        dataMap.put(nextMapKey++, text);
                        break;
                    case "HashMap":
                        dataHashMap.put(nextHashMapKey++, text);
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
                            dataCollection.remove(selectedItem);
                            dataCollection.add(newText);
                            break;
                        case "HashSet":
                            dataSet.remove(selectedItem);
                            dataSet.add(newText);
                            break;
                        case "LinkedHashMap":
                            for (Map.Entry<Integer, String> entry : dataMap.entrySet()) {
                                if (entry.getValue().equals(selectedItem)) {
                                    dataMap.put(entry.getKey(), newText);
                                    break;
                                }
                            }
                            break;
                        case "HashMap":
                            for (Map.Entry<Integer, String> entry : dataHashMap.entrySet()) {
                                if (entry.getValue().equals(selectedItem)) {
                                    dataHashMap.put(entry.getKey(), newText);
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
                        dataCollection.remove(selectedItem);
                        break;
                    case "HashSet":
                        dataSet.remove(selectedItem);
                        break;
                    case "LinkedHashMap":
                        Iterator<Map.Entry<Integer, String>> iterator = dataMap.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry<Integer, String> entry = iterator.next();
                            if (entry.getValue().equals(selectedItem)) {
                                iterator.remove();
                                break;
                            }
                        }
                        break;
                    case "HashMap":
                        Iterator<Map.Entry<Integer, String>> iteratorHashMap = dataHashMap.entrySet().iterator();
                        while (iteratorHashMap.hasNext()) {
                            Map.Entry<Integer, String> entry = iteratorHashMap.next();
                            if (entry.getValue().equals(selectedItem)) {
                                iteratorHashMap.remove();
                                break;
                            }
                        }
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
                        matches = dataCollection.stream()
                                .filter(data -> pattern.matcher(data).find())
                                .collect(Collectors.toList());
                        break;
                    case "HashSet":
                        matches = dataSet.stream()
                                .filter(data -> pattern.matcher(data).find())
                                .collect(Collectors.toList());
                        break;
                    case "LinkedHashMap":
                        matches = dataMap.values().stream()
                                .filter(data -> pattern.matcher(data).find())
                                .collect(Collectors.toList());
                        break;
                    case "HashMap":
                        matches = dataHashMap.values().stream()
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
            switchCollection(selectedCollection);
        });

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new javafx.geometry.Insets(10));
        layout.getChildren().addAll(
                new Label("Text Data"), textInput, addButton, updateButton, deleteButton,
                new Label("Regex Pattern"), regexInput, searchButton,
                currentCollectionLabel,
                new Label("Data List"), listView, new Label("Search Results"), resultArea,
                new Label("Switch Collection"), collectionComboBox
        );

        // Scene
        Scene scene = new Scene(layout, 400, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void refreshListView() {
        listView.getItems().clear();
        switch (collectionComboBox.getValue()) {
            case "ArrayList":
                listView.getItems().addAll(dataCollection);
                break;
            case "HashSet":
                listView.getItems().addAll(dataSet);
                break;
            case "LinkedHashMap":
                for (Integer key : dataMap.keySet()) {
                    String value = dataMap.get(key);
                    listView.getItems().add(key + ": " + value);
                }
                break;
            case "HashMap":
                for (Integer key : dataHashMap.keySet()) {
                    String value = dataHashMap.get(key);
                    listView.getItems().add(key + ": " + value);
                }
                break;
        }
    }

    private void switchCollection(String collectionType) {
        switch (collectionType) {
            case "ArrayList":
                // Convert current collection to ArrayList
                dataCollection = new ArrayList<>(dataCollection);
                break;
            case "HashSet":
                // Convert current collection to HashSet
                dataSet = new HashSet<>(dataCollection);
                break;
            case "LinkedHashMap":
                // Convert current collection to LinkedHashMap
                dataMap = new LinkedHashMap<>(dataMap);
                break;
            case "HashMap":
                // Convert current collection to HashMap
                dataHashMap = new HashMap<>(dataHashMap);
                break;
        }
        refreshListView();
        updateCollectionLabel(collectionType);
    }

    private void updateCollectionLabel(String collectionType) {
        currentCollectionLabel.setText("Current Collection: " + collectionType);
    }

    public static void main(String[] args) {
        launch(args);
    }
}



