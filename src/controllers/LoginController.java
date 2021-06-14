package controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController extends BaseController implements Initializable {
    public ChoiceBox<String> role;
    public PasswordField password;
    public Button loginAction;
    public static Stage loginStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        role.getItems().addAll("Преподаватель", "Обучающийся");
        role.setValue("Обучающийся");
    }

    public void loginAction() {
        if (Objects.equals(role.getSelectionModel().getSelectedItem(), "Обучающийся")) {
            if (Objects.equals(password.getText(), "student")) {
                MainController.isTeacher = false;
                loginStage.close();
            }
        } else if (Objects.equals(role.getSelectionModel().getSelectedItem(), "Преподаватель")) {
            if (Objects.equals(password.getText(), "teacher")) {
                MainController.isTeacher = true;
                loginStage.close();
            }
        }
    }
}
