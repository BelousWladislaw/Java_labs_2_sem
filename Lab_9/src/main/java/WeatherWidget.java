import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherWidget {
    public static void main(String[] args) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://air-quality.p.rapidapi.com/history/airquality?lon=-78.638&lat=35.779"))
                .header("X-RapidAPI-Key", "2f73569cefmsh1d3a485eccfb956p1ea800jsn66163630d8e2")
                .header("X-RapidAPI-Host", "air-quality.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            // Разбор JSON-ответа с помощью GSON
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);

            // Получение необходимых данных о качестве воздуха из JSON
            JsonArray airQualityData = jsonObject.getAsJsonArray("data");

            StringBuilder htmlContent = new StringBuilder();
            htmlContent.append("<html><head><title>Weather Widget</title></head><body>");
            htmlContent.append("<h1>Raleigh</h1>");  // Использование названия города "Raleigh"

            for (int i = 0; i < airQualityData.size(); i++) {
                JsonObject currentData = airQualityData.get(i).getAsJsonObject();
                int aqi = currentData.get("aqi").getAsInt();
                double co = currentData.get("co").getAsDouble();
                double no2 = currentData.get("no2").getAsDouble();
                double o3 = currentData.get("o3").getAsDouble();
                double pm10 = currentData.get("pm10").getAsDouble();
                double pm25 = currentData.get("pm25").getAsDouble();
                double so2 = currentData.get("so2").getAsDouble();
                String timestampLocal = currentData.get("timestamp_local").getAsString();

                // Добавление информации о качестве воздуха в HTML
                htmlContent.append("<h2>Air Quality on ").append(timestampLocal).append("</h2>");
                htmlContent.append("<p>AQI: ").append(aqi).append("</p>");
                htmlContent.append("<p>CO: ").append(co).append(" μg/m³</p>");
                htmlContent.append("<p>NO2: ").append(no2).append(" ppb</p>");
                htmlContent.append("<p>O3: ").append(o3).append(" ppb</p>");
                htmlContent.append("<p>PM10: ").append(pm10).append(" μg/m³</p>");
                htmlContent.append("<p>PM2.5: ").append(pm25).append(" μg/m³</p>");
                htmlContent.append("<p>SO2: ").append(so2).append(" ppb</p>");
                htmlContent.append("<br/>");
            }
            htmlContent.append("</body></html>");

            // Сохранение данных в файл
            String fileName = "weather_widget.html";
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(htmlContent.toString());
            writer.close();

            System.out.println("Weather widget HTML page saved successfully.");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}