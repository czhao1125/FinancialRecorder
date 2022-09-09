package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LaunchPage extends JFrame implements ActionListener {

    private JButton enterButton;
    private JTextField textField;
    private JLabel enterLabel;
    private static Double balance;

    // EFFECTS: construct a launch page for user to input current balance
    LaunchPage() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new FlowLayout());

        enterButton = new JButton();
        enterButton.setText("ENTER");
        enterButton.addActionListener(this);

        // REQUIRE: Only accepts integers
        textField = new JTextField();
        textField.setPreferredSize(new Dimension(300, 60));
        textField.setFont(new Font("Consolas", Font.PLAIN, 50));
        //textField.setForeground(Color.BLACK);
        textField.setBackground(Color.LIGHT_GRAY);
        //textField.setText("Your current Balance is: ");

        enterLabel = new JLabel();
        enterLabel.setText("Your current Balance is: ");
        enterLabel.setFont(new Font("Consolas", Font.PLAIN,30));
        enterLabel.setForeground(Color.BLACK);
        enterLabel.setOpaque(true);

        this.add(enterLabel);
        this.add(textField);
        this.add(enterButton);

        this.pack();
        this.setVisible(true);
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 300,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 10);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == enterButton) {
            this.dispose();
            balance = Double.parseDouble(textField.getText());
            new ui.FinancialRecorderUI();
        }
    }

    public static Double getBalance() {
        return balance;
    }
}
