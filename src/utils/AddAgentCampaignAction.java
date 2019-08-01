// Source File Name: AddAgentCampaignAction.java

package utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public class AddAgentCampaignAction {

    private final String agen_id;
    private final String camp_nombre;
    
    protected SQLiteDatabase database;
    
    public AddAgentCampaignAction(HttpServletRequest request) {
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
            ResultSet rsa = database.selectSql("SELECT * FROM " + SQLiteDatabase.campagentTable + " WHERE camp_id = ? and agen_id = ?", params);
            if (rsa.next()) {
              result = "Error, already exist";  
            } else {
                String insert = database.insertSql("INSERT INTO " + SQLiteDatabase.campagentTable
                    + " (camp_id, agen_id)"
                    + " VALUES (?,?);"
                    , params);
                result = insert;
            }
        } else {
            result = "Error, agent or campaign does not exist";
        }
        return result;
    }
    
}
