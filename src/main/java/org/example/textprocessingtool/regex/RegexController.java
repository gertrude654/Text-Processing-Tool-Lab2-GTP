package org.example.textprocessingtool.regex;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class RegexController {


    @FXML
    private TextField emailTextField;

    @FXML
    private TextField patternTextField;

    @FXML
    private TextField replacementTextField;

    @FXML
    private TextArea resultTextArea;

    private RegexExpression regexExpression;

    @FXML
    public void initialize() {
        regexExpression = new RegexExpression();
    }

    @FXML
    private void validateEmail() {
        String email = emailTextField.getText();
        if (!email.isEmpty()) {
            boolean isValid = regexExpression.isValidEmail(email);
            displayResult("Email validity: " + isValid);
        } else {
            displayResult("Please enter valid email address.");
        }
    }

    @FXML
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

    @FXML
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

    @FXML
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
}
