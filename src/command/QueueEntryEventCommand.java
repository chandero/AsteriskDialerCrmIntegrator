// Source File Name:   QueueEnterEventCommand.java

package command;

import command.updaters.QueueEntryEventUpdater;
import exceptions.UpdateDataException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.QueueEntryEvent;
import utils.CrmConnector;

// Referenced classes of package command:
//            Command

public class QueueEntryEventCommand
    implements Command
{

    public QueueEntryEventCommand()
    {
    }

    public void execute(ManagerEvent event)
    {
        QueueEntryEvent eventBridge = (QueueEntryEvent)event;
        QueueEntryEventUpdater eventUpdater = new QueueEntryEventUpdater(eventBridge);
        CrmConnector connector = new CrmConnector();
        java.util.List requestParams = eventUpdater.getRequestParams();
        requestParams.add(new BasicNameValuePair("callstatus", "beginCall"));
        if(requestParams != null)
            connector.sendCommand(requestParams);
    }

    private static final Logger logger = Logger.getLogger(OutboundEventCommand.class);

}
