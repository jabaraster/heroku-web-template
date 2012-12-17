/**
 * 
 */
package jabara.sample.web;

import jabara.sample.web.rest.SampleRestApplication;
import jabara.sample.web.ui.SampleWicketApplication;
import jabara.servlet.RequestDumpFilter;
import jabara.servlet.UTF8EncodingFilter;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

import org.apache.wicket.protocol.http.IWebApplicationFactory;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WicketFilter;

import com.sun.jersey.spi.container.servlet.ServletContainer;

/**
 * @author jabaraster
 */
@WebListener
public class SampleWebInitializer implements ServletContextListener {

    private static final String PATH_WICKET = "/ui/*";  //$NON-NLS-1$
    private static final String PATH_REST   = "/rest/*"; //$NON-NLS-1$

    /**
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextDestroyed(@SuppressWarnings("unused") final ServletContextEvent pSce) {
        // 処理なし
    }

    /**
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextInitialized(final ServletContextEvent pSce) {
        final ServletContext servletContext = pSce.getServletContext();

        initializeJersey(servletContext);
        initializeWicket(servletContext);

        // Filterは後に登録したものがより早く適用される.
        // このためWicketFilterが処理するリクエストにもDumpFilterを適用するには
        // WicketFilterより後にDumpFilterを登録するようにする.
        initializeDumpFilter(servletContext);

        initializeEncodingFilter(servletContext);
    }

    private static FilterRegistration.Dynamic addFiter(final ServletContext pServletContext, final Class<? extends Filter> pFilterType) {
        return pServletContext.addFilter(pFilterType.getName(), pFilterType);
    }

    private static ServletRegistration.Dynamic addServlet(final ServletContext pContext, final Class<? extends Servlet> pServletType) {
        return pContext.addServlet(pServletType.getName(), pServletType);
    }

    private static void initializeDumpFilter(final ServletContext pServletContext) {
        addFiter(pServletContext, RequestDumpFilter.class).addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*"); //$NON-NLS-1$
    }

    private static void initializeEncodingFilter(final ServletContext pServletContext) {
        addFiter(pServletContext, UTF8EncodingFilter.class).addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*"); //$NON-NLS-1$
    }

    private static void initializeJersey(final ServletContext pServletContext) {
        final ServletRegistration.Dynamic jerseyServlet = addServlet(pServletContext, ServletContainer.class);
        jerseyServlet.setInitParameter(ServletContainer.APPLICATION_CONFIG_CLASS, SampleRestApplication.class.getName());
        jerseyServlet.addMapping(PATH_REST);
    }

    private static void initializeWicket(final ServletContext pServletContext) {
        final FilterRegistration.Dynamic filter = addFiter(pServletContext, WicketFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, PATH_WICKET);
        filter.setInitParameter(WicketFilter.FILTER_MAPPING_PARAM, PATH_WICKET);
        filter.setInitParameter(WicketFilter.APP_FACT_PARAM, F.class.getName());
    }

    /**
     * 本来はprivateでいいのだが、そうするとWicketがエラーを投げるのでやむなくpublicにしています.
     * 
     * @author jabaraster
     */
    public static final class F implements IWebApplicationFactory {

        /**
         * @see org.apache.wicket.protocol.http.IWebApplicationFactory#createApplication(org.apache.wicket.protocol.http.WicketFilter)
         */
        @Override
        public WebApplication createApplication(@SuppressWarnings("unused") final WicketFilter pFilter) {
            return new SampleWicketApplication();
        }

        /**
         * @see org.apache.wicket.protocol.http.IWebApplicationFactory#destroy(org.apache.wicket.protocol.http.WicketFilter)
         */
        @Override
        public void destroy(@SuppressWarnings("unused") final WicketFilter pFilter) {
            // 処理なし
        }
    }
}
