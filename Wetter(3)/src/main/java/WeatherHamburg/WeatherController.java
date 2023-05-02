package WeatherHamburg;

import com.fasterxml.jackson.databind.JsonNode; //Import von Jackson-Bibliothek, für JSON Verarbeitung. Jackson-Bibliothek ist eine Java-Bibliothek, die zur Verarbeitung von JSON-Daten verwendet wird.
import com.fasterxml.jackson.databind.ObjectMapper; //Java-Bibliothek = Sammlung von vordefinierten Java-Klassen und -Methoden, um spezifische Funktionen oder Aufgaben in Java-Anwendungen auszuführen
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class WeatherController {

    @GetMapping("/")
    public String getWeather(Model model) {

        String city = "Hamburg";
        String apiKey = "f6d8c028d0bf09b68c233f63002e1a8c";
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;

        
        // RestTemplate ermöglicht, eine REST-Anfrage an die OpenWeatherMap-API zu senden und die Wetterdaten für Hamburg abzurufen
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);

         // Verwendet den ObjectMapper, um JSON-Daten zu übersetzen/deserialisieren und in Java-Objekte umzuwandeln
        ObjectMapper mapper = new ObjectMapper();
        try { 
            JsonNode root = mapper.readTree(result); 

            // Temperatur in Celsius extrahiert
            double temperature = root.path("main").path("temp").asDouble() - 273.15; //Kelvin
            DecimalFormat df = new DecimalFormat("#.##");
            String tempInCelsius = df.format(temperature);

            // Wetterbeschreibung extrahiert
            String description = root.path("weather").get(0).path("description").asText();

            // Der Name der Stadt wird extrahiert
            String cityName = root.path("name").asText();

            // Datum und Uhrzeit werden abgelesen
            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = date.format(formatter);

            // Das Objekt WeatherInfo wird mit den extrahierten Daten erstellt 
            WeatherInfo weatherInfo = new WeatherInfo(cityName, formattedDate, tempInCelsius, description);

            // Nun wird WeatherInfo zum Model hinzugefügt, damit die Informationen für den Nutzer erkennbar werden
            model.addAttribute("weatherInfo", weatherInfo);

            return "weather";

        } catch (IOException e) { //Falls Fehler auftreten, es ist eine Unterklasse von Exception
            e.printStackTrace();
        }

        return "error";
    }
    @GetMapping("/index")
    public String getIndexPage() {
        return "index";
    }
    
    @GetMapping("/forecast")
    public String getWeatherForecast(Model model) {

        String city = "Hamburg";
        String apiKey = "f6d8c028d0bf09b68c233f63002e1a8c"; //API Schlüssel, einzigartig
        String url = "http://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + apiKey; //Damit JavaSpring Zugriff auf die API erhält

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(result);

            // Informationen werden in der Liste für die Vorhersage gespeichert
            List<Forecast> forecasts = new ArrayList<>();
            JsonNode listNode = root.path("list"); //Node.js ist eine plattformübergreifende Open-Source-JavaScript-Laufzeitumgebung, die JavaScript-Code außerhalb eines Webbrowsers ausführen kann
            for (JsonNode node : listNode) {
                // Temperatur
                double temperature = node.path("main").path("temp").asDouble() - 273.15;
                DecimalFormat df = new DecimalFormat("#.##");
                String tempInCelsius = df.format(temperature);

                // Wetterbeschreibung
                String description = node.path("weather").get(0).path("description").asText();

                // Datum und Uhrzeit
                String dtTxt = node.path("dt_txt").asText();

                // Mithilfe der herausgenommenen Daten wird die Vorhersage erstellt
                Forecast forecast = new Forecast(dtTxt, tempInCelsius, description);
                forecasts.add(forecast);
            }

                // Wetterbericht nach Datum sortieren
                Collections.sort(forecasts);

                // Sortierte Vorhersagen werden zum Model hinzugefügt
                model.addAttribute("forecasts", forecasts);

                return "forecast";

        } catch (IOException e) { //Exception, also Ausnahme
            e.printStackTrace();
        }

        return "error";
    }

    // Klasse im inneren, damit Daten gespeichert werden und nach Datum sortiert werden
    private static class Forecast implements Comparable<Forecast> {

        private String date;
        private String temperature;
        private String description;

        public Forecast(String date, String temperature, String description) {
            this.date = date;
            this.temperature = temperature;
            this.description = description;
        }

        public String getDate() {
            return date;
        }

        public String getTemperature() {
            return temperature;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public int compareTo(Forecast o) {
            return this.date.compareTo(o.date);
        }
    }

}
