package gatlingdemostore.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@lombok.Data
public class TestData {

    @Expose
    @SerializedName("data")
    private Data data;

    public Data getData() {
        return data;
    }
}
