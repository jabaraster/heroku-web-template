import jabara.sample.web.HerokuMemcachierClient;

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
    private static final Logger _logger                         = Logger.getLogger(SampleWebStarter.class);

    private static final int    DEFAULT_PORT                    = 8081;
    private static final String DEFAULT_MEMCACHED_SERVER_STRING = "localhost:11211";                       //$NON-NLS-1$

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
        final String webPort = System.getenv("PORT"); //$NON-NLS-1$
        if (webPort == null || webPort.isEmpty()) {
            return DEFAULT_PORT;
        }
        return Integer.parseInt(webPort);
    }

    private static void setupMemcachedSessionIdManager(final Server server) throws IOException {
        final MemcachedSessionIdManager memcachedSessionIdManager;
        final String string = System.getenv("MEMCACHIER_SERVERS"); //$NON-NLS-1$
        if (string == null || string.isEmpty()) {

            if (_logger.isTraceEnabled()) {
                _logger.trace("use local memcached."); //$NON-NLS-1$
            }

            memcachedSessionIdManager = new MemcachedSessionIdManager(server, DEFAULT_MEMCACHED_SERVER_STRING);

        } else {
            if (_logger.isTraceEnabled()) {
                _logger.trace("use memcachier on heroku."); //$NON-NLS-1$
            }

            memcachedSessionIdManager = new MemcachedSessionIdManager(server, null, new AbstractMemcachedClientFactory() {
                @Override
                public AbstractKeyValueStoreClient create(@SuppressWarnings("unused") final String pServerString) {
                    return new HerokuMemcachierClient();
                }
            });
        }
        memcachedSessionIdManager.setKeyPrefix("session:"); //$NON-NLS-1$
        server.setSessionIdManager(memcachedSessionIdManager);
    }
}