// Source File Name:   QueueCallerJoinEventCommand.java

package command;

import command.updaters.QueueCallerJoinEventUpdater;
import command.updaters.QueueEntryEventUpdater;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.QueueCallerJoinEvent;
import org.asteriskjava.manager.event.QueueEntryEvent;
import utils.CrmConnector;

// Referenced classes of package command:
//            Command

public class QueueCallerJoinEventCommand
    implements Command
{

    public QueueCallerJoinEventCommand()
    {
    }

    public void execute(ManagerEvent event)
    {
        QueueCallerJoinEvent eventBridge = (QueueCallerJoinEvent)event;
        QueueCallerJoinEventUpdater eventUpdater = new QueueCallerJoinEventUpdater(eventBridge);
        CrmConnector connector = new CrmConnector();
        java.util.List requestParams = eventUpdater.getRequestParams();
        requestParams.add(new BasicNameValuePair("callstatus", "beginCall"));
        if(requestParams != null)
            connector.sendCommand(requestParams);
    }

    private static final Logger logger = Logger.getLogger(OutboundEventCommand.class);

}
