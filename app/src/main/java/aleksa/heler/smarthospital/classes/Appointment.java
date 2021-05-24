package aleksa.heler.smarthospital.classes;

public class Appointment {
    private String id;
    private String date;
    private String text;

    public Appointment(String date, String text) {
        this.date = date;
        this.text = text;
    }

    public String getId() { return id; }

    public String getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public void setId(String id) { this.id = id; }

    public void setDate(String date) {
        this.date = date;
    }

    public void setText(String text) {
        this.text = text;
    }

}
