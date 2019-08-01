// Source File Name:   QueueMemberStatusEventUpdater.java

package command.updaters;

import exceptions.UpdateDataException;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.asteriskjava.manager.event.QueueMemberEvent;
import utils.SQLiteDatabase;

// Referenced classes of package command.updaters:
//            AbstractEventUpdater

public class QueueMemberStatusEventUpdater extends AbstractEventUpdater
{

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(QueueMemberStatusEventUpdater.class);
    
    public QueueMemberStatusEventUpdater(QueueMemberEvent event)
    {
        this.event = event;
    }

    public void update()
        throws UpdateDataException
    {
        /* 
            0 - AST_DEVICE_UNKNOWN
            1 - AST_DEVICE_NOT_INUSE
            2 - AST_DEVICE_INUSE
            3 - AST_DEVICE_BUSY
            4 - AST_DEVICE_INVALID
            5 - AST_DEVICE_UNAVAILABLE
            6 - AST_DEVICE_RINGING
            7 - AST_DEVICE_RINGINUSE
            8 - AST_DEVICE_ONHOLD
        */

        String statusExten = "BUSY";
        int status = event.getStatus();
        Boolean paused = event.getPaused();
        if (!paused) {
           if (status == 1) {
               statusExten = "IDLE";
           } else {
               statusExten = "BUSY";
           }
        } else {
           statusExten = "BUSY"; 
        }
        logger.info("Registrando estado del agente: " + event.getQueue() + ", extension: " + event.getStateinterface() + ", estado: " + event.getStatus());
        List params = new ArrayList<>();
        params.add(statusExten);
        params.add(event.getQueue());
        try {
            database.updateSql((new StringBuilder()).append("UPDATE ").append(SQLiteDatabase.agentTable).append(" SET agen_exten_estado = ? WHERE agen_exten = ?").toString(), params);
        } catch (SQLException ex) {
            Logger.getLogger(QueueMemberStatusEventUpdater.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private final QueueMemberEvent event;
}
