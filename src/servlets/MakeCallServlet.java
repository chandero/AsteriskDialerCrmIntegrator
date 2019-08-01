// Source File Name:   MakeCallServlet.java

package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.log4j.Logger;
import utils.EnqueueCall;

public class MakeCallServlet extends HttpServlet
{

    public MakeCallServlet()
    {
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        try
        {
            EnqueueCall enqueue = new EnqueueCall(request);
            String responseStr = enqueue.doEnqueueCallFromRequest();
            writer.println(responseStr);
            writer.close();
        }
        catch(Exception ex)
        {
            logger.fatal("Error on send resposne to vtiger", ex);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        processRequest(request, response);
    }

    private static final Logger logger = Logger.getLogger(MakeCallServlet.class);

}
