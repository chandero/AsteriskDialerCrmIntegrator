// Source file name: ListAgentCampaignServlet.java

package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.SQLiteDatabase;

@WebServlet(name="ListAgentCampaignServlet", urlPatterns={"/lstagentcampaign"})
public class ListAgentCampaignServlet extends HttpServlet {
    
  private static final Logger logger = Logger.getLogger(ListCallInfoServlet.class);
  protected SQLiteDatabase database = SQLiteDatabase.getInstance();

  public ListAgentCampaignServlet() {
  }
  
  protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    String camp_nombre = request.getParameter("n");

    JSONObject listObject = new JSONObject();

    try
    {
        JSONArray calls = new JSONArray();

        List<Object> params = new ArrayList<Object>();
        params.add(camp_nombre);
       
        ResultSet rs = database.selectSql("SELECT a.agen_id, a.agen_nombre, a.agen_exten, a.agen_estado FROM " + SQLiteDatabase.campagentTable
                    + " ac LEFT JOIN " + SQLiteDatabase.agentTable + " a ON a.uid = ac.agen_id "
                    + " LEFT JOIN " + SQLiteDatabase.campsTable + " c ON c.uid = ac.camp_id "
                    + " WHERE c.camp_nombre = ?;"
                    , params);
        while (rs.next()){
                JSONObject o = new JSONObject();
                o.put("agen_id", rs.getString("agen_id"));
                o.put("agen_nombre", rs.getString("agen_nombre"));
                o.put("agen_exten", rs.getString("agen_exten"));
                o.put("agen_estado", rs.getString("agen_estado"));
                calls.add(o);
        }
        listObject.put("agentcampaigninfo", calls);
    } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(ListCallInfoServlet.class.getName()).log(Level.SEVERE, null, ex);
    }
        
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();

    try { 
            writer.println(listObject.toJSONString());
    }
    catch (Throwable localThrowable1)
    {
          throw localThrowable1;
    } finally {
          if (writer != null) 
                  try { 
                      writer.close(); 
                  } catch (Throwable localThrowable2) {
                      localThrowable2.addSuppressed(localThrowable2); 
                  } else 
                  writer.close(); 
    }
  }
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    processRequest(request, response);
  }
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    processRequest(request, response);
  }    
}
