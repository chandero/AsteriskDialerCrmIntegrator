// Source File Name:   ActivateCampaignAction.java

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
public class ActivateCampaignAction {

    private final String camp_nombre;
    
    protected SQLiteDatabase database;
    
    public ActivateCampaignAction(HttpServletRequest request) {
      this.camp_nombre = request.getParameter("n");
      database = SQLiteDatabase.getInstance();
    }

    public String doActionFromRequest() throws SQLException, ParseException {
        String result;
        List<Object> params = new ArrayList<>();
        params.add("CORRIENDO");
        params.add(this.camp_nombre);
        Boolean update = database.updateSql("UPDATE " + SQLiteDatabase.campsTable + " SET "
                + "camp_estado = ? "
                + "WHERE camp_nombre = ?;"
                , params);
        if (!update)
        {
            result = "Error";
        } else {
            result = "Success";
        }
        return result;
    }    
}
