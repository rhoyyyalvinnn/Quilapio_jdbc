package com.example.csit228f2_2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HelloController {
    @FXML
    private TextField tfName;

    @FXML
    private TextField tfEmail;

    @FXML
    private ComboBox<String> courseComboBox;

    @FXML
    private CheckBox subject1CheckBox;

    @FXML
    private CheckBox subject2CheckBox;

    @FXML
    private CheckBox subject3CheckBox;

    @FXML
    private TextField tfStudentId;

    @FXML
    public static String currentStudentID;


    @FXML
    private void handleSubmit() {
        String name = tfName.getText();
        String email = tfEmail.getText();
        String course = courseComboBox.getValue();
        String studentId = tfStudentId.getText();
        currentStudentID = studentId;
        boolean subject1Selected = subject1CheckBox.isSelected();
        boolean subject2Selected = subject2CheckBox.isSelected();
        boolean subject3Selected = subject3CheckBox.isSelected();

        // Database operations
        try (Connection connection = MySQLConnection.getConnection()) {
            // Set auto-commit to false
            connection.setAutoCommit(false);

            // Check if the user has already enrolled
            String checkEnrollmentQuery = "SELECT * FROM students WHERE studentID = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkEnrollmentQuery)) {
                checkStatement.setString(1, studentId);
                ResultSet resultSet = checkStatement.executeQuery();

                if (resultSet.next()) {
                    // User already enrolled, update the enrollment information
                    String updateEnrollmentQuery = "UPDATE students SET name = ?, email = ?, course = ?, subject_oop1 = ?, subject_Rizal = ?, subject_Calculus = ? WHERE studentID = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateEnrollmentQuery)) {
                        updateStatement.setString(1, name);
                        updateStatement.setString(2, email);
                        updateStatement.setString(3, course);
                        updateStatement.setInt(4, subject1Selected ? 1 : 0);
                        updateStatement.setInt(5, subject2Selected ? 1 : 0);
                        updateStatement.setInt(6, subject3Selected ? 1 : 0);
                        updateStatement.setString(7, studentId);
                        int updatedRows = updateStatement.executeUpdate();
                        if (updatedRows > 0) {
                            showInformationAlert("Enrollment Updated Successfully");
                            System.out.println(currentStudentID);
                            loadWelcomeFXML(); // Call loadWelcomeFXML with the studentId
                        }
                    }
                } else {
                    // User not enrolled yet, insert the enrollment information
                    String insertStudentQuery = "INSERT INTO students (name, email, course, studentID, subject_oop1, subject_Rizal, subject_Calculus) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement studentStatement = connection.prepareStatement(insertStudentQuery)) {
                        studentStatement.setString(1, name);
                        studentStatement.setString(2, email);
                        studentStatement.setString(3, course);
                        studentStatement.setString(4, studentId);
                        studentStatement.setInt(5, subject1Selected ? 1 : 0);
                        studentStatement.setInt(6, subject2Selected ? 1 : 0);
                        studentStatement.setInt(7, subject3Selected ? 1 : 0);
                        int insertedRows = studentStatement.executeUpdate();
                        if (insertedRows > 0) {
                            showInformationAlert("Enrollment Inserted Successfully");
                            // Load welcome.fxml
                            System.out.println(currentStudentID);
                            loadWelcomeFXML(); // Call loadWelcomeFXML with the studentId
                        }
                    }
                }
            }

            // Commit the transaction
            connection.commit();
            System.out.println("Transaction committed successfully.");
        } catch (SQLException | IOException e) {
            // Roll back the transaction if any operation fails
            e.printStackTrace();
            System.out.println("Transaction failed. Rolling back changes...");
            try {
                Connection connection = null;
                connection.rollback();
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
        }
    }

    private void loadWelcomeFXML() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("welcome.fxml"));
        Parent root = loader.load();

        // Create a new scene and set it in the stage
        Scene scene = new Scene(root);
        Stage stage = (Stage) tfName.getScene().getWindow();
        stage.setScene(scene);
    }


    private void showInformationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
