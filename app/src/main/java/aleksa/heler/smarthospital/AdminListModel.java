package aleksa.heler.smarthospital;

import android.graphics.drawable.Drawable;

public class AdminListModel {
    private Drawable image;
    private String text;
    private boolean active;

    public AdminListModel(Drawable image, String text) {
        this.image = image;
        this.text = text;
        this.active = true;
    }

    public Drawable getImage() {
        return image;
    }

    public String getText() {
        return text;
    }

    public boolean isActive() {
        return active;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
