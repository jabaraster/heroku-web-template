package jabara.sample.web;

import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;

import org.eclipse.jetty.nosql.memcached.spymemcached.BinarySpyMemcachedClient;

/**
 * @author jabaraster
 */
public class MemcachedClient extends BinarySpyMemcachedClient {

    private final String  username;
    private final String  password;
    private final boolean needAuth;

    /**
     * @param pServerString -
     */
    public MemcachedClient(final String pServerString) {
        super(pServerString);
        this.needAuth = false;
        this.username = null;
        this.password = null;
    }

    /**
     * @param pServerString -
     * @param pUsername -
     * @param pPassword -
     */
    public MemcachedClient(final String pServerString, final String pUsername, final String pPassword) {
        super(pServerString);
        this.needAuth = true;
        this.username = pUsername;
        this.password = pPassword;
    }

    /**
     * @see org.eclipse.jetty.nosql.memcached.spymemcached.BinarySpyMemcachedClient#getConnectionFactoryBuilder()
     */
    @Override
    protected ConnectionFactoryBuilder getConnectionFactoryBuilder() {
        final ConnectionFactoryBuilder factoryBuilder = super.getConnectionFactoryBuilder();
        factoryBuilder.setProtocol(ConnectionFactoryBuilder.Protocol.BINARY);

        if (this.needAuth) {
            final AuthDescriptor ad = new AuthDescriptor(new String[] { "PLAIN" }, new PlainCallbackHandler(this.username, this.password)); //$NON-NLS-1$
            factoryBuilder.setAuthDescriptor(ad);
        }

        return factoryBuilder;
    }
}