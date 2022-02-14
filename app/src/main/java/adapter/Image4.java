package adapter;

import java.io.Serializable;

/**
 * Created by PASCHAL GREEN on 11/40/2016.
 */

public class Image4 implements Serializable {
    private String name;
    private String url, status, fbk_id, insta_id, phone, email, image_path, album_name;
    private String timestamp;
    public Image4() {
    }
    public Image4(String url, String name, String status, String fbk_id, String insta_id, String phone, String email, String image_path, String album_name, String timestamp) {
        this.url = url;
        this.name = name;
        this.status = status;
        this.fbk_id = fbk_id;
        this.insta_id = insta_id;
        this.phone = phone;
        this.email = email;
        this.image_path = image_path;
        this.album_name =album_name;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getFbk_id() {
        return fbk_id;
    }
    public void setFbk_id(String fbk_id) {
        this.fbk_id = fbk_id;
    }
    public String getInsta_id() {
        return insta_id;
    }
    public void setInsta_id(String insta_id) {
        this.insta_id = insta_id;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
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
