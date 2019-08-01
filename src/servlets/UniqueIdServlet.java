// Source file name: UniqueIdServlet.java

package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

@WebServlet(name="UniqueIdServlet", urlPatterns={"/lstunique"})
public class UniqueIdServlet extends HttpServlet {
    
  private static final Logger logger = Logger.getLogger(UniqueIdServlet.class);
  protected SQLiteDatabase database = SQLiteDatabase.getInstance();

  public UniqueIdServlet() {
  }
  
  protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   
    JSONObject listObject = new JSONObject();

    try
    {
        JSONArray calls = new JSONArray();

        List<Object> params = new ArrayList<Object>();
       
        ResultSet rs = database.selectSql("SELECT * FROM " + SQLiteDatabase.uniqueIdTable);
        
        while (rs.next()) {
            params.clear();
            params.add(rs.getString("agen_uid"));
            String agen_uid = "";
            String agen_id = "";
            String agen_nombre = "";
            String agen_exten = "";
            String agen_estado = "";
            String agen_exten_estado = "";
            ResultSet rsa = database.selectSql("SELECT *"
                    + " FROM " + SQLiteDatabase.agentTable + " WHERE uid = ?"
                    , params);
            while (rsa.next()) {
                agen_uid = rsa.getString("uid");
                agen_id = rsa.getString("agen_id");
                agen_nombre = rsa.getString("agen_nombre");
                agen_exten = rsa.getString("agen_exten");
                agen_estado = rsa.getString("agen_estado");
                agen_exten_estado = rsa.getString("agen_exten_estado");
            }
            JSONObject o = new JSONObject();
                o.put("uniqueId",  rs.getString("uid"));
                o.put("call_agen_uid", rs.getString("agen_uid"));
                o.put("agen_uid", agen_uid);
                o.put("agen_id", agen_id);
                o.put("agen_nombre", agen_nombre);
                o.put("agen_exten", agen_exten);
                o.put("agen_estado", agen_estado);
                o.put("agen_exten_estado", agen_exten_estado);
            calls.add(o);
        }
       
        listObject.put("uniqueIdinfo", calls);
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
