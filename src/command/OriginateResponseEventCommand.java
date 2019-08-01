// Source File Name:   OriginateResponseEventCommand.java

package command;

import command.updaters.OriginateResponseEventUpdater;
import exceptions.UpdateDataException;
import org.apache.log4j.Logger;
import org.asteriskjava.manager.event.*;

// Referenced classes of package command:
//            Command

public class OriginateResponseEventCommand
    implements Command
{

    public OriginateResponseEventCommand()
    {
    }

    public void execute(ManagerEvent event)
    {
        OriginateResponseEventUpdater eventUpdater;
        eventUpdater = new OriginateResponseEventUpdater((OriginateResponseEvent)event);
        
        try
        {
            eventUpdater.update();
        }
        catch(UpdateDataException ex)
        {
            logger.error("Error on update data on Dial Event", ex);
        }
    }

    private static final Logger logger = Logger.getLogger(OriginateResponseEventCommand.class);

}