import com.google.gson.Gson;
import gatlingdemostore.dto.TestData;

public class TestEdu {

    public static void main(String[] args) {

        var responseJson = """
                {
                  "data": {
                    "id" : "0984902029",
                    "name" : "Sayings of the Century"
                  }
                }
                """;

        TestData testData = new Gson().fromJson(responseJson, TestData.class);

        System.out.println(testData.getData().getId());

    }
}
