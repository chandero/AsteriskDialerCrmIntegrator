// Source File Name:   OutboundCall.java

package utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.response.ManagerResponse;

public class OutboundCall {

   private static final Logger logger = Logger.getLogger(OutboundCall.class);
   private static final List ignoreCallParamsNames = new ArrayList();
   private final String from;
   private final String to;
   private String context;
   private final String secret;
   private final Map callParams;
   
   private final String strLeadID;
   private final String strPhone;
   private final String strSchool;
   private final String strProgram;
   private final String strAgent;
   private final String strServiceId;
   private final String strLoadId;
   private final String idPriority;
   private final String strExten;


   public OutboundCall(HttpServletRequest request) {
       System.out.println("utils.OutboundCall.<init>()" + request.toString());
      this.from = request.getParameter("from");
      this.to = request.getParameter("to");
      this.secret = request.getParameter("strServiceId");
      this.strLeadID = request.getParameter("strLeadID");
      this.strPhone = request.getParameter("strPhone");
      this.strSchool = request.getParameter("strSchool");
      this.strProgram = request.getParameter("strProgram");
      this.strAgent = request.getParameter("strAgent");
      this.strServiceId = request.getParameter("strServiceId");
      this.strLoadId = request.getParameter("strLoadId");
      this.idPriority = request.getParameter("idPriority");
      this.strExten = request.getParameter("strExten");

      this.callParams = new HashMap();
      
      Enumeration parameterNames = request.getParameterNames();

      while(parameterNames.hasMoreElements()) {
         String parameterName = (String)parameterNames.nextElement();
         if(!ignoreCallParamsNames.contains(parameterName)) {
            this.callParams.put(parameterName, request.getParameter(parameterName));
         }
      }
   }

   public String doCallFromRequest() {
      String secretKey = PropertiesReader.getProperty("strServiceId");
      String queue = PropertiesReader.getProperty("queue");
      if (this.strExten != null) {
          queue = this.strExten;
      }
      this.context = PropertiesReader.getProperty("context");
      if(this.secret.equals(secretKey)) {
         OriginateAction action = new OriginateAction();
         action.setChannel(ChannelProtocolResolver.getOriginateChannelProtocol(queue) + "/" + queue);
         action.setContext(this.context);
         action.setExten(this.strPhone);
         action.setCallerId(this.strLeadID);
         action.setPriority(Integer.valueOf(this.idPriority));
         action.setVariables(this.callParams);
         ManagerConnection connection = ConnectionManager.getConnection();
         try {
            connection.login();
            ManagerResponse ex = connection.sendAction(action, 30000L);
            connection.logoff();
            return ex.getResponse();
         } catch (TimeoutException|IOException|AuthenticationFailedException var5) {
            logger.fatal("Error on send request to asterisk", var5);
            connection.logoff();
            return "Error: " + var5.getMessage();
         }
      } else {
         return "Authentication Failure";
      }
   }

   static {
      ignoreCallParamsNames.add("from");
      ignoreCallParamsNames.add("to");
      ignoreCallParamsNames.add("context");
      ignoreCallParamsNames.add("secret");
      ignoreCallParamsNames.add("event");
      ignoreCallParamsNames.add("strPhone");
      ignoreCallParamsNames.add("strServiceId");
      ignoreCallParamsNames.add("strLoadId");
      ignoreCallParamsNames.add("idPriority");
      ignoreCallParamsNames.add("strExten");
   }
}