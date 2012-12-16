import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.TagLibConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;

/**
 * @author jabaraster
 */
public class SampleWebStarter {

    private static final int DEFAULT_PORT = 8081;

    /**
     * @param pArgs -
     * @throws Exception -
     */
    public static void main(final String[] pArgs) throws Exception {
        final int port = getWebPort();
        final String webappDirLocation = "src/main/webapp/"; //$NON-NLS-1$

        final Server server = new Server(port);

        // final MemcachedSessionIdManager memcachedSessionIdManager = new MemcachedSessionIdManager(server);
        //        memcachedSessionIdManager.setServerString("localhost:11211"); //$NON-NLS-1$
        //        memcachedSessionIdManager.setKeyPrefix("session:"); //$NON-NLS-1$
        // server.setSessionIdManager(memcachedSessionIdManager);

        final WebAppContext context = new WebAppContext();
        context.setConfigurations(new Configuration[] { //
        new AnnotationConfiguration() //
                , new WebXmlConfiguration() //
                , new WebInfConfiguration() //
                , new TagLibConfiguration() //
                , new PlusConfiguration() //
                , new MetaInfConfiguration() //
                , new FragmentConfiguration() //
                , new EnvConfiguration() //
        });
        context.setContextPath("/"); //$NON-NLS-1$
        context.setDescriptor(webappDirLocation + "/WEB-INF/web.xml"); //$NON-NLS-1$
        context.setResourceBase(webappDirLocation);
        context.setParentLoaderPriority(true);

        server.setHandler(context);
        server.start();
        server.join();
    }

    private static int getWebPort() {
        final String webPort = System.getenv("PORT"); //$NON-NLS-1$
        if (webPort == null || webPort.isEmpty()) {
            return DEFAULT_PORT;
        }
        return Integer.parseInt(webPort);
    }
}