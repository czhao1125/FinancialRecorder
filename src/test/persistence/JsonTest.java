package persistence;

import model.Record;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Citation: JSonSerializationDemo Project

public class JsonTest {
    protected void checkRecord(String date, String storeName, String productName, double price,
                               boolean isItAnExpenditure, Record record) {
        assertEquals(date, record.getDate());
        assertEquals(storeName, record.getStoreName());
        assertEquals(productName, record.getProductName());
        assertEquals(price, record.getPrice());
        assertEquals(isItAnExpenditure, record.getIsItAnExpenditure());
    }
}
