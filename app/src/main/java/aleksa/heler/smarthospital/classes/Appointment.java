package aleksa.heler.smarthospital.classes;

// Describes an appointment. Has ID that is same as the user to which it is linked
public class Appointment {
    private String id;
    private String date;
    private String text;

    // Constructor
    public Appointment(String id, String date, String text) {
        this.id = id;
        this.date = date;
        this.text = text;
    }

    // Getters
    public String getId() { return id; }
    public String getDate() {
        return date;
    }
    public String getText() {
        return text;
    }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setDate(String date) {
        this.date = date;
    }
    public void setText(String text) {
        this.text = text;
    }

}
