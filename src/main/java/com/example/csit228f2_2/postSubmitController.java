package com.example.csit228f2_2;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class postSubmitController {
    @FXML
    private Label displayLabel;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnConfirm;
    @FXML
    private Button btnDelete;

    private String currentStudentID; // Field to store the current student's ID

    // Setter method for the current student's ID
    public void setCurrentStudentID(String studentID) {
        this.currentStudentID = studentID;
    }

    @FXML
    private void initialize() {
        // Check if currentStudentID is set
        if (HelloController.currentStudentID != null) {
            // Fetch student information from the database based on the current student's ID
            Student student = fetchStudentInformationFromDatabase(HelloController.currentStudentID);

            // Display the fetched information in the label
            String displayText = "Name: " + student.getName() + "\n"
                    + "ID: " + student.getId() + "\n"
                    + "Email: " + student.getEmail() + "\n"
                    + "Course: " + student.getCourse() + "\n"
                    + "Subjects Enrolled: " + getEnrolledSubjects(student);
            displayLabel.setText(displayText);
        } else {
            displayLabel.setText("No student ID provided.");
        }
    }


    private Student fetchStudentInformationFromDatabase(String studentID) {
        String name = "";
        String id = "";
        String email = "";
        String course = "";
        boolean subjectOOP1 = false;
        boolean subjectRizal = false;
        boolean subjectCalculus = false;

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM students WHERE studentID = ?");
        ) {
            statement.setString(1, currentStudentID); // Use the passed studentID parameter here
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    name = resultSet.getString("name");
                    id = resultSet.getString("studentID");
                    email = resultSet.getString("email");
                    course = resultSet.getString("course");

                    subjectOOP1 = resultSet.getInt("subject_oop1") == 1;
                    subjectRizal = resultSet.getInt("subject_Rizal") == 1;
                    subjectCalculus = resultSet.getInt("subject_Calculus") == 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Student(name, id, email, course, subjectOOP1, subjectRizal, subjectCalculus);
    }



    private String getEnrolledSubjects(Student student) {
        StringBuilder subjectsEnrolled = new StringBuilder();
        if (student.isSubjectOOP1()) {
            subjectsEnrolled.append("Object-oriented programming");
        }
        if (student.isSubjectRizal()) {
            if (subjectsEnrolled.length() > 0) {
                subjectsEnrolled.append(", ");
            }
            subjectsEnrolled.append("Rizal");
        }
        if (student.isSubjectCalculus()) {
            if (subjectsEnrolled.length() > 0) {
                subjectsEnrolled.append(", ");
            }
            subjectsEnrolled.append("Calculus");
        }
        return subjectsEnrolled.toString();
    }


    @FXML
    private void handleConfirmButtonClick() {
        // Show a confirmation dialog
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Thank you!", ButtonType.OK);
        confirmation.setHeaderText(null);
        confirmation.showAndWait();

        // Close the application if OK is clicked
        if (confirmation.getResult() == ButtonType.OK) {
            Stage stage = (Stage) displayLabel.getScene().getWindow();
            stage.close();
        }
    }
    @FXML
    private void deleteStudentInformation() {
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM students WHERE studentID = ?");
        ) {
            statement.setString(1, HelloController.currentStudentID);
            statement.executeUpdate();
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Record Deleted", ButtonType.OK);
            confirmation.showAndWait();

            if (confirmation.getResult() == ButtonType.OK) {
                Stage stage = (Stage) displayLabel.getScene().getWindow();
                stage.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
