// Source File Name:   AgentStatusAction.java

package utils;

import java.io.IOException;
import org.apache.log4j.Logger;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.QueueStatusAction;
import org.asteriskjava.manager.response.ManagerResponse;

public class AgentStatusAction {

   private static final Logger logger = Logger.getLogger(AgentStatusAction.class);

   private final String context;
   private final String exten;
    private final ManagerConnection asteriskConnection;

   public AgentStatusAction(String context, String exten, ManagerConnection asteriskConnection) {
       this.context = context;
       this.exten = exten;
       this.asteriskConnection = asteriskConnection;
   }

   public String doAction() {
         QueueStatusAction actionq = new QueueStatusAction();
         actionq.setQueue(this.exten);
         try {
            System.out.println("Enviando petición de Estado de Cola");
            ManagerResponse ex = asteriskConnection.sendAction(actionq, 1000L);
            logger.info("Respuesta peticion estado cola: " + ex.getResponse());
            return ex.getResponse();
         } catch (TimeoutException|IOException var5) {
            return "Error: " + var5.getMessage();
         }

   }
}