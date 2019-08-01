// Source File Name:   DeactivateCampaignAction.java

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
public class DeactivateCampaignAction {

    private final String camp_nombre;
    
    protected SQLiteDatabase database;
    
    public DeactivateCampaignAction(HttpServletRequest request) {
      this.camp_nombre = request.getParameter("n");
      database = SQLiteDatabase.getInstance();
    }

    public String doActionFromRequest() throws SQLException, ParseException {
        String result;
        List<Object> params = new ArrayList<>();
        params.add("LISTA");
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
