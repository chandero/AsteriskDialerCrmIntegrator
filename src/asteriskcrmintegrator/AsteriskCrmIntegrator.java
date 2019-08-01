// Source File Name:   AsteriskCrmIntegrator.java

package asteriskcrmintegrator;

import command.helpers.CommandsExecutor;
import org.apache.log4j.Logger;
import org.asteriskjava.manager.ManagerConnection;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import servlets.*;
import utils.*;

public class AsteriskCrmIntegrator
{

    public AsteriskCrmIntegrator()
    {
    }

    public static void main(String args[])
    {
        try
        {
            ManagerConnection asteriskConnection = ConnectionManager.getConnection();
            asteriskConnection.addEventListener(new CommandsExecutor());
            WebAppContext context = new WebAppContext();
            context.setWar(".");
            context.addServlet(new ServletHolder(new RecordingServlet()), "/recording");
            context.addServlet(new ServletHolder(new MakeCallServlet()), "/makecall");
            context.addServlet(new ServletHolder(new ListCallInfoServlet()), "/lstcallinfo");
            context.addServlet(new ServletHolder(new DeleteListServlet()), "/dellist");
            context.addServlet(new ServletHolder(new CdrServlet()), "/cdrinfo");
            context.addServlet(new ServletHolder(new AddCampaignServlet()), "/addcampaign");
            context.addServlet(new ServletHolder(new DeleteCampaignServlet()), "/delcampaign");
            context.addServlet(new ServletHolder(new UpdateCampaignServlet()), "/updcampaign");
            context.addServlet(new ServletHolder(new ListCampaignServlet()), "/lstcampaign");
            context.addServlet(new ServletHolder(new ActivateCampaignServlet()), "/oncampaign");
            context.addServlet(new ServletHolder(new DeactivateCampaignServlet()), "/offcampaign");
            context.addServlet(new ServletHolder(new AddAgentServlet()), "/addagent");
            context.addServlet(new ServletHolder(new DeleteAgentServlet()), "/delagent");
            context.addServlet(new ServletHolder(new UpdateAgentServlet()), "/updagent");
            context.addServlet(new ServletHolder(new ListAgentServlet()), "/lstagent");
            context.addServlet(new ServletHolder(new AddAgentCampaignServlet()), "/addagentcampaign");
            context.addServlet(new ServletHolder(new DeleteAgentCampaignServlet()), "/delagentcampaign");
            context.addServlet(new ServletHolder(new ListAgentCampaignServlet()), "/lstagentcampaign");  
            context.addServlet(new ServletHolder(new UniqueIdServlet()), "/lstunique");
            
            context.addServlet(new ServletHolder(new RootServlet()), "");
            HandlerList handlers = new HandlerList();
            handlers.setHandlers(new Handler[] {
                context, new DefaultHandler()
            });
            // Migration.applyMigrations();
            (new Thread(new ConnectionTask(asteriskConnection))).start();
            (new Thread(new DialerTask())).start();
            Server server = new Server();
            SelectChannelConnector connector = new SelectChannelConnector();
            connector.setPort(Integer.parseInt(PropertiesReader.getProperty("ServerPort")));
            server.addConnector(connector);
            server.setHandler(handlers);
            server.start();
            server.join();
        }
        catch(Exception ex)
        {
            logger.fatal((new StringBuilder()).append("Critical error on starting connector: ").append(ex.getMessage()).toString(), ex);
            throw new RuntimeException("Critical error on starting connector. Stop running.");
        }
    }

    private static final Logger logger = Logger.getLogger(AsteriskCrmIntegrator.class);
    public static int ASTERISK_VERSION = 0;

}
