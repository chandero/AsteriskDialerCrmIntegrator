// Source File Name:   ExtenStatusCommand.java

package command;

import command.updaters.ExtenStatusEventUpdater;
import exceptions.UpdateDataException;
import org.apache.log4j.Logger;
import org.asteriskjava.manager.event.ExtensionStatusEvent;
import org.asteriskjava.manager.event.ManagerEvent;

/**
 *
 * @author Alexander
 */
public class ExtenStatusCommand implements Command {

    private static final Logger logger = Logger.getLogger(ExtenStatusCommand.class);
    
    public ExtenStatusCommand() {
    }
    
    @Override
    public void execute(ManagerEvent event) {
        ExtenStatusEventUpdater eventUpdater = new ExtenStatusEventUpdater((ExtensionStatusEvent)event);
        try {
            eventUpdater.update();
        }
        catch(UpdateDataException ex)
        {
            logger.error("Error on update data on Cdr Event", ex);
        }
    }
    
}
