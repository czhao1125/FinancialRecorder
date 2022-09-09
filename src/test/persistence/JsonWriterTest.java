package persistence;

import model.Record;
import model.RecordList;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// Citation: JSonSerializationDemo Project

public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            RecordList recordList = new RecordList();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyRecordList() {
        try {
            RecordList recordList = new RecordList();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyRecordList.json");
            writer.open();
            writer.write(recordList);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyRecordList.json");
            recordList = reader.read();
            assertEquals(0, recordList.getSize());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralRecordList() {
        try {
            RecordList recordList = new RecordList();
            recordList.addRecord(new Record("2022.02.17", "SaveOnFood", "Milk",
                    10, true));
            recordList.addRecord(new Record("2022.02.18", "Saving1", "Saving1",
                    50, false));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralRecordList.json");
            writer.open();
            writer.write(recordList);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralRecordList.json");
            recordList = reader.read();
            List<Record> records = recordList.getRecordList();
            assertEquals(2, records.size());
            checkRecord("2022.02.17", "SaveOnFood", "Milk", 10,
                    true, records.get(0));
            checkRecord("2022.02.18", "Saving1", "Saving1", 50,
                    false, records.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
