package br.com.wasys.filhoescola.realm;

import io.realm.RealmObject;

/**
 * Created by bruno on 04/07/17.
 */

public class Cache extends RealmObject {

    private byte[] data;
    private String url;
    private String mimeType;
    private String enconding;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getEnconding() {
        return enconding;
    }

    public void setEnconding(String enconding) {
        this.enconding = enconding;
    }
}
