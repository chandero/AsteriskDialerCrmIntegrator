// Source File Name:   ConnectionTask.java

package utils;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.asteriskjava.AsteriskVersion;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.QueueStatusAction;
import org.asteriskjava.manager.response.ManagerResponse;

public class ConnectionTask
  implements Runnable
{
  private ManagerConnection asteriskConnection;
  private static final Logger logger = Logger.getLogger(ConnectionTask.class);
  protected SQLiteDatabase database;
  private final String context;
  
  public ConnectionTask(ManagerConnection asteriskConnection) {
    this.asteriskConnection = asteriskConnection;
    this.database = SQLiteDatabase.getInstance();
    this.context = PropertiesReader.getProperty("context");
  }
  
  public void run()
  {
    boolean isConnected = false;
    while (!isConnected) {
      try {
        asteriskConnection.login("on");
        AsteriskVersion astVersion = asteriskConnection.getVersion();
        asteriskcrmintegrator.AsteriskCrmIntegrator.ASTERISK_VERSION = astVersion.hashCode();
        isConnected = true;
      }
      catch (IllegalStateException|IOException|AuthenticationFailedException|TimeoutException ex) {
        logger.warn("Can not connect to Asterisk:", ex);
        try {
          Thread.sleep(1000L);
        }
        catch (InterruptedException localInterruptedException) {}
      }
    }
    boolean isChecking = false;
    List<Object> updateParams = new ArrayList<>();
    updateParams.add("LISTA");
    updateParams.add("UNKNOWN");
      try {
          database.updateSql("UPDATE " + SQLiteDatabase.agentTable + " SET agen_estado = ?, agen_exten_estado = ?", updateParams);
      } catch (SQLException ex) {
          java.util.logging.Logger.getLogger(ConnectionTask.class.getName()).log(Level.SEVERE, null, ex);
      }
    while (!isChecking && isConnected) {
      try {
          isChecking = true;
          QueueStatusAction actionq = new QueueStatusAction();
          try {
              ManagerResponse ex = asteriskConnection.sendAction(actionq, 1000L);
          } catch (TimeoutException|IOException var5) {
              logger.error("Error: " + var5.getMessage());
          }
          isChecking = false;
          Thread.sleep(10000L);
      }
      catch (IllegalStateException ex) {
        logger.warn("Can not connect to Asterisk:", ex);
        try {
          Thread.sleep(10000L);
        } catch (InterruptedException ex1) {
              java.util.logging.Logger.getLogger(AgentStatusTask.class.getName()).log(Level.SEVERE, null, ex1);
          }
      } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(ConnectionTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  }
}