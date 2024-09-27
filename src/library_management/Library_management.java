package library_management;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Library_management extends JFrame implements ActionListener {
    private JLabel label1, label2, label3, label4, label5, label6, label7;
    private JTextField textField1, textField2, textField3, textField4, textField5, textField6, textField7;
    private JButton addButton, viewButton, editButton, deleteButton, clearButton, exitButton;
    private JPanel panel;
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public Library_management() {
        // Initialize Database Connection
        connectDB();

        // GUI Setup
        setTitle("Library Management System");
        setSize(600, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        label1 = new JLabel("Book ID");
        label2 = new JLabel("Book Title");
        label3 = new JLabel("Author");
        label4 = new JLabel("Publisher");
        label5 = new JLabel("Year of Publication");
        label6 = new JLabel("Country");
        label7 = new JLabel("Number of Copies");

        textField1 = new JTextField(10);
        textField2 = new JTextField(20);
        textField3 = new JTextField(20);
        textField4 = new JTextField(20);
        textField5 = new JTextField(10);
        textField6 = new JTextField(20);
        textField7 = new JTextField(10);

        addButton = new JButton("Add");
        viewButton = new JButton("View");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");
        exitButton = new JButton("Exit");

        addButton.addActionListener(this);
        viewButton.addActionListener(this);
        editButton.addActionListener(this);
        deleteButton.addActionListener(this);
        clearButton.addActionListener(this);
        exitButton.addActionListener(this);

        panel = new JPanel(new GridLayout(10, 2));
        panel.add(label1);
        panel.add(textField1);
        panel.add(label2);
        panel.add(textField2);
        panel.add(label3);
        panel.add(textField3);
        panel.add(label4);
        panel.add(textField4);
        panel.add(label5);
        panel.add(textField5);
        panel.add(label6);
        panel.add(textField6);
        panel.add(label7);
        panel.add(textField7);
        panel.add(addButton);
        panel.add(viewButton);
        panel.add(editButton);
        panel.add(deleteButton);
        panel.add(clearButton);
        panel.add(exitButton);

        add(panel);
        setVisible(true);
    }

    private void connectDB() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tushar", "root", "");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error connecting to database: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addBook();
        } else if (e.getSource() == viewButton) {
            viewBooks();
        } else if (e.getSource() == editButton) {
            editBook();
        } else if (e.getSource() == deleteButton) {
            deleteBook();
        } else if (e.getSource() == clearButton) {
            clearFields();
        } else if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }

    private void addBook() {
        int id = Integer.parseInt(textField1.getText());
        String title = textField2.getText();
        String author = textField3.getText();
        String publisher = textField4.getText();
        int year = Integer.parseInt(textField5.getText());
        String country = textField6.getText();
        int copies = Integer.parseInt(textField7.getText());

        try {
            String query = "INSERT INTO library (bookid, booktitle, author, publisher, year, country, copies) VALUES (?, ?, ?, ?, ?, ?, ?)";
            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ps.setString(2, title);
            ps.setString(3, author);
            ps.setString(4, publisher);
            ps.setInt(5, year);
            ps.setString(6, country);
            ps.setInt(7, copies);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Book added successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding book: " + ex.getMessage());
        }
    }

    private void viewBooks() {
        try {
            String query = "SELECT * FROM library";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            System.out.println("Book Id\tBook Title\tAuthor\tPublisher\tYear\tCountry\tCopies");
            while (rs.next()) {
                System.out.println(rs.getInt("bookid") + "\t" + rs.getString("booktitle") + "\t" + rs.getString("author") + "\t" + rs.getString("publisher") + "\t" + rs.getInt("year") + "\t" + rs.getString("country") + "\t" + rs.getInt("copies"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error viewing books: " + ex.getMessage());
        }
    }

    private void editBook() {
        String oldBookID = JOptionPane.showInputDialog(this, "Enter book ID to edit:");
        String newBookID = JOptionPane.showInputDialog(this, "Enter new Book ID:");

        try {
            String query = "UPDATE library SET bookid = ? WHERE bookid = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1, Integer.parseInt(newBookID));
            ps.setInt(2, Integer.parseInt(oldBookID));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Book edited successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error editing book: " + ex.getMessage());
        }
    }

    private void deleteBook() {
        String bookID = JOptionPane.showInputDialog(this, "Enter book ID to delete:");

        try {
            String query = "DELETE FROM library WHERE bookid = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1, Integer.parseInt(bookID));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Book deleted successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error deleting book: " + ex.getMessage());
        }
    }

    private void clearFields() {
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
        textField5.setText("");
        textField6.setText("");
        textField7.setText("");
    }

    public static void main(String[] args) {
        new Library_management();
    }
}
