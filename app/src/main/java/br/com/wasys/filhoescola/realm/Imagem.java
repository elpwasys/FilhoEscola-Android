package br.com.wasys.filhoescola.realm;

import br.com.wasys.filhoescola.model.ImagemModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by bruno on 04/07/17.
 */

public class Imagem extends RealmObject {

    @PrimaryKey
    private Long id;
    private String caminho;


    public void createFrom(ImagemModel imagemModel){
        this.caminho = imagemModel.caminho;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }
}
