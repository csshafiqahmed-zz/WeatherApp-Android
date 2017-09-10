package com.choch.michaeldicioccio.weatherapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by michaeldicioccio on 9/9/17.
 */

public class WeatherDataJson {

    JSONObject getJsonFromUrl(final String data_url) throws IOException, JSONException {
        InputStream is = new URL(data_url).openStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) { sb.append((char) cp); }
        return new JSONObject(sb.toString());
    }

    ArrayList<Weather> parseJson(JSONObject jsonObject) throws JSONException {
        ArrayList<Weather> weatherArrayList = new ArrayList<>();
        //TODO: Use current info for "NOW" and first value in "DAY"
        Calendar date = getCurrentDateTime(jsonObject);
        String condition = getCurrentCondition(jsonObject);
        int temperature = getCurrentTemperature(jsonObject);
        int temperature_high, temperature_low;
        int wind_mph = getCurrentWind(jsonObject);
        int humidity = getCurrentHumidity(jsonObject);
        int precipitation = getCurrentPrecipitation(jsonObject);

        JSONArray jsonForecastArray = jsonObject.getJSONObject("forecast").getJSONArray("forecastday");
        for (int i = 0; i < jsonForecastArray.length(); i++) {
            JSONObject jsonForecastObject = new JSONObject(jsonForecastArray.get(i).toString());

            if (i == 0) {
                temperature_high = getTemperatureHigh(jsonForecastObject);
                temperature_low = getTemperatureLow(jsonForecastObject);

            } else {
                date = getDateTime(jsonForecastObject);
                condition = getCondition(jsonForecastObject);
                temperature = getTemperature(jsonForecastObject);
                temperature_high = getTemperatureHigh(jsonForecastObject);
                temperature_low = getTemperatureLow(jsonForecastObject);
                wind_mph = getWind(jsonForecastObject);
                humidity = getHumidity(jsonForecastObject);
                precipitation = getPrecipitation(jsonForecastObject);
            }

            System.out.println(date.getTime());
            System.out.println(condition);
            System.out.println(temperature);
            System.out.println(temperature_high);
            System.out.println(temperature_low);
            System.out.println(wind_mph);
            System.out.println(humidity);
            System.out.println(precipitation);

            Weather weather = new Weather(
                    date,
                    condition,
                    temperature,
                    temperature_high,
                    temperature_low,
                    wind_mph,
                    humidity,
                    precipitation);

            weatherArrayList.add(weather);
        }

        return weatherArrayList;
    }

    private Calendar getCurrentDateTime(JSONObject jsonObject) {
        try {
            String date_time = jsonObject.getJSONObject("current").getString("last_updated");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateFormat.parse(date_time));
            return cal;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Calendar getDateTime(JSONObject jsonObject) {
        try {
            String date_time = jsonObject.getString("date");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateFormat.parse(date_time));
            return cal;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getCurrentCondition(JSONObject jsonObject) {
        try{
            String condition = jsonObject.getJSONObject("current").getJSONObject("condition").getString("text");
            return condition;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getCondition(JSONObject jsonObject) {
        try{
            String condition = jsonObject.getJSONObject("day").getJSONObject("condition").getString("text");
            return condition;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private int getCurrentTemperature(JSONObject jsonObject) {
        try {
            int temperature = (int) Math.round(jsonObject.getJSONObject("current").getDouble("temp_f"));
            return temperature;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getTemperature(JSONObject jsonObject) {
        try {
            int temperature = (int) Math.round(jsonObject.getJSONObject("day").getDouble("avgtemp_f"));
            return temperature;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getTemperatureHigh(JSONObject jsonObject) {
        try {
            int temperature_high = (int) Math.round(jsonObject.getJSONObject("day").getDouble("maxtemp_f"));
            return temperature_high;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getTemperatureLow(JSONObject jsonObject) {
        try {
            int temperature_low = (int) Math.round(jsonObject.getJSONObject("day").getDouble("mintemp_f"));
            return temperature_low;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getCurrentWind(JSONObject jsonObject) {
        try {
            int wind_mph = (int) Math.round(jsonObject.getJSONObject("current").getDouble("wind_mph"));
            return wind_mph;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getWind(JSONObject jsonObject) {
        try {
            int wind_mph = (int) Math.round(jsonObject.getJSONObject("day").getDouble("maxwind_mph"));
            return wind_mph;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getCurrentHumidity(JSONObject jsonObject) {
        try {
            int humidity = (int) Math.round(jsonObject.getJSONObject("current").getDouble("humidity"));
            return humidity;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getHumidity(JSONObject jsonObject) {
        try {
            int humidity = (int) Math.round(jsonObject.getJSONObject("day").getDouble("avghumidity"));
            return humidity;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getCurrentPrecipitation(JSONObject jsonObject) {
        try {
            int precipitation = (int) Math.round(jsonObject.getJSONObject("current").getDouble("wind_mph"));
            return precipitation;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getPrecipitation(JSONObject jsonObject) {
        try {
            int precipitation = (int) Math.round(jsonObject.getJSONObject("day").getDouble("totalprecip_in"));
            return precipitation;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
