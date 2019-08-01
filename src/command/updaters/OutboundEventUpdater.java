// Source File Name:   OutboundEventUpdater.java

package command.updaters;

import exceptions.UpdateDataException;
import java.sql.SQLException;
import java.util.*;
import org.apache.http.message.BasicNameValuePair;
import org.asteriskjava.manager.event.BridgeEvent;
import utils.CommonUtils;
import utils.SQLiteDatabase;

// Referenced classes of package command.updaters:
//            AbstractEventUpdater

public class OutboundEventUpdater extends AbstractEventUpdater
{

    public OutboundEventUpdater(BridgeEvent event)
    {
        this.event = event;
    }

    public List getRequestParams()
    {
        String callId = getCallIdByEventUid(CommonUtils.getFormattedUniqueId(event.getUniqueId1()));
        List<BasicNameValuePair> eventParams = super.getRequestParamsWithCustomVariables(callId);
        return eventParams;
    }

    public void update()
        throws UpdateDataException
    {
        String uniqueid1 = CommonUtils.getFormattedUniqueId(event.getUniqueId1());
        String uniqueid2 = CommonUtils.getFormattedUniqueId(event.getUniqueId2());
        String callId = getBridgeCallId();
        try
        {
            if(callId == null)
            {
                List createParams = new ArrayList();
                createParams.add(uniqueid1);
                createParams.add(uniqueid2);
                createParams.add("BridgeEvent");
                createParams.add(getCallerId1Number());
                createParams.add(getCallerId2Number());
                createParams.add(event.getBridgeState());
                database.updateSql((new StringBuilder()).append("INSERT INTO ").append(SQLiteDatabase.callsTable).append("(srcuid, destuid, event, from_number, to_number, bridged) VALUES(?,?,?,?,?,?);").toString(), createParams);
            } else
            {
                List updateParams = new ArrayList();
                updateParams.add(getCallerId1Number());
                updateParams.add(getCallerId2Number());
                updateParams.add("BridgeEvent");
                updateParams.add(event.getBridgeState());
                updateParams.add(callId);
                database.updateSql((new StringBuilder()).append("UPDATE ").append(SQLiteDatabase.callsTable).append(" SET from_number = ?, to_number = ?, event = ?, bridged = ? WHERE uid = ?;").toString(), updateParams);
            }
        }
        catch(SQLException ex)
        {
            throw new UpdateDataException("Cannot update Bridge event", ex);
        }
    }

    private String getBridgeCallId()
    {
        String callId = null;
        String uniqueid1 = CommonUtils.getFormattedUniqueId(event.getUniqueId1());
        String uniqueid2 = CommonUtils.getFormattedUniqueId(event.getUniqueId2());
        List callUUIDs = getCallIdBySource(uniqueid1);
        if(callUUIDs.isEmpty())
            callUUIDs = getCallIdBySource(uniqueid2);
        if(!callUUIDs.isEmpty())
            callId = (String)callUUIDs.get(callUUIDs.size() - 1);
        return callId;
    }

    private String getCallerId1Number()
    {
        String number = getCallerNumberFromChannel(event.getChannel1());
        if(number == null)
            number = event.getCallerId1();
        return number;
    }

    private String getCallerId2Number()
    {
        String number = getCallerNumberFromChannel(event.getChannel2());
        if(number == null)
            number = event.getCallerId2();
        return number;
    }

    private final BridgeEvent event;
}
