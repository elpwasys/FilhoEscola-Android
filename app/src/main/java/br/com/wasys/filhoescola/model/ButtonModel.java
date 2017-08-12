package br.com.wasys.filhoescola.model;

import java.io.Serializable;

import br.com.wasys.filhoescola.enumeradores.PositionEnum;

/**
 * Created by bruno on 12/08/17.
 */

public class ButtonModel implements Serializable{
    private int id;
    private String src;
    private PositionEnum position;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public PositionEnum getPosition() {
        return position;
    }

    public void setPosition(PositionEnum position) {
        this.position = position;
    }
}
