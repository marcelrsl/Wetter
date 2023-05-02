package WeatherHamburg;

public class WeatherInfo {
    // private Instanzvariablen zur Speicherung von Wetterinformationen
    private String cityName;       // Stadtname
    private String date;           // Datum
    private String temperature;    // Temperatur
    private String description;    // Beschreibung

    // Konstruktor, um Wetterinformationen zu initialisieren
    public WeatherInfo(String cityName, String date, String temperature, String description) {
        this.cityName = cityName;             // Initialisierung des Stadtname
        this.date = date;                     // Initialisierung des Datums
        this.temperature = temperature;       // Initialisierung der Temperatur
        this.description = description;       // Initialisierung der Beschreibung
    }

    // Getter-Methoden für den Zugriff auf die Wetterinformationen
    public String getCityName() {
        return cityName;   // Rückgabe des Stadtname
    }

    public String getDate() {
        return date;       // Rückgabe des Datums
    }

    public String getTemperature() {
        return temperature;  // Rückgabe der Temperatur
    }

    public String getDescription() {
        return description;  // Rückgabe der Beschreibung
    }
}
