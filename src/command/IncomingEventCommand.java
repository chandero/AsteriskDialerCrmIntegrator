// Source File Name:   DialEventCommand.java

package command;

import command.updaters.IncomingEventUpdater;
import exceptions.UpdateDataException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.asteriskjava.manager.event.*;
import utils.CrmConnector;

// Referenced classes of package command:
//            Command

public class IncomingEventCommand
    implements Command
{

    public IncomingEventCommand()
    {
    }

    public void execute(ManagerEvent event)
    {
        IncomingEventUpdater eventUpdater;
        if(event.getClass() == DialEvent.class)
            eventUpdater = new IncomingEventUpdater((DialEvent)event);
        else
            eventUpdater = new IncomingEventUpdater((DialBeginEvent)event);
        try
        {
            eventUpdater.update();
            CrmConnector connector = new CrmConnector();
            java.util.List requestParams = eventUpdater.getRequestParams();
            requestParams.add(new BasicNameValuePair("callstatus", "incomingCall"));
            // if(requestParams != null)
                // connector.sendCommand(eventUpdater.getRequestParams());
        }
        catch(UpdateDataException ex)
        {
            logger.error("Error on update data on Dial Event", ex);
        }
    }

    private static final Logger logger = Logger.getLogger(IncomingEventCommand.class);

}
