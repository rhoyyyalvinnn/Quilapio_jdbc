package com.example.csit228f2_2;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HelloApplication extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {

        try(Connection c = MySQLConnection.getConnection();
            Statement statement = c.createStatement()) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(50) NOT NULL, " +
                    "password VARCHAR(50) NOT NULL)";
            statement.execute(createTableQuery);
            System.out.println("Table Created Successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        AnchorPane pnMain = new AnchorPane();
        GridPane grid = new GridPane();
        pnMain.getChildren().add(grid);
        grid.setAlignment(Pos.CENTER);
        Text sceneTitle = new Text("Welcome to CSIT228");
        sceneTitle.setFill(Paint.valueOf("#325622"));
        sceneTitle.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 69));
        grid.add(sceneTitle, 0, 0, 2, 1);

        Label lblUsername = new Label("Username: ");
        lblUsername.setTextFill(Paint.valueOf("#c251d5"));
        lblUsername.setFont(Font.font(30));
        grid.add(lblUsername, 0, 1);

        TextField tfUsername = new TextField();
        tfUsername.setFont(Font.font(25));
        grid.add(tfUsername, 1, 1);

        Label lblPassword = new Label("Password: ");
        lblPassword.setTextFill(Paint.valueOf("#c251d5"));
        lblPassword.setFont(Font.font(30));
        grid.add(lblPassword, 0, 2);

        PasswordField pfPassword = new PasswordField();
        pfPassword.setFont(Font.font(25));
        grid.add(pfPassword, 1, 2);

        TextField tfPassword = new TextField();
        tfPassword.setFont(Font.font(25));
        tfPassword.setVisible(false);
        grid.add(tfPassword, 1, 2);

        Button btnShow = new Button("<*>");
        btnShow.setFont(Font.font(20));
        HBox hbShow = new HBox();
        hbShow.getChildren().add(btnShow);
        hbShow.setAlignment(Pos.CENTER_LEFT);
        hbShow.setSpacing(5);
        grid.add(hbShow, 2, 2);

        btnShow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (pfPassword.isVisible()) {
                    tfPassword.setText(pfPassword.getText());
                    tfPassword.setVisible(true);
                    pfPassword.setVisible(false);
                } else {
                    pfPassword.setText(tfPassword.getText());
                    pfPassword.setVisible(true);
                    tfPassword.setVisible(false);
                }
            }
        });

        Button btnSignIn = new Button("Sign In");
        btnSignIn.setFont(Font.font(30));
        Button btnRegister = new Button("Register");
        btnRegister.setFont(Font.font(30));
        HBox hbButtons = new HBox();
        hbButtons.getChildren().addAll(btnSignIn, btnRegister);
        hbButtons.setAlignment(Pos.CENTER);
        hbButtons.setSpacing(20);
        grid.add(hbButtons, 0, 3, 3, 1);

        final Text actionTarget = new Text("");
        actionTarget.setFont(Font.font(20));
        grid.add(actionTarget, 1, 4);

        btnSignIn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String username = tfUsername.getText();
                String password = pfPassword.getText();
                String name = "";
                String pass = "";
                try (Connection c = MySQLConnection.getConnection();
                     Statement statement = c.createStatement()) {
                    String selectQuery = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'";
                    ResultSet result = statement.executeQuery(selectQuery);

                    if (result.next()) {
                        // Successful sign-in
                        int id = result.getInt("id");
                        name = result.getString("username");
                        pass = result.getString("password");

                        // Load hello-view.fxml
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
                        Scene scene = new Scene(loader.load());

                        // Set the scene for the stage
                        stage.setScene(scene);
                        stage.show();

                        // Create table for student information
                        String createStudentTableQuery = "CREATE TABLE IF NOT EXISTS students (" +
                                "id INT AUTO_INCREMENT PRIMARY KEY," +
                                "studentID INT NOT NULL," +
                                "name VARCHAR(50) NOT NULL," +
                                "email VARCHAR(50) NOT NULL," +
                                "course VARCHAR(50) NOT NULL," +
                                "subject_oop1 TINYINT DEFAULT 0," +
                                "subject_Rizal TINYINT DEFAULT 0," +
                                "subject_Calculus TINYINT DEFAULT 0)";
                        statement.execute(createStudentTableQuery);
                        System.out.println("Student Table Created Successfully");
                    } else {
                        // Invalid username/password
                        actionTarget.setText("Invalid username/password");
                        actionTarget.setOpacity(1);
                    }
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
            }
        });



        btnRegister.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try (Connection c = MySQLConnection.getConnection();
                     PreparedStatement preparedStatement = c.prepareStatement(
                             "INSERT INTO users (username, password) VALUES (?, ?)"
                     )) {
                    String username = tfUsername.getText();
                    String password = pfPassword.getText();
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, password);

                    int rowsInserted = preparedStatement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Data inserted successfully");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        EventHandler<KeyEvent> fieldChange = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent actionEvent) {
                actionTarget.setOpacity(0);
            }
        };
        tfUsername.setOnKeyTyped(fieldChange);
        pfPassword.setOnKeyTyped(fieldChange);

        Scene scene = new Scene(pnMain);
        stage.setScene(scene);
        stage.show();
    }
}
