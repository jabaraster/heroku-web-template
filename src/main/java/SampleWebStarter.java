import jabara.sample.web.MemcachedClient;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.nosql.kvs.AbstractKeyValueStoreClient;
import org.eclipse.jetty.nosql.memcached.AbstractMemcachedClientFactory;
import org.eclipse.jetty.nosql.memcached.MemcachedSessionIdManager;
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
    private static final Logger _logger                = Logger.getLogger(SampleWebStarter.class);

    /**
     * 
     */
    public static final String  KEY_WEB_PORT           = "web.port";                              //$NON-NLS-1$
    /**
     * 
     */
    public static final String  KEY_MEMCACHED_SERVERS  = "memcached.servers";                     //$NON-NLS-1$
    /**
     * 
     */
    public static final String  KEY_MEMCACHED_USERNAME = "memcached.username";                    //$NON-NLS-1$
    /**
     * 
     */
    public static final String  KEY_MEMCACHED_PASSWORD = "memcached.password";                    //$NON-NLS-1$

    /**
     * @param pArgs -
     * @throws Exception -
     */
    public static void main(final String[] pArgs) throws Exception {
        final int port = getWebPort();
        final String webappDirLocation = "src/main/webapp/"; //$NON-NLS-1$

        final Server server = new Server(port);

        setupMemcachedSessionIdManager(server);

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
        return Integer.parseInt(System.getProperty(KEY_WEB_PORT));
    }

    private static void setupMemcachedSessionIdManager(final Server server) throws IOException {
        final String serverString = System.getProperty(KEY_MEMCACHED_SERVERS);
        final MemcachedSessionIdManager memcachedSessionIdManager = new MemcachedSessionIdManager(server, serverString,
                new AbstractMemcachedClientFactory() {
                    @Override
                    public AbstractKeyValueStoreClient create(final String pServerString) {
                        final String username = System.getProperty(KEY_MEMCACHED_USERNAME);
                        if (username == null) {
                            return new MemcachedClient(pServerString);
                        }
                        final String password = System.getProperty(KEY_MEMCACHED_PASSWORD);
                        return new MemcachedClient(pServerString, username, password);
                    }
                });
        memcachedSessionIdManager.setKeyPrefix("session:"); //$NON-NLS-1$
        server.setSessionIdManager(memcachedSessionIdManager);
    }
}