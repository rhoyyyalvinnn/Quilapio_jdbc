package com.example.csit228f2_2;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class postSubmitController {
    @FXML
    private Label displayLabel;

    @FXML
    private void initialize() {
        // Fetch student information from the database
        Student student = fetchStudentInformationFromDatabase();

        // Display the fetched information in the label
        String displayText = "Name: " + student.getName() + "\n"
                + "ID: " + student.getId() + "\n"
                + "Email: " + student.getEmail() + "\n"
                + "Course: " + student.getCourse() + "\n"
                + "Subjects Enrolled: " + getEnrolledSubjects(student);
        displayLabel.setText(displayText);
    }

    private Student fetchStudentInformationFromDatabase() {
        String name = "";
        String id = "";
        String email = "";
        String course = "";
        boolean subjectOOP1 = false;
        boolean subjectRizal = false;
        boolean subjectCalculus = false;

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM students");
             ResultSet resultSet = statement.executeQuery()) {

            // Assuming there's only one student record for demonstration
            if (resultSet.next()) {
                name = resultSet.getString("name");
                id = resultSet.getString("student_id");
                email = resultSet.getString("email");
                course = resultSet.getString("course");

                // Check subjects enrolled
                subjectOOP1 = resultSet.getInt("subject_oop1") == 1;
                subjectRizal = resultSet.getInt("subject_Rizal") == 1;
                subjectCalculus = resultSet.getInt("subject_Calculus") == 1;
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
}
