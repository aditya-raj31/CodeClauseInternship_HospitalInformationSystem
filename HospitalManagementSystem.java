import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Database connection utility
class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/hospitalSystem";
    private static final String USER = "root";
    private static final String PASSWORD = "@Mahadev01";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

// Model class for Patient
class Patient {
    private int id;
    private String name;
    private int age;
    private String gender;
    private String address;
    private String phone;

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}

// Data Access Object (DAO) for Patient
class PatientDAO {
    public void addPatient(Patient patient) throws SQLException {
        String query = "INSERT INTO patients (name, age, gender, address, phone) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, patient.getName());
            stmt.setInt(2, patient.getAge());
            stmt.setString(3, patient.getGender());
            stmt.setString(4, patient.getAddress());
            stmt.setString(5, patient.getPhone());
            stmt.executeUpdate();
        }
    }

    public List<Patient> getAllPatients() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM patients";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Patient patient = new Patient();
                patient.setId(rs.getInt("id"));
                patient.setName(rs.getString("name"));
                patient.setAge(rs.getInt("age"));
                patient.setGender(rs.getString("gender"));
                patient.setAddress(rs.getString("address"));
                patient.setPhone(rs.getString("phone"));
                patients.add(patient);
            }
        }
        return patients;
    }
    // More CRUD operations (update, delete) can be implemented similarly
}

// Main application with Swing-based UI
public class HospitalManagementSystem extends JFrame {
    private JTextField nameField;
    private JTextField ageField;
    private JTextField genderField;
    private JTextField addressField;
    private JTextField phoneField;
    private PatientDAO patientDAO;

    public HospitalManagementSystem() {
        setTitle("Hospital Management System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        patientDAO = new PatientDAO();

        JPanel panel = new JPanel(new GridLayout(6, 2));

        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Age:"));
        ageField = new JTextField();
        panel.add(ageField);

        panel.add(new JLabel("Gender:"));
        genderField = new JTextField();
        panel.add(genderField);

        panel.add(new JLabel("Address:"));
        addressField = new JTextField();
        panel.add(addressField);

        panel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        panel.add(phoneField);

        JButton addButton = new JButton("Add Patient");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addPatient();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        panel.add(addButton);

        add(panel);
    }

    private void addPatient() throws SQLException {
        String name = nameField.getText();
        int age = Integer.parseInt(ageField.getText());
        String gender = genderField.getText();
        String address = addressField.getText();
        String phone = phoneField.getText();

        Patient patient = new Patient();
        patient.setName(name);
        patient.setAge(age);
        patient.setGender(gender);
        patient.setAddress(address);
        patient.setPhone(phone);

        patientDAO.addPatient(patient);
        JOptionPane.showMessageDialog(this, "Patient added successfully!");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HospitalManagementSystem().setVisible(true);
            }
        });
    }
}
