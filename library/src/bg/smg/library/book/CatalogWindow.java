package bg.smg.library.book;
import javax.swing.*;

import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CatalogWindow extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private Connection connection;
/*
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        	CatalogWindow objectListScreen = new CatalogWindow();
            objectListScreen.createAndShowGUI();
        });
    }
*/
    public CatalogWindow() {
        setTitle("Catalog");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        connectToDatabase();
    }

    public void createAndShowGUI() {
        // Create the table model with columns
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Title");
        tableModel.addColumn("Author");
        tableModel.addColumn("Pages");
        tableModel.addColumn("Category");
        tableModel.addColumn("Publisher");
        tableModel.addColumn("year_of_publication ");
        tableModel.addColumn("City");

        // Create the table
        table = new JTable(tableModel);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);

        // Create buttons
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton detailsButton = new JButton("Details");

        // Add button actions
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Get the data of the selected row
                    String title = (String) tableModel.getValueAt(selectedRow, 0);
                    String author = (String) tableModel.getValueAt(selectedRow, 1);
                    int pages = (int) tableModel.getValueAt(selectedRow, 2);
                    String category = (String) tableModel.getValueAt(selectedRow, 3);
                    String publisher = (String) tableModel.getValueAt(selectedRow, 4);
                    int year_of_publication = (int) tableModel.getValueAt(selectedRow, 5);
                    String city = (String) tableModel.getValueAt(selectedRow, 6);

                    // Open the edit screen with the data
                    openEditScreen(title, author, pages, category, publisher, year_of_publication, city);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Perform soft delete or actual delete based on your requirement
                    softDeleteObject(selectedRow);
                }
            }
        });

        detailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Get the data of the selected row and display details
                    String title = (String) tableModel.getValueAt(selectedRow, 0);
                    String author = (String) tableModel.getValueAt(selectedRow, 1);
                    int pages = (int) tableModel.getValueAt(selectedRow, 2);
                    String category = (String) tableModel.getValueAt(selectedRow, 3);
                    String publisher = (String) tableModel.getValueAt(selectedRow, 4);
                    int year_of_publication = (int) tableModel.getValueAt(selectedRow, 5);
                    String city = (String) tableModel.getValueAt(selectedRow, 6);

                    // Display details (you can implement a details dialog or any other view)
                    displayDetails(title, author, pages, category, publisher, year_of_publication, city);
                }
            }
        });

        // Add buttons to a panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(detailsButton);

        // Create the main layout
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Populate the table with data from the database
        retrieveDataFromDatabase();

        // Set up frame properties
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public void updateTable() {
        // Clear existing data from the table
        clearTable();

        // Populate the table with data from the database
        retrieveDataFromDatabase();
    }

    private void clearTable() {
        // Clear the table model
        tableModel.setRowCount(0);
    }
    private void retrieveDataFromDatabase() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM books");

            while (resultSet.next()) {
                Object[] rowData = {
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getInt("pages"),
                        resultSet.getString("category"),
                        resultSet.getString("publisher"),
                        resultSet.getInt("year_of_publication"),
                        resultSet.getString("city")
                };

                tableModel.addRow(rowData);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openEditScreen(String title, String author, int pages, String category, String publisher, int year_of_publication, String city) {
    	EditScreenDialog editScreenDialog = new EditScreenDialog(this, connection, title, author, pages, category, publisher, year_of_publication, city);
        editScreenDialog.setVisible(true);
    }
    public class EditScreenDialog extends JDialog {

        private JTextField titleField;
        private JTextField authorField;
        private JTextField pagesField;
        private JTextField categoryField;
        private JTextField publisherField;
        private JTextField year_of_publicationField;
        private JTextField cityField;

        private Connection connection;
        private CatalogWindow parentScreen;

        public EditScreenDialog(CatalogWindow parentScreen, Connection connection, String title, String author, int pages, String category, String publisher, int year_of_publication, String city) {
            super(parentScreen, "Edit Book", true);
            this.parentScreen = parentScreen;
            this.connection = connection;

            // Create components
            titleField = new JTextField(title);
            authorField = new JTextField(author);
            pagesField = new JTextField(String.valueOf(pages));
            categoryField = new JTextField(category);
            publisherField = new JTextField(publisher);
            year_of_publicationField = new JTextField(String.valueOf(year_of_publication));
            cityField = new JTextField(city);

            JButton saveButton = new JButton("Save");

            // Add action listener for the save button
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    saveChanges();
                }
            });

            // Create layout
            setLayout(new GridLayout(8, 2));
            add(new JLabel("Title:"));
            add(titleField);
            add(new JLabel("Author:"));
            add(authorField);
            add(new JLabel("Pages:"));
            add(pagesField);
            add(new JLabel("Category:"));
            add(categoryField);
            add(new JLabel("Publisher:"));
            add(publisherField);
            add(new JLabel("Year of Publication:"));
            add(year_of_publicationField);
            add(new JLabel("City:"));
            add(cityField);
            add(saveButton);

            // Set frame properties
            pack();
            setLocationRelativeTo(parentScreen);
            setResizable(false);
        }

        private void saveChanges() {
            try {
                // Get the data from the fields
                String title = titleField.getText();
                String author = authorField.getText();
                int pages = Integer.parseInt(pagesField.getText());
                String category = categoryField.getText();
                String publisher = publisherField.getText();
                int year_of_publication = Integer.parseInt(year_of_publicationField.getText());
                String city = cityField.getText();

                // Update the data in the database
                updateBookInDatabase(title, author, pages, category, publisher, year_of_publication, city);

                // Update the table in the parent screen
               parentScreen.updateTable();

                // Close the dialog
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers for pages and year of publication.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void updateBookInDatabase(String title, String author, int pages, String category, String publisher, int year_of_publication, String city) {
            try {
                // Prepare the update statement
                String updateQuery = "UPDATE books SET title=?, author=?, pages=?, category=?, publisher=?, year_of_publication=?, city=? WHERE title=?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                    preparedStatement.setString(1, title);
                    preparedStatement.setString(2, author);
                    preparedStatement.setInt(3, pages);
                    preparedStatement.setString(4, category);
                    preparedStatement.setString(5, publisher);
                    preparedStatement.setInt(6, year_of_publication);
                    preparedStatement.setString(7, city);
                    preparedStatement.setString(8, title); // Use the original title to identify the book

                    // Execute the update
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating the book in the database.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
   
    private void softDeleteObject(int selectedRow) {
        // Get the title of the book to be soft-deleted
        String titleToDelete = (String) tableModel.getValueAt(selectedRow, 0);

        // Implement the logic for soft deletion (update the "is_deleted" flag in the database)
        try {
            // Prepare the update statement
            String updateQuery = "UPDATE books SET is_deleted=true WHERE title=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, titleToDelete);

                // Execute the update
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error soft deleting the book in the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Remove the row from the table model
        tableModel.removeRow(selectedRow);
    }

   
    private void displayDetails(String title, String author, int pages, String catalog, String publisher, int year_of_publication, String city) {
        JOptionPane.showMessageDialog(this,
                "Title: " + title + "\n" +
                        "Author: " + author + "\n" +
                        "Pages: " + pages + "\n" +
                        "Catalog: " + catalog + "\n" +
                        "Publisher: " + publisher + "\n" +
                        "year_of_publication: " + year_of_publication + "\n" +
                        "City: " + city,
                "Details",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void connectToDatabase() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            // Connect to the database
            String url = "jdbc:mariadb://localhost:3306/library";
            String username = "root"; // Change to your MariaDB username
            String password = null; // Change to your MariaDB password
            connection = DriverManager.getConnection(url, username, password);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeDatabaseConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDefaultCloseOperation(int operation) {
        closeDatabaseConnection();
        super.setDefaultCloseOperation(operation);
    }
    
}
