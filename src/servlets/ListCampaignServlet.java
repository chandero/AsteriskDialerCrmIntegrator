// Source file name: ListCampaignServlet.java

package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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

@WebServlet(name="ListCampaingServlet", urlPatterns={"/lstcampaign"})
public class ListCampaignServlet extends HttpServlet {
    
  private static final Logger logger = Logger.getLogger(ListCallInfoServlet.class);
  protected SQLiteDatabase database = SQLiteDatabase.getInstance();

  public ListCampaignServlet() {
  }
  
  protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   
    JSONObject listObject = new JSONObject();

    try
    {
        JSONArray calls = new JSONArray();

        List<Object> params = new ArrayList<Object>();
       
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
        
        ResultSet rs = database.selectSql("SELECT *"
                    + " FROM " + SQLiteDatabase.campsTable
                    , params);
        while (rs.next()){
                JSONObject o = new JSONObject();
                o.put("uid", rs.getString("uid"));
                o.put("camp_nombre", rs.getString("camp_nombre"));
                o.put("camp_fechainicial", sdf.format(rs.getDate("camp_fechainicial").getTime()));
                o.put("camp_fechafinal", sdf.format(rs.getDate("camp_fechafinal").getTime()));
                o.put("camp_horainicial_manana", stf.format(rs.getDate("camp_horainicial_manana").getTime()));
                o.put("camp_horafinal_manana", stf.format(rs.getDate("camp_horafinal_manana").getTime()));
                o.put("camp_horainicial_tarde", stf.format(rs.getDate("camp_horainicial_tarde").getTime()));
                o.put("camp_horafinal_tarde", stf.format(rs.getDate("camp_horafinal_tarde").getTime()));
                o.put("camp_cola", rs.getString("camp_cola"));
                o.put("camp_estado", rs.getString("camp_estado"));
                calls.add(o);
        }
        listObject.put("campsinfo", calls);
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
