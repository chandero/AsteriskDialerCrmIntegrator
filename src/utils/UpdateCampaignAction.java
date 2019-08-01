// Source File Name:   UpdateCampaignAction.java

package utils;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author FUNAPOYO_SISTEMAS
 */
public class UpdateCampaignAction {

    private final String camp_nombre;
    private final String camp_fechainicial;
    private final String camp_fechafinal;
    private final String camp_horainicial_manana;
    private final String camp_horafinal_manana;
    private final String camp_horainicial_tarde;
    private final String camp_horafinal_tarde;
    private final String camp_estado;
    
    protected SQLiteDatabase database;
    
    public UpdateCampaignAction(HttpServletRequest request) {
      this.camp_nombre = request.getParameter("n");
      this.camp_fechainicial = request.getParameter("fi");
      this.camp_fechafinal = request.getParameter("ff");
      this.camp_horainicial_manana = request.getParameter("him");
      this.camp_horafinal_manana = request.getParameter("hfm");
      this.camp_horainicial_tarde = request.getParameter("hit");
      this.camp_horafinal_tarde = request.getParameter("hft");
      this.camp_estado = request.getParameter("e");
      
      database = SQLiteDatabase.getInstance();
    }

    public String doActionFromRequest() throws SQLException, ParseException {
        String result;
        List<Object> params = new ArrayList<>();
        DateFormat df = DateFormat.getDateInstance();
        DateFormat tf = DateFormat.getTimeInstance();
        params.add(df.parse(this.camp_fechainicial));
        params.add(df.parse(this.camp_fechafinal));
        params.add(tf.parse(this.camp_horainicial_manana));
        params.add(tf.parse(this.camp_horafinal_manana));
        params.add(tf.parse(this.camp_horainicial_tarde));
        params.add(tf.parse(this.camp_horafinal_tarde));
        params.add(this.camp_estado);
        params.add(this.camp_nombre);
        Boolean update = database.updateSql("UPDATE " + SQLiteDatabase.campsTable + " SET "
                + "camp_fechainicial = ?,"
                + "camp_fechafinal = ?,"
                + "camp_horainicial_manana = ?,"
                + "camp_horafinal_manana = ?,"
                + "camp_horainicial_tarde = ?,"
                + "camp_horafinal_tarde = ?,"
                + "camp_estado = ? "
                + "WHERE camp_nombre = ?;"
                , params);
        if (!update)
        {
            String insert = database.insertSql("INSERT INTO " + SQLiteDatabase.campsTable
                    + " (camp_fechainicial, camp_fechafinal, camp_horainicial_manana, camp_horafinal_manana"
                    + " camp_horainicial_tarde, camp_horafinal_tarde, camp_estado, camp_nombre) "
                    + " VALUES (?,?,?,?,?,?,?,?);"
                    , params);
            result = insert;
        } else {
            result = "Success";
        }
        return result;
    }
    
}
