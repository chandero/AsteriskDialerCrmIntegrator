// Source File Name:   DeleteCampaignAction.java

package utils;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author FUNAPOYO_SISTEMAS
 */
public class DeleteCampaignAction {

    private final String camp_nombre;
    
    protected SQLiteDatabase database;
    
    public DeleteCampaignAction(HttpServletRequest request) {
      this.camp_nombre = request.getParameter("n");
      database = SQLiteDatabase.getInstance();
    }

    public String doActionFromRequest() throws SQLException, ParseException {
        String result;
        List<Object> params = new ArrayList<>();
        params.add(this.camp_nombre);
        Boolean delete = database.deleteSql("DELETE FROM " + SQLiteDatabase.campsTable
                + " WHERE camp_nombre = ?;"
                , params);
        if (delete) {
            result = "Success";
        } else {
            result = "Error";
        }
            
        return result;
    }
    
}
