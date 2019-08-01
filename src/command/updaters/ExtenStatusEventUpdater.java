// Source file name: ExtenStatusEventUpdater.java

package command.updaters;

import exceptions.UpdateDataException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.asteriskjava.manager.event.ExtensionStatusEvent;
import utils.SQLiteDatabase;
/**
 *
 * @author Alexander
 */
public class ExtenStatusEventUpdater extends AbstractEventUpdater {

    private final ExtensionStatusEvent event;
    
    protected SQLiteDatabase database;
    
    public ExtenStatusEventUpdater(ExtensionStatusEvent event) {
        this.event = event;
        this.database = SQLiteDatabase.getInstance();
    }

    @Override
    public void update() throws UpdateDataException {
        try {
            String exten = event.getExten();
            String status = event.getStatustext();
            List<Object> params = new ArrayList<>();
            params.add(status);
            params.add(exten);
            Boolean updated = database.updateSql("UPDATE " + SQLiteDatabase.agentTable + " SET agen_exten_status = ? WHERE agen_exten = ?", params);
        } catch (SQLException ex) {
            Logger.getLogger(ExtenStatusEventUpdater.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
