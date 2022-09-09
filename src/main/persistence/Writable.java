package persistence;

import org.json.JSONObject;

// Citation: JSonSerializationDemo Project

public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
    // Citation: JSonSerializationDemo Project
}
