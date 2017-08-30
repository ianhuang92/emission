package monash.emission;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Ian on 19/08/2017.
 */

public class RestClient {
    private static final String DEGREE = "\u00b0";
    public static Gson gson = new Gson();
    private static final String WEATHER_URI = "http://api.openweathermap.org/data/2.5/weather?";
    private static final String WEATHER_API_KEY = "&APPID=b02a5efe90c998025335d0fd6de2d234";


//    public static String getAirPollution(String lat,String lon){
//        String api_path = "http://api.openweathermap.org/pollution/v1/co/{location}/{datetime}.json?appid={api_key}";
//    }

    public static String getWeatherInfo(String lat, String lon) {
        String api_path = WEATHER_URI + "lat=" + lat + "&lon=" + lon + WEATHER_API_KEY;
        String temperature = "";
        String humidity = "";
        String weather = "";
        String wind = "";
        String textResult = "";
        String location = "";
        try {
            URL url = new URL(api_path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to GET
            conn.setRequestMethod("GET");
            //add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
            //read the input steream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
            JSONObject jsonObject = new JSONObject(textResult);
            location = (String) jsonObject.get("name");
            System.out.println(location);
            JSONArray weatherMsgs = new JSONArray(jsonObject.get("weather").toString());
            JSONObject mainMsg = new JSONObject(jsonObject.get("main").toString());
            Double temp = (Double) mainMsg.get("temp") - 273.15;
            temperature = Double.parseDouble(String.format("%.2f", temp)) + DEGREE; //set the precision
            humidity = mainMsg.get("humidity") + "%";
            JSONObject windMsg = new JSONObject(jsonObject.get("wind").toString());
            wind =  windMsg.get("speed") + " m/s";
            //String weatherDesc = "";
            System.out.println(temperature);
            System.out.println(humidity);
            for (int i = 0; i < weatherMsgs.length(); i++) {
                JSONObject jobj = weatherMsgs.getJSONObject(i);
                weather = jobj.getString("main");
                //  weatherDesc = jobj.getString("description");
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return "";
        }
        return location + "," + weather + "," + temperature + "," + humidity + "," + wind;
    }
}
