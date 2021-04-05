/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package su.olegus.ws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.servlet.http.HttpSession;
import javax.websocket.EncodeException;
import javax.websocket.Session;

/**
 *
 * @author plintus
 */
@ApplicationScoped
public class WSHandler {

    private final HashMap<Session, HttpSession> sessions = new HashMap();
    private final HashMap<Session, HashMap> sessionsvars = new HashMap();

    public void addSession(Session session, HttpSession httpsession) {
        sessions.put(session, httpsession);
        sessionsvars.put(session, new HashMap());
        Logger.getLogger(WSHandler.class.getName()).log(Level.INFO, "addSession {0} httpSession: {1}",
                new Object[]{session.getId(), httpsession});
        Logger.getLogger(WSHandler.class.getName()).log(Level.INFO, "sessions count {0}", sessions.size());
    }

    public void removeSession(Session session) {
        Logger.getLogger(WSHandler.class.getName()).log(Level.INFO, "removeSession {0}", session);
        sessions.remove(session);
        sessionsvars.remove(session);
        Logger.getLogger(WSHandler.class.getName()).log(Level.INFO, "sessions count {0}", sessions.size());
    }
    public HttpSession getSession(Session session) {
        Logger.getLogger(WSHandler.class.getName()).log(Level.INFO, "getSession sessions count {0}", sessions.size());
        return sessions.get(session);
    }

    public void addSessionVar(Session session, String key, Object value) {
        sessionsvars.get(session).put(key, value);
        Logger.getLogger(WSHandler.class.getName()).log(Level.INFO, "addSessionVar {0} key: {1} value: {1}",
                new Object[]{session.getId(), key, value});
    }

    public void removeSessionVar(Session session, String key) {
        sessionsvars.get(session).remove(key);
        Logger.getLogger(WSHandler.class.getName()).log(Level.INFO, "removeSessionVar {0} key: {1}",
                new Object[]{session.getId(), key});
    }

    public Object getSessionVar(Session session, String key) {
        return sessionsvars.get(session).get(key);
    }

    public void sendToAllConnectedSessions() {
        String event = "testmessage";
        Logger.getLogger(WSHandler.class.getName()).log(Level.INFO, "sendToAllConnectedSessions sessions count {0}", sessions.size());
        for (Session key : sessions.keySet()) {
            try {
                JSONResponse response = new JSONResponse();
                response.setResponse(event);
                response.setResult("true");
                key.getBasicRemote().sendObject(response);
            } catch (IOException ex) {
                Logger.getLogger(WSHandler.class.getName()).log(Level.WARNING, "IOException {0}", ex);
            } catch (EncodeException ex) {
                Logger.getLogger(WSHandler.class.getName()).log(Level.WARNING, "EncodeException {0}", ex);
            }
        }
    }

    public void fireManagerEvent(WSEvent me) {
        //Logger.getLogger(WSHandler.class.getName()).log(Level.INFO, "Fire ManagerEvent {0}", me);
        Logger.getLogger(WSHandler.class.getName()).log(Level.WARNING, "sessions count {0}", sessions.size());
        //try {
            JSONObject json = null;
            JSONResponse response = new JSONResponse();

            if (me.getClass().getName().equals("org.asteriskjava.manager.event.PeerStatusEvent")) {
                /*
                response.setResponse("PeerStatusEvent");
                WSEvent pse = (WSEvent) me;
                String[] peer = "";
                List<Session> ss = findSessionByUser(peer[1]);
                for (Session s : ss) {
                    String obj = ""
                            + "{\"peer\":\"" + pse.getPeer() + "\""
                            + ",\"date\":\"" + pse.getDateReceived() + "\""
                            + ",\"status\":\"" + pse.getPeerStatus() + "\""
                            + ",\"channeltype\":\"" + pse.getChannelType() + "\"}";
                    response.setResult(obj);
                    s.getBasicRemote().sendObject(response);

                }*/
            }

        //} catch (IOException ex) {
        //    Logger.getLogger(WSHandler.class.getName()).log(Level.WARNING, "IOException {0}", ex);
        //} catch (EncodeException ex) {
        //    Logger.getLogger(WSHandler.class.getName()).log(Level.WARNING, "EncodeException {0}", ex);
        //}
    }
/*
    protected List<Session> findSessionByUser(String user) {
        List<Session> ret = new ArrayList();
        if (user != null) {
            for (Session key : sessions.keySet()) {
                HttpSession httpSession = sessions.get(key);
                if (httpSession != null) {
                    UserInfo userInfo = (UserInfo) httpSession.getAttribute("UserInfo");
                    if (userInfo.getName().equals(user)) {
                        ret.add(key);
                    }
                }
            }
        }
        return ret;
    }*/
}
