//Nihal Kiran Shetty
//230970065
//MCA 2 sem SECTION A
package com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.mongodb.client.*;
import org.bson.Document;

class Login extends JFrame {
    private final MongoClient mongoClient;
    private final MongoDatabase database;

    Login() {
        // Initialize MongoDB client and connect to the database
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("javaFisac");
        
        loginGUI();
    }

    public void loginGUI() {
        setLayout(null);
        getContentPane().setBackground(Color.black);
        setTitle("ENTRENCE EXAM");
        setSize(380, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //LOGIN GUI STRUCTURE
        JLabel lblhead = new JLabel("Login Form");
        JLabel lbluname = new JLabel("Username :");
        JLabel lblpasswd = new JLabel("Password :");
        final JTextField txtuname = new JTextField();
        final JPasswordField txtpwd = new JPasswordField(); // Use JPasswordField for password
        JButton btnLogin = new JButton("Login");

        //SETING THE COORDINATES FOR THE GIU COMPONENTS
        lblhead.setBounds(70, 10, 250, 100);
        lbluname.setBounds(50, 110, 90, 30);
        txtuname.setBounds(160, 110, 130, 30);
        lblpasswd.setBounds(50, 150, 90, 30);
        txtpwd.setBounds(160, 150, 130, 30);

        btnLogin.setBounds(120, 220, 110, 30);

        lblhead.setFont(new Font("SansSerif", Font.BOLD, 40));
        lblhead.setForeground(Color.WHITE);
        lbluname.setForeground(Color.WHITE);
        lblpasswd.setForeground(Color.WHITE);
        txtuname.setBackground(Color.lightGray);
        txtpwd.setBackground(Color.lightGray);
        btnLogin.setBackground(Color.darkGray);
        btnLogin.setForeground(Color.WHITE);


        add(lblhead);
        add(lbluname);
        add(txtuname);
        add(lblpasswd);
        add(txtpwd);
        add(btnLogin);

        setVisible(true);

        final JLabel lblErr = new JLabel("Invalid username or password!");
        lblErr.setBounds(110, 170, 300, 50);
        lblErr.setForeground(Color.red);
        add(lblErr);
        lblErr.setVisible(false);

        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String uname = txtuname.getText();
                String pass = new String(txtpwd.getPassword()); // Get password as string
                
                // Check authentication against MongoDB
                MongoCollection<Document> collection = database.getCollection("users");
                Document query = new Document("username", uname).append("password", pass);
                Document user = collection.find(query).first();
                
                if (user != null) {
                    new Quiz(uname);
                } else {
                    lblErr.setVisible(true);
                }
            }
        });
    }

    public void quizGUI() {
        JLabel q1 = new JLabel("Question 1");
        q1.setBounds(50, 110, 90, 30);
        add(q1);
    }

    public static void main(String[] args) {
        new Login();
    }
}
