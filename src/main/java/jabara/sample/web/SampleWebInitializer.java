/**
 * 
 */
package jabara.sample.web;

import jabara.sample.web.rest.SampleRestApplication;
import jabara.sample.web.ui.SampleWicketApplication;

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
        initializeDumpFilter(servletContext);
    }

    private static FilterRegistration.Dynamic addFiter(final ServletContext pServletContext, final Class<? extends Filter> pFilterType) {
        return pServletContext.addFilter(pFilterType.getName(), pFilterType);
    }

    private static ServletRegistration.Dynamic addServlet(final ServletContext pContext, final Class<? extends Servlet> pServletType) {
        return pContext.addServlet(pServletType.getName(), pServletType);
    }

    private static void initializeDumpFilter(final ServletContext pServletContext) {
        addFiter(pServletContext, DumpFilter.class).addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*"); //$NON-NLS-1$
    }

    private static void initializeJersey(final ServletContext pServletContext) {
        final ServletRegistration.Dynamic jerseyServlet = addServlet(pServletContext, ServletContainer.class);
        jerseyServlet.setInitParameter(ServletContainer.APPLICATION_CONFIG_CLASS, SampleRestApplication.class.getName());
        jerseyServlet.addMapping("/rest/*"); //$NON-NLS-1$
    }

    private static void initializeWicket(final ServletContext pServletContext) {
        final FilterRegistration.Dynamic filter = addFiter(pServletContext, WicketFilter.class);
        final String PATH = "/ui/*"; //$NON-NLS-1$
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, PATH);
        filter.setInitParameter(WicketFilter.FILTER_MAPPING_PARAM, PATH);
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
