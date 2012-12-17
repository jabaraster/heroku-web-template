/**
 * 
 */
package jabara.sample.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author jabaraster
 */
public class DumpFilter implements Filter {

    /**
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        //
    }

    /**
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(final ServletRequest pRequest, final ServletResponse pResponse, final FilterChain pChain) throws IOException,
            ServletException {
        final HttpServletRequest request = (HttpServletRequest) pRequest;

        System.out.println("-------------- " + request.getRequestURI()); //$NON-NLS-1$
        dumpCookie(request);
        dumpHeaders(request);
        dumpRequestParameter(request);

        pChain.doFilter(pRequest, pResponse);
    }

    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(@SuppressWarnings("unused") final FilterConfig pFilterConfig) {
        //
    }

    private static void dumpCookie(final HttpServletRequest pRequest) {
        System.out.println("  -- Cookies"); //$NON-NLS-1$
        final Cookie[] cookies = pRequest.getCookies();
        if (cookies != null) {
            for (final Cookie cookie : cookies) {
                System.out.println("    " + cookie.getName() + ": " + cookie.getValue()); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
    }

    private static void dumpHeaders(final HttpServletRequest pRequest) {
        System.out.println("  -- Request headers"); //$NON-NLS-1$
        for (final Enumeration<String> headerNames = pRequest.getHeaderNames(); headerNames.hasMoreElements();) {
            final String headerName = headerNames.nextElement();
            System.out.println("    " + headerName + ": " + toList(pRequest.getHeaders(headerName))); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    private static void dumpRequestParameter(final HttpServletRequest pRequest) {
        System.out.println("  -- Request Parameters"); //$NON-NLS-1$
        for (final Map.Entry<String, String[]> parameter : pRequest.getParameterMap().entrySet()) {
            System.out.println("    " + parameter.getKey() + ": " + Arrays.asList(parameter.getValue())); //$NON-NLS-1$//$NON-NLS-2$
        }
    }

    private static List<Object> toList(final Enumeration<?> pEnumeration) {
        final List<Object> sb = new ArrayList<Object>();
        while (pEnumeration.hasMoreElements()) {
            sb.add(pEnumeration.nextElement());
        }
        return sb;
    }

}
