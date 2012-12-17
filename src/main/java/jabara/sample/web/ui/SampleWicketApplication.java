/**
 * 
 */
package jabara.sample.web.ui;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

/**
 * 
 * @author jabaraster
 */
public class SampleWicketApplication extends WebApplication {

    private static final String ENC = "UTF-8"; //$NON-NLS-1$

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<? extends Page> getHomePage() {
        return IndexPage.class;
    }

    /**
     * @see org.apache.wicket.protocol.http.WebApplication#init()
     */
    @Override
    protected void init() {
        super.init();

        getMarkupSettings().setDefaultMarkupEncoding(ENC);
        getRequestCycleSettings().setResponseRequestEncoding(getMarkupSettings().getDefaultMarkupEncoding());
    }
}
