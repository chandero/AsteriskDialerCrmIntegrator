// Source File Name: DeleteAgentCampaignAction.java

package utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public class DeleteAgentCampaignAction {

    private final String agen_id;
    private final String camp_nombre;
    
    protected SQLiteDatabase database;
    
    public DeleteAgentCampaignAction(HttpServletRequest request) {
      this.agen_id = request.getParameter("i");
      this.camp_nombre = request.getParameter("n");
      database = SQLiteDatabase.getInstance();
    }

    public String doActionFromRequest() throws SQLException, ParseException {
        String result;
        List<Object> params = new ArrayList<>();
        params.add(this.camp_nombre);
        int a_uid = 0;
        int c_uid = 0;
        ResultSet rs = database.selectSql("SELECT * FROM " + SQLiteDatabase.campsTable + " WHERE camp_nombre = ?;", params);
        while (rs.next()){
            c_uid = rs.getInt("uid");
        }
        params.clear();
        params.add(this.agen_id);
        rs = database.selectSql("SELECT * FROM " + SQLiteDatabase.agentTable + " WHERE agen_id = ?;", params);
        while (rs.next()){
            a_uid = rs.getInt("uid");
        }
        
        if (a_uid > 0 & c_uid > 0) {
            
            // Buscar Agente en Campaña
            params.clear();
            params.add(c_uid);
            params.add(a_uid);
            Boolean delete = database.deleteSql("DELETE FROM " + SQLiteDatabase.campagentTable + " WHERE camp_id = ? and agen_id = ?", params);
            if (delete) {
              result = "Success";  
            } else {
                result = "Error, can't delete";
            }
        } else {
            result = "Error, agent or campaign does not exist";
        }
        return result;
    }
    
}
