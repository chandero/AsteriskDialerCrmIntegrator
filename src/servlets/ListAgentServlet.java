// Source file name: ListAgentServlet.java

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

@WebServlet(name="ListAgentServlet", urlPatterns={"/lstagent"})
public class ListAgentServlet extends HttpServlet {
    
  private static final Logger logger = Logger.getLogger(ListCallInfoServlet.class);
  protected SQLiteDatabase database = SQLiteDatabase.getInstance();

  public ListAgentServlet() {
  }
  
  protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   
    JSONObject listObject = new JSONObject();

    try
    {
        JSONArray calls = new JSONArray();

        List<Object> params = new ArrayList<Object>();
       
        ResultSet rs = database.selectSql("SELECT *"
                    + " FROM " + SQLiteDatabase.agentTable
                    , params);
        while (rs.next()){
                JSONObject o = new JSONObject();
                o.put("uid", rs.getString("uid"));
                o.put("agen_id", rs.getString("agen_id"));
                o.put("agen_nombre", rs.getString("agen_nombre"));
                o.put("agen_exten", rs.getString("agen_exten"));
                o.put("agen_estado", rs.getString("agen_estado"));
                o.put("agen_exten_estado", rs.getString("agen_exten_estado"));
                calls.add(o);
        }
        listObject.put("agentssinfo", calls);
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
