/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package su.olegus.ws;

import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import su.olegus.info.EtcInfo;

/**
 *
 * @author plintus
 */
public class JsonObjectDecoder implements Decoder.Text<JSONObject> {

    @Override
    public void init(EndpointConfig ec) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public boolean willDecode(String string) {
        Json.createReader(new StringReader(string));
        return true;
    }

    @Override
    public JSONObject decode(String string) throws DecodeException {

        JsonReader jsonReader = Json.createReader(new StringReader(string));
        JsonObject jo = jsonReader.readObject();

        Logger.getLogger(JsonObjectDecoder.class.getName()).log(Level.INFO, "decode {0}", new Object[]{jo});
        JSONObject ret = new JSONObject();
        ret.setType(jo.getString("type"));
        switch (jo.getString("type")) {
            case "logout":
            case "aegetfiles":
            case "aegetfile": 
            case "euploadfile":  {
                Integer obj = jo.getInt("object");
                ret.setObject(obj);
                break;
            }
            case "aesetetc": {
                JsonObject obj = jo.getJsonObject("object");
                EtcInfo etc = new EtcInfo();
                etc.setId(obj.getInt("id"));
                etc.setKey(obj.getString("key"));
                etc.setValue(obj.getString("value"));
                ret.setObject(etc);
                break;
            }
            case "buploadfile": {
                JsonObject obj = jo.getJsonObject("object");
                UploadInfo ui = new UploadInfo();
                ui.setOriginalname(obj.getString("filename"));
                ui.setCaptcha(obj.getString("captcha"));
                ret.setObject(ui);
                break;
            }
            case "processfile":  {
                String obj = jo.getString("object");
                ret.setObject(obj);
                break;
            }
        }
        return ret;
    }

}
