package ui;

import model.Event;
import model.EventLog;
import model.Record;
import model.RecordList;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FinancialRecorderUI extends JFrame implements ActionListener {
    private static int WIDTH = 1300;
    private static final int HEIGHT = 600;
    private static final int PANELWIDTH = WIDTH -= 200;
    private JPanel displayRecordArea;
    private JPanel inputArea;
    private JPanel allTopArea;
    private JButton enterButton;
    private JPanel requirePanel;
    private JLabel currentBalanceDisplay;
    private JPanel saveAndLoadPanel;
    private JPanel middlePanel;

    private JTextField dateTextField;
    private JTextField storeTextField;
    private JTextField productTextField;
    private JTextField priceTextField;
    private JTextField expenditureTextField;

    private String balanceSetup = "Your Current Balance is: ";

    private RecordList records;
    private Double balance;
    private Double initialBalance;

    private String date;
    private String storeName;
    private String productName;
    private Double price;
    private boolean isItAnExpenditure;

    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private static final String saveDestination = "./data/recordlist.json";

    public FinancialRecorderUI() {
        super("Financial Recorder");
        initializeFields();
        initializePage();
        initializeMiddlePage();
    }

    // EFFECTS: initializes a new records list and set all needed fields
    private void initializeFields() {
        records = new RecordList();
        this.initialBalance = LaunchPage.getBalance();
        balance = initialBalance;
        jsonWriter = new JsonWriter(saveDestination);
        jsonReader = new JsonReader(saveDestination);
    }

    // EFFECTS: initialize the setup page for user
    private void initializePage() {
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH,HEIGHT);
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(20,20,20,20));
        setVisible(true);
        setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 5,
                Toolkit.getDefaultToolkit().getScreenSize().height / 9);
        setDisplayArea();
        setInputArea();
        setResizable(false);
        setLocationRelativeTo(null);
    }

    // EFFECTS: initialize the middle panel on page
    private void initializeMiddlePage() {
        middlePanel = new JPanel(new GridLayout(2,1));
        initializeDelete();
        initializeBalance();
        add(middlePanel, BorderLayout.CENTER);
    }

    private void initializeDelete() {
        JPanel deletePanel = new JPanel(new GridLayout(1,3));
        //deletePanel.setFont(new Font("Consolas", Font.PLAIN, 50));
        JLabel delete = new JLabel("ENTER THE RECORD NUMBER YOU WANT TO DELETE:");
        JTextField deleteTextField = new JTextField(60);
        JButton enterButton = new JButton("ENTER");
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int num = Integer.parseInt(deleteTextField.getText());
                for (int i = 0; i < records.getSize(); i++) {
                    if (i == num) {
                        records.deleteRecord(i);
                        displayRecords();
                        fixBalance();
                        changeDisplayedBalance();
                    }
                }
                deleteTextField.setText(null);
            }
        });
        deletePanel.add(delete);
        deletePanel.add(deleteTextField);
        deletePanel.add(enterButton);

        middlePanel.add(deletePanel);
    }

    // EFFECTS: initialize the display of balance
    private void initializeBalance() {
        currentBalanceDisplay = new JLabel();
        currentBalanceDisplay.setText(balanceSetup + balance);
        currentBalanceDisplay.setFont(new Font("Consolas", Font.PLAIN, 20));
        middlePanel.add(currentBalanceDisplay);
    }


    // EFFECTS: initialize the display area for added record lists
    private void setDisplayArea() {
        allTopArea = new JPanel(new GridLayout(4,1));

        allTopArea.setBackground(Color.lightGray);
        allTopArea.setPreferredSize(new Dimension(PANELWIDTH,400));
        allTopArea.setVisible(true);
        add(allTopArea, BorderLayout.NORTH);

        setUpSave();
        setUpLoad();
        setUpImage();
        setUpQuit();
        allTopArea.add(saveAndLoadPanel);
        setUpDisplayRecordFieldTitleArea();
        setUpDisplayRecordArea();
    }

    // EFFECTS: set up the save option
    private void setUpSave() {
        saveAndLoadPanel = new JPanel(new GridLayout(1, 4));
        //saveAndLoadPanel.setPreferredSize(new Dimension(PANELWIDTH,30));
        JButton saveButton = new JButton("SAVE FILE");

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    jsonWriter.open();
                    jsonWriter.write(records);
                    jsonWriter.close();
                    //System.out.println("Saved successfully to" + saveDestination);
                } catch (FileNotFoundException e) {
                    System.out.println("File not found, unable to save to" + saveDestination);
                }
            }
        });
        saveAndLoadPanel.add(saveButton);
    }

    // EFFECTS: set up the load option
    private void setUpLoad() {
        JButton loadButton = new JButton("LOAD FILE");

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    balance = initialBalance;
                    records = jsonReader.read();
                    displayRecords();
                    //System.out.println("Loaded Record List from" + saveDestination);
                } catch (IOException e) {
                    System.out.println("File not found, unable to load.");
                }

                fixBalance();
                changeDisplayedBalance();
            }
        });
        saveAndLoadPanel.add(loadButton);
    }

    // EFFECTS: display an image between the two buttons
    private void setUpImage() {
        BufferedImage myImage = null;
        JLabel picLabel = new JLabel();
        Image scaledImage = null;
        try {
            myImage = ImageIO.read(new File("./data/moneyImage.png"));
            scaledImage = myImage.getScaledInstance(100,108,Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        picLabel.setIcon(new ImageIcon(scaledImage));
        saveAndLoadPanel.add(picLabel);
    }

    // MODIFIES: this
    // EFFECTS: display a quit button
    private void setUpQuit() {
        JButton quitButton = new JButton("QUIT");

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                dispose();
                for (Event next: EventLog.getInstance()) {
                    System.out.println(next.toString());
                }
            }
        });
        saveAndLoadPanel.add(quitButton);

    }

    // EFFECTS: display the title of the record fields
    private void setUpDisplayRecordFieldTitleArea() {
        JPanel displayPanel = new JPanel(new GridLayout(1,6));
        displayPanel.setBackground(Color.pink);
        displayPanel.setFont(new Font("Consolas", Font.PLAIN, 50));

        JLabel recordNum = new JLabel("RECORD NUMBER");
        displayPanel.add(recordNum);

        JLabel date = new JLabel("DATE");
        displayPanel.add(date);

        JLabel store = new JLabel("STORE");
        displayPanel.add(store);

        JLabel product = new JLabel("PRODUCT");
        displayPanel.add(product);

        JLabel price = new JLabel("PRICE");
        displayPanel.add(price);

        JLabel expenditure = new JLabel("EXPENDITURE?");
        displayPanel.add(expenditure);

        allTopArea.add(displayPanel);
    }

    // EFFECTS: set up the display of the records
    private void setUpDisplayRecordArea() {
        displayRecordArea = new JPanel(new GridLayout(0,1)); //！！！
        displayRecordArea.setBackground(Color.lightGray);
        //displayRecordArea.setPreferredSize(new Dimension(PANELWIDTH,400));
        displayRecordArea.setVisible(true);
        JScrollPane scrollPane = new JScrollPane(displayRecordArea);
        displayRecordArea.setAutoscrolls(false);

        allTopArea.add(scrollPane); // !!!
    }

    // EFFECTS: initialize the user input area for adding new record and button
    private void setInputArea() {
        JPanel inputPanel = new JPanel(new GridLayout(3, 1));
        inputPanel.setBackground(Color.lightGray);
        inputPanel.setVisible(true);
        add(inputPanel, BorderLayout.SOUTH);

        JLabel warning = new JLabel("YOU MUST ENTER ALL FIELDS BEFORE PRESSING THE BUTTON");
        warning.setForeground(Color.RED);
        inputPanel.add(warning);

        inputArea = new JPanel(new GridLayout(1,10));
        inputArea.setBackground(Color.lightGray);
        inputArea.setVisible(true);
        addInputFields();
        enterButton = new JButton("ENTER");
        enterButton.addActionListener(this);
        inputArea.add(enterButton);
        inputPanel.add(inputArea);

        requirePanel = new JPanel(new GridLayout(1, 10));
        requirePanel.setBackground(Color.lightGray);
        addRequire();
        inputPanel.add(requirePanel);
    }


    // EFFECTS: initialize the user input area fields
    private void addInputFields() {
        JLabel date = new JLabel("DATE:");
        dateTextField = new JTextField(60);
        inputArea.add(date);
        inputArea.add(dateTextField);

        JLabel store = new JLabel("STORE:");
        storeTextField = new JTextField(60);
        inputArea.add(store);
        inputArea.add(storeTextField);

        JLabel product = new JLabel("PRODUCT:");
        productTextField = new JTextField(60);
        inputArea.add(product);
        inputArea.add(productTextField);

        JLabel price = new JLabel("PRICE:");
        priceTextField = new JTextField(20);
        inputArea.add(price);
        inputArea.add(priceTextField);

        JLabel expenditure = new JLabel("EXPENDITURE?");
        expenditureTextField = new JTextField(20);
        inputArea.add(expenditure);
        inputArea.add(expenditureTextField);
    }

    // EFFECTS: add requirements for user input
    private void addRequire() {
        JLabel dateRequire = new JLabel("");
        requirePanel.add(dateRequire);
        requirePanel.add(dateRequire);

        JLabel storeRequire = new JLabel("");
        requirePanel.add(storeRequire);
        requirePanel.add(storeRequire);

        JLabel productRequire = new JLabel("");
        requirePanel.add(productRequire);
        requirePanel.add(productRequire);

        JLabel priceRequire = new JLabel("ONLY NUMBERS");
        priceRequire.setForeground(Color.RED);
        requirePanel.add(priceRequire);
        requirePanel.add(priceRequire);

        JLabel expenditureRequire = new JLabel("ONLY BOOLEAN");
        expenditureRequire.setForeground(Color.RED);
        requirePanel.add(expenditureRequire);
        requirePanel.add(expenditureRequire);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == enterButton) {
            this.date = dateTextField.getText();
            this.storeName = storeTextField.getText();
            this.productName = productTextField.getText();
            this.price = Double.valueOf(priceTextField.getText());
            this.isItAnExpenditure = Boolean.parseBoolean(expenditureTextField.getText());

            makeNewRecord();
            displayRecords();
            changeDisplayedBalance();

            dateTextField.setText(null);
            storeTextField.setText(null);
            productTextField.setText(null);
            priceTextField.setText(null);
            expenditureTextField.setText(null);
        }
    }

    // MODIFIES: records, balance
    // EFFECTS: with given user input, make new record and add it to list, and change balance accordingly to record
    private void makeNewRecord() {
        if (price < 0) {
            this.isItAnExpenditure = true;
            this.price = price * -1;
            balance -= price;
        } else {
            if (isItAnExpenditure) {
                balance -= price;
            } else {
                balance += price;
            }
        }

        Record recordNew = new Record(date, storeName, productName, price, isItAnExpenditure);
        records.addRecord(recordNew);
    }

    // EFFECTS: display the new added record
    private void displayNewRecord() {
        JPanel singleRecordPanel = new JPanel(new GridLayout(1, 6));
        singleRecordPanel.setBackground(Color.lightGray);
        singleRecordPanel.setPreferredSize(new Dimension(PANELWIDTH,50));
        singleRecordPanel.setFont(new Font("Consolas", Font.PLAIN, 50));

        JLabel recordNum = new JLabel(String.valueOf(records.getSize() - 1));
        singleRecordPanel.add(recordNum);

        JLabel date = new JLabel(this.date);
        singleRecordPanel.add(date);

        JLabel store = new JLabel(this.storeName);
        singleRecordPanel.add(store);

        JLabel product = new JLabel(this.productName);
        singleRecordPanel.add(product);

        JLabel price = new JLabel(String.valueOf(this.price));
        singleRecordPanel.add(price);

        JLabel expenditure = new JLabel(String.valueOf(this.isItAnExpenditure));
        singleRecordPanel.add(expenditure);

        displayRecordArea.add(singleRecordPanel);
    }

    // EFFECTS: display all records
    private void displayRecords() {
        displayRecordArea.removeAll();
        for (int i = 0; i < records.getSize(); i++) {
            JPanel allRecordsPanel = new JPanel(new GridLayout(1, 6));
            allRecordsPanel.setBackground(Color.lightGray);
            allRecordsPanel.setPreferredSize(new Dimension(PANELWIDTH,50));
            allRecordsPanel.setFont(new Font("Consolas", Font.PLAIN, 50));

            Record r = records.getRecord(i);

            JLabel recordNum = new JLabel(String.valueOf(i));
            allRecordsPanel.add(recordNum);

            JLabel date = new JLabel(r.getDate());
            allRecordsPanel.add(date);

            JLabel store = new JLabel(r.getStoreName());
            allRecordsPanel.add(store);

            JLabel product = new JLabel(r.getProductName());
            allRecordsPanel.add(product);

            JLabel price = new JLabel(String.valueOf(r.getPrice()));
            allRecordsPanel.add(price);

            JLabel expenditure = new JLabel(String.valueOf(r.getIsItAnExpenditure()));
            allRecordsPanel.add(expenditure);

            displayRecordArea.add(allRecordsPanel);
        }
    }

    // EFFECTS: change the displayed balance to current balance
    private void changeDisplayedBalance() {
        currentBalanceDisplay.setText(balanceSetup + this.balance);
    }

    // MODIFIES: balance
    // EFFECTS: fix balance when records are changed
    private void fixBalance() {
        balance = initialBalance;
        for (int i = 0; i < records.getSize(); i++) {
            if (records.getRecord(i).getIsItAnExpenditure() == true) {
                balance -= records.getRecord(i).getPrice();
            } else {
                balance += records.getRecord(i).getPrice();
            }
        }
    }
}
