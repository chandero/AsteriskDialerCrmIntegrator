/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.updaters;

import exceptions.UpdateDataException;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import org.asteriskjava.manager.event.QueueEntryEvent;
import utils.CommonUtils;

public class QueueEntryEventUpdater extends AbstractEventUpdater {

    private final QueueEntryEvent event;

    public QueueEntryEventUpdater(QueueEntryEvent event) {
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
