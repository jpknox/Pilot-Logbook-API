package app.data.aircraft;

public class Aircraft {

    private String type;
    private String registration;

    public Aircraft() {
    }

    public Aircraft(String type, String registration) {
        this.type = type;
        this.registration = registration;
    }

    public String getType() {
        return type;
    }

    public String getRegistration() {
        return registration;
    }
}
