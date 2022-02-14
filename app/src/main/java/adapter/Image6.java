package adapter;

import java.io.Serializable;

/**
 * Created by PASCHAL GREEN on 11/40/2016.
 */

public class Image6 implements Serializable {
    private String url, email, image_path, postheading, album_name;
    private String timestamp;
    public Image6() {
    }
    public Image6(String url, String email, String image_path,String postheading, String album_name, String timestamp) {
        this.url = url;
        this.postheading = postheading;
        this.email = email;
        this.image_path = image_path;
        this.album_name =album_name;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getPostheading() {
        return postheading;
    }
    public void setPostheading(String postheading) {
        this.postheading = postheading;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getImage_path() {
        return image_path;
    }
    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
    public String getAlbum_name() {
        return album_name;
    }
    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
