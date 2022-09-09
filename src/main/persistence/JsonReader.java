package persistence;

import model.Record;
import model.RecordList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Citation: JSonSerializationDemo Project

// Represents a reader that reads records from JSON data stored in file
public class JsonReader {
    private String file;

    // EFFECTS: constructs reader to read from the file in source
    public JsonReader(String file) {
        this.file = file;
    }

    // EFFECTS: read recordList from file and returns it
    // throws IOException if an error occurs reading data from file
    public RecordList read() throws IOException {
        String jsonData = readFile(file);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseRecordList(jsonObject);
    }

    // EFFECTS: reads file as string and returns it
    private String readFile(String file) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(file), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses record List from JSON object and return it
    private RecordList parseRecordList(JSONObject jsonObject) {
        RecordList recordList = new RecordList();
        addRecordList(recordList, jsonObject);
        return recordList;
    }

    // MODIFIES: RecordList
    // EFFECTS: parses recordList from JSON object and adds them to RecordList
    private void addRecordList(RecordList recordList, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("records");
        for (Object json : jsonArray) {
            JSONObject nextRecord = (JSONObject) json;
            addRecord(recordList, nextRecord);
        }
    }

    // MODIFIES: RecordList
    // EFFECTS: parses record from JSON object and adds it to RecordList
    private void addRecord(RecordList recordList, JSONObject jsonObject) {
        String date = jsonObject.getString("Date");
        String storeName = jsonObject.getString("StoreName");
        String productName = jsonObject.getString("ProductName");
        int price = jsonObject.getInt("Price");
        boolean isItAnExpenditure = jsonObject.getBoolean("IsItAnExpenditure");
        Record record = new Record(date, storeName, productName, price, isItAnExpenditure);
        recordList.addRecord(record);
    }


}
