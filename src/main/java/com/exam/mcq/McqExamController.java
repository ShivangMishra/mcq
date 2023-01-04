package com.exam.mcq;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class McqExamController {

    @FXML
    private Label quesTextLabel;

    @FXML
    private Label quesNumLabel;

    @FXML
    private RadioButton option1;

    @FXML
    private RadioButton option2;

    @FXML
    private RadioButton option3;

    @FXML
    private RadioButton option4;

    @FXML
    private Button saveButton;

    private int currentQuesNum;

    private List<Question> questions;

    private List<String> responses;

    @FXML
    private AnchorPane testPane;
    @FXML
    private AnchorPane resultPane;

    @FXML
    private VBox answerSheet;

    @FXML
    private Label scoreLabel;

    @FXML
    private Label messageLabel;

    public void start() {
        testPane.setVisible(true);
        resultPane.setVisible(false);
        ToggleGroup quesOptions = new ToggleGroup();
        option1.setToggleGroup(quesOptions);
        option2.setToggleGroup(quesOptions);
        option3.setToggleGroup(quesOptions);
        option4.setToggleGroup(quesOptions);

        option1.setMinWidth(Region.USE_PREF_SIZE);
        option2.setMinWidth(Region.USE_PREF_SIZE);
        option3.setMinWidth(Region.USE_PREF_SIZE);
        option4.setMinWidth(Region.USE_PREF_SIZE);

        responses = new ArrayList<>();
        loadAllQuestions();
        showQuestion(1);
    }

    private void showQuestion(int num) {
        currentQuesNum = num;
        Question question = questions.get(num - 1);
        quesTextLabel.setText(question.text());
        quesNumLabel.setText(currentQuesNum + ". ");
        int nOptions = question.options().size();
        List<String> options = question.options();
        if (nOptions == 4) {
            option1.setVisible(true);
            option2.setVisible(true);
            option3.setVisible(true);
            option4.setVisible(true);

            option1.setSelected(false);
            option2.setSelected(false);
            option3.setSelected(false);
            option4.setSelected(false);

            option1.setText(options.get(0));
            option2.setText(options.get(1));
            option3.setText(options.get(2));
            option4.setText(options.get(3));
        } else if (nOptions == 2) {
            option1.setVisible(true);
            option2.setVisible(true);
            option3.setVisible(false);
            option4.setVisible(false);

            option1.setText(options.get(0));
            option2.setText(options.get(1));

            option3.setText("");
            option4.setText("");
        } else {
            System.out.println("UNSUPPORTED NUM OPTIONS :" + nOptions);
            return;
        }
        option1.setText(question.options().get(0));
    }

    private void loadAllQuestions() {
        questions = new ArrayList<>();
        try (Scanner quesSc = new Scanner(Objects.requireNonNull(McqExamApplication.class.getResource("questions")).openStream());
             Scanner ansSc = new Scanner(Objects.requireNonNull(McqExamApplication.class.getResource("answers")).openStream())) {
            while (quesSc.hasNextLine()) {
                String line = quesSc.nextLine();
                while (line.trim().isEmpty())
                    line = quesSc.nextLine();

                String quesText = line;
                List<String> options = new ArrayList<>();
                for (int i = 1; i <= 4; i++) {
                    if (quesSc.hasNextLine()) {
                        String option = quesSc.nextLine();
                        if (option.trim().isEmpty()) {
                            break;
                        }
                        options.add(option);
                    }
                }
                Question question = new Question(quesText, options, ansSc.nextLine());
                questions.add(question);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void saveResponse() {
        String response;
        if (option1.isSelected()) {
            response = option1.getText();
        } else if (option2.isSelected()) {
            response = option2.getText();
        } else if (option3.isSelected()) {
            response = option3.getText();
        } else if (option4.isSelected()) {
            response = option4.getText();
        } else {
            return;
        }
        responses.add(response);
        if (currentQuesNum == questions.size()) {
            showResult();
        } else {
            showQuestion(++currentQuesNum);
        }
    }

    private void showResult() {
        int nTotal = questions.size();
        int nCorrect = 0;
        for (int i = 0; i < nTotal; i++) {
            String answer = questions.get(i).correctOption();
            String response = responses.get(i);
            if (response.equalsIgnoreCase(answer)) {
                nCorrect++;
            }
        }

        double percentage = nCorrect * 100.0 / nTotal;

        String message = percentage > 75 ? "Good Job Yousuf" : "Try Again Yousuf!";
        messageLabel.setText(message);
        scoreLabel.setText(String.format("You scored %d/%d", nCorrect, nTotal) + " (" + percentage + "%)");

        List<Label> labels = new ArrayList<>();

        for (int i = 0; i < nTotal; i++) {
            Question question = questions.get(i);
            String labelText = "Q " + (i + 1) + ". " + question.text() + "\n";
            labelText += "Correct Option: " + question.correctOption() + "\n";
            labelText += "Your response: " + responses.get(i);
            Label label = new Label(labelText);
//            label.setTextFill(Color.WHITE);
            label.setMinHeight(Region.USE_PREF_SIZE);
            label.setWrapText(true);
            label.prefWidthProperty().bind(answerSheet.prefWidthProperty());

            if (question.correctOption().equalsIgnoreCase(responses.get(i))) {
                label.setTextFill(Color.GREEN);
            } else {
                label.setTextFill(Color.RED);
            }
            labels.add(label);
        }

        answerSheet.getChildren().addAll(labels);

        testPane.setVisible(false);
        resultPane.setVisible(true);
    }
}