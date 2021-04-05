/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package su.olegus.ws;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.AccessTimeout;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpSession;
import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import su.olegus.bean.EtcBeanRemote;
import su.olegus.info.EtcInfo;
import su.olegus.info.FilesInfo;
import su.olegus.info.UserInfo;

/**
 *
 * @author plintus
 */
@ApplicationScoped
@ServerEndpoint(value = "/WSfilehost", configurator = Configurator.class, encoders = JsonObjectEncoder.class, decoders = JsonObjectDecoder.class)
//@Stateful
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@AccessTimeout(value = 30000)
public class WSocket implements Serializable {

    private EndpointConfig config;
    @EJB
    private EtcBeanRemote etcEjb;

    @Inject
    private WSHandler sessionHandler;

    @OnOpen
    public void onConnectionOpen(Session session, EndpointConfig config) {
        this.config = config;
        HttpSession httpSession = (HttpSession) this.config.getUserProperties().get("httpSession");
        Logger.getLogger(WSocket.class.getName()).log(Level.INFO, "socket open wsSession: {0} httpSession: {1}",
                new Object[]{session.getId(), httpSession});
        //sessions.put(session, httpSession);
        sessionHandler.addSession(session, httpSession);
    }

    @OnClose
    public void onConnectionClose(Session session) {
        Logger.getLogger(WSocket.class.getName()).log(Level.INFO, "socket close session: {0}", session.getId());
        //sessions.remove(session);
        sessionHandler.removeSession(session);
    }

    @OnMessage
    public JSONResponse onMessage(JSONObject message, Session session) throws IOException, EncodeException {
        HttpSession httpSession = (HttpSession) sessionHandler.getSession(session);
        Logger.getLogger(WSocket.class.getName()).log(Level.INFO, "onMessage session: {0} message: {1} httpSession: {2}",
                new Object[]{session.getId(), message, httpSession});
        JSONResponse response = new JSONResponse();
        String type = message.getType();
        response.setResponse(type);
        response.setResult("false");
        if (httpSession != null) {
            UserInfo userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
            Logger.getLogger(WSocket.class.getName()).log(Level.INFO, "onMessage type: {0} ", type);
            try {
                if (userInfo != null) {
                    //for only users 
                    switch (type) {
                        case "aegetetcs": {
                            if ((userInfo.getRole().equals("administrator"))
                                    || (userInfo.getRole().equals("manager"))) {
                                List<EtcInfo> etcs = etcEjb.getEtcs();
                                JsonArrayBuilder jar = Json.createArrayBuilder();
                                for (EtcInfo etc : etcs) {
                                    jar.add(Json.createObjectBuilder()
                                            .add("id", etc.getId())
                                            .add("title", etc.getKey()));
                                }
                                JsonObjectBuilder job = Json.createObjectBuilder();
                                job.add("items", jar);
                                response.setResult(job.build().toString());
                            }
                            break;
                        }
                        case "aegetetc": {
                            if ((userInfo.getRole().equals("administrator"))
                                    || (userInfo.getRole().equals("manager"))) {
                                Integer id = (Integer) message.getObject();
                                EtcInfo etc;
                                if ((id != null) && (id != 0)) {
                                    etc = etcEjb.getEtc(id);
                                } else {
                                    etc = new EtcInfo();
                                    etc.setId(0);
                                    etc.setKey("key");
                                    etc.setValue("value");
                                }
                                JsonObjectBuilder job = Json.createObjectBuilder();
                                job.add("id", etc.getId())
                                        .add("key", etc.getKey())
                                        .add("value", etc.getValue());
                                response.setResult(job.build().toString());
                            }
                            break;
                        }
                        case "aesetetc": {
                            if ((userInfo.getRole().equals("administrator"))
                                    || (userInfo.getRole().equals("manager"))) {
                                EtcInfo etc = (EtcInfo) message.getObject();
                                if (etcEjb.setEtc(etc) != null) {
                                    response.setResult("ok");
                                }
                            }
                            break;
                        }
                        case "aegetfiles": {
                            if ((userInfo.getRole().equals("administrator"))
                                    || (userInfo.getRole().equals("manager"))) {
                                List<FilesInfo> files = etcEjb.getFiles();
                                JsonArrayBuilder jar = Json.createArrayBuilder();
                                for (FilesInfo file : files) {
                                    jar.add(Json.createObjectBuilder()
                                            .add("id", file.getId())
                                            .add("title", file.getName()));
                                    /*jar.add(Json.createObjectBuilder()
                                            .add("id", file.getId())
                                            .add("name", file.getName())
                                            .add("description", file.getDescription())
                                            .add("filemime", file.getFilemime())
                                            .add("filepath", file.getFilepath())
                                            .add("realname", file.getRealname()));*/
                                }
                                JsonObjectBuilder job = Json.createObjectBuilder();
                                job.add("items", jar);
                                response.setResult(job.build().toString());
                            }
                            break;
                        }
                        case "aegetfile": {
                            if ((userInfo.getRole().equals("administrator"))
                                    || (userInfo.getRole().equals("manager"))) {
                                Integer id = (Integer) message.getObject();
                                FilesInfo file;
                                if ((id != null) && (id != 0)) {
                                    file = etcEjb.getFile(id);
                                } else {
                                    file = new FilesInfo();
                                    file.setId(0);
                                    file.setName("undefined");
                                    file.setRealname("undefined");
                                    file.setDescription("undefined");
                                    file.setFilemime("undefined");
                                    file.setFilepath("undefined");
                                }
                                JsonObjectBuilder job = Json.createObjectBuilder()
                                        .add("id", file.getId())
                                        .add("name", file.getName())
                                        .add("description", file.getDescription())
                                        .add("filemime", file.getFilemime())
                                        .add("filepath", file.getFilepath())
                                        .add("realname", file.getRealname());
                                response.setResult(job.build().toString());
                            }
                            break;
                        }
                        case "aesetfile": {
                            if ((userInfo.getRole().equals("administrator"))
                                    || (userInfo.getRole().equals("manager"))) {
                                FilesInfo id = (FilesInfo) message.getObject();
                                FilesInfo r = etcEjb.setFileDescription(id);
                                if (r != null) {
                                    response.setResult("ok");
                                } else {
                                    response.setResult("error");
                                }
                            }
                            break;
                        }
                        case "getfileselect": {
                            response.setResult("error");
                            if ((userInfo.getRole().equals("administrator"))
                                    || (userInfo.getRole().equals("manager"))) {
                                String id = (String) message.getObject();
                                List<FilesInfo> files = etcEjb.getFiles();
                                JsonArrayBuilder jar = Json.createArrayBuilder();
                                for (FilesInfo file : files) {
                                    jar.add(Json.createObjectBuilder()
                                            .add("id", file.getId())
                                            .add("name", file.getName())
                                            .add("title", file.getRealname()));
                                }
                                JsonObjectBuilder job = Json.createObjectBuilder();
                                job.add("items", jar);
                                response.setResult(job.build().toString());
                            }
                            break;
                        }
                    }
                } else {
                    //for only guests 
                    switch (type) {
                    }
                }
                //for all users
                switch (type) {
                    case "processfile": {
                        String id = (String) message.getObject();
                        FilesInfo r = etcEjb.processFile(id);
                        String p100 = null;
                        String p200 = null;
                        String p800 = null;
                        if (r != null) {
                            String icon = "_blank.png";
                            switch (r.getFilemime()) {
                                case "audio/aac":
                                    icon = "aac.png";
                                    break;
                                case "application/vnd.adobe.illustrator":
                                    icon = "ai.png";
                                    break;
                                case "audio/aiff":
                                    icon = "aif.png";
                                    break;
                                case "video/x-msvideo":
                                    icon = "avi.png";
                                    break;
                                case "image/bmp":
                                    icon = "bmp.png";
                                    break;
                                case "application/msword":
                                    icon = "doc.png";
                                    break;
                                case "application/acad":
                                    icon = "dwg.png";
                                    break;
                                case "application/dxf":
                                    icon = "dxf.png";
                                    break;
                                case "application/postscript":
                                    icon = "eps.png";
                                    break;
                                case "video/x-flv":
                                    icon = "flv.png";
                                    break;
                                case "image/gif":
                                    icon = "gif.png";
                                    break;
                                case "text/html":
                                    icon = "html.png";
                                    break;
                                case " application/iso-image":
                                    icon = "iso.png";
                                    break;
                                case "image/jpeg":
                                    icon = "jpg.png";
                                    p100 = etcEjb.createImagePreview(r.getName(), 100);
                                    p200 = etcEjb.createImagePreview(r.getName(), 200);
                                    p800 = etcEjb.createImagePreview(r.getName(), 800);
                                    break;
                                case "audio/mpeg3":
                                    icon = "mp3.png";
                                    break;
                                case "audio/mpeg":
                                    icon = "mp3.png";
                                    break;
                                case "video/mpeg":
                                    icon = "mp4.png";
                                    break;
                                case "application/vnd.oasis.opendocument.spreadsheet":
                                    icon = "ods.png";
                                    break;
                                case "application/vnd.oasis.opendocument.text ":
                                    icon = "odt.png";
                                    break;
                                case " application/vnd.oasis.opendocument.text ":
                                    icon = "pdf.png";
                                    break;
                                case "image/png":
                                    icon = "png.png";
                                    p100 = etcEjb.createImagePreview(r.getName(), 100);
                                    p200 = etcEjb.createImagePreview(r.getName(), 200);
                                    p800 = etcEjb.createImagePreview(r.getName(), 800);
                                    break;
                                case "application/vnd.ms-powerpoint":
                                    icon = "ppt.png";
                                    break;
                                case "video/quicktime":
                                    icon = "qt.png";
                                    break;
                                case "application/x-rar-compressed":
                                    icon = "rar.png";
                                    break;
                                case "application/rtf":
                                    icon = "rtf.png";
                                    break;
                                case "application/x-tar":
                                    icon = "tgz.png";
                                    break;
                                case "image/tiff":
                                    icon = "tiff.png";
                                    break;
                                case "audio/x-wav":
                                    icon = "wav.png";
                                    break;
                                case "application/vnd.ms-excel":
                                    icon = "xls.png";
                                    break;
                                case "application/xml":
                                    icon = "xml.png";
                                    break;
                                case "application/zip":
                                    icon = "zip.png";
                                    break;
                            }
                            String fordownload = "name=" + r.getName() + "&type=file";
                            JsonObjectBuilder job = Json.createObjectBuilder()
                                    .add("name", r.getName())
                                    .add("type", r.getFilemime())
                                    .add("dlink", fordownload)
                                    .add("icon", icon);

                            if (p100 != null) {
                                job.add("p100", p100);
                            }
                            if (p200 != null) {
                                job.add("p200", p200);
                            }
                            if (p800 != null) {
                                job.add("p800", p800);
                            }
                            response.setResult(job.build().toString());
                            httpSession.setAttribute("captcha", null);
                        }
                        break;
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(WSocket.class.getName()).log(Level.SEVERE, "Exception {0}", ex);
            }
        }
        return response;
    }

    private String sendGet(String url, String useragent) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");
        //add request header
        con.setRequestProperty("User-Agent", useragent);
        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();

    }

    // HTTP POST request
    private String sendPost(String url, String useragent) throws Exception {

        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", useragent);
        //con.setRequestProperty("Accept-Language", "en-US,en;q=0.5"); 
        //String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        //wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
