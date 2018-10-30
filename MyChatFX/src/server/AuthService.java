package server;

import java.sql.*;

public class AuthService {

    private Connection connection;
    private PreparedStatement preparedStatement;

    public void connect() {
        String sql = "SELECT nick FROM users WHERE login = ? AND pass = ?;";

        try {
            //Class.forName("com.mysql.jdbc.Driver");
            Class.forName("org.sqlite.JDBC");
            //String url = "jdbc:mysql://localhost:3306/messanger";
            //String user = "root";
            //String password = "admin";
            //connection = DriverManager.getConnection(url, user, password);
            connection = DriverManager.getConnection("jdbc:sqlite:src/res/dateBase.db");
            preparedStatement = connection.prepareStatement(sql);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public String getNickByLoginAndPass(String login, String password) {
        try {
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getString("nick");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static void main(String[] args) {
//        AuthService a = new AuthService();
//        a.connect();
//        a.addNewUserInBase("log1", "pas1", "nick1");
//    }

    public String addNewUserInBase(String login, String password, String nick) {
        try {
            Statement statementCheck = connection.createStatement();
            ResultSet resultSet = statementCheck.executeQuery("SELECT login FROM users;");
            while (resultSet.next()) {
                if (resultSet.getString("login").equals(login))
                    return "loginIsBusy";
                else {
                    try {
                        PreparedStatement preparedStatementR = connection.prepareStatement("INSERT INTO users(login, pass, nick) VALUES (?,?,?);");
                        preparedStatementR.setString(1, login);
                        preparedStatementR.setString(2, password);
                        preparedStatementR.setString(3, nick);
                        preparedStatementR.executeUpdate();
                        return "loginOk";
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void disconnect() {
        try {
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
