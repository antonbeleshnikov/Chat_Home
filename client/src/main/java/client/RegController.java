package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class RegController {
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField nicknameField;
    @FXML
    private TextArea textArea;

    private Controller controller;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @FXML
    public void tryToReg(ActionEvent actionEvent) {
        String login = loginField.getText().trim();
        String password = passwordField.getText().trim();
        String nickname = nicknameField.getText().trim();

        if(login.equals("") || password.equals("") || nickname.equals("")) {
            textArea.appendText("Ни одно из полей не должно быть пустым\n");
            return;
        }
        if(login.contains(" ") || password.contains(" ") || nickname.contains(" ")) {
            textArea.appendText("В логине, пароле и никнейме должны отсутсовать пробелы\n");
            return;
        }

        controller.registration(login,password,nickname);
    }

    public void regResult(String message) {
        textArea.appendText(message + "\n");
    }
}
