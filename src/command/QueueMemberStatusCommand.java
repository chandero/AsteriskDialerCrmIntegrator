// Source File Name:   ExtenStatusCommand.java

package command;

import exceptions.UpdateDataException;
import org.apache.log4j.Logger;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.QueueMemberEvent;
import command.updaters.QueueMemberStatusEventUpdater;

/**
 *
 * @author Alexander
 */
public class QueueMemberStatusCommand implements Command {

    private static final Logger logger = Logger.getLogger(QueueMemberStatusCommand.class);
    
    public QueueMemberStatusCommand() {
    }
    
    @Override
    public void execute(ManagerEvent event) {
        logger.info("Recibi evento member estado");
        QueueMemberStatusEventUpdater eventUpdater = new QueueMemberStatusEventUpdater((QueueMemberEvent)event);
        try {
            eventUpdater.update();
        }
        catch(UpdateDataException ex)
        {
            logger.error("Error on update data on Cdr Event", ex);
        }
    }
    
}
