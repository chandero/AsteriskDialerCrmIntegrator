// Source File Name:   OutboundEventCommand.java

package command;

import command.updaters.OutboundEventUpdater;
import exceptions.UpdateDataException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.asteriskjava.manager.event.BridgeEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import utils.CrmConnector;

// Referenced classes of package command:
//            Command

public class OutboundEventCommand
    implements Command
{

    public OutboundEventCommand()
    {
    }

    public void execute(ManagerEvent event)
    {
        OutboundEventUpdater eventUpdater = new OutboundEventUpdater((BridgeEvent)event);
        BridgeEvent eventBridge = (BridgeEvent)event;
        try
        {
            eventUpdater.update();
            CrmConnector connector = new CrmConnector();
            java.util.List requestParams = eventUpdater.getRequestParams();
            requestParams.add(new BasicNameValuePair("callstatus", "beginCall"));
            requestParams.add(new BasicNameValuePair("state", eventBridge.getBridgeState()));
            requestParams.add(new BasicNameValuePair("channel1", eventBridge.getChannel1()));
            requestParams.add(new BasicNameValuePair("channel2", eventBridge.getChannel2()));
            // if(requestParams != null)
                // connector.sendCommand(requestParams);
        }
        catch(UpdateDataException ex)
        {
            logger.error("Error on update data on Bridge Event", ex);
        }
    }

    private static final Logger logger = Logger.getLogger(OutboundEventCommand.class);

}
