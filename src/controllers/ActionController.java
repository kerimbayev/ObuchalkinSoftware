package controllers;

import entities.EducationalMaterialEntity;
import entities.TestAnswerEntity;
import entities.TestEntity;
import entities.TestQuestionEntity;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.EducationalMaterialModel;
import models.TestModel;

import java.net.URL;
import java.util.ResourceBundle;

public class ActionController extends BaseController implements Initializable {
    public static EducationalMaterialEntity choosedMaterial = null;
    public static TestEntity test = null;
    public static TestQuestionEntity testQuestion = null;
    public static TestAnswerEntity testAnswer = null;
    public static boolean isRename = false;
    public static boolean addTest = false;
    public static boolean addQuestion = false;
    public static boolean addAnswer = false;

    public TextField textField;
    public Label label;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (ActionController.isRename) {
            if (choosedMaterial != null) {
                label.setText("Наименование:");
                textField.setText(choosedMaterial.getName());
            } else if (test != null) {
                label.setText("Наименование:");
                textField.setText(test.getName());
            } else if (testQuestion != null) {
                label.setText("Вопрос:");
                textField.setText(testQuestion.getQuestion());
            } else if (testAnswer != null) {
                label.setText("Ответ:");
                textField.setText(testAnswer.getAnswer());
            }
        } else if (ActionController.addTest) {
            label.setText("Наименование:");
        } else if (ActionController.addQuestion) {
            label.setText("Вопрос:");
        } else if (ActionController.addAnswer) {
            label.setText("Ответ:");
        }
    }

    public void action() {
        if (!textField.getText().isEmpty()) {
            if (ActionController.isRename) {
                if (ActionController.choosedMaterial != null) {
                    ActionController.choosedMaterial.setName(textField.getText());
                    EducationalMaterialModel.getInstance().updateEducationalMaterial(ActionController.choosedMaterial);
                } else if (ActionController.test != null) {
                    ActionController.test.setName(textField.getText());
                    TestModel.getInstance().updateTest(ActionController.test);
                } else if (ActionController.testQuestion != null) {
                    ActionController.testQuestion.setQuestion(textField.getText());
                    TestModel.getInstance().updateTestQuestion(ActionController.testQuestion);
                } else if (ActionController.testAnswer != null) {
                    ActionController.testAnswer.setAnswer(textField.getText());
                    TestModel.getInstance().updateTestAnswer(ActionController.testAnswer);
                }
            } else if (ActionController.addTest) {
                TestModel.getInstance().addTest(new TestEntity(0, textField.getText()));
            } else if (ActionController.addQuestion) {
                TestModel.getInstance().addTestQuestion(new TestQuestionEntity(0, ActionController.test, textField.getText()));
            } else if (ActionController.addAnswer) {
                TestModel.getInstance().addTestAnswer(new TestAnswerEntity(0, ActionController.testQuestion, textField.getText(), "n"));
            }
            getStage().close();
        } else {
            newAlert(Alert.AlertType.ERROR, ERROR, ERROR_EMPTY_FIELD);
        }
    }

    public static void clear() {
       ActionController.choosedMaterial = null;
       ActionController.test = null;
       ActionController.testQuestion = null;
       ActionController.testAnswer = null;
       ActionController.isRename = false;
       ActionController.addTest = false;
       ActionController.addQuestion = false;
       ActionController.addAnswer = false;
    }
}
