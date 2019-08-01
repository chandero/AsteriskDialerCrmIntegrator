// Source File Name:   CommandsFactory.java

package command.helpers;

import command.*;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.asteriskjava.manager.event.ManagerEvent;

public class CommandsFactory
{

    private static final Logger logger = Logger.getLogger(CommandsFactory.class);
    
    public CommandsFactory()
    {
    }

    public static Command createCommand(ManagerEvent event)
        throws IllegalAccessException, InstantiationException, ClassNotFoundException
    {
        String simpleEventName = event.getClass().getSimpleName();
        logger.info("Nombre del evento : " + simpleEventName);
        if(eventProcessors.containsKey(simpleEventName))
            return (Command)((Class)eventProcessors.get(simpleEventName)).newInstance();
        else
            return null;
    }

    private static final Map eventProcessors;

    static 
    {
        eventProcessors = new HashMap();
        eventProcessors.put("BridgeEvent", OutboundEventCommand.class);
        eventProcessors.put("CdrEvent", CdrEventCommand.class);
        eventProcessors.put("DialEvent", IncomingEventCommand.class);
        eventProcessors.put("DialBeginEvent", IncomingEventCommand.class);
        eventProcessors.put("HangupEvent", HangupEventCommand.class);
        eventProcessors.put("VarSetEvent", VarSetEventCommand.class);
        eventProcessors.put("QueueMemberEvent", QueueMemberStatusCommand.class);
        eventProcessors.put("OriginateResponseEvent", OriginateResponseEventCommand.class);
        eventProcessors.put("QueueEntryEvent", QueueEntryEventCommand.class);
    }
}
