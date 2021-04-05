/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package su.olegus.ws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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

/**
 *
 * @author plintus
 *
 */
@ServerEndpoint(value = "/WSfilehostupload", configurator = Configurator.class,
        encoders = JsonObjectEncoder.class,
        decoders = JsonObjectDecoder.class)
public class Fileupload {

    @Inject
    private WSHandler sessionHandler;

    final private HashMap sessionfiles = new HashMap();
    private EndpointConfig config;

    String savePath;

    @OnOpen
    public void onConnectionOpen(Session session, EndpointConfig config) {
        this.config = config;
        HttpSession httpSession = (HttpSession) this.config.getUserProperties().get("httpSession");
        session.setMaxBinaryMessageBufferSize(1024 * 12);
        session.setMaxTextMessageBufferSize(1024 * 12);
        Logger.getLogger(Fileupload.class.getName()).log(Level.INFO, "socket open wsSession: {0} httpSession: {1} MaxBinary: {2} MaxText: {3}",
                new Object[]{session.getId(), httpSession, session.getMaxBinaryMessageBufferSize(), session.getMaxTextMessageBufferSize()});
        //sessions.put(session, httpSession);
        sessionHandler.addSession(session, httpSession);
        try {
            InitialContext ic = new InitialContext();
            EtcBeanRemote etcEjb = (EtcBeanRemote) ic.lookup("java:global/filehost/filehost-ejb/EtcBean!su.olegus.bean.EtcBeanRemote");
            EtcInfo uploadinfo = etcEjb.getEtcByKey("upload");
            if (uploadinfo != null) {
                savePath = uploadinfo.getValue();
            }
        } catch (NamingException ex) {
            Logger.getLogger(Fileupload.class.getName()).log(Level.SEVERE, "NamingException {0}", ex);
        }
    }

    @OnClose
    public void onConnectionClose(Session session) {
        Logger.getLogger(Fileupload.class.getName()).log(Level.INFO, "socket close session: {0}", session.getId());
        //sessions.remove(session);
        sessionHandler.removeSession(session);
    }

    @OnMessage
    public JSONResponse onMessage(JSONObject message, Session session) throws IOException, EncodeException {
        HttpSession httpSession = (HttpSession) this.config.getUserProperties().get("httpSession");
        Logger.getLogger(Fileupload.class.getName()).log(Level.INFO, "onMessage session: {0} message: {1} httpSession: {2}",
                new Object[]{session.getId(), message, httpSession});
        JSONResponse response = new JSONResponse();
        String type = message.getType();
        response.setResponse(type);
        response.setResult("false");
        if ((httpSession != null) && (savePath != null) && (!savePath.isEmpty())) {
            //UserInfo userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
            Logger.getLogger(Fileupload.class.getName()).log(Level.INFO, "onMessage type: {0} ", type);
            switch (type) {
                case "buploadfile": {
                    String sout = "";
                    UploadInfo ui = (UploadInfo) message.getObject();
                    String captcha = (String) httpSession.getAttribute("captcha");
                    Logger.getLogger(Fileupload.class.getName()).log(Level.INFO, "captcha: {0} ", captcha);
                    if ((captcha != null) && (ui.getCaptcha() != null)
                            && (captcha.equals(ui.getCaptcha().toUpperCase()))) {
                        Logger.getLogger(Fileupload.class.getName()).log(Level.INFO, "getOriginalname(): {0} ", ui.getOriginalname());
                        String[] nameparts = ui.getOriginalname().split("\\.");
                        String exten = nameparts[nameparts.length - 1];
                        String newFilename = UUID.randomUUID().toString().replaceAll("\\-", "");
                        //ui.setFilename(newFilename);
                        ui.setFilename(newFilename + "." + exten);
                        sessionfiles.put(session.getId(), ui);
                        sout = "ok";
                        response.setResult(sout);
                    }
                    break;
                }
                case "euploadfile": {
                    String sout = "";
                    UploadInfo ui = (UploadInfo) sessionfiles.get(session.getId());
                    String mimeType = Files.probeContentType(Paths.get(savePath + ui.getFilename()));
                    Logger.getLogger(Fileupload.class.getName()).log(Level.INFO, "Upload mimeType {0}", mimeType);
                    sout = ui.getFilename();
                    response.setResult(sout);
                    sessionfiles.remove(session.getId());
                    break;
                }
            }
        }
        return response;
    }

    @OnMessage(maxMessageSize = 1024 * 64)
    public JSONResponse Fileupload(ByteBuffer msg, boolean last, Session session) {
        Logger.getLogger(Fileupload.class.getName()).log(Level.INFO, "processUpload session.getId() {0}", session.getId());
        //session.getMaxBinaryMessageBufferSize()
        JSONResponse response = new JSONResponse();
        UploadInfo ui = (UploadInfo) sessionfiles.get(session.getId());
        if (ui != null) {
            //String newFilename = UUID.randomUUID().toString().replaceAll("-", "");
            //Logger.getLogger(WSja.class.getName()).log(Level.INFO, "processUpload newFilename {0}", newFilename);
            //ui.setFilename(newFilename);
            try {
                File uploadedFile = new File(savePath + ui.getFilename());
                FileOutputStream fos = new FileOutputStream(uploadedFile, true);
                while (msg.hasRemaining()) {
                    fos.write(msg.get());
                }
                fos.flush();
                fos.close();
                //save descriptor
                HashMap dschm = new HashMap();
                dschm.put("filename", savePath + ui.getFilename());
                dschm.put("originalname", ui.getOriginalname());
                dschm.put("description", ui.getDescription());
                String dscPath = savePath + ui.getFilename() + ".dsc";
                FileOutputStream dscout = new FileOutputStream(new File(dscPath));
                ObjectOutputStream oos = new ObjectOutputStream(dscout);
                oos.writeObject(dschm);
                dscout.close();

                response.setResponse("processupload");
                response.setResult("ok");
            } catch (IOException ex) {
                Logger.getLogger(Fileupload.class.getName()).log(Level.SEVERE, "Exception {0}", ex);
            }
        }
        return response;
    }

}
