// Source File Name:   AddCampaignAction.java

package utils;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author FUNAPOYO_SISTEMAS
 */
public class AddCampaignAction {

    private final String camp_nombre;
    private final String camp_fechainicial;
    private final String camp_fechafinal;
    private final String camp_horainicial_manana;
    private final String camp_horafinal_manana;
    private final String camp_horainicial_tarde;
    private final String camp_horafinal_tarde;
    private final String camp_cola;
    private final String camp_estado;
    private final String camp_tiempofuera;
    
    protected SQLiteDatabase database;
    
    public AddCampaignAction(HttpServletRequest request) {
      this.camp_nombre = request.getParameter("n");
      this.camp_fechainicial = request.getParameter("fi");
      this.camp_fechafinal = request.getParameter("ff");
      this.camp_horainicial_manana = request.getParameter("him");
      this.camp_horafinal_manana = request.getParameter("hfm");
      this.camp_horainicial_tarde = request.getParameter("hit");
      this.camp_horafinal_tarde = request.getParameter("hft");
      this.camp_cola = request.getParameter("q");
      this.camp_estado = request.getParameter("e");
      this.camp_tiempofuera = request.getParameter("tf");
      
      database = SQLiteDatabase.getInstance();
    }

    public String doActionFromRequest() throws SQLException, ParseException {
        String result;
        List<Object> params = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
        params.add(sdf.parse(this.camp_fechainicial));
        params.add(sdf.parse(this.camp_fechafinal));
        params.add(stf.parse(this.camp_horainicial_manana));
        params.add(stf.parse(this.camp_horafinal_manana));
        params.add(stf.parse(this.camp_horainicial_tarde));
        params.add(stf.parse(this.camp_horafinal_tarde));
        params.add(this.camp_cola);
        params.add(this.camp_estado);
        params.add(Integer.valueOf(this.camp_tiempofuera));        
        params.add(this.camp_nombre);
        Boolean update = database.updateSql("UPDATE " + SQLiteDatabase.campsTable + " SET "
                + "camp_fechainicial = ?,"
                + "camp_fechafinal = ?,"
                + "camp_horainicial_manana = ?,"
                + "camp_horafinal_manana = ?,"
                + "camp_horainicial_tarde = ?,"
                + "camp_horafinal_tarde = ?,"
                + "camp_cola = ?,"
                + "camp_estado = ?, "
                + "camp_tiempofuera = ?"
                + "WHERE camp_nombre = ?;"
                , params);
        if (!update)
        {
            String query = "INSERT INTO " + SQLiteDatabase.campsTable
                    + " (camp_fechainicial, camp_fechafinal, camp_horainicial_manana, camp_horafinal_manana,"
                    + " camp_horainicial_tarde, camp_horafinal_tarde, camp_cola, camp_estado, camp_tiempofuera, camp_nombre) "
                    + " VALUES (?,?,?,?,?,?,?,?,?,?);";
            String insert = database.insertSql(query, params);
            result = insert;
        } else {
            result = "Success";
        }
        return result;
    }
    
}
