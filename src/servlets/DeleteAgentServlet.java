/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import utils.DeleteAgentAction;

@WebServlet(name="DeleteAgentServlet", urlPatterns={"/delagent"})
public class DeleteAgentServlet extends HttpServlet {

    public DeleteAgentServlet() {
    }
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        try
        {
            DeleteAgentAction action = new DeleteAgentAction(request);
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

    private static final Logger logger = Logger.getLogger(DeleteAgentServlet.class);    
}
