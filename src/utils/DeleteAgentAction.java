// Source File Name: DeleteAgentAction.java

package utils;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public class DeleteAgentAction {

    private final String agen_id;
    
    protected SQLiteDatabase database;
    
    public DeleteAgentAction(HttpServletRequest request) {
      this.agen_id = request.getParameter("i");
      database = SQLiteDatabase.getInstance();
    }

    public String doActionFromRequest() throws SQLException, ParseException {
        String result;
        List<Object> params = new ArrayList<>();
        params.add(this.agen_id);
        
        Boolean delete = database.deleteSql("DELETE FROM " + SQLiteDatabase.agentTable
                + " WHERE agen_id = ?;"
                , params);
        if (delete)
        {
            result = "Success";
        } else {
            result = "Error";
        }
        return result;
    }
    
}
