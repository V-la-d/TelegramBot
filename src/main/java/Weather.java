import org.json.JSONArray;
import org.json.JSONObject;
import sun.plugin2.message.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Weather {
    public static String getWeather(String message, Model model) throws IOException {
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + message + "&units=metric&appid=dbceb288796d416a8e47c6eef8fc8a81");

        Scanner scanner = new Scanner((InputStream) url.getContent());
        String result = "";
        while (scanner.hasNext()) {
            result += scanner.nextLine();
        }
        JSONObject jsonObject = new JSONObject(result);
        model.setName(jsonObject.getString("name"));

        JSONObject main = jsonObject.getJSONObject("main");

        model.setTemp(main.getDouble("temp"));
        model.setPressure(main.getInt("pressure"));
        model.setHumidity(main.getDouble("humidity"));

        JSONArray jsonArray = jsonObject.getJSONArray("weather");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
            model.setIcon((String) jsonObject2.get("icon"));
            model.setWeather((String) jsonObject2.get("main"));
        }


        return "Weather in: " + model.getName() + "\nTemperature: " + model.getTemp() + "°C" + "\nPressure: " + model.getPressure() + " мм" +
                "\nHumidity: " + model.getHumidity() + " %" + "\nDescription: " +
                model.getWeather() + "\n" + "http://openweathermap.org/img/w/" + model.getIcon() + ".png";

    }
}