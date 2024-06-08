//Nihal Kiran Shetty
//230970065
//MCA 2 sem SECTION A
package com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.mongodb.client.*;
import com.mongodb.client.model.UpdateOptions; // Import required MongoDB classes

import org.bson.Document; // Import required MongoDB class for Document

public class Score extends JFrame implements ActionListener {
    String uname;
    int score;

    Score(String uname, int score) {
        this.uname = uname;
        this.score = score;

        setLayout(null);
        setTitle("ENTRENCE EXAM");
        setBackground(Color.WHITE);
        setSize(450, 350);
        setLocationRelativeTo(null);

        JLabel lblThank = new JLabel("Thank you " + uname + "!");
        lblThank.setBounds(90, 50, 300, 40);
        lblThank.setFont(new Font("Tahoma", Font.PLAIN, 30));
        add(lblThank);

        JLabel lblScore = new JLabel("Score: " + score);
        lblScore.setBounds(150, 150, 300, 40);
        lblScore.setFont(new Font("Tahoma", Font.PLAIN, 24));
        add(lblScore);

        JButton btnExit = new JButton("Exit");
        btnExit.setFont(new Font("Tahoma", Font.PLAIN, 22));
        btnExit.setBackground(Color.BLACK);
        btnExit.setForeground(Color.WHITE);
        btnExit.setBounds(150, 200, 100, 50);
        btnExit.addActionListener(this); // Add ActionListener to the exit button
        add(btnExit);

        setVisible(true);

        // Store score in MongoDB
        storeScoreInDB();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Exit")) {
            dispose(); // Close the JFrame
            System.exit(0); // Terminate the program
        }
    }

    public void storeScoreInDB() {
        MongoClient mongoClient = null;
        try {
            mongoClient = MongoClients.create("mongodb://localhost:27017"); // Create MongoDB client
            MongoDatabase database = mongoClient.getDatabase("javaFisac"); // Access the database
            MongoCollection<Document> collection = database.getCollection("users"); // Access the collection
    
            // Create query document
            Document query = new Document("username", uname); 
            Document update = new Document("$set", new Document("score", score)); 
            UpdateOptions options = new UpdateOptions().upsert(true); 
    
            collection.updateOne(query, update, options); // Update or insert document
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mongoClient != null) {
                mongoClient.close(); 
            }
        }
    }
    

    public static void main(String[] args) {
        new Score("", 0); // Create an instance of Score JFrame
    }
}
