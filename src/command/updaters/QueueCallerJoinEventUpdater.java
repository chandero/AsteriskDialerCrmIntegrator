/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.updaters;

import exceptions.UpdateDataException;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import org.asteriskjava.manager.event.QueueCallerJoinEvent;
import utils.CommonUtils;

public class QueueCallerJoinEventUpdater extends AbstractEventUpdater {

    private final QueueCallerJoinEvent event;

    public QueueCallerJoinEventUpdater(QueueCallerJoinEvent event) {
        this.event = event;
    }
    
    @Override
    public void update() throws UpdateDataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public List getRequestParams()
    {
        String callId = getCallIdByEventUid(CommonUtils.getFormattedUniqueId(event.getUniqueId()));
        List<BasicNameValuePair> eventParams = super.getRequestParamsWithCustomVariables(callId);
        return eventParams;
    }    
    
}
