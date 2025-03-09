package com.wewe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class MathQuizGameGUI {
    private JFrame frame;
    private JPanel panel;
    private JTextField questionField, answerField;
    private JButton submitButton, nextButton;
    private JLabel scoreLabel;
    private int score = 0;
    private Random random;
    private int num1, num2, answer;
    private String operator;

    public MathQuizGameGUI() {
        frame = new JFrame("Math Quiz Game");
        panel = new JPanel();
        random = new Random();

        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        panel.setLayout(new BorderLayout());

        // คำถามและคำตอบ
        questionField = new JTextField();
        questionField.setFont(new Font("Arial", Font.PLAIN, 18));
        questionField.setHorizontalAlignment(JTextField.CENTER);
        questionField.setEditable(false);
        panel.add(questionField, BorderLayout.NORTH);

        answerField = new JTextField();
        answerField.setFont(new Font("Arial", Font.PLAIN, 18));
        answerField.setHorizontalAlignment(JTextField.CENTER);
        panel.add(answerField, BorderLayout.CENTER);

        // ปุ่ม
        submitButton = new JButton("Answer");
        submitButton.setFont(new Font("Arial", Font.PLAIN, 18));
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAnswer();
            }
        });

        nextButton = new JButton("Next question");
        nextButton.setFont(new Font("Arial", Font.PLAIN, 18));
        nextButton.setEnabled(false);
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextQuestion();
            }
        });

        // แสดงผลคะแนน
        scoreLabel = new JLabel("score: " + score, JLabel.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        buttonPanel.add(nextButton);

        panel.add(scoreLabel, BorderLayout.SOUTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);

        nextQuestion();
    }

    private void nextQuestion() {
        num1 = random.nextInt(10) + 1;
        num2 = random.nextInt(10) + 1;
        int operation = random.nextInt(4);

        if (operation == 0) {
            operator = "+";
            answer = num1 + num2;
        } else if (operation == 1) {
            operator = "-";
            answer = num1 - num2;
        } else if (operation == 2) {
            operator = "*";
            answer = num1 * num2;
        } else if (operation == 3) {
            operator = "/";
            answer = num1 / num2;
        }

        questionField.setText(num1 + " " + operator + " " + num2);
        answerField.setText("");
        nextButton.setEnabled(false);
        submitButton.setEnabled(true);
    }

    private void checkAnswer() {
        try {
            int userAnswer = Integer.parseInt(answerField.getText());
            if (userAnswer == answer) {
                score++;
                scoreLabel.setText("score: " + score);
                JOptionPane.showMessageDialog(frame, "correct!");
            } else {
                JOptionPane.showMessageDialog(frame, "Wrong! The correct answer is: " + answer);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Please enter your answer in numbers.!");
        }

        submitButton.setEnabled(false);
        nextButton.setEnabled(true);
    }

    public static void main(String[] args) {
        new MathQuizGameGUI();
    }
}
