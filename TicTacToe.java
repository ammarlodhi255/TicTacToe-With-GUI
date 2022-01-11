import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;

public class TicTacToe extends JFrame {
    private JLabel jLabel;
    private JPanel midPanel;
    private JButton b1 = new JButton(""), b2 = new JButton(""), b3 = new JButton(""),
    b4 = new JButton(""), b5 = new JButton(""), b6 = new JButton(""),
    b7 = new JButton(""), b8 = new JButton(""), b9 = new JButton("");
    private byte buttonClick = 0;
    private String prevClick = new String(new Random().nextInt(2) == 0 ? "X" : "O");
    private JButton[][] buttonArray = {{b1, b2, b3}, {b4, b5, b6}, {b7, b8, b9}};
    private ListenToButtonEvent listen;
    private TicTacToe currentFrame = this;
    private JButton reset;

    public TicTacToe() {
        jLabel = new JLabel("TIC-TAC-TOE", SwingConstants.CENTER); // setting ordinary label for now. When the player wins or if there is a tie, label will be updated
        midPanel = new JPanel();
        listen = new ListenToButtonEvent();
        reset = new JButton("Reset");

        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                currentFrame.dispose();
                main(new String[0]);
            }
        });

        reset.setFocusable(false);
        reset.setBackground(new Color(59, 89, 182));
        reset.setForeground(new Color(255, 255, 255));
        reset.setFont(new Font("Garamond", Font.BOLD + Font.ITALIC, 30));
        jLabel.setFont(new Font("Garamond", Font.BOLD + Font.ITALIC, 50));
        setLayout(new BorderLayout());
        add(reset, BorderLayout.SOUTH);
        add(jLabel, BorderLayout.NORTH);
        midPanel.setLayout(new GridLayout(3, 3, 3, 3));

        for(int i = 0; i < buttonArray.length; i++)
            for(int j = 0; j < buttonArray[i].length; j++) {
                buttonArray[i][j].setName("");
                buttonArray[i][j].setFont(new Font("Aerial", Font.BOLD, 100));
                buttonArray[i][j].setFocusable(false);
                buttonArray[i][j].addActionListener(listen);
                midPanel.add(buttonArray[i][j]);
            }

        midPanel.setBackground(new Color(59, 89, 182));
        add(midPanel);
        setTitle("TIC-TAC-TOE");
        setSize(840, 600);
        setVisible(true);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private class ListenToButtonEvent implements ActionListener {
        boolean found;
        String toCheck;
        boolean stop;
        int victoryRow = -1, victoryCol = -1, victoryDiagonal = -1;
        public void actionPerformed(ActionEvent evt) {
            if(prevClick.equals("X")) {
                ((JButton) evt.getSource()).setText("O");
                ((JButton) evt.getSource()).setName("O");
                prevClick = "O";
            }
            else if(prevClick.equals("O")) {
                ((JButton) evt.getSource()).setText("X");
                ((JButton) evt.getSource()).setName("X");
                prevClick = "X";
            }
            ((JButton) evt.getSource()).removeActionListener(this);

            reset();
            for(int row = 0; row < buttonArray.length; row++) {
                for(int col = 0; col < buttonArray[row].length; col++) {
                    if(buttonArray[row][col].getName().equals(toCheck))
                        found = true;
                    else {
                        found = false;
                        break;
                    }
                }
                if(found) {
                    victoryRow = row;
                    break;
                } else if(!stop && row == buttonArray.length - 1) {
                    row = -1;
                    toCheck = "O";
                    stop = true;
                }
            }

            if(!found) {
                reset();
                for(int col = 0; col < buttonArray.length; col++) {
                    for(int row = 0; row < buttonArray[col].length; row++) {
                        if(buttonArray[row][col].getName().equals(toCheck))
                            found = true;
                        else {
                            found = false;
                            break;
                        }
                    }
                    if(found) {
                        victoryCol = col;
                        break;
                    } else if(!stop && col == buttonArray.length - 1) {
                        col = -1;
                        toCheck = "O";
                        stop = true;
                    }
                }
            }

            if(!found) {
                reset();
                for(int i = 1; i <= 2; i++) {
                    for(int row = 0; row < buttonArray.length; row++) {
                        if(buttonArray[row][row].getName().equals(toCheck))
                            found = true;
                        else {
                            found = false;
                            break;
                        }
                    }
                    if(found) {
                        victoryDiagonal = 1;
                        break;
                    }
                    toCheck = "O";
                }
            }

            if(!found) {
                reset();
                for(int i = 1; i <= 2; i++) {
                    int row0 = 0;
                    for(int row = buttonArray.length - 1; row >= 0; row--) {
                        if(buttonArray[row0][row].getName().equals(toCheck))
                            found = true;
                        else {
                            found = false;
                            break;
                        }
                        row0++;
                    }
                    if(found) {
                        victoryDiagonal = 2;
                        break;
                    }
                    toCheck = "O";
                }
            }

            buttonClick++;
            if(found) setPlayerWon();
            else if(buttonClick == 9) {
                jLabel.setText("It's a Tie!!");
                for(int i = 0; i < buttonArray.length; i++)
                    for(int j = 0; j < buttonArray[i].length; j++)
                    	buttonArray[i][j].setForeground(new Color(255, 0, 0));
            }
        }

        public void reset() {
            toCheck = "X";
            stop = false;
            found = false;
        }

        public void setPlayerWon() {
            jLabel.setText("\"" + toCheck + "\"" + " Player won");
            jLabel.setForeground(new Color(59, 89, 182));
            if(victoryRow != -1)
                for(int i = 0; i < buttonArray.length; i++)
                    buttonArray[victoryRow][i].setForeground(new Color(50, 205, 50));
            else if(victoryCol != -1)
                for(int i = 0; i < buttonArray.length; i++)
                    buttonArray[i][victoryCol].setForeground(new Color(50, 205, 50));
            else if(victoryDiagonal == 1)
                for(int i = 0; i < buttonArray.length; i++)
                    buttonArray[i][i].setForeground(new Color(50, 205, 50));
            else if(victoryDiagonal == 2) {
                int row0 = 0;
                for(int i = buttonArray.length - 1; i >= 0; i--) {
                    buttonArray[row0][i].setForeground(new Color(50, 205, 50));
                    row0++;
                }
            }
            for(int i = 0; i < buttonArray.length; i++)
                for(int j = 0; j < buttonArray[i].length; j++)
                    buttonArray[i][j].removeActionListener(listen);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TicTacToe();
            }
        });
    }
}
