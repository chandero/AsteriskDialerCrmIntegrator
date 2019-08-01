// Source File Name: DialerAction.java
package utils;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.response.ManagerResponse;

public class DialerAction {

    private static final Logger logger = Logger.getLogger(DialerAction.class);
    protected SQLiteDatabase database;
    private final String context;
    private final String queue;

    public DialerAction() {
        database = SQLiteDatabase.getInstance();
        this.context = PropertiesReader.getProperty("context");
        this.queue = PropertiesReader.getProperty("queue");
    }

    public String doAction() throws SQLException, ParseException {
        String result = "";
        System.out.println("Ingrese a Realizar Marcacion");
        SimpleDateFormat sdft = new SimpleDateFormat("HH:mm:ss");
        List<Object> params = new ArrayList<>();
        params.add("CORRIENDO");
        ResultSet rs = database.selectSql("SELECT * FROM " + SQLiteDatabase.campsTable
                + " WHERE camp_estado = ?;",
                 params);
        while (rs.next()) {
            // Validar horario de la campaña
            System.out.println("Leyendo Campanha: " + rs.getString("camp_nombre"));
            String camp_nombre = rs.getString("camp_nombre");
            Date fechainicial = new Date(rs.getDate("camp_fechainicial").getTime());
            Date fechafinal = new Date(rs.getDate("camp_fechafinal").getTime());
            String horainicial_manana = sdft.format(rs.getDate("camp_horainicial_manana").getTime());
            String horafinal_manana = sdft.format(rs.getDate("camp_horafinal_manana").getTime());            
            String horainicial_tarde = sdft.format(rs.getDate("camp_horainicial_tarde").getTime());
            String horafinal_tarde = sdft.format(rs.getDate("camp_horafinal_tarde").getTime());
            
            fechainicial.setHours(0);
            fechainicial.setMinutes(0);
            fechainicial.setSeconds(0);
            
            fechafinal.setHours(23);
            fechafinal.setMinutes(59);
            fechafinal.setSeconds(59);            

            SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd");

            Date ahora = Calendar.getInstance().getTime();
            
            SimpleDateFormat sdfdt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            Calendar fim = Calendar.getInstance();
            Calendar ffm = Calendar.getInstance();
            
            Calendar fit = Calendar.getInstance();
            Calendar fft = Calendar.getInstance();            
            
            fim.set(Calendar.YEAR, ahora.getYear() + 1900);
            fim.set(Calendar.MONTH, ahora.getMonth());
            fim.set(Calendar.DATE, ahora.getDate());
            fim.set(Calendar.HOUR_OF_DAY, Integer.valueOf(horainicial_manana.split(":")[0]));
            fim.set(Calendar.MINUTE, Integer.valueOf(horainicial_manana.split(":")[1]));
            fim.set(Calendar.SECOND, Integer.valueOf(horainicial_manana.split(":")[2]));
            
            ffm.set(Calendar.YEAR, ahora.getYear() + 1900);
            ffm.set(Calendar.MONTH, ahora.getMonth());
            ffm.set(Calendar.DATE, ahora.getDate());
            ffm.set(Calendar.HOUR_OF_DAY, Integer.valueOf(horafinal_manana.split(":")[0]));
            ffm.set(Calendar.MINUTE, Integer.valueOf(horafinal_manana.split(":")[1]));
            ffm.set(Calendar.SECOND, Integer.valueOf(horafinal_manana.split(":")[2]));    
            
            fit.set(Calendar.YEAR, ahora.getYear() + 1900);
            fit.set(Calendar.MONTH, ahora.getMonth());
            fit.set(Calendar.DATE, ahora.getDate());
            fit.set(Calendar.HOUR_OF_DAY, Integer.valueOf(horainicial_tarde.split(":")[0]));
            fit.set(Calendar.MINUTE, Integer.valueOf(horainicial_tarde.split(":")[1]));
            fit.set(Calendar.SECOND, Integer.valueOf(horainicial_tarde.split(":")[2]));
            
            fft.set(Calendar.YEAR, ahora.getYear() + 1900);
            fft.set(Calendar.MONTH, ahora.getMonth());
            fft.set(Calendar.DATE, ahora.getDate());
            fft.set(Calendar.HOUR_OF_DAY, Integer.valueOf(horafinal_tarde.split(":")[0]));
            fft.set(Calendar.MINUTE, Integer.valueOf(horafinal_tarde.split(":")[1]));
            fft.set(Calendar.SECOND, Integer.valueOf(horafinal_tarde.split(":")[2]));

            Boolean targetInMorning = ( ahora.after(fim.getTime()) && ahora.before(ffm.getTime()) );
            Boolean targetInAfternoon = ( ahora.after(fit.getTime()) && ahora.before(fft.getTime()) );
            
           
            Boolean targetInDay = ahora.compareTo(fechainicial) >= 0 && ahora.compareTo(fechafinal) <= 0;
            
            if ( targetInDay  && (targetInMorning || targetInAfternoon)) {
                // Buscar Si hay Lead (Contacto) para Marcar Lista 1
                Boolean hayEnL1 = false;
                Boolean hayEnL2 = false;
                Boolean hayEnL3 = false;
                params.clear();
                params.add(camp_nombre);
                params.add(1);
                params.add("PENDIENTE");
                ResultSet rsl = database.selectSql("SELECT * FROM " + SQLiteDatabase.listsTable + " WHERE list_service = ? and list_group = ? and list_status = ?;", params);

                while (rsl.next()) {
                    hayEnL1 = true;
                    // Validar agente al que se debe conectar la llamada
                    params.clear();
                    params.add(rsl.getString("list_agent"));
                    ResultSet rsa = database.selectSql("SELECT * FROM " + SQLiteDatabase.agentTable + " WHERE agen_id = ?;", params);
                    while (rsa.next()) {
                        if (rsa.getString("agen_exten_estado").toUpperCase().equals("IDLE") && rsa.getString("agen_estado").toUpperCase().equals("LISTA")) {
                            Map callParams = new HashMap();
                            callParams.put("strServiceId", rsl.getString("list_service"));
                            callParams.put("strLeadID", rsl.getString("list_leadid"));
                            callParams.put("strList", rsl.getString("list_group"));
                            callParams.put("strAgent", rsl.getString("list_agent"));
                            callParams.put("list_uid", rsl.getString("uid"));
                            List<Object> aparams = new ArrayList<>();
                            aparams.add("OCUPADO");
                            aparams.add(rsa.getInt("uid"));
                            database.updateSql("UPDATE " + SQLiteDatabase.agentTable + " SET agen_estado = ? WHERE uid = ?", aparams);
                            logger.info("Realizando Marcación de Campanha:" + camp_nombre + ", numero: " + rsl.getString("list_phonenumber") + ", leadId: " + rsl.getString("list_leadid"));
                            OriginateAction action = new OriginateAction();
                            action.setChannel("Local/" + rsl.getString("list_phonenumber") + "@from-interno");
                            action.setContext(this.context);
                            action.setPriority(1);
                            action.setExten(rsa.getString("agen_exten"));
                            action.setCallerId(rsl.getString("list_agent"));
                            action.setAccount(rsl.getString("uid"));
                            action.setActionId(rsa.getString("uid"));
                            action.setVariables(callParams);
                            action.setAsync(true);
                            ManagerConnection connection = ConnectionManager.getConnection();
                            try {
                                connection.login();
                                ManagerResponse ex = connection.sendAction(action, 10000L);
                                connection.logoff();
                                return ex.getResponse();
                            } catch (TimeoutException | IOException | AuthenticationFailedException var5) {
                                logger.fatal("Error on send request to asterisk", var5);
                                connection.logoff();
                                return "Error: " + var5.getMessage();
                            }
                        }
                    }
                }

                if (!hayEnL1) {
                    params.clear();
                    params.add(camp_nombre);
                    params.add(2);
                    params.add("PENDIENTE");
                    rsl = database.selectSql("SELECT * FROM " + SQLiteDatabase.listsTable + " WHERE list_service = ? and list_group = ? and list_status = ?;", params);

                    while (rsl.next()) {
                        hayEnL2 = true;
                        // Validar agente al que se debe conectar la llamada
                        params.clear();
                        params.add(rsl.getString("list_agent"));
                        ResultSet rsa = database.selectSql("SELECT * FROM " + SQLiteDatabase.agentTable + " WHERE agen_id = ?;", params);
                        while (rsa.next()) {
                            if (rsa.getString("agen_exten_estado").toUpperCase().equals("IDLE") && rsa.getString("agen_estado").toUpperCase().equals("LISTA")) {
                                Map callParams = new HashMap();
                                callParams.put("strServiceId", rsl.getString("list_service"));
                                callParams.put("strLeadID", rsl.getString("list_leadid"));
                                callParams.put("strList", rsl.getString("list_group"));
                                callParams.put("strAgent", rsl.getString("list_agent"));
                                callParams.put("list_uid", rsl.getString("uid"));
                                List<Object> aparams = new ArrayList<>();
                                aparams.add("OCUPADO");
                                aparams.add(rsa.getInt("uid"));
                                database.updateSql("UPDATE " + SQLiteDatabase.agentTable + " SET agen_estado = ? WHERE uid = ?", aparams);
                                logger.info("Realizando Marcación de Campanha:" + camp_nombre + ", numero: " + rsl.getString("list_phonenumber") + ", leadId: " + rsl.getString("list_leadid"));
                                OriginateAction action = new OriginateAction();
                                action.setChannel("Local/" + rsl.getString("list_phonenumber") + "@from-interno");
                                action.setContext(this.context);
                                action.setPriority(1);
                                action.setExten(rsa.getString("agen_exten"));
                                action.setCallerId(rsl.getString("list_agent"));
                                action.setAccount(rsl.getString("uid"));
                                action.setActionId(rsa.getString("uid"));
                                action.setVariables(callParams);
                                action.setAsync(true);
                                ManagerConnection connection = ConnectionManager.getConnection();
                                try {
                                    connection.login();
                                    ManagerResponse ex = connection.sendAction(action, 10000L);
                                    connection.logoff();
                                    return ex.getResponse();
                                } catch (TimeoutException | IOException | AuthenticationFailedException var5) {
                                    logger.fatal("Error on send request to asterisk", var5);
                                    connection.logoff();
                                    return "Error: " + var5.getMessage();
                                }
                            }
                        }
                    }

                }
                if (!hayEnL2) {
                    params.clear();
                    params.add(camp_nombre);
                    params.add(3);
                    params.add("PENDIENTE");
                    rsl = database.selectSql("SELECT * FROM " + SQLiteDatabase.listsTable + " WHERE list_service = ? and list_group = ? and list_status = ?;", params);

                    while (rsl.next()) {
                        hayEnL3 = true;
                        // Validar agente al que se debe conectar la llamada
                        params.clear();
                        params.add(rsl.getString("list_agent"));
                        ResultSet rsa = database.selectSql("SELECT * FROM " + SQLiteDatabase.agentTable + " WHERE agen_id = ?;", params);
                        while (rsa.next()) {
                            if (rsa.getString("agen_exten_estado").toUpperCase().equals("IDLE")) {
                                Map callParams = new HashMap();
                                callParams.put("strServiceId", rsl.getString("list_service"));
                                callParams.put("strLeadID", rsl.getString("list_leadid"));
                                callParams.put("strList", rsl.getString("list_group"));
                                callParams.put("strAgent", rsl.getString("list_agent"));
                                callParams.put("list_uid", rsl.getString("uid"));
                                logger.info("Realizando Marcación de Campanha:" + camp_nombre + ", numero: " + rsl.getString("list_phonenumber") + ", leadId: " + rsl.getString("list_leadid"));
                                OriginateAction action = new OriginateAction();
                                // action.setChannel(ChannelProtocolResolver.getOriginateChannelProtocol(this.queue) + "/" + this.queue);
                                // action.setContext(this.context);
                                // action.setExten(rsl.getString("list_phonenumber"));
                                action.setChannel("Local/" + rsl.getString("list_phonenumber") + "@from-interno");
                                action.setContext(this.context);
                                action.setPriority(1);
                                action.setExten(rs.getString("camp_queue"));
                                action.setCallerId(rsl.getString("list_agent"));
                                action.setAccount(rsl.getString("uid"));
                                action.setVariables(callParams);
                                action.setAsync(true);
                                ManagerConnection connection = ConnectionManager.getConnection();
                                try {
                                    connection.login();
                                    ManagerResponse ex = connection.sendAction(action, 10000L);
                                    connection.logoff();
                                    return ex.getResponse();
                                } catch (TimeoutException | IOException | AuthenticationFailedException var5) {
                                    logger.fatal("Error on send request to asterisk", var5);
                                    connection.logoff();
                                    return "Error: " + var5.getMessage();
                                }
                            }
                        }
                    }

                }                
            } else {
                System.out.println("Campanha No Esta En Horario: " + rs.getString("camp_nombre"));
            }
        }
        return result;
    }

}
