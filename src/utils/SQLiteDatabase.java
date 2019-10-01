// Source File Name:   SQLiteDatabase.java

package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLiteDatabase {
    public static String callsTable = "crm_calls_info";
    public static String listsTable = "crm_list_info";
    public static String campsTable = "crm_campaign_info";
    public static String agentTable = "crm_agent_info";
    public static String campagentTable ="crm_campaign_agent_info";
    public static String uniqueIdTable = "crm_unique_info";
    public static String versionTable = "version";
    private static SQLiteDatabase database = null;
    private static final String dbName = "CrmAsterisk";
    private Connection connection;

    public static synchronized SQLiteDatabase getInstance() {
        if (database == null) {
            database = new SQLiteDatabase();
        }
        return database;
    }
    
    public synchronized String insertSql(String sql, List<Object> params) throws SQLException {
        int count;
        Long uid;
        try (PreparedStatement stmt = this.prepareSql(sql, params);){
            count = stmt.executeUpdate();
       
            if (count == 0) {
                throw new SQLException("Creating call failed, no rows affected.");
        }   

        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                uid = generatedKeys.getLong(1);
            }
            else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }            
        }
        return uid.toString();
    }

    public synchronized Boolean updateSql(String sql, List<Object> params) throws SQLException {
        Boolean result;
        try (PreparedStatement stmt = this.prepareSql(sql, params);){
            result = stmt.executeUpdate() > 0;
        }
        return result;
    }

    public synchronized void updateSql(String sql) throws SQLException {
        this.updateSql(sql, new ArrayList<>());
    }

    public synchronized ResultSet selectSql(String sql, List<Object> params) throws SQLException {
        PreparedStatement stmt = this.prepareSql(sql, params);
        return stmt.executeQuery();
    }
    
    public synchronized ResultSet selectSql(String sql) throws SQLException {
        PreparedStatement stmt = this.connection.prepareStatement(sql);
        return stmt.executeQuery();
    }    
    
    public synchronized Boolean deleteSql(String sql, List<Object> params) throws SQLException {
        PreparedStatement stmt = this.prepareSql(sql, params);
        return stmt.executeUpdate() > 0;
    }
    
    public void close() throws SQLException {
        if (this.connection != null) {
            this.connection.close();
            this.connection = null;
        }
        database = null;
    }

    private SQLiteDatabase() {
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + PropertiesReader.getProperty((String)"AsteriskAppDBPath") + "/" + dbName + ".db");
        }
        catch (SQLException ex) {
            throw new RuntimeException("Unable connect to Database" + ex.getMessage());
        }
        if (!this.checkStructure()) {
            this.createStructure();
        }
        this.createVariablesFieldsStructure();
    }

    private boolean checkStructure() {
        try {
            int tablesCount;
            try (Statement stmt = this.connection.createStatement();
                 ResultSet result = stmt.executeQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='" + callsTable + "';");
            ){
                result.next();
                tablesCount = result.getInt(1);
            }
            try (
                 Statement stmt = this.connection.createStatement();
                 ResultSet result = stmt.executeQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='" + listsTable + "';");
            ){
                result.next();
                tablesCount += result.getInt(1);
            }
            try (
                 Statement stmt = this.connection.createStatement();
                 ResultSet result = stmt.executeQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='" + campsTable + "';");
            ){
                result.next();
                tablesCount += result.getInt(1);
            }             
            try (
                 Statement stmt = this.connection.createStatement();
                 ResultSet result = stmt.executeQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='" + agentTable + "';");
            ){
                result.next();
                tablesCount += result.getInt(1);
            } 
                        try (
                 Statement stmt = this.connection.createStatement();
                 ResultSet result = stmt.executeQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='" + campagentTable + "';");
            ){
                result.next();
                tablesCount += result.getInt(1);
            }
            try (
                 Statement stmt = this.connection.createStatement();
                 ResultSet result = stmt.executeQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='" + uniqueIdTable + "';");
            ){
                result.next();
                tablesCount += result.getInt(1);
            }                         
            
            return tablesCount == 6;
        }
        catch (SQLException ex) {
            throw new RuntimeException("Unable to check database structure" + ex.getMessage());
        }
    }

    private void createVariablesFieldsStructure() {
        for (String variableName : PropertiesReader.getAsteriskLookUpVariablesNames()) {
            if (this.isColumnExists(variableName)) continue;
            this.createDefaultColumn(variableName);
        }
    }

    private boolean isColumnExists(String columnName) {
        boolean columnExists;
        columnExists = true;
        try {
            try (Statement stmt = this.connection.createStatement();){
                stmt.executeQuery("SELECT " + columnName + " FROM " + callsTable + ";");
            }
        }
        catch (SQLException ex) {
            columnExists = false;
        }
        return columnExists;
    }

    private void createDefaultColumn(String columnName) {
        try {
            try (Statement stmt = this.connection.createStatement();){
                stmt.executeUpdate("ALTER TABLE " + callsTable + " ADD COLUMN " + columnName + " varchar(255) DEFAULT NULL;");
            }
        }
        catch (SQLException ex) {
            throw new RuntimeException("Unable to create column " + columnName, ex);
        }
    }

    private void createStructure() {
        try {
            try (Statement stmt = this.connection.createStatement();){
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + callsTable + " (uid INTEGER PRIMARY KEY AUTOINCREMENT,srcuid varchar(255) NOT NULL,destuid varchar(255) DEFAULT NULL,event varchar(50) DEFAULT NULL,direction varchar(50) DEFAULT NULL,channel varchar(255) DEFAULT NULL,from_number varchar(255) DEFAULT NULL,to_number varchar(255) DEFAULT NULL,starttime datetime DEFAULT NULL,endtime datetime DEFAULT NULL,totalduration int DEFAULT NULL,bridged varchar(20) DEFAULT NULL,callcause varchar(50) DEFAULT NULL,recordingpath varchar(255) DEFAULT NULL,recordingurl varchar(255) DEFAULT NULL, disposition varchar(255));");
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + listsTable + " (uid INTEGER PRIMARY KEY AUTOINCREMENT,list_service varchar(255), list_group varchar(255), list_phonenumber varchar(255), list_agent varchar(255), list_priority varchar(255), list_leadid varchar(255), list_calluid, list_status varchar(255));");
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + campsTable + " (uid INTEGER PRIMARY KEY AUTOINCREMENT,camp_nombre varchar(255) NOT NULL UNIQUE, camp_fechainicial date, camp_fechafinal date, camp_horainicial_manana datetime, camp_horafinal_manana datetime, camp_horainicial_tarde datetime, camp_horafinal_tarde datetime, camp_cola varchar(255), camp_estado varchar(255), camp_tiempofuera integer);");
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + agentTable + " (uid INTEGER PRIMARY KEY AUTOINCREMENT,agen_id varchar(255) NOT NULL UNIQUE, agen_nombre varchar(255), agen_exten varchar(255), agen_estado varchar(255), agen_exten_estado varchar(255), agen_laststartcall integer, agen_lastendcall integer);");
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + campagentTable + " (uid INTEGER PRIMARY KEY AUTOINCREMENT,camp_id integer, agen_id integer);");
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + uniqueIdTable + "(uid varchar(255), agen_uid varchar(255));");
            }
        }
        catch (SQLException ex) {
            throw new RuntimeException("Cannot create database stucture" + ex.getMessage());
        }
    }

    private PreparedStatement prepareSql(String sql, List<Object> params) throws SQLException {
        PreparedStatement stmt = this.connection.prepareStatement(sql);
        int paramIndex = 1;
        for (Object currentParam : params) {
            stmt.setObject(paramIndex, currentParam);
            ++paramIndex;
        }
        return stmt;
    }
    
}