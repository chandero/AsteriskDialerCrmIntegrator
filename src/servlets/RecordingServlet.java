// Source File Name:   RecordingServlet.java

package servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import utils.PropertiesReader;
import utils.RecordingAction;

@WebServlet(name="Recording", urlPatterns={"/recording"})
public class RecordingServlet
  extends HttpServlet
{
  public RecordingServlet() {}
  
  private static final Logger logger = Logger.getLogger(MakeCallServlet.class);
  
  protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String id = request.getParameter("id");
    String secretKey = request.getParameter("secret");
    RecordingAction record = new RecordingAction(id);
    ServletContext context = getServletContext();
    int readedBytes = 0;
    
    if ((PropertiesReader.isNeedCheckKey()) && (!PropertiesReader.getProperty("CrmSecretKey").equals(secretKey))) {
      response.setStatus(400);
      response.setContentType("text/html;charset=UTF-8");
      logger.warn("Request to get recording with invalid secret key " + secretKey);
  
      return;
    }
    try
    {
      try {
        File recordFile = record.getFile();
        

        String mime = context.getMimeType(recordFile.getAbsolutePath());
        response.setContentType(mime != null ? mime : "application/octet-stream");
        response.setContentLength((int)recordFile.length());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + recordFile.getName() + "\"");
        

        byte[] buffer = new byte['Ѐ'];
        FileInputStream inputStream = new FileInputStream(recordFile);
        ServletOutputStream outputStream = response.getOutputStream();
        while ((readedBytes = inputStream.read(buffer)) != -1) {
          outputStream.write(buffer, 0, readedBytes);
        }
        outputStream.flush();
      } catch (FileNotFoundException ex) {
        response.reset();
        response.setStatus(404);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();

        try { 
            writer.println("File not found or no permissions to read file. Report to your system administrator");
        }
        catch (Throwable localThrowable1)
        {
          throw localThrowable1;
        } finally {
          if (writer != null) 
              if (readedBytes != 0) 
                  try { 
                      writer.close(); 
                  } catch (Throwable localThrowable2) {
                      localThrowable2.addSuppressed(localThrowable2); 
                  } else 
                  writer.close(); 
        }
        logger.warn("Error on get file:", ex);
      }
    } catch (IOException ex) {
      logger.warn("Error on transmiss response to crm", ex);
    }
  }
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    processRequest(request, response);
  }
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    processRequest(request, response);
  }
}
