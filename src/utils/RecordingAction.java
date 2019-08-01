// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RecordingAction.java

package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

// Referenced classes of package utils:
//            SQLiteDatabase

public class RecordingAction
{

    public RecordingAction(String id)
    {
        database = SQLiteDatabase.getInstance();
        this.id = id;
    }

    public File getFile()
        throws FileNotFoundException
    {
        String recordPath = getRecordPath(id);
        if(recordPath == null)
            throw new FileNotFoundException();
        File file = new File(recordPath);
        if(!file.exists())
            throw new FileNotFoundException();
        else
            return file;
    }

    private String getRecordPath(String id)
    {
        List selectParams = new ArrayList();
        selectParams.add(id);
        try
        {
            ResultSet result = database.selectSql((new StringBuilder()).append("SELECT recordingpath FROM ").append(SQLiteDatabase.callsTable).append(" WHERE uid = ? ;").toString(), selectParams);
            String path = result.getString(1);
            result.getStatement().close();
            return path;
        }
        catch(SQLException ex)
        {
            logger.fatal("Error on select recording path", ex);
        }
        return null;
    }

    private static final Logger logger = Logger.getLogger(RecordingAction.class);
    private final String id;
    protected SQLiteDatabase database;

}
