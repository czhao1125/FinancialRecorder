package model;


import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Represents a record list of expenditures or deposits
public class RecordList implements Writable {
    ArrayList<Record> records = new ArrayList<Record>();

    // MODIFIES: this
    // EFFECTS: add a new record to the list
    public void addRecord(Record record) {
        records.add(record);
        EventLog.getInstance().logEvent(new Event("New record added to record list"));
    }

    // EFFECTS: return the selected record in list
    public Record getRecord(int i) {
        return records.get(i);
    }

    // MODIFIES: this
    // EFFECTS: delete a record from the list
    public void deleteRecord(int i) {
        records.remove(i);
        EventLog.getInstance().logEvent(new Event("A record has been deleted from record list"));
    }


    // EFFECTS: get the size of the list
    public int getSize() {
        return records.size();
    }

    // EFFECTS: returns this as JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("records", recordsToJson());
        return json;
    }

    // EFFECTS: returns record in this records as a JSON array
    private JSONArray recordsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Record record : records) {
            jsonArray.put(record.toJson());
        }
        return jsonArray;
    }

    // EFFECTS: returns an unmodifiable list of records
    public List<Record> getRecordList() {
        return Collections.unmodifiableList(records);
    }
}
