package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents a record of expenditure/deposit with
// date, storeName, productName, price, and if it is an expenditure (in dollar)
public class Record implements Writable {
    private String date;                 //The date of the record
    private String storeName;            //Store name
    private String productName;          //Product name
    private double price;                //Price of the product or the Amount that want to deposit
    private boolean isItAnExpenditure;   //If the record is an expenditure or deposit

    // REQUIRES: date, storeName, and productName has a non-zero length
    // EFFECTS: record will have given date, storeName, productName, and price shown
    //          If the record records an expenditure (isItAnExpenditure = true), price will be subtracted
    //          If the record records a deposit (isItAnExpenditure = false), price will be added
    //          If price < 0, then price is set to positive and isItAnExpenditure is set to true, as it will be
    //             automatically interpreted as an expenditure (decrease balance)
    public Record(String date, String storeName, String productName, double price, boolean isItAnExpenditure) {
        this.date = date;
        this.storeName = storeName;
        this.productName = productName;
        if (price < 0) {
            this.price = price * -1;
            this.isItAnExpenditure = true;
        } else {
            this.price = price;
            this.isItAnExpenditure = isItAnExpenditure;
        }
    }

    public void setDate(String d) {
        date = d;
    }

    public String getDate() {
        return date;
    }

    public void setStoreName(String s) {
        storeName = s;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setProductName(String pn) {
        productName = pn;
    }

    public String getProductName() {
        return productName;
    }

    public void setPrice(double p) {
        price = p;
    }

    public double getPrice() {
        return price;
    }

    public void setIsItAnExpenditure(boolean e) {
        isItAnExpenditure = e;
    }

    public boolean getIsItAnExpenditure() {
        return isItAnExpenditure;
    }

    // EFFECTS: return record as a String (Displays the record)
    public String displayRecord() {
        return ("Date: " + date + " | " + "Store Name: " + storeName + " | " + "Product Name: "
                + productName + " | " + "Price: " + Double.toString(price) + " | " + "Is It An Expenditure: "
                + Boolean.toString(isItAnExpenditure));
    }

    // EFFECTS: returns this as JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Date", date);
        json.put("StoreName", storeName);
        json.put("ProductName", productName);
        json.put("Price", price);
        json.put("IsItAnExpenditure", isItAnExpenditure);
        return json;
    }

}
