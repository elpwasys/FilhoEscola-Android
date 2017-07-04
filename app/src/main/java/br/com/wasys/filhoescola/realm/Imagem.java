package br.com.wasys.filhoescola.realm;

import io.realm.RealmObject;

/**
 * Created by bruno on 04/07/17.
 */

public class Imagem extends RealmObject {

    private Integer id;
    private String caminho;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }
}
