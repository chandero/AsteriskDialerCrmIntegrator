// Source File Name:   DialerTask.java

package utils;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.asteriskjava.manager.ManagerConnection;


public class DialerTask
  implements Runnable
{

  private static final Logger logger = Logger.getLogger(ConnectionTask.class);
  
  public DialerTask() {
  }
  
  public void run()
  {
    // Leer campañas activas
    // DialerAction action = new DialerAction();
    DialerAgentAction action = new DialerAgentAction();
    while (true) {      
        try {

            System.out.println("Iniciando Marcacion...");              
            action.doAction();
            System.out.println("Finalizada Marcacion...");            
            Thread.sleep(5000L);
        } catch (SQLException ex) {
          java.util.logging.Logger.getLogger(DialerTask.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
          java.util.logging.Logger.getLogger(DialerTask.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
          java.util.logging.Logger.getLogger(DialerTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  }
}