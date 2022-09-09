package ui;

import model.Record;
import model.RecordList;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

// Financial Recorder application
public class FinancialRecorder {
    private RecordList records;
    private Record recordNew;
    private Scanner input;
    private Double balance;
    private Double initialBalance;
    private String date;
    private String storeName;
    private String productName;
    private Double price;
    private boolean isItAnExpenditure;
    private int recordNumber;
    private Record selectedRecord;
    private Double newPrice;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private static final String saveDestination = "./data/recordlist.json";

    // EFFECTS: runs the financial recorder
    public FinancialRecorder() throws FileNotFoundException {
        jsonWriter = new JsonWriter(saveDestination);
        jsonReader = new JsonReader(saveDestination);
        runRecorder();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runRecorder() {
        boolean keepAdding = true;
        String command = null;
        init();
        letUserInitializeBalance();
        command = input.next();
        processBalance(command);

        while (keepAdding) {
            showOptions();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("quit")) {
                keepAdding = false;
                System.out.println("Exited Recorder successfully");
            } else {
                processOption(command);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: make a new list of records
    private void init() {
        records = new RecordList();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    // REQUIRES: only enter Double
    // EFFECTS: let user input the initialized balance value
    private void letUserInitializeBalance() {
        System.out.println("What is your current balance?");
    }

    // REQUIRES: only enter Double
    // MODIFIES: this
    // EFFECTS: initialize the user's balance value to input
    private void processBalance(String command) {
        balance = Double.parseDouble(command);
        initialBalance = balance;
    }

    // EFFECTS: displays menu of options to user
    private void showOptions() {
        System.out.println("\nSelect from:");
        System.out.println("\tadd -> add a new record");
        System.out.println("\tchange -> change a existing record");
        System.out.println("\tdelete -> delete an existing record");
        System.out.println("\tview -> view current records");
        System.out.println("\tsave -> save list to file");
        System.out.println("\tload -> load list from file");
        System.out.println("\tquit -> quit");
    }

    // EFFECTS: process the input option
    private void processOption(String command) {
        if (command.equals("add")) {
            doAddRecord();
        } else if (command.equals("change")) {
            doChangeRecord();
        } else if (command.equals("delete")) {
            doDeleteRecord();
        } else if (command.equals("view")) {
            doView();
        } else if (command.equals("save")) {
            doSave();
        } else if (command.equals("load")) {
            doLoad();
        } else {
            System.out.println("Selection is not valid!");
        }
    }

    // MODIFIES: this and balance
    // EFFECTS: add a new record with the given information for record
    //          minus price from balance if it is an expenditure, if saving then add price to balance
    private void doAddRecord() {
        System.out.println("What is the date of this record?");
        date = input.next();
        System.out.println("What is the Store name? (If it is a deposit then enter: saving, or anything you prefer)");
        storeName = input.next();
        System.out.println("What is the Product name? (If it is a deposit then enter: saving, or anything you prefer)");
        productName = input.next();
        System.out.println("What is the price? (If it is a deposit then enter deposit amount) "
                + "Please only enter Double)");
        price = input.nextDouble();
        System.out.println("If it is an expenditure, enter: true ,if it is a deposit, enter: false");
        isItAnExpenditure = input.nextBoolean();

        if (isItAnExpenditure) {
            balance -= price;
        } else {
            balance += price;
        }

        recordNew = new Record(date, storeName, productName, price, isItAnExpenditure);
        records.addRecord(recordNew);

        System.out.println("Your current balance is: " + balance);
    }

    // MODIFIES: this and balance
    // EFFECTS: change an existing record
    //          fix balance after record's price is changed
    private void doChangeRecord() {
        showRecords(records);
        System.out.println("Which record you want to change? (directly input only the number of the record)");
        recordNumber = input.nextInt();
        System.out.println("What is the date of this record?");
        records.getRecord(recordNumber).setDate(input.next());
        System.out.println("What is the Store name? (If it is a deposit then enter: saving, or anything you prefer)");
        records.getRecord(recordNumber).setStoreName(input.next());
        System.out.println("What is the Product name? (If it is a deposit then enter: saving, or anything you prefer)");
        records.getRecord(recordNumber).setProductName(input.next());
        System.out.println("What is the price? (If it is a deposit then enter deposit amount) "
                + "Please only enter Double)");
        newPrice = input.nextDouble();
        System.out.println("If it is an expenditure, enter: true ,if it is a deposit, enter: false");
        initializeBalanceToBeforeThisRecord();

        records.getRecord(recordNumber).setPrice(newPrice);
        records.getRecord(recordNumber).setIsItAnExpenditure(input.nextBoolean());

        changeNewBalance();

        System.out.println("Changed Successfully");
        showRecords(records);
        System.out.println("Your current balance is: " + balance);
    }

    // MODIFIES: this
    // EFFECTS: change balance to before the record was proceeded in the first time
    private void initializeBalanceToBeforeThisRecord() {
        if (records.getRecord(recordNumber).getIsItAnExpenditure() == true) {
            balance += records.getRecord(recordNumber).getPrice();
        } else {
            balance -= records.getRecord(recordNumber).getPrice();
        }
    }

    // MODIFIES: this
    // EFFECTS: change balance accordingly to after the record are changed
    private void changeNewBalance() {
        if (records.getRecord(recordNumber).getIsItAnExpenditure() == true) {
            balance -= records.getRecord(recordNumber).getPrice();
        } else {
            balance += records.getRecord(recordNumber).getPrice();
        }
    }


    // MODIFIES: this and balance
    // EFFECTS: delete an existing record in record list
    //          fix balance after record is deleted
    private void doDeleteRecord() {
        showRecords(records);
        System.out.println("Which records you want to remove? (directly input the number of the record)");
        recordNumber = input.nextInt();
        selectedRecord = records.getRecord(recordNumber);
        if (selectedRecord.getIsItAnExpenditure() == true) {
            balance += selectedRecord.getPrice();
        } else {
            balance -= selectedRecord.getPrice();
        }
        records.deleteRecord(recordNumber);
        System.out.println("Deleted Successfully");
        System.out.println("Your current balance is: " + balance);
    }

    // EFFECTS: display all the records in list
    private void doView() {
        showRecords(records);
    }

    // EFFECTS: display all the records in list
    private void showRecords(RecordList r) {
        for (int i = 0; i < records.getSize(); i++) {
            System.out.println("Record " + i + " | " + records.getRecord(i).displayRecord());
        }
        System.out.println("Your current balance is: " + balance);
    }

    // EFFECTS: Save the records to file
    //          If FileNotFoundException caught, display file not found
    private void doSave() {
        try {
            jsonWriter.open();
            jsonWriter.write(records);
            jsonWriter.close();
            System.out.println("Saved successfully to" + saveDestination);
        } catch (FileNotFoundException e) {
            System.out.println("File not found, unable to save to" + saveDestination);
        }
    }

    // MODIFIES: this
    // EFFECTS: load the saved records from file
    //          Fix the current balance after the records are loaded
    //          if IOException caught, display file not found
    private void doLoad() {
        try {
            balance = initialBalance;
            records = jsonReader.read();
            System.out.println("Loaded Record List from" + saveDestination);
        } catch (IOException e) {
            System.out.println("File not found, unable to load.");
        }

        for (int i = 0; i < records.getSize(); i++) {
            if (records.getRecord(i).getIsItAnExpenditure() == true) {
                balance -= records.getRecord(i).getPrice();
            } else {
                balance += records.getRecord(i).getPrice();
            }
        }
    }
}
