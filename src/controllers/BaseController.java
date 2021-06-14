package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public abstract class BaseController {
    // Ошибки
    public final String ERROR                                   = "Ошибка";
    public final String ERROR_SOMETHING_GOES_WRONG              = "Что-то пошло не так";
    public final String ERROR_FILE_WAS_NOT_SAVED                = "Произошла ошибка при сохранении файла";
    public final String ERROR_EMPTY_FIELD                       = "Поле не заполнено";
    public final String ERROR_NO_PROGRAM_FOR_THIS_FILE          = "На вашем устройстве нет программы, способной открыть этот формат файла";
    // Информация
    public final String INFORMATION                             = "Информация";
    public final String INFORMATION_FILE_SUCCESSFULLY_SAVED     = "Файл был успешно сохранен";
    public final String INFORMATION_NOTE_IS_EMPTY               = "Заметка пуста";
    public final String INFORMATION_FILE_NOT_CHOOSED            = "Вы не выбрали файл";
    public final String INFORMATION_TEST_NOT_CHOOSED            = "Вы не выбрали тест";
    public final String INFORMATION_QUESTION_NOT_CHOOSED        = "Вы не выбрали вопрос";
    public final String INFORMATION_ANSWER_NOT_CHOOSED          = "Вы не выбрали ответ";
    public final String INFORMATION_CHOOSE_EDUCATIONAL_MATERIAL = "Выберите окно с текстом/видео/аудио";
    // Подтверждения
    public final String CONFIRMATION                            = "Подтверждение";
    public final String CONFIRMATION_DOWNLOAD_NOTE              = "Сохранить заметку?";
    public final String CONFIRMATION_CLEAR_NOTE                 = "Вы уверены, что хотите очистить заметку?";
    public final String CONFIRMATION_DELETE_FILE                = "Вы уверены, что хотите удалить файл?";
    public final String CONFIRMATION_DELETE_TEST                = "Вы уверены, что хотите удалить тест?";
    public final String CONFIRMATION_DELETE_QUESTION            = "Вы уверены, что хотите удалить вопрос?";
    public final String CONFIRMATION_DELETE_ANSWER              = "Вы уверены, что хотите удалить ответ?";

    private static Stage stage = new Stage();

    public Optional<ButtonType> newAlert(Alert.AlertType alertType, String title, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        return alert.showAndWait();
    }

    public Stage getScene(String resourceFXML) {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(resourceFXML));
            stage.setTitle("Obuchalkin Software");
            stage.setScene(new Scene(root));
        } catch (IOException exception) {
            newAlert(Alert.AlertType.ERROR, ERROR, ERROR_SOMETHING_GOES_WRONG);
        }
        return stage;
    }

    public Stage getStage() {
        return stage;
    }
}
