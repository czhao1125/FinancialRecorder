package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecordTest {
    private Record saveOnFood;
    private Record cornerStore;
    private Record bookStore;
    private Record saving1;
    private Record saving2;
    private Record dollarma;

    @BeforeEach
    public void setup() {
        saveOnFood = new Record("2022.02.03", "SaveOnFood", "Strawberries",
                5.99, true);
        cornerStore = new Record("2022.02.03", "CornerStore", "Milk", 6.89,
                true);
        bookStore = new Record("2022.02.04", "BookStore", "Book", 92.00,
                true);
        saving1 = new Record("2022.02.04", "Saving1", "Saving1", 100,
                false);
        saving2 = new Record("2022.02.05", "Saving2", "Saving2", 1,
                false);

        dollarma = new Record("2022.02.04", "Dollarma", "Cookie", -10,
                false);
    }

    @Test
    public void testConstructor() {
        assertEquals("2022.02.03", saveOnFood.getDate());
        assertEquals("CornerStore", cornerStore.getStoreName());
        assertEquals("Book", bookStore.getProductName());
        assertTrue(saveOnFood.getIsItAnExpenditure());
        assertEquals(100, saving1.getPrice());
        assertFalse(saving2.getIsItAnExpenditure());
        assertEquals(10, dollarma.getPrice());
        assertTrue(dollarma.getIsItAnExpenditure());
        saveOnFood.setDate("2022.02.10");
        saveOnFood.setStoreName("Saving3");
        saveOnFood.setProductName("saving3");
        saveOnFood.setPrice(20);
        saveOnFood.setIsItAnExpenditure(false);
        assertEquals("2022.02.10", saveOnFood.getDate());
        assertEquals("Saving3", saveOnFood.getStoreName());
        assertEquals("saving3", saveOnFood.getProductName());
        assertEquals(20, saveOnFood.getPrice());
        assertFalse(saveOnFood.getIsItAnExpenditure());
    }

    @Test
    public void testDisplayRecord() {
        assertEquals("Date: 2022.02.03 | Store Name: SaveOnFood | Product Name: Strawberries |" +
                " Price: 5.99 | Is It An Expenditure: true", saveOnFood.displayRecord());
    }
}