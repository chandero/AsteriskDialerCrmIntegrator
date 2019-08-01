// Source File Name:   AgentStatusTask.java

package utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.asteriskjava.manager.ManagerConnection;


public class AgentStatusTask
  implements Runnable
{
  private static final Logger logger = Logger.getLogger(AgentStatusTask.class);
  private Boolean isChecking = false;
  private final String context;
  
  protected SQLiteDatabase database;
   private ManagerConnection asteriskConnection;
  
  public AgentStatusTask(ManagerConnection asteriskConnection) {
      this.database = SQLiteDatabase.getInstance();
      this.context = PropertiesReader.getProperty("context");
  }
  
  public void run()
  {
    while (!isChecking) {
      try {
          isChecking = true;
          List<Object> params = new ArrayList<>();
          ResultSet rs = database.selectSql("SELECT * FROM " + SQLiteDatabase.agentTable, params);
          while (rs.next()){
              List<Object> updateParams = new ArrayList<>();
              updateParams.add("VERIFICANDO");
              updateParams.add(rs.getString("uid"));
              database.updateSql("UPDATE " + SQLiteDatabase.agentTable + " SET agen_estado = ? WHERE uid = ?");
              AgentStatusAction action = new AgentStatusAction(context, rs.getString("agen_exten"), this.asteriskConnection);
              action.doAction();
          }
          Thread.sleep(10000L);
          isChecking = false;
      }
      catch (IllegalStateException ex) {
        logger.warn("Can not connect to Asterisk:", ex);
        try {
          Thread.sleep(1000L);
        } catch (InterruptedException ex1) {
              java.util.logging.Logger.getLogger(AgentStatusTask.class.getName()).log(Level.SEVERE, null, ex1);
          }
      } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(AgentStatusTask.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(AgentStatusTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  }
}
