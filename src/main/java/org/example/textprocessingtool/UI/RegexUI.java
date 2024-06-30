package org.example.textprocessingtool.UI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.textprocessingtool.regex.RegexExpression;

public class RegexUI extends Application {

    private TextField emailTextField;
    private TextField patternTextField;
    private TextField replacementTextField;
    private TextArea resultTextArea;
    private RegexExpression regexExpression; // Instance for handling regex operations

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Text processing tool");

        // Initialize RegexExpressions instance
        regexExpression = new RegexExpression();

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

        resultTextArea = new TextArea();
        resultTextArea.setEditable(false);
        resultTextArea.setWrapText(true);
        resultTextArea.setPrefHeight(200);

        // Layout
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        vbox.getChildren().addAll(
                emailLabel, emailTextField,
                patternLabel, patternTextField,
                replacementLabel, replacementTextField,
                validateButton, searchButton, matchButton, replaceButton,
                resultTextArea
        );

        // Scene
        Scene scene = new Scene(vbox, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void validateEmail() {
        String email = emailTextField.getText();
        if (!email.isEmpty()) {
            boolean isValid = regexExpression.isValidEmail(email);
            displayResult("Email validity: " + isValid);
        } else {
            displayResult("Please enter valid email address.");
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
            displayResult("Please enter an text and a pattern to match.");
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
            displayResult("Please enter an text, a pattern to replace, and replacement text.");
        }
    }

    private void displayResult(String message) {
        resultTextArea.setText(message );
    }

    public static void main(String[] args) {
        launch(args);
    }
}
