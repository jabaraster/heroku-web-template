/**
 * 
 */
package jabara.sample.web.rest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.wicket.util.io.IOUtils;
import org.junit.Test;

/**
 * 
 * @author jabaraster
 */
public class TimeResourceTest {

    /**
     * @throws Throwable -
     */
    @SuppressWarnings({ "static-method", "nls" })
    @Test
    public void _test() throws Throwable {
        // final TimeResource.Time pT = new TimeResource.Time(Calendar.getInstance().getTime().toString());
        // final StringBuilder sb = new StringBuilder();
        // JSON.encode(pT, sb, true);
        // System.out.println(new String(sb));

        final HttpURLConnection conn = (HttpURLConnection) new URL("http://localhost:8081/rest/time/now").openConnection();
        conn.addRequestProperty("accept", "application/json");

        final InputStream in = conn.getInputStream();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.copy(in, out);
        System.out.println(new String(out.toByteArray(), "UTF-8"));

    }
}
