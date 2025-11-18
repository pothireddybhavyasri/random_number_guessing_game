import javax.swing.*;
import java.awt.*;
import java.io.*;

public class random extends JFrame {

    int min = 1, max = 100, number, attempts = 0, bestScore = Integer.MAX_VALUE;
    String playerName = "Player";
    JTextField guessField;
    JLabel msgLabel, attemptsLabel, hintLabel, bestLabel;

    public random() {

        askPlayerName();
        loadBestScore();
        generateNumber();

        setTitle("Guess the Number");
        setSize(380, 360);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        msgLabel = new JLabel("Guess a number between " + min + " and " + max);
        msgLabel.setBounds(40, 20, 350, 30);
        add(msgLabel);

        JLabel nameLabel = new JLabel("Player: " + playerName);
        nameLabel.setBounds(40, 50, 200, 30);
        add(nameLabel);

        guessField = new JTextField();
        guessField.setBounds(120, 90, 140, 35);
        add(guessField);

        JButton guessButton = new JButton("GUESS");
        guessButton.setBounds(130, 140, 110, 40);
        add(guessButton);

        attemptsLabel = new JLabel("Attempts: 0");
        attemptsLabel.setBounds(40, 190, 200, 30);
        add(attemptsLabel);

        hintLabel = new JLabel("Hint: ???");
        hintLabel.setBounds(40, 220, 300, 30);
        add(hintLabel);

        bestLabel = new JLabel("Best Score: " + (bestScore == Integer.MAX_VALUE ? "None" : bestScore));
        bestLabel.setBounds(40, 250, 200, 30);
        add(bestLabel);

        guessButton.addActionListener(e -> handleGuess());

        setVisible(true);
    }

    private void handleGuess() {
        try {
            int guess = Integer.parseInt(guessField.getText());
            attempts++;
            attemptsLabel.setText("Attempts: " + attempts);

            if (guess > number) {
                msgLabel.setText("Too High!");
                hintLabel.setText(getHint());
            } else if (guess < number) {
                msgLabel.setText("Too Low!");
                hintLabel.setText(getHint());
            } else {
                msgLabel.setText("Correct!");
                showConfettiPopup();
                saveBestScore();
                JOptionPane.showMessageDialog(this, "Correct! Number: " + number +
                        "\nAttempts: " + attempts);
                generateNumber();
                attempts = 0;
                attemptsLabel.setText("Attempts: 0");
            }
            guessField.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid number!");
        }
    }

    private void askPlayerName() {
        playerName = JOptionPane.showInputDialog("Enter your name:");
        if (playerName == null || playerName.isEmpty())
            playerName = "Player";
    }

    private void loadBestScore() {
        try {
            File f = new File("bestscore.txt");
            if (!f.exists())
                return;
            BufferedReader br = new BufferedReader(new FileReader(f));
            bestScore = Integer.parseInt(br.readLine());
            br.close();
        } catch (Exception ignored) {
        }
    }

    private void saveBestScore() {
        if (attempts < bestScore) {
            bestScore = attempts;
            try {
                PrintWriter pw = new PrintWriter("bestscore.txt");
                pw.println(bestScore);
                pw.close();
            } catch (Exception ignored) {
            }
        }
    }

    private String getHint() {
        if (attempts <= 1)
            return "Hint: Close!";
        return (number % 2 == 0) ? "Hint: Number is EVEN" : "Hint: Number is ODD";
    }

    private void generateNumber() {
        number = (int) (Math.random() * (max - min + 1)) + min;
    }

    public static void showConfettiPopup() {
        JDialog popup = new JDialog();
        popup.setSize(250, 250);
        popup.add(new ConfettiPanel(70));
        popup.setLocationRelativeTo(null);
        popup.setVisible(true);
    }

    public static void main(String[] args) {
        new random();
    }
}

class ConfettiPanel extends JPanel {
    int[] x, y, speed;
    Color[] colors;

    ConfettiPanel(int count) {
        x = new int[count];
        y = new int[count];
        speed = new int[count];
        colors = new Color[count];
        for (int i = 0; i < count; i++) {
            x[i] = (int) (Math.random() * 250);
            y[i] = (int) (Math.random() * -250);
            speed[i] = 2 + (int) (Math.random() * 4);
            colors[i] = new Color((int) (Math.random() * 255), (int) (Math.random() * 255),
                    (int) (Math.random() * 255));
        }
        new Timer(20, e -> {
            for (int i = 0; i < count; i++) {
                y[i] += speed[i];
                if (y[i] > 250)
                    y[i] = -20;
            }
            repaint();
        }).start();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < x.length; i++) {
            g.setColor(colors[i]);
            g.fillOval(x[i], y[i], 10, 10);
        }
    }
}