//Nihal Kiran Shetty
//230970065
//MCA 2 sem SECTION A
package com;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import com.mongodb.client.*;
import org.bson.Document;

public class Quiz extends JFrame implements ActionListener {
    String selectedans[] = new String[10];
    String questions[][] = new String[10][5];
    String answers[] = new String[10];

    JLabel lblqno, lblqtn,lblhead;
    JRadioButton opt1, opt2, opt3, opt4;
    JButton btnNext, btnSubmit;
    ButtonGroup grpoptn;
    long qtcount;
    int count = 0, total = 0;

    String uname;

    Quiz(String uname) 
    {
        this.uname = uname;


        getContentPane().setBackground(Color.lightGray);
        setBounds(50, 0, 1200, 500);
        setTitle("ENTRENCE EXAM");
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //MCQS GUI
        lblhead = new JLabel("WELCOME TO THE MCQs EXAM , WISH YOU ALL THE BEST");
        lblqno = new JLabel(" ");
        lblqtn = new JLabel(" ");

        opt1 = new JRadioButton(" ");
        opt2 = new JRadioButton(" ");
        opt3 = new JRadioButton(" ");
        opt4 = new JRadioButton(" ");

        //FUNCTIONS TO FORMAT AND PLACE RADIO BUTTONS
        createOptions(opt1, 250);
        createOptions(opt2, 280);
        createOptions(opt3, 310);
        createOptions(opt4, 340);

        add(opt1);
        add(opt2);
        add(opt3);
        add(opt4);

        lblhead.setBounds(100, 80, 1000, 50);
        lblhead.setForeground(Color.BLACK);
        lblqno.setBounds(30, 200, 100, 30);
        lblqtn.setBounds(70, 200, 1200, 30);
        lblhead.setFont(new Font("Tahoma", Font.PLAIN, 35));
        lblqno.setFont(new Font("Tahoma", Font.PLAIN, 22));
        lblqtn.setFont(new Font("Tahoma", Font.PLAIN, 22));

        grpoptn = new ButtonGroup();
        grpoptn.add(opt1);
        grpoptn.add(opt2);
        grpoptn.add(opt3);
        grpoptn.add(opt4);

        btnNext = new JButton("Next");
        btnNext.setBounds(30, 370, 150, 70);
        btnNext.setFont(new Font("Tahoma", Font.PLAIN, 22));
        btnNext.setBackground(Color.BLACK);
        btnNext.setForeground(Color.white);

        btnSubmit = new JButton("Submit");
        btnSubmit.setBounds(980, 370, 150, 70);
        btnSubmit.setFont(new Font("Tahoma", Font.PLAIN, 22));
        btnSubmit.setBackground(Color.BLACK);
        btnSubmit.setForeground(Color.WHITE);

        btnNext.addActionListener(this);
      
        btnSubmit.addActionListener(this);

        add(btnNext);
     
        add(btnSubmit);

        add(lblhead);
        add(lblqno);
        add(lblqtn);

        for (int i = 0; i < 10; i++) 
        {
            answers[i]="none";
        }

        setVisible(true);

        // Load questions from MongoDB
        loadQuestionsFromDB();

        quizFrames(count);
    }

    public void createOptions(JRadioButton opt, int y) 
    {
        opt.setBounds(30, y, 1100, 30);
        opt.setBackground(Color.white);
        opt.setFont(new Font("Dialog", Font.PLAIN, 20));
    }

    //FUNCTION TO LOAD QUESTION FROM MONGODB
    public void loadQuestionsFromDB() {
        MongoClient mongoClient = null;
        try {
            //CONNECTION STRING TO MONGODB
            mongoClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongoClient.getDatabase("javaFisac");
            MongoCollection<Document> collection = database.getCollection("questions");
            FindIterable<Document> questions = collection.find();
            
            qtcount=collection.countDocuments();

          
            
            int index = 0;
            for (Document question : questions) {
                this.questions[index][0] = question.getString("question");
                this.questions[index][1] = question.getString("option1");
                this.questions[index][2] = question.getString("option2");
                this.questions[index][3] = question.getString("option3");
                this.questions[index][4] = question.getString("option4");
                this.answers[index] = question.getString("correctOption");
                index++;

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mongoClient != null) {
                mongoClient.close();
            }
        }
    }
    

    public void quizFrames(int count) {
        lblqno.setText("" + (count + 1) + ".");
        lblqtn.setText(questions[count][0]);

        opt1.setText(questions[count][1]);
        opt2.setText(questions[count][2]);
        opt3.setText(questions[count][3]);
        opt4.setText(questions[count][4]);

        opt1.setActionCommand(questions[count][1]);
        opt2.setActionCommand(questions[count][2]);
        opt3.setActionCommand(questions[count][3]);
        opt4.setActionCommand(questions[count][4]);

        grpoptn.clearSelection();
    }

    public void actionPerformed(ActionEvent e)
     {
        if (grpoptn.getSelection() != null) {
            //store the selected answer in userAns[]
            selectedans[count] = grpoptn.getSelection().getActionCommand();
    }

        if (e.getSource() == btnNext) {
            count++;
            if (count < 4)
                quizFrames(count);
            else {
                quizFrames(count);
                btnNext.setEnabled(false);
            }
        }
    
        if (e.getSource() == btnSubmit) {
            // Iterate over the selected answers and check against the correct answers
            for (int i = 0; i < answers.length; i++) {
                // Check if the selected answer is not null and is equal to the correct answer
                if (selectedans[i] != null && selectedans[i].equals(answers[i])) {
                    total++;
                }
            }
        
            // Display score
            new Score(uname, total);
        }
        
    }
    

    public static void main(String[] args) {
        new Quiz("uname");
    }
}
