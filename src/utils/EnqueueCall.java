// Source File Name:   OutboundCall.java

package utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class EnqueueCall {

   private static final Logger logger = Logger.getLogger(OutboundCall.class);
   private static final List ignoreCallParamsNames = new ArrayList();
   private final String context;
   private final Map callParams;
   
   private final String strLeadID;
   private final String strPhone;
   private final String strList;
   private final String strAgent;
   private final String strServiceId;
   private final String strIdPriority;
   
   protected SQLiteDatabase database = SQLiteDatabase.getInstance();


   public EnqueueCall(HttpServletRequest request) {
      System.out.println("utils.EnqueueCall.<init>()" + request.toString());
      this.strLeadID = request.getParameter("strLeadID");
      this.strServiceId = request.getParameter("strServiceId");      
      this.strPhone = request.getParameter("strPhone");
      this.strList = request.getParameter("strList");
      this.strAgent = request.getParameter("strAgent");
      this.strIdPriority = request.getParameter("idPriority");
      this.context = PropertiesReader.getProperty("context");

      this.callParams = new HashMap();
      
      Enumeration parameterNames = request.getParameterNames();

      while(parameterNames.hasMoreElements()) {
         String parameterName = (String)parameterNames.nextElement();
         if(!ignoreCallParamsNames.contains(parameterName)) {
            this.callParams.put(parameterName, request.getParameter(parameterName));
         }
      }
   }

   public String doEnqueueCallFromRequest() {
       String result = "Error";
       try { 
           List<Object> params = new ArrayList<Object>();
           params.add(this.strServiceId);
           params.add(this.strList);
           params.add(this.strPhone);
           params.add(this.strAgent);
           params.add(this.strIdPriority);
           params.add(this.strLeadID);
           params.add("PENDIENTE");
           result = database.insertSql("INSERT INTO " + SQLiteDatabase.listsTable + " (list_service, list_group, list_phonenumber, list_agent, list_priority, list_leadid, list_status ) VALUES (?,?,?,?,?,?,?);", params);
       } catch (Exception e) {
           logger.error("Error Encolando :" + e);
       }
       return result;
   }

   static {
      ignoreCallParamsNames.add("event");
   }
}