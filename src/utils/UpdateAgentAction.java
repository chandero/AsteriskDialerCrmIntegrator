// Source File Name: UpdateAgentAction.java


package utils;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public class UpdateAgentAction {

    private final String agen_id;
    private final String agen_nombre;
    private final String agen_exten;
    private final String agen_estado;
    
    protected SQLiteDatabase database;
    
    public UpdateAgentAction(HttpServletRequest request) {
      this.agen_id = request.getParameter("i");
      this.agen_nombre = request.getParameter("n");
      this.agen_exten = request.getParameter("e");
      this.agen_estado = request.getParameter("s");
      database = SQLiteDatabase.getInstance();
    }

    public String doActionFromRequest() throws SQLException, ParseException {
        String result;
        List<Object> params = new ArrayList<>();
        params.add(this.agen_nombre);
        params.add(this.agen_exten);
        params.add(this.agen_estado);
        params.add(this.agen_id);        
        
        Boolean update = database.updateSql("UPDATE " + SQLiteDatabase.agentTable + " SET "
                + "agen_nombre = ?,"
                + "agen_exten = ?,"
                + "agen_estado = ?"
                + "WHERE agen_id = ?;"
                , params);
        if (!update)
        {
            String insert = database.insertSql("INSERT INTO " + SQLiteDatabase.agentTable
                    + " (agen_nombre, agen_exten, agen_estado, agen_id)"
                    + " VALUES (?,?,?,?);"
                    , params);
            result = insert;
        } else {
            result = "Success";
        }
        return result;
    }
    
}
