package jabara.sample.web;

import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;

import org.eclipse.jetty.nosql.memcached.spymemcached.BinarySpyMemcachedClient;

/**
 * @author jabaraster
 */
public class HerokuMemcachierClient extends BinarySpyMemcachedClient {

    private final String _username;
    private final String _password;

    /**
     * 
     */
    public HerokuMemcachierClient() {
        this("127.0.0.1:11211"); //$NON-NLS-1$
    }

    /**
     * @param _ -
     */
    public HerokuMemcachierClient(final String _) {
        super(_);

        final String servers = System.getenv("MEMCACHIER_SERVERS"); //$NON-NLS-1$
        final String username = System.getenv("MEMCACHIER_USERNAME"); //$NON-NLS-1$
        final String password = System.getenv("MEMCACHIER_PASSWORD"); //$NON-NLS-1$
        if (servers == null || username == null || password == null) {
            throw new RuntimeException("not enough environment variables set for heroku environment"); //$NON-NLS-1$
        }

        final StringBuilder sb = new StringBuilder();
        for (final String s : servers.split("\\s+")) { //$NON-NLS-1$
            final int finalColon = s.lastIndexOf(":"); //$NON-NLS-1$
            final String server = 0 < finalColon ? s : s + ":" + 11211; //$NON-NLS-1$
            sb.append(server + " "); //$NON-NLS-1$
        }
        // setServerString(sb.toString().trim());

        setServerString(servers);

        this._username = username;
        this._password = password;
    }

    /**
     * @see org.eclipse.jetty.nosql.memcached.spymemcached.BinarySpyMemcachedClient#getConnectionFactoryBuilder()
     */
    @Override
    protected ConnectionFactoryBuilder getConnectionFactoryBuilder() {
        final ConnectionFactoryBuilder factoryBuilder = super.getConnectionFactoryBuilder();
        factoryBuilder.setProtocol(ConnectionFactoryBuilder.Protocol.BINARY);

        final AuthDescriptor ad = new AuthDescriptor(new String[] { "PLAIN" }, new PlainCallbackHandler(this._username, this._password)); //$NON-NLS-1$
        factoryBuilder.setAuthDescriptor(ad);

        return factoryBuilder;
    }
}