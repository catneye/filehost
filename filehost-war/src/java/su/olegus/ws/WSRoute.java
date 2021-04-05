/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package su.olegus.ws;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 *
 * @author plintus
 */
//@MessageDriven(mappedName = "WSRoute",  activationConfig = {
@MessageDriven(mappedName = "jms/Queue", activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/queue/Filehost"),
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
})
@ApplicationScoped
public class WSRoute implements MessageListener {

    @Inject
    private WSHandler ws;

    @Override
    public void onMessage(Message message) {
        try {
            //Logger.getLogger(WSRoute.class.getName()).log(Level.INFO, "message {0}", 2);
            ObjectMessage msg = (ObjectMessage) message;
            WSEvent me = (WSEvent) msg.getObject();
            ws.fireManagerEvent(me);
        } catch (JMSException ex) {
            Logger.getLogger(WSRoute.class.getName()).log(Level.WARNING, "JMSException {0}", ex);
        } catch (Exception ex) {
            Logger.getLogger(WSRoute.class.getName()).log(Level.WARNING, "Exception {0}", ex);
        }
    }
    @PostConstruct
    public void initialize() {
            Logger.getLogger(WSRoute.class.getName()).log(Level.INFO, "initialize");
    }

}
