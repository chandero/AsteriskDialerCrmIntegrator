// Source File Name:   OriginateResponseEventUpdater.java

package command.updaters;

import exceptions.UpdateDataException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.asteriskjava.manager.event.OriginateResponseEvent;
import utils.*;

// Referenced classes of package command.updaters:
//            AbstractEventUpdater

public class OriginateResponseEventUpdater extends AbstractEventUpdater
{

    private final OriginateResponseEvent event;

    public OriginateResponseEventUpdater(OriginateResponseEvent originateEvent)
    {
        this.event = originateEvent;
    }

    public void update()
        throws UpdateDataException
    {

            String uid = event.getActionId();
            List updateParams = new ArrayList();
            
            System.out.println("OriginateResponse: " + event.getResponse());
            
            if (event.getResponse().equals("Failure")) {
                updateParams.add("LISTA");
            } else {
                updateParams.add("OCUPADO");
            }
            updateParams.add(uid);

            try {
                database.updateSql("UPDATE " + SQLiteDatabase.agentTable + " SET agen_estado = ? WHERE uid = ?", updateParams);
                if (event.getResponse().equals("Success")) {
                    updateParams.clear();
                    updateParams.add(event.getUniqueId());
                    updateParams.add(uid);
                    database.insertSql("INSERT INTO " + SQLiteDatabase.uniqueIdTable + " VALUES (?,?);", updateParams);
                }
            }
            catch(SQLException ex)
            {
                throw new UpdateDataException("Cannot update on UniqueId List event", ex);
            }
    }
}
