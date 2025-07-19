import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        new QuizApp();
    }
}

class QuizApp extends JFrame implements ActionListener {
    ArrayList<Question> questions = new ArrayList<>();
    int current = 0, score = 0;
    JLabel qLabel, timerLabel;
    JRadioButton[] options = new JRadioButton[4];
    ButtonGroup bg = new ButtonGroup();
    JButton nextBtn, submitBtn;
    javax.swing.Timer timer;
    int timeLeft = 15;

    public QuizApp() {
        loadQuestionsFromFile("questions.txt");
        createUI();
        startTimer();
        displayQuestion(current);
    }
    
    // Question class definition
    class Question {
        String question;
        String optionA;
        String optionB;
        String optionC;
        String optionD;
        String correctOption;
    
        public Question(String question, String optionA, String optionB, String optionC, String optionD, String correctOption) {
            this.question = question;
            this.optionA = optionA;
            this.optionB = optionB;
            this.optionC = optionC;
            this.optionD = optionD;
            this.correctOption = correctOption;
        }
    }

    void createUI() {
        setTitle("Online Quiz System");
        setSize(600, 400);
        setLayout(null);

        qLabel = new JLabel();
        qLabel.setBounds(50, 30, 500, 30);
        add(qLabel);

        int y = 70;
        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            options[i].setBounds(50, y, 500, 30);
            bg.add(options[i]);
            add(options[i]);
            y += 40;
        }

        timerLabel = new JLabel("Time Left: 15s");
        timerLabel.setBounds(400, 10, 150, 30);
        add(timerLabel);

        nextBtn = new JButton("Next");
        nextBtn.setBounds(150, 250, 100, 30);
        nextBtn.addActionListener(this);
        add(nextBtn);

        submitBtn = new JButton("Submit");
        submitBtn.setBounds(300, 250, 100, 30);
        submitBtn.addActionListener(this);
        add(submitBtn);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    void displayQuestion(int index) {
        if (index >= questions.size()) return;

        Question q = questions.get(index);
        qLabel.setText("Q" + (index + 1) + ": " + q.question);
        options[0].setText(q.optionA);
        options[1].setText(q.optionB);
        options[2].setText(q.optionC);
        options[3].setText(q.optionD);
        bg.clearSelection();
    }

    void loadQuestionsFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    questions.add(new Question(data[0], data[1], data[2], data[3], data[4], data[5]));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading questions!");
        }
    }

    void startTimer() {
        timer = new javax.swing.Timer(1000, _ -> {
            timeLeft--;
            timerLabel.setText("Time Left: " + timeLeft + "s");
            if (timeLeft <= 0) {
                nextQuestion();
            }
        });
        timer.start();
    }

    void nextQuestion() {
        checkAnswer();
        current++;
        if (current < questions.size()) {
            displayQuestion(current);
            timeLeft = 15;
        } else {
            timer.stop();
            showResult();
        }
    }

    void checkAnswer() {
        if (bg.getSelection() == null) return;

        String selected = "";
        if (options[0].isSelected()) selected = "A";
        else if (options[1].isSelected()) selected = "B";
        else if (options[2].isSelected()) selected = "C";
        else if (options[3].isSelected()) selected = "D";

        if (selected.equals(questions.get(current).correctOption)) {
            score++;
        }
    }

    void showResult() {
        JOptionPane.showMessageDialog(this, "Quiz Over!\nYour Score: " + score + "/" + questions.size());
        System.exit(0);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextBtn) {
            nextQuestion();
        } else if (e.getSource() == submitBtn) {
            timer.stop();
            checkAnswer();
            showResult();
        }
    }
}
