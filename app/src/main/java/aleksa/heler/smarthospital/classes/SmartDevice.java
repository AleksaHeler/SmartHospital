package aleksa.heler.smarthospital.classes;

import android.graphics.drawable.Drawable;

public class SmartDevice {
    private String id;
    private String name;
    private String type;
    private String active;
    private String image;

    // Constructor
    public SmartDevice(String id, String name, String type, String active, String image) {
        this.id = id;
        this.name = name;
        this.type = type;
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
    public String getType() {
        return type;
    }
    public String getActive() {
        return active;
    }
    public String getImage() {
        return image;
    }

    // Setters
    public void setImage(String image) {
        this.image = image;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setActive(String active) {
        this.active = active;
    }
    public void setId(String id) {
        this.id = id;
    }
}
