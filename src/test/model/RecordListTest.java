package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RecordListTest {
    RecordList list1;
    Record saveOnFood;
    Record saving1;

    @BeforeEach
    public void setup() {
        list1 = new RecordList();
        saveOnFood = new Record("2022.02.03", "SaveOnFood", "Strawberries",
                5.99, true);
        saving1 = new Record("2022.02.04", "Saving1", "Saving1", 100,
                false);
    }

    @Test
    public void testAddRecord() {
        assertEquals(0, list1.getSize());
        list1.addRecord(saveOnFood);
        assertEquals(1, list1.getSize());
        list1.addRecord(saving1);
        assertEquals(2,list1.getSize());
    }

    @Test
    public void testGetRecord(){
        list1.addRecord(saveOnFood);
        list1.addRecord(saving1);
        assertEquals(saveOnFood, list1.getRecord(0));
        assertEquals(saving1, list1.getRecord(1));
    }

    @Test
    public void testDeleteRecord(){
        list1.addRecord(saveOnFood);
        list1.addRecord(saving1);
        assertEquals(2,list1.getSize());
        list1.deleteRecord(1);
        assertEquals(1,list1.getSize());
        list1.deleteRecord(0);
        assertEquals(0,list1.getSize());
    }

}
