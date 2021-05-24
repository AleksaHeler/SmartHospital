package aleksa.heler.smarthospital.classes;

import android.graphics.drawable.Drawable;

public class SmartDevice {
    private String id;
    private String name;
    private boolean active;
    private Drawable image;

    // Constructor
    public SmartDevice(String id, String name, boolean active, Drawable image) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.image = image;
    }

    // Getters
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public boolean isActive() {
        return active;
    }
    public Drawable getImage() {
        return image;
    }

    // Setters
    public void setImage(Drawable image) {
        this.image = image;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public void setId(String id) {
        this.id = id;
    }
}
