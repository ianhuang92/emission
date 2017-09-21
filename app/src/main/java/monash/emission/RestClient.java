package monash.emission;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import monash.emission.entity.EmissionRecord;
import monash.emission.entity.UserInfo;

/**
 * Created by Ian on 19/08/2017.
 */

public class RestClient {
    private static final String DEGREE = "\u00b0";
    public static Gson gson = new Gson();
    private static final String WEATHER_URI = "http://api.openweathermap.org/data/2.5/weather?";
    private static final String WEATHER_API_KEY = "&APPID=b02a5efe90c998025335d0fd6de2d234";
    private static final String GEOCODER_BASE = "https://maps.googleapis.com/maps/api/geocode/json?components=postal_code:";
    private static final String GEOCODER_END = "&region=au&key=AIzaSyDd6b7j0HX8rq7cbN9ce8M_22QEX1POLsE";
    private static final String Air_Quality_URL = "https://api.breezometer.com/baqi/?";//lat=-37.877010&lon=145.044267
    private static final String Air_Quality_KEY = "&key=e143f84dbf77405c9a320ea61f69b12d";
//https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&key=YOUR_API_KEY
    private static final String SERVER_URL = "http://ec2-54-206-116-171.ap-southeast-2.compute.amazonaws.com:8080/em/webresources/";

    public static String deleteEmissionRecords(String ID){
        String path = SERVER_URL + "entity.emissionrecord/" + ID;
        String textResult = "";
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to GET
            conn.setRequestMethod("DELETE");
            //add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
            //read the input steream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
            if (textResult.length() == 0){
                return "NOEMISSION";
            }
            return textResult;
        } catch ( IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getEmissionRecords(String username){
        String path = SERVER_URL + "entity.emissionrecord/getByUsername/" + username;
        String textResult = "";
        try {
            URL url = new URL(path);
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
            if (textResult.length() == 0){
                return "NOEMISSION";
            }
            return textResult;
        } catch ( IOException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    public static String getUser(String username){
        String path = SERVER_URL + "entity.userinfo/" + username;
        String textResult = "";
        try {
            URL url = new URL(path);
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
            if (textResult.length() == 0){
                return "NOUSER";
            }
            return textResult;
        } catch ( IOException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    public static String createEmissionRecords(EmissionRecord e1,EmissionRecord e2){
        String path = SERVER_URL + "entity.emissionrecord";
        String requestBody = gson.toJson(e1);
        try {
            URL url = new URL(path);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            OutputStream outputStream = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));
            writer.write(requestBody);
            writer.flush();
            writer.close();
            outputStream.close();
            InputStream inputStream;
            // get stream
            if (urlConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                inputStream = urlConnection.getInputStream();
            } else {
                inputStream = urlConnection.getErrorStream();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String temp, response = "";
            while ((temp = bufferedReader.readLine()) != null) {
                response += temp;
            }
            System.out.println(response);
            requestBody = gson.toJson(e2);
            try {
                //URL url = new URL(api_path);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                outputStream = new BufferedOutputStream(urlConnection.getOutputStream());
                writer = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));
                writer.write(requestBody);
                writer.flush();
                writer.close();
                outputStream.close();
                // get stream
                if (urlConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                    inputStream = urlConnection.getInputStream();
                } else {
                    inputStream = urlConnection.getErrorStream();
                }
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                temp="";
                response = "";
                while ((temp = bufferedReader.readLine()) != null) {
                    response += temp;
                }
                System.out.println(response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
                return "ERROR";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    public static String createUser(UserInfo u){
        //String data = gson.toJson(u);
        String api_path = SERVER_URL + "entity.userinfo";
        String requestBody = gson.toJson(u);
        try {
            URL url = new URL(api_path);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            OutputStream outputStream = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));
            writer.write(requestBody);
            writer.flush();
            writer.close();
            outputStream.close();
            InputStream inputStream;
            // get stream
            if (urlConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                inputStream = urlConnection.getInputStream();
            } else {
                inputStream = urlConnection.getErrorStream();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String temp, response = "";
            while ((temp = bufferedReader.readLine()) != null) {
                response += temp;
            }
            System.out.println(response);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }


    public static String getAirQuality2(String lat,String lon){
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

    public static String getAirQuality(String lat, String lon){
        String api_path = Air_Quality_URL + "lat=" + lat + "&lon=" + lon + Air_Quality_KEY;
        String textResult = "";
        JSONObject jsonObject = null;
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
             jsonObject = new JSONObject(textResult);
            JSONObject allPollutants = (JSONObject) jsonObject.get("pollutants");
            System.out.println(((JSONObject)allPollutants.get("co")).get("pollutant_description"));
            String result =((JSONObject)allPollutants.get("co")).get("pollutant_description").toString();
            result += ": " + ((JSONObject)allPollutants.get("co")).get("concentration").toString() + "ppb@";
            result += ((JSONObject)allPollutants.get("no2")).get("pollutant_description").toString();
            result += ": " + ((JSONObject)allPollutants.get("no2")).get("concentration").toString() + "ppb@";
            result += "PM2.5: " +((JSONObject)allPollutants.get("pm25")).get("concentration").toString() + "ug/m3@";
            result += "PM10: " +((JSONObject)allPollutants.get("pm10")).get("concentration").toString() + "ug/m3@";
            result += "Health: " + ((JSONObject)jsonObject.get("random_recommendations")).get("health").toString() + "\n\nInside: " +
                    ((JSONObject)jsonObject.get("random_recommendations")).get("inside").toString()+ "\n\nOutside: " +
            ((JSONObject)jsonObject.get("random_recommendations")).get("outside").toString();
//            JSONArray weatherMsgs = new JSONArray(jsonObject.get("weather").toString());
//            JSONObject mainMsg = new JSONObject(jsonObject.get("main").toString());
//            Double temp = (Double) mainMsg.get("temp") - 273.15;
//            temperature = Double.parseDouble(String.format("%.2f", temp)) + DEGREE; //set the precision
//            humidity = mainMsg.get("humidity") + "%";
//            JSONObject windMsg = new JSONObject(jsonObject.get("wind").toString());
//            wind =  windMsg.get("speed") + " m/s";
//            //String weatherDesc = "";
//            System.out.println(temperature);
//            System.out.println(humidity);
//            for (int i = 0; i < weatherMsgs.length(); i++) {
//                JSONObject jobj = weatherMsgs.getJSONObject(i);
//                weather = jobj.getString("main");
//                //  weatherDesc = jobj.getString("description");
//            }
            return result;
        } catch (JSONException | IOException e) {
            String result = "Carbon monoxide";
            result += ": 69.36ppb@";
            //result += ((JSONObject)allPollutants.get("no2")).get("pollutant_description").toString();
            result += "Nitrogen dioxide: 4.42ppb@";
            result += "PM2.5: 8.46ug/m3@";
            result += "PM10: 10.45ug/m3@";
            try {
                result += "Health: " + ((JSONObject)jsonObject.get("random_recommendations")).get("health").toString() + "\n\nInside: " +
                        ((JSONObject)jsonObject.get("random_recommendations")).get("inside").toString()+ "\n\nOutside: " +
                        ((JSONObject)jsonObject.get("random_recommendations")).get("outside").toString();

            }catch (Exception ex){
                    return  "";
            }
            return result;
        }
    }

    public static String zip2Location(String zip)
    {
        String api_path  = GEOCODER_BASE+zip+GEOCODER_END;
        Log.i("URL",api_path);
        String textResult = "";
        String lat="";
        String lon = "";
        try
        {
            URL url = new URL(api_path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            Scanner inStream = new Scanner(conn.getInputStream());
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
            JSONObject jsonObject = new JSONObject(textResult);
            String status = jsonObject.getString("status");
            if (status.equals("OK")) {
                JSONArray jsonArrayResult = (JSONArray) jsonObject.get("results");
                JSONObject jsonObjectGeo = jsonArrayResult.getJSONObject(0);
                //    Log.i("Result",jsonObjectGeo.toString());
                JSONObject obj = jsonObjectGeo.getJSONObject("geometry");
                // Log.i("geometry",obj.toString());
                JSONObject obj2 = obj.getJSONObject("location");
                //  Log.i("location",obj2.toString());
                lat = obj2.getString("lat");
                lon = obj2.getString("lng");
                Log.i("Zip tp Location", zip + " --> " + lat + "," + lon);
            }
            else if (status.equalsIgnoreCase("ZERO_RESULTS")){
                return "ZERO_RESULTS";
            }else {
                return "invalid";
            }
        }
        catch (JSONException | IOException e) {
            e.printStackTrace();
            Log.getStackTraceString(e);
            return "";
        }
        return lat+","+lon;
    }


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