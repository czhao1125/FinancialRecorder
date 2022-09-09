package persistence;

import model.Record;
import model.RecordList;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// Citation: JSonSerializationDemo Project

public class JsonReaderTest extends JsonTest{
    private JsonReader reader;

    @Test
    void testReaderNonExistentFile() {
        reader = new JsonReader("./data/testReaderNonExistentFile.json");
        try {
            RecordList recordList = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyRecordList() {
        reader = new JsonReader("./data/testReaderEmptyRecordList.json");
        try {
            RecordList recordList = reader.read();
            assertEquals(0, recordList.getSize());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralRecordList() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralRecordList.json");
        try {
            RecordList recordList = reader.read();
            //RecordList records = new RecordList();
            List<Record> records = recordList.getRecordList();
            assertEquals(2, records.size());
            checkRecord("2022.02.17", "SaveOnFood", "Milk", 10,
                    true, records.get(0));
            checkRecord("2022.02.18", "Saving1", "Saving1", 50,
                    false, records.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

}
