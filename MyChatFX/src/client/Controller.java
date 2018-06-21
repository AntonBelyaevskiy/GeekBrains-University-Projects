package client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private static final int PORT = 8189;
    private static final String SERVER_IP = "localhost";

    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;

    @FXML
    TextArea textArea;
    @FXML
    TextField loginField;
    @FXML
    TextField passField;
    @FXML
    TextField msgField;
    @FXML
    ListView<String> clientsListView;
    @FXML
    Button sendBtn;
    @FXML
    TextField authLoginInBase;
    @FXML
    PasswordField authPassInBase;
    @FXML
    Button putInBaseBtn;
    @FXML
    Button showPutInBaseBlock;
    @FXML
    Button showLogInBlock;
    @FXML
    Button enterIn;
    @FXML
    TextField authNickInBase;
    @FXML
    Text serviceText;
    @FXML
    Button exitBtn;
    @FXML
    Label nickLabel;
    @FXML
    Label toNick;
    @FXML
    GridPane rootNode;
    @FXML
    Label textInfo;
    @FXML
    HBox messBlock;
    @FXML
    VBox listBlock;

    private boolean authorized;
    private String myNick;
    private ObservableList<String> clientsList;
    private boolean isOutFromChatSelf = false;
    private boolean privateMsg = false;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setAuthorized(false);
        Platform.runLater(() -> loginField.requestFocus());
    }

    private void setAuthorized(Boolean authorized) {
        this.authorized = authorized;
        if (authorized) {
            showPutInBaseBlock.setVisible(false);
            showPutInBaseBlock.setManaged(false);
            showLogInBlock.setVisible(false);
            showLogInBlock.setManaged(false);
            clientsListView.setVisible(true);
            clientsListView.setManaged(true);
            textArea.setVisible(true);
            textArea.setManaged(true);
            msgField.setVisible(true);
            msgField.setManaged(true);
            loginField.setVisible(false);
            loginField.setManaged(false);
            passField.setVisible(false);
            passField.setManaged(false);
            enterIn.setVisible(false);
            enterIn.setManaged(false);
            authLoginInBase.setVisible(false);
            authLoginInBase.setManaged(false);
            authPassInBase.setVisible(false);
            authPassInBase.setManaged(false);
            putInBaseBtn.setVisible(false);
            putInBaseBtn.setManaged(false);
            serviceText.setVisible(false);
            serviceText.setManaged(false);
            Platform.runLater(() -> msgField.requestFocus());
            isOutFromChatSelf = false;
            exitBtn.setVisible(true);
            exitBtn.setManaged(true);
            nickLabel.setVisible(true);
            nickLabel.setManaged(true);
            textInfo.setVisible(true);
            textInfo.setManaged(true);
            messBlock.setVisible(true);
            messBlock.setManaged(true);
            listBlock.setVisible(true);
            listBlock.setManaged(true);
            Platform.runLater(() -> {
                nickLabel.setText(myNick);
                nickLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #0076B5");
            });

        } else {
            msgField.clear();
            showPutInBaseBlock.setVisible(true);
            showPutInBaseBlock.setManaged(true);
            showLogInBlock.setVisible(false);
            showLogInBlock.setManaged(false);
            clientsListView.setVisible(false);
            clientsListView.setManaged(false);
            textArea.setVisible(false);
            textArea.setManaged(false);
            msgField.setVisible(false);
            msgField.setManaged(false);
            loginField.setVisible(true);
            loginField.setManaged(true);
            passField.setVisible(true);
            passField.setManaged(true);
            enterIn.setVisible(true);
            enterIn.setManaged(true);
            authLoginInBase.setVisible(false);
            authLoginInBase.setManaged(false);
            authPassInBase.setVisible(false);
            authPassInBase.setManaged(false);
            putInBaseBtn.setVisible(false);
            putInBaseBtn.setManaged(false);
            serviceText.setVisible(true);
            serviceText.setManaged(true);
            exitBtn.setVisible(false);
            exitBtn.setManaged(false);
            nickLabel.setVisible(false);
            nickLabel.setManaged(false);
            sendBtn.setVisible(false);
            sendBtn.setManaged(false);
            textInfo.setVisible(false);
            textInfo.setManaged(false);
            messBlock.setVisible(false);
            messBlock.setManaged(false);
            listBlock.setVisible(false);
            listBlock.setManaged(false);
            myNick = "";
            Platform.runLater(() -> nickLabel.setText(""));

        }
    }

    private void connect() {
        try {
            socket = new Socket(SERVER_IP, PORT);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());

            clientsList = FXCollections.observableArrayList();
            clientsListView.setItems(clientsList);
            clientsListView.setCellFactory(new Callback<>() {
                @Override
                public ListCell<String> call(ListView<String> param) {
                    return new ListCell<>() {
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (!empty) {
                                setText(item);
                                setStyle("-fx-font-weight: bold; -fx-background-color: transparent");
                            }else {
                                setStyle("-fx-background-color: transparent");
                            }
                        }
                    };
                }
            });


            Thread threadInMsg = new Thread(() -> {
                try {
                    while (true) {
                        String msg = in.readUTF();

                        if (msg.equals("/accauntuse")) {
                            serviceText.setText("Учетная запись уже используется");

                        }

                        if (msg.equals("/wrongid")) {
                            serviceText.setText("Неверный логин или пароль");

                        }

                        if (msg.startsWith("/authok ")) {
                            myNick = msg.split("\\s")[1];
                            setAuthorized(true);
                            break;
                        }

                        if(msg.equals("/loginBusy")){
                            serviceText.setText("Логин уже используется");
                            authLoginInBase.clear();
                            authPassInBase.clear();
                            authNickInBase.clear();
                        }

                        if(msg.equals("/loginOk")){
                            serviceText.setText("Вы успешно зарегистрировались");
                            authLoginInBase.clear();
                            authPassInBase.clear();
                            authNickInBase.clear();
                        }

                        if(msg.equals("/errorLogin")){
                            serviceText.setText("Временно отсутствует связь с Базой Данных. Попрбуйте позже.");
                            authLoginInBase.clear();
                            authPassInBase.clear();
                            authNickInBase.clear();
                        }

                    }
                    while (true) {
                        String msg = in.readUTF();

                        if (msg.startsWith("/")) {
                            if (msg.startsWith("/clientslist ")) {
                                String[] data = msg.split("\\s");
                                Platform.runLater(() -> {
                                    clientsList.clear();
                                    for (int i = 1; i < data.length; i++) {
                                        if (!data[i].equals(myNick))
                                            clientsList.addAll(data[i]);

                                    }
                                });
                            }

                            if(msg.startsWith("/inOrOut")){
                                Platform.runLater(() ->{
                                    textInfo.setText(msg.split("\\s",2)[1]);
                                    textInfo.getStyleClass().add("serviceText");
                                });
                            }

                        } else {
                            textArea.appendText(msg + "\n");
                        }
                    }
                } catch (IOException e) {
                    if (authorized)
                        if (isOutFromChatSelf)
                            serviceText.setText("Вы покинули мессенджер");
                        else
                            serviceText.setText("Сервер перестал отвечать.");
                    else
                        serviceText.setText("Время авторизации вышло. Соединение с сервером разорвано.");
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    setAuthorized(false);
                }
            });
            threadInMsg.setDaemon(true);
            threadInMsg.start();
        } catch (IOException e) {
            serviceText.setText("Не удалось подключиться к серверу. Проверьте сетевое соединение.");
        }
    }

    public void onAuthClick() {
        if (loginField.getText().isEmpty() || passField.getText().isEmpty()) {
            serviceText.setText("Не все данные введены");
        }

        if (socket == null || socket.isClosed())
            connect();


        try {
            out.writeUTF("/auth " + loginField.getText() + " " + passField.getText());
            loginField.clear();
            passField.clear();
        } catch (IOException e) {
            showAlert("Не удалось подключиться.");
        }

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(60_000);
                if (!authorized) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    public void sendMessage() {
        if (msgField.getText().equals("")) {
            return;
        }
        try {
            if (msgField.getText().equals("/end"))
                isOutFromChatSelf = true;

            if (toNick.getText().equals(""))
                out.writeUTF(msgField.getText());
            else {
                out.writeUTF("/w " + toNick.getText() + " " + msgField.getText());
            }


            msgField.clear();
            msgField.requestFocus();
        } catch (IOException e) {
            showAlert("Ошибка отправки сообщения");
        }

        sendBtn.setVisible(false);
        sendBtn.setManaged(false);

    }

    private void showAlert(String msg) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Возникли проблемы");
            alert.setHeaderText(null);
            alert.setContentText(msg);
            alert.showAndWait();
        });
    }

    public void clickOnClientList(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            if (!privateMsg) {
                if (clientsListView.getSelectionModel().getSelectedItem() != null) {
                    activatePrivateMsg();
                }
            } else {
                if (!clientsListView.getSelectionModel().getSelectedItem().equals(toNick.getText())) {
                    activatePrivateMsg();
                } else {
                    disActivatePrivateMsg();
                }
            }
        }
    }

    public void showSendBtn(KeyEvent keyEvent) {
        if (msgField.getText().equals("")) {
            sendBtn.setVisible(false);
            sendBtn.setManaged(false);
            return;
        }
        sendBtn.setVisible(true);
        sendBtn.setManaged(true);
    }

    public void regInBase(ActionEvent actionEvent) {
        showPutInBaseBlock.setVisible(false);
        showPutInBaseBlock.setManaged(false);
        loginField.setVisible(false);
        loginField.setManaged(false);
        passField.setVisible(false);
        passField.setManaged(false);
        enterIn.setVisible(false);
        enterIn.setManaged(false);

        showLogInBlock.setVisible(true);
        showLogInBlock.setManaged(true);
        authLoginInBase.setVisible(true);
        authLoginInBase.setManaged(true);
        authPassInBase.setVisible(true);
        authPassInBase.setManaged(true);
        putInBaseBtn.setVisible(true);
        putInBaseBtn.setManaged(true);
        authNickInBase.setVisible(true);
        authNickInBase.setManaged(true);

        serviceText.setText("");
    }

    public void logIn(ActionEvent actionEvent) {
        showPutInBaseBlock.setVisible(true);
        showPutInBaseBlock.setManaged(true);
        loginField.setVisible(true);
        loginField.setManaged(true);
        passField.setVisible(true);
        passField.setManaged(true);
        enterIn.setVisible(true);
        enterIn.setManaged(true);

        showLogInBlock.setVisible(false);
        showLogInBlock.setManaged(false);
        authLoginInBase.setVisible(false);
        authLoginInBase.setManaged(false);
        authPassInBase.setVisible(false);
        authPassInBase.setManaged(false);
        putInBaseBtn.setVisible(false);
        putInBaseBtn.setManaged(false);
        authNickInBase.setVisible(false);
        authNickInBase.setManaged(false);

        serviceText.setText("");
    }

    public void exitFromMessenger(ActionEvent actionEvent) {
        try {
            isOutFromChatSelf = true;
            out.writeUTF("/end");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void activatePrivateMsg() {
        toNick.setText(clientsListView.getSelectionModel().getSelectedItem());
        toNick.setVisible(true);
        toNick.setManaged(true);
        msgField.requestFocus();
        msgField.selectEnd();
        privateMsg = true;
    }

    private void disActivatePrivateMsg() {
        toNick.setText("");
        toNick.setVisible(false);
        toNick.setManaged(false);
        toNick.setStyle(null);
        msgField.requestFocus();
        msgField.selectEnd();
        privateMsg = false;
    }

    public void regNewUser(ActionEvent actionEvent) {

        if (authLoginInBase.getText().isEmpty() || authPassInBase.getText().isEmpty() || authNickInBase.getText().isEmpty()) {
            serviceText.setText("Не все данные введены");
            authLoginInBase.clear();
            authPassInBase.clear();
            authNickInBase.clear();
        }

        if (socket == null || socket.isClosed())
            connect();

        try {
            out.writeUTF("/newUser " + authLoginInBase.getText() + " " + authPassInBase.getText() + " " + authNickInBase.getText());
        } catch (IOException e) {
            showAlert("Не удалось подключиться.");
        }
    }
}


