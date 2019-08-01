// Source File Name:   DeleteListAction.java

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
public class DeleteListAction {

    private final String list_service;
    private final String list_group;
    
    protected SQLiteDatabase database;
    
    public DeleteListAction(HttpServletRequest request) {
      this.list_service = request.getParameter("n");
      this.list_group = request.getParameter("g");
      database = SQLiteDatabase.getInstance();
    }

    public String doActionFromRequest() throws SQLException, ParseException {
        String result;
        List<Object> params = new ArrayList<>();
        params.add(this.list_service);
        params.add(this.list_group);
        Boolean delete = database.deleteSql("DELETE FROM " + SQLiteDatabase.listsTable
                + " WHERE list_service = ? and list_group = ?;"
                , params);
        if (delete) {
            result = "Success";
        } else {
            result = "Error";
        }
            
        return result;
    }
    
}
