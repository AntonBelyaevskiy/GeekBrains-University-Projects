//package client;
//
//import javafx.application.Platform;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.*;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.net.Socket;
//import java.net.URL;
//import java.util.ResourceBundle;
//
//
//public class EnterInClass implements Initializable {
//
//    @FXML
//    public Button showPutInBaseBtn;
//    @FXML
//    public Button showLogInBtn;
//    @FXML
//    public TextField loginField;
//    @FXML
//    public PasswordField passField;
//    @FXML
//    public Button enterIn;
//    @FXML
//    public TextField authLoginInBase;
//    @FXML
//    public TextField authPassInBase;
//    @FXML
//    public Button putInBaseBtn;
//    @FXML
//    public HBox loginBox;
//    @FXML
//    public HBox passBox;
//    @FXML
//    public HBox authLoginBox;
//    @FXML
//    public HBox authPassBox;
//    @FXML
//    public VBox root;
//
//    private boolean authorized;
//
//    private static final int PORT = 8189;
//    private static final String SERVER_IP = "localhost";
//    private Socket socket;
//    private DataOutputStream out;
//    private DataInputStream in;
//    private String myNick;
//
//
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//
//    }
//
//    public void onAuthClick() {
//
////        if (loginField.getText().isEmpty() || passField.getText().isEmpty()) {
////            showAlert("Не все данные для авторизации введены");
////        }
////
////        if (socket == null || socket.isClosed())
////            connect();
////
////        try {
////            out.writeUTF("/auth " + loginField.getText() + " " + passField.getText());
////            loginField.clear();
////            passField.clear();
////        } catch (IOException e) {
////            showAlert("Не удалось подключиться.");
////        }
////
////        Thread thread = new Thread(() -> {
////            try {
////                Thread.sleep(60_000);
////                if (!authorized) {
////                    try {
////                        socket.close();
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    }
////                }
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            }
////        });
////
////        thread.setDaemon(true);
////        thread.start();
//
//    }
//
//    private void connect() {
//        try {
//            socket = new Socket(SERVER_IP, PORT);
//            out = new DataOutputStream(socket.getOutputStream());
//            in = new DataInputStream(socket.getInputStream());
//
//            Thread threadInMsg = new Thread(() -> {
//                try {
//                    while (true) {
//                        String msg = in.readUTF();
//                        if (msg.startsWith("/authok ")) {
//                            makeNewConnection(true);
//                            myNick = msg.split("\\s")[1];
//                            break;
//                        }
//                    }
//                } catch (IOException e) {
//                    if (authorized)
//                        showAlert("Сервер перестал отвечать.");
//                    else
//                        showAlert("Время авторизации вышло. Соединение с сервером разорвано.");
//                } finally {
//                    try {
//                        socket.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    makeNewConnection(false);
//                }
//            });
//            threadInMsg.setDaemon(true);
//            threadInMsg.start();
//        } catch (IOException e) {
//            showAlert("Не удалось подключиться к серверу. Проверьте сетевое соединение.");
//        }
//    }
//
//    private void makeNewConnection(boolean yesOrNo) {
//        if(yesOrNo){
//
//        }
//    }
//
//    public void regInBase(ActionEvent actionEvent) {
//        showPutInBaseBtn.setVisible(false);
//        showPutInBaseBtn.setManaged(false);
//        loginBox.setVisible(false);
//        loginBox.setManaged(false);
//        passBox.setVisible(false);
//        passBox.setManaged(false);
//        enterIn.setVisible(false);
//        enterIn.setManaged(false);
//
//        showLogInBtn.setVisible(true);
//        showLogInBtn.setManaged(true);
//        authLoginBox.setVisible(true);
//        authLoginBox.setManaged(true);
//        authPassBox.setVisible(true);
//        authPassBox.setManaged(true);
//        putInBaseBtn.setVisible(true);
//        putInBaseBtn.setManaged(true);
//    }
//
//    public void logIn(ActionEvent actionEvent) {
//        showPutInBaseBtn.setVisible(true);
//        showPutInBaseBtn.setManaged(true);
//        loginBox.setVisible(true);
//        loginBox.setManaged(true);
//        passBox.setVisible(true);
//        passBox.setManaged(true);
//        enterIn.setVisible(true);
//        enterIn.setManaged(true);
//
//        showLogInBtn.setVisible(false);
//        showLogInBtn.setManaged(false);
//        authLoginBox.setVisible(false);
//        authLoginBox.setManaged(false);
//        authPassBox.setVisible(false);
//        authPassBox.setManaged(false);
//        putInBaseBtn.setVisible(false);
//        putInBaseBtn.setManaged(false);
//    }
//
//    private void showAlert(String msg) {
//        Platform.runLater(() -> {
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Возникли проблемы");
//            alert.setHeaderText(null);
//            alert.setContentText(msg);
//            alert.showAndWait();
//        });
//    }
//}
