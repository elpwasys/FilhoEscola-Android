package br.com.wasys.filhoescola.enumeradores;

/**
 * Created by bruno on 05/07/17.
 */

public enum PositionEnum {

    LEFT("LEFT"),
    RIGHT("RIGHT");
    private String position;

    PositionEnum(String position){
        this.position = position;
    }

    @Override
    public String toString() {
        return position;
    }

    public static PositionEnum getPosition(String position) {
        if(position.equals("LEFT")){
            return LEFT;
        }else {
            return RIGHT;
        }

    }

}
