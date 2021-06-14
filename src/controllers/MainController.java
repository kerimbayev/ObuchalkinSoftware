package controllers;

import entities.EducationalMaterialEntity;
import entities.TestAnswerEntity;
import entities.TestEntity;
import entities.TestQuestionEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.TestModel;
import models.thread.CopyFile;
import models.EducationalMaterialModel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController extends BaseController implements Initializable {
    public TextArea note;
    public ListView<EducationalMaterialEntity> textList;  //Списки продакшн
    public ListView<EducationalMaterialEntity> videoList; //
    public ListView<EducationalMaterialEntity> audioList; //
    public ListView<TestEntity> testList;                 //
    public Tab text_tab;  //Табы
    public Tab video_tab; //
    public Tab audio_tab; //
    public Tab test_tab;  //
    public Button download;    //Кнопки управления
    public Button open;        //
    public Button update;      //
    public Button upload;      //
    public Button rename;      //
    public Button delete;      //
    public Button production;  //
    public Button development; //
    public VBox testDevelopment;    //режим разработки тестов
    public ListView<TestEntity> testListDev;              //разработка тестов, вопросов и ответов
    public ListView<TestQuestionEntity> questionList;     //
    public TableView<TestAnswerEntity> answerTable;       //
    public TableColumn<TestAnswerEntity, String> answer;  //
    public TableColumn<TestAnswerEntity, String> isRight; //

    public static Boolean isTeacher = null; //Вошел преподаватель или студент

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Stage stage = getScene("views/LoginView.fxml");
        LoginController.loginStage = stage;
        stage.showAndWait();
        if (isTeacher == null) {
            System.exit(0);
        }
        answer.setCellValueFactory(new PropertyValueFactory<>("answer"));
        isRight.setCellValueFactory(new PropertyValueFactory<>("isRight"));
        updateListViews();
        textList.setOnMouseClicked(event -> {
            if (!textList.getSelectionModel().isEmpty()) {
                if (event.getClickCount() == 2) {
                    openMaterialAction();
                }
            }
        });
        videoList.setOnMouseClicked(event -> {
            if (!videoList.getSelectionModel().isEmpty()) {
                if (event.getClickCount() == 2) {
                    openMaterialAction();
                }
            }
        });
        audioList.setOnMouseClicked(event -> {
            if (!audioList.getSelectionModel().isEmpty()) {
                if (event.getClickCount() == 2) {
                    openMaterialAction();
                }
            }
        });
        testListDev.setOnMouseClicked(event -> {
            if (!testListDev.getSelectionModel().isEmpty()) {
                TestEntity test = testListDev.getSelectionModel().getSelectedItem();
                answerTable.getItems().clear();
                questionList.setItems(TestModel.getInstance().getTestQuestionsByTest(test));
            }
        });
        questionList.setOnMouseClicked(event -> {
            if (!questionList.getSelectionModel().isEmpty()) {
                TestQuestionEntity testQuestion = questionList.getSelectionModel().getSelectedItem();
                answerTable.setItems(TestModel.getInstance().getTestAnswersByTestQuestion(testQuestion));
            }
        });
        testProduction();
        if(!isTeacher) {
            update.setDisable(true);
            upload.setDisable(true);
            rename.setDisable(true);
            delete.setDisable(true);
            production.setDisable(true);
            development.setDisable(true);
        }
    }

    public void updateListViews() {
        textList.setItems(EducationalMaterialModel.getInstance().getTextEducationalMaterials());
        videoList.setItems(EducationalMaterialModel.getInstance().getVideoEducationalMaterials());
        audioList.setItems(EducationalMaterialModel.getInstance().getAudioEducationalMaterials());
        testList.setItems(TestModel.getInstance().getTests());
        testListDev.setItems(TestModel.getInstance().getTests());
        questionList.setItems(FXCollections.observableArrayList());
        answerTable.setItems(FXCollections.observableArrayList());
    }

    public void openMaterial(EducationalMaterialEntity educationalMaterial) {
        if (Desktop.isDesktopSupported()) {
            try {
                String file = getClass().getClassLoader().getResource("resources").getPath() +
                        File.separator + educationalMaterial.getType() +
                        File.separator + educationalMaterial.getPath();
                File myFile = new File(file);
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                newAlert(Alert.AlertType.ERROR, ERROR, ERROR_NO_PROGRAM_FOR_THIS_FILE);
            }
        }
    }

    public void downloadNoteAction() {
        if (note.getText().isEmpty()) {
            newAlert(Alert.AlertType.INFORMATION, INFORMATION, INFORMATION_NOTE_IS_EMPTY);
        } else {
            Optional<ButtonType> option = newAlert(Alert.AlertType.CONFIRMATION, CONFIRMATION, CONFIRMATION_DOWNLOAD_NOTE);
            if (option.isPresent()) {
                if (option.get() == ButtonType.OK) {
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Выберите папку");
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    fileChooser.setAcceptAllFileFilterUsed(false);
                    if (fileChooser.showOpenDialog(new JButton()) == JFileChooser.APPROVE_OPTION) {
                        String path = fileChooser.getSelectedFile().getPath();
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
                        try {
                            FileWriter fw = new FileWriter(path + File.separator + "note_" + formatter.format(new Date()) + ".txt");
                            fw.write(note.getText());
                            fw.close();
                            newAlert(Alert.AlertType.INFORMATION, INFORMATION, INFORMATION_FILE_SUCCESSFULLY_SAVED);
                        } catch (IOException exception) {
                            newAlert(Alert.AlertType.ERROR, ERROR, ERROR_FILE_WAS_NOT_SAVED);
                        }
                    }
                }
            }
        }
    }

    public void clearNoteAction() {
        Optional<ButtonType> option = newAlert(Alert.AlertType.CONFIRMATION, CONFIRMATION, CONFIRMATION_CLEAR_NOTE);
        if (option.isPresent())
            if (option.get() == ButtonType.OK)
                note.clear();
    }

    public void downloadMaterialAction() {
        if (text_tab.isSelected()) {
            if (!textList.getSelectionModel().isEmpty()) {
                downloadFileChooser(textList.getSelectionModel().getSelectedItem());
            } else {
                newAlert(Alert.AlertType.INFORMATION, INFORMATION, INFORMATION_FILE_NOT_CHOOSED);
            }
        } else if (video_tab.isSelected()) {
            if (!videoList.getSelectionModel().isEmpty()) {
                downloadFileChooser(videoList.getSelectionModel().getSelectedItem());
            } else {
                newAlert(Alert.AlertType.INFORMATION, INFORMATION, INFORMATION_FILE_NOT_CHOOSED);
            }
        } else if (audio_tab.isSelected()) {
            if (!audioList.getSelectionModel().isEmpty()) {
                downloadFileChooser(audioList.getSelectionModel().getSelectedItem());
            } else {
                newAlert(Alert.AlertType.INFORMATION, INFORMATION, INFORMATION_FILE_NOT_CHOOSED);
            }
        } else {
            newAlert(Alert.AlertType.INFORMATION, INFORMATION, INFORMATION_CHOOSE_EDUCATIONAL_MATERIAL);
        }
    }

    public void deleteMaterialAction() {
        if (text_tab.isSelected()) {
            if (textList.getSelectionModel().isEmpty()) {
                newAlert(Alert.AlertType.INFORMATION, INFORMATION, INFORMATION_FILE_NOT_CHOOSED);
            } else {
                Optional<ButtonType> option = newAlert(Alert.AlertType.CONFIRMATION, CONFIRMATION, CONFIRMATION_DELETE_FILE);
                if (option.isPresent()) {
                    if (option.get() == ButtonType.OK) {
                        EducationalMaterialEntity selectedMaterial = textList.getSelectionModel().getSelectedItem();
                        File file = new File(getClass().getClassLoader().getResource("resources").getPath() + File.separator + selectedMaterial.getType() + File.separator + selectedMaterial.getPath());
                        if (file.delete()) {
                            EducationalMaterialModel.getInstance().deleteEducationalMaterial(selectedMaterial);
                            updateListViews();
                        } else {
                            newAlert(Alert.AlertType.ERROR, ERROR, ERROR_SOMETHING_GOES_WRONG);
                        }
                    }
                }
            }
        } else if (video_tab.isSelected()) {
            if (videoList.getSelectionModel().isEmpty()) {
                newAlert(Alert.AlertType.INFORMATION, INFORMATION, INFORMATION_FILE_NOT_CHOOSED);
            } else {
                Optional<ButtonType> option = newAlert(Alert.AlertType.CONFIRMATION, CONFIRMATION, CONFIRMATION_DELETE_FILE);
                if (option.isPresent()) {
                    if (option.get() == ButtonType.OK) {
                        EducationalMaterialEntity selectedMaterial = videoList.getSelectionModel().getSelectedItem();
                        File file = new File(getClass().getClassLoader().getResource("resources").getPath() + File.separator + selectedMaterial.getType() + File.separator + selectedMaterial.getPath());
                        if (file.delete()) {
                            EducationalMaterialModel.getInstance().deleteEducationalMaterial(selectedMaterial);
                            updateListViews();
                        } else {
                            newAlert(Alert.AlertType.ERROR, ERROR, ERROR_SOMETHING_GOES_WRONG);
                        }
                    }
                }
            }
        } else if (audio_tab.isSelected()) {
            if (audioList.getSelectionModel().isEmpty()) {
                newAlert(Alert.AlertType.INFORMATION, INFORMATION, INFORMATION_FILE_NOT_CHOOSED);
            } else {
                Optional<ButtonType> option = newAlert(Alert.AlertType.CONFIRMATION, CONFIRMATION, CONFIRMATION_DELETE_FILE);
                if (option.isPresent()) {
                    if (option.get() == ButtonType.OK) {
                        EducationalMaterialEntity selectedMaterial = audioList.getSelectionModel().getSelectedItem();
                        File file = new File(getClass().getClassLoader().getResource("resources").getPath() + File.separator + selectedMaterial.getType() + File.separator + selectedMaterial.getPath());
                        if (file.delete()) {
                            EducationalMaterialModel.getInstance().deleteEducationalMaterial(selectedMaterial);
                            updateListViews();
                        } else {
                            newAlert(Alert.AlertType.ERROR, ERROR, ERROR_SOMETHING_GOES_WRONG);
                        }
                    }
                }
            }
        } else {
            newAlert(Alert.AlertType.INFORMATION, INFORMATION, INFORMATION_CHOOSE_EDUCATIONAL_MATERIAL);
        }
    }

    public void renameMaterialAction() {
        if (text_tab.isSelected()) {
            if (textList.getSelectionModel().isEmpty()) {
                newAlert(Alert.AlertType.INFORMATION, INFORMATION, INFORMATION_FILE_NOT_CHOOSED);
            } else {
                ActionController.isRename = true;
                ActionController.choosedMaterial = textList.getSelectionModel().getSelectedItem();
                Stage stage = getScene("views/ActionView.fxml");
                stage.showAndWait();
                ActionController.clear();
                updateListViews();
            }
        } else if (video_tab.isSelected()) {
            if (videoList.getSelectionModel().isEmpty()) {
                newAlert(Alert.AlertType.INFORMATION, INFORMATION, INFORMATION_FILE_NOT_CHOOSED);
            } else {
                ActionController.isRename = true;
                ActionController.choosedMaterial = videoList.getSelectionModel().getSelectedItem();
                Stage stage = getScene("views/ActionView.fxml");
                stage.showAndWait();
                ActionController.clear();
                updateListViews();
            }
        } else if (audio_tab.isSelected()) {
            if (audioList.getSelectionModel().isEmpty()) {
                newAlert(Alert.AlertType.INFORMATION, INFORMATION, INFORMATION_FILE_NOT_CHOOSED);
            } else {
                ActionController.isRename = true;
                ActionController.choosedMaterial = audioList.getSelectionModel().getSelectedItem();
                Stage stage = getScene("views/ActionView.fxml");
                stage.showAndWait();
                ActionController.clear();
                updateListViews();
            }
        } else {
            newAlert(Alert.AlertType.INFORMATION, INFORMATION, INFORMATION_CHOOSE_EDUCATIONAL_MATERIAL);
        }
    }

    public void uploadMaterialAction() {
        if (text_tab.isSelected()) {
            uploadFileChooser(EducationalMaterialModel.EDUCATIONAL_MATERIAL_TYPE_TEXT);
        } else if (video_tab.isSelected()) {
            uploadFileChooser(EducationalMaterialModel.EDUCATIONAL_MATERIAL_TYPE_VIDEO);
        } else if (audio_tab.isSelected()) {
            uploadFileChooser(EducationalMaterialModel.EDUCATIONAL_MATERIAL_TYPE_AUDIO);
        } else {
            newAlert(Alert.AlertType.INFORMATION, INFORMATION, INFORMATION_CHOOSE_EDUCATIONAL_MATERIAL);
        }
    }

    public void openMaterialAction() {
        if (text_tab.isSelected()) {
            if (textList.getSelectionModel().isEmpty()) {
                newAlert(Alert.AlertType.INFORMATION, INFORMATION, INFORMATION_FILE_NOT_CHOOSED);
            } else {
                openMaterial(textList.getSelectionModel().getSelectedItem());
            }
        } else if (video_tab.isSelected()) {
            if (videoList.getSelectionModel().isEmpty()) {
                newAlert(Alert.AlertType.INFORMATION, INFORMATION, INFORMATION_FILE_NOT_CHOOSED);
            } else {
                openMaterial(videoList.getSelectionModel().getSelectedItem());
            }
        } else if (audio_tab.isSelected()) {
            if (audioList.getSelectionModel().isEmpty()) {
                newAlert(Alert.AlertType.INFORMATION, INFORMATION, INFORMATION_FILE_NOT_CHOOSED);
            } else {
                openMaterial(audioList.getSelectionModel().getSelectedItem());
            }
        } else {
            newAlert(Alert.AlertType.INFORMATION, INFORMATION, INFORMATION_CHOOSE_EDUCATIONAL_MATERIAL);
        }
    }

    public void updateMaterialAction() {
        updateListViews();
    }

    public void testAddAction() {
        ActionController.addTest = true;
        Stage stage = getScene("views/ActionView.fxml");
        stage.showAndWait();
        ActionController.clear();
        testList.setItems(TestModel.getInstance().getTests());
        testListDev.setItems(TestModel.getInstance().getTests());
        if (!questionList.getItems().isEmpty()) questionList.getItems().clear();
        if (!answerTable.getItems().isEmpty()) answerTable.getItems().clear();
    }

    public void testRenameAction() {
        if (testListDev.getSelectionModel().isEmpty()) {
            newAlert(Alert.AlertType.INFORMATION, INFORMATION, INFORMATION_TEST_NOT_CHOOSED);
        } else {
            ActionController.isRename = true;
            ActionController.test = testListDev.getSelectionModel().getSelectedItem();
            Stage stage = getScene("views/ActionView.fxml");
            stage.showAndWait();
            ActionController.clear();
            testList.setItems(TestModel.getInstance().getTests());
            testListDev.setItems(TestModel.getInstance().getTests());
            if (!questionList.getItems().isEmpty()) questionList.getItems().clear();
            if (!answerTable.getItems().isEmpty()) answerTable.getItems().clear();
        }
    }

    public void testDeleteAction() {
        if (testListDev.getSelectionModel().isEmpty()) {
            newAlert(Alert.AlertType.INFORMATION, INFORMATION, INFORMATION_TEST_NOT_CHOOSED);
        } else {
            Optional<ButtonType> option = newAlert(Alert.AlertType.CONFIRMATION, CONFIRMATION, CONFIRMATION_DELETE_TEST);
            if (option.isPresent()) {
                if (option.get() == ButtonType.OK) {
                    TestEntity test = testListDev.getSelectionModel().getSelectedItem();
                    TestModel.getInstance().getTestQuestionsByTest(test).forEach(question -> {
                        ObservableList<TestAnswerEntity> answers = TestModel.getInstance().getTestAnswersByTestQuestion(question);
                        answers.forEach(answer -> {
                            TestModel.getInstance().deleteTestAnswer(answer);
                        });
                        TestModel.getInstance().deleteTestQuestion(question);
                    });
                    TestModel.getInstance().deleteTest(test);
                    testList.setItems(TestModel.getInstance().getTests());
                    testListDev.setItems(TestModel.getInstance().getTests());
                    if (!questionList.getItems().isEmpty()) questionList.getItems().clear();
                    if (!answerTable.getItems().isEmpty()) answerTable.getItems().clear();
                }
            }
        }
    }

    public void questionAddAction() {
        if (testListDev.getSelectionModel().isEmpty()) {
            newAlert(Alert.AlertType.INFORMATION, INFORMATION, INFORMATION_TEST_NOT_CHOOSED);
        } else {
            TestEntity test = testListDev.getSelectionModel().getSelectedItem();
            ActionController.addQuestion = true;
            ActionController.test = test;
            Stage stage = getScene("views/ActionView.fxml");
            stage.showAndWait();
            ActionController.clear();
            questionList.setItems(TestModel.getInstance().getTestQuestionsByTest(test));
            if (!answerTable.getItems().isEmpty()) answerTable.getItems().clear();
        }
    }

    public void questionRenameAction() {
        if (questionList.getSelectionModel().isEmpty()) {
            newAlert(Alert.AlertType.INFORMATION, INFORMATION, INFORMATION_QUESTION_NOT_CHOOSED);
        } else {
            TestQuestionEntity question = questionList.getSelectionModel().getSelectedItem();
            ActionController.isRename = true;
            ActionController.testQuestion = question;
            Stage stage = getScene("views/ActionView.fxml");
            stage.showAndWait();
            ActionController.clear();
            questionList.setItems(TestModel.getInstance().getTestQuestionsByTest(question.getTest()));
            if (!answerTable.getItems().isEmpty()) answerTable.getItems().clear();
        }
    }

    public void questionDeleteAction() {
        if (questionList.getSelectionModel().isEmpty()) {
            newAlert(Alert.AlertType.INFORMATION, INFORMATION, INFORMATION_QUESTION_NOT_CHOOSED);
        } else {
            Optional<ButtonType> option = newAlert(Alert.AlertType.CONFIRMATION, CONFIRMATION, CONFIRMATION_DELETE_QUESTION);
            if (option.isPresent()) {
                TestQuestionEntity question = questionList.getSelectionModel().getSelectedItem();
                ObservableList<TestAnswerEntity> answers = TestModel.getInstance().getTestAnswersByTestQuestion(question);
                answers.forEach(answer -> {
                    TestModel.getInstance().deleteTestAnswer(answer);
                });
                TestModel.getInstance().deleteTestQuestion(question);
                questionList.setItems(TestModel.getInstance().getTestQuestionsByTest(question.getTest()));
                if (!answerTable.getItems().isEmpty()) answerTable.getItems().clear();
            }
        }
    }

    public void answerAddAction() {
        if (questionList.getSelectionModel().isEmpty()) {
            newAlert(Alert.AlertType.INFORMATION, INFORMATION, INFORMATION_QUESTION_NOT_CHOOSED);
        } else {
            TestQuestionEntity testQuestion = questionList.getSelectionModel().getSelectedItem();
            ActionController.addAnswer = true;
            ActionController.testQuestion = testQuestion;
            Stage stage = getScene("views/ActionView.fxml");
            stage.showAndWait();
            ActionController.clear();
            answerTable.setItems(TestModel.getInstance().getTestAnswersByTestQuestion(questionList.getSelectionModel().getSelectedItem()));
        }
    }

    public void answerRenameAction() {
        if (answerTable.getSelectionModel().isEmpty()) {
            newAlert(Alert.AlertType.INFORMATION, INFORMATION, INFORMATION_ANSWER_NOT_CHOOSED);
        } else {
            ActionController.isRename = true;
            ActionController.testAnswer = answerTable.getSelectionModel().getSelectedItem();
            Stage stage = getScene("views/ActionView.fxml");
            stage.showAndWait();
            ActionController.clear();
            answerTable.setItems(TestModel.getInstance().getTestAnswersByTestQuestion(questionList.getSelectionModel().getSelectedItem()));
        }
    }

    public void answerRightWrongAction() {
        if (answerTable.getSelectionModel().isEmpty()) {
            newAlert(Alert.AlertType.INFORMATION, INFORMATION, INFORMATION_ANSWER_NOT_CHOOSED);
        } else {
            if (questionList.getSelectionModel().isEmpty()) {
                newAlert(Alert.AlertType.INFORMATION, INFORMATION, INFORMATION_QUESTION_NOT_CHOOSED);
            } else {
                TestAnswerEntity testAnswer = answerTable.getSelectionModel().getSelectedItem();
                testAnswer.setRight(!testAnswer.isRight());
                TestModel.getInstance().updateTestAnswer(testAnswer);
                answerTable.setItems(TestModel.getInstance().getTestAnswersByTestQuestion(questionList.getSelectionModel().getSelectedItem()));
            }
        }
    }

    public void answerDeleteAction() {
        if (answerTable.getSelectionModel().isEmpty()) {
            newAlert(Alert.AlertType.INFORMATION, INFORMATION, INFORMATION_ANSWER_NOT_CHOOSED);
        } else {
            if (questionList.getSelectionModel().isEmpty()) {
                newAlert(Alert.AlertType.INFORMATION, INFORMATION, INFORMATION_QUESTION_NOT_CHOOSED);
            } else {
                Optional<ButtonType> option = newAlert(Alert.AlertType.CONFIRMATION, CONFIRMATION, CONFIRMATION_DELETE_ANSWER);
                if (option.isPresent()) {
                    TestAnswerEntity answer = answerTable.getSelectionModel().getSelectedItem();
                    TestModel.getInstance().deleteTestAnswer(answer);
                    answerTable.setItems(TestModel.getInstance().getTestAnswersByTestQuestion(questionList.getSelectionModel().getSelectedItem()));
                }
            }
        }
    }

    public void testProduction() {
        testList.setVisible(true);
        testDevelopment.setVisible(false);
    }

    public void testDevelopment() {
        testList.setVisible(false);
        testDevelopment.setVisible(true);
    }

    private void uploadFileChooser(String type)  {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String path = getClass().getClassLoader().getResource("resources").getPath() + File.separator + type;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Выберите файл");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (type.equals(EducationalMaterialModel.EDUCATIONAL_MATERIAL_TYPE_TEXT)) {
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PDF-файл", "pdf"));
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Office Word файл", "docx", "doc"));
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Office PowerPoint файл", "pptx", "ppt"));
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Office Excel файл", "xlsx", "xls"));
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("TXT-файл", "txt"));
        }
        if (type.equals(EducationalMaterialModel.EDUCATIONAL_MATERIAL_TYPE_VIDEO)) {
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Video File", "mp4", "avi", "mkv", "mpg", "mpeg"));
        }
        if (type.equals(EducationalMaterialModel.EDUCATIONAL_MATERIAL_TYPE_AUDIO)) {
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Audio File", "mp3", "wav", "wma"));
        }
        fileChooser.setAcceptAllFileFilterUsed(false);
        if (fileChooser.showOpenDialog(new JButton()) == JFileChooser.APPROVE_OPTION) {
            String choosedFilePath = fileChooser.getSelectedFile().getPath();
            String choosedFileName = fileChooser.getSelectedFile().getName();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
            String fileName = formatter.format(new Date()) + "_" + choosedFileName;
            if (copyFile(choosedFilePath, path + File.separator + fileName)) {
                EducationalMaterialModel.getInstance().addEducationalMaterial(new EducationalMaterialEntity(0, type, choosedFileName, fileName));
                updateListViews();
            }
        }
    }

    private void downloadFileChooser(EducationalMaterialEntity educationalMaterial) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String path = getClass().getClassLoader().getResource("resources").getPath() + File.separator + educationalMaterial.getType() + File.separator + educationalMaterial.getPath();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Куда сохранить файл?");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        if (fileChooser.showOpenDialog(new JButton()) == JFileChooser.APPROVE_OPTION) {
            String choosedFilePath = fileChooser.getSelectedFile().getPath();
            copyFile(path, choosedFilePath + File.separator + educationalMaterial.getPath());
        }
    }

    private boolean copyFile(String path1, String path2) {
        CopyFile copyFile = new CopyFile(path1, path2);
        Thread thread = new Thread(copyFile);
        thread.start();
        return true;
    }
}
