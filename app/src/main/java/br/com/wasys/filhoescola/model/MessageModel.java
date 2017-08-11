package br.com.wasys.filhoescola.model;

import org.json.JSONObject;

import br.com.wasys.filhoescola.enumeradores.TypeMessage;

/**
 * Created by bruno on 10/08/17.
 */

public class MessageModel {

    private TypeMessage type;
    private String title;
    private String content;

    public MessageModel (){}

    public MessageModel(String json) {
        try{
            JSONObject jsonObject = new JSONObject(json);
            if(!jsonObject.isNull("title"))
                this.title = jsonObject.getString("title");
            if(!jsonObject.isNull("content"))
                this.content = jsonObject.getString("content");
            this.type = TypeMessage.getAssunto(jsonObject.getString("type"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public TypeMessage getType() {
        return type;
    }

    public void setType(TypeMessage type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
