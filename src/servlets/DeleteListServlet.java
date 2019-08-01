// Source file name: DeleteListServlet.java

package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import utils.DeleteCampaignAction;
import utils.DeleteListAction;


@WebServlet(name="DeleteListServlet", urlPatterns={"/dellist"})
public class DeleteListServlet extends HttpServlet {

    public DeleteListServlet() {
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        try
        {
            DeleteListAction action = new DeleteListAction(request);
            String responseStr = action.doActionFromRequest();
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

    private static final Logger logger = Logger.getLogger(DeleteCampaignServlet.class);    
}
