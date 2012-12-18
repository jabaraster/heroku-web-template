/**
 * 
 */
package jabara.sample.service;

import jabara.general.ArgUtil;
import jabara.jpa.util.SystemPropertyToPostgreJpaPropertiesParser;
import jabara.jpa_guice.JpaModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * @author jabaraster
 */
public final class Supplier {

    /**
     * 
     */
    public static final String    PERSISTENCE_UNIT_NAME = "mainPersistenceUnit"; //$NON-NLS-1$

    private static final Injector _injector             = createInjector();

    private Supplier() {
        // 　処理なし
    }

    /**
     * 指定のオブジェクトを取得します. <br>
     * このメソッドを使っていいのは次のクラスだけです. <br>
     * <ul>
     * <li>JAX-RSのリソースクラス</li>
     * <li>WicketのPageクラス</li>
     * </ul>
     * 
     * @param pType 取得するオブジェクトの型.
     * @return オブジェクト.
     */
    public static <T> T getObject(final Class<T> pType) {
        ArgUtil.checkNull(pType, "pType"); //$NON-NLS-1$
        return _injector.getInstance(pType);
    }

    private static Injector createInjector() {
        final JpaModule jpaModule = new JpaModule(PERSISTENCE_UNIT_NAME, new SystemPropertyToPostgreJpaPropertiesParser());
        return Guice.createInjector(jpaModule);
    }
}
