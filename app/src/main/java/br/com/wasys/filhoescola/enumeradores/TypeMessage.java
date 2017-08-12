package br.com.wasys.filhoescola.enumeradores;

/**
 * Created by bruno on 05/07/17.
 */

public enum TypeMessage {

    INFO("INFO"),
    ERROR("ERROR"),
    SUCCESS("SUCCESS"),
    WARNING("WARNING");

    private String type;

    TypeMessage(String type){
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    public static TypeMessage getType(String type) {
        if(type.equals("INFO")){
            return INFO;
        }else if(type.equals("ERROR")){
            return ERROR;
        }else if(type.equals("SUCCESS")){
            return SUCCESS;
        }else {
            return WARNING;
        }

    }

}
