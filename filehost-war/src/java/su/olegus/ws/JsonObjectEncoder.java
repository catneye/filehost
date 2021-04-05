/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package su.olegus.ws;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 *
 * @author plintus
 */
public class JsonObjectEncoder  implements Encoder.Text<JSONResponse>{

    @Override
    public String encode(JSONResponse jo) throws EncodeException {
        
        Logger.getLogger(JsonObjectEncoder.class.getName()).log(Level.INFO, "encode {0}", jo);
        JsonObjectBuilder jsonObjectBuilder=Json.createObjectBuilder();
        
        jsonObjectBuilder.add("response", jo.getResponse());
        jsonObjectBuilder.add("result", jo.getResult());
        JsonObject jsonObject = jsonObjectBuilder.build();
    return jsonObject.toString();
    
    }

    @Override
    public void init(EndpointConfig ec) {
    }

    @Override
    public void destroy() {
    }
    
}
