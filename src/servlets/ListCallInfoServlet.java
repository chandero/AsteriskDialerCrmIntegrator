// Source File Name:   ListCallInfoServlet.java

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
import utils.ListCallInfo;
import utils.SQLiteDatabase;

@WebServlet(name="ListCallInfo", urlPatterns={"/listcallinfo"})
public class ListCallInfoServlet
  extends HttpServlet
{
  public ListCallInfoServlet() {}
  
  private static final Logger logger = Logger.getLogger(ListCallInfoServlet.class);
  protected SQLiteDatabase database = SQLiteDatabase.getInstance();
  
  protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String strServiceId = request.getParameter("strServiceId");
    String strList = request.getParameter("strList");
    
    JSONObject listObject = new JSONObject();

    try
    {
        JSONArray calls = new JSONArray();

        List<Object> params = new ArrayList<>();
        params.add(strServiceId);
        params.add(strList);
        
        ResultSet rs = database.selectSql("SELECT "
                    + "uid, list_service, list_group, list_phonenumber, list_agent, list_priority, list_leadid, list_status"
                    + " FROM " + SQLiteDatabase.listsTable + " WHERE list_service = ? and list_group = ?"
                    , params);
        while (rs.next()){
                ListCallInfo call = new ListCallInfo();
                call.setUid(rs.getString("uid"));
                call.setList_service(rs.getString("list_service"));
                call.setList_group(rs.getString("list_group"));
                call.setList_phonenumber(rs.getString("list_phonenumber"));
                call.setList_agent(rs.getString("list_agent"));
                call.setList_priority(rs.getString("list_priority"));
                call.setList_leadid(rs.getString("list_leadid"));
                call.setList_status(rs.getString("list_status"));

                JSONObject o = new JSONObject();
                o.put("uid", call.getUid());
                o.put("list_service", call.getList_service());
                o.put("list_group", call.getList_group());
                o.put("list_phonenumber", call.getList_phonenumber());
                o.put("list_agent", call.getList_agent());
                o.put("list_priority", call.getList_priority());
                o.put("list_leadid", call.getList_leadid());
                o.put("list_status", call.getList_status());
                calls.add(o);
        }
        listObject.put("callsinfo", calls);
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
