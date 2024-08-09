import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount > balance) {
            return false; // Insufficient balance
        }
        balance -= amount;
        return true;
    }
}

class User {
    private String name;
    private int pin;
    private BankAccount account;

    public User(String name, int pin, double initialBalance) {
        this.name = name;
        this.pin = pin;
        this.account = new BankAccount(initialBalance);
    }

    public String getName() {
        return name;
    }

    public int getPin() {
        return pin;
    }

    public BankAccount getAccount() {
        return account;
    }
}

class ATM {
    private User currentUser;

    public ATM() {}

    public boolean login(User user, int enteredPin) {
        if (user.getPin() == enteredPin) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public void logout() {
        currentUser = null;
    }

    public boolean withdraw(double amount) {
        return currentUser.getAccount().withdraw(amount);
    }

    public void deposit(double amount) {
        currentUser.getAccount().deposit(amount);
    }

    public double checkBalance() {
        return currentUser.getAccount().getBalance();
    }

    public User getCurrentUser() {
        return currentUser;
    }
}

public class ATMInterface {
    private static final double INITIAL_BALANCE = 1000.00;

    public static void main(String[] args) {
        User[] users = {
            new User("Rahul", 1234, INITIAL_BALANCE),
            new User("Boby", 2345, INITIAL_BALANCE),
            new User("Dinesh", 3456, INITIAL_BALANCE),
            new User("Ritesh", 4567, INITIAL_BALANCE),
            new User("Karan", 5678, INITIAL_BALANCE)
        };

        ATM atm = new ATM();
        
        JFrame frame = new JFrame("ATM Interface");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel loginPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(173, 216, 230),
                        0, getHeight(), new Color(100, 149, 237));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        loginPanel.setLayout(new GridLayout(4, 1, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel userLabel = new JLabel("User Name:");
        JComboBox<String> userComboBox = new JComboBox<>();
        for (User user : users) {
            userComboBox.addItem(user.getName());
        }

        JLabel pinLabel = new JLabel("ATM PIN:");
        JPasswordField pinField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        setButtonStyle(loginButton);

        loginPanel.add(userLabel);
        loginPanel.add(userComboBox);
        loginPanel.add(pinLabel);
        loginPanel.add(pinField);
        loginPanel.add(loginButton);

        frame.add(loginPanel, BorderLayout.CENTER);
        frame.setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedUser = (String) userComboBox.getSelectedItem();
                int enteredPin = Integer.parseInt(new String(pinField.getPassword()));

                for (User user : users) {
                    if (user.getName().equals(selectedUser) && atm.login(user, enteredPin)) {
                        showMainPanel(frame, atm);
                        return;
                    }
                }
                JOptionPane.showMessageDialog(frame,
                        "Invalid username or PIN. Please try again.",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private static void showMainPanel(JFrame frame, ATM atm) {
        frame.getContentPane().removeAll();

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(173, 216, 230),
                        0, getHeight(), new Color(100, 149, 237));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridLayout(5, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton checkBalanceButton = new JButton("Check Balance");
        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton logoutButton = new JButton("Logout");

        setButtonStyle(checkBalanceButton);
        setButtonStyle(depositButton);
        setButtonStyle(withdrawButton);
        setButtonStyle(logoutButton);

        mainPanel.add(checkBalanceButton);
        mainPanel.add(depositButton);
        mainPanel.add(withdrawButton);
        mainPanel.add(logoutButton);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();

        checkBalanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame,
                        "Your current balance is: $" + atm.checkBalance(),
                        "Balance Check", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog(frame, "Enter amount to deposit:");
                if (input != null) {
                    try {
                        double amount = Double.parseDouble(input);
                        if (amount <= 0) {
                            JOptionPane.showMessageDialog(frame,
                                    "Please enter a valid deposit amount.",
                                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
                        } else {
                            atm.deposit(amount);
                            JOptionPane.showMessageDialog(frame,
                                    "Successfully deposited: $" + amount,
                                    "Deposit Successful", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame,
                                "Invalid input. Please enter a numeric value.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog(frame, "Enter amount to withdraw:");
                if (input != null) {
                    try {
                        double amount = Double.parseDouble(input);
                        if (amount <= 0) {
                            JOptionPane.showMessageDialog(frame,
                                    "Please enter a valid withdrawal amount.",
                                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
                        } else if (atm.withdraw(amount)) {
                            JOptionPane.showMessageDialog(frame,
                                    "Successfully withdrew: $" + amount,
                                    "Withdrawal Successful", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(frame,
                                    "Insufficient balance.",
                                    "Withdrawal Failed", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame,
                                "Invalid input. Please enter a numeric value.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atm.logout();
                frame.getContentPane().removeAll();
                main(null); // Restart the application
            }
        });
    }

    private static void setButtonStyle(JButton button) {
        button.setBackground(new Color(135, 206, 250));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
    }
}