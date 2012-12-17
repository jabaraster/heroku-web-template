package jabara.sample.web;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class SampleServlet
 */
@WebServlet("/sample")
public class SampleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(final HttpServletRequest pRequest, final HttpServletResponse pResponse) throws IOException {

        final HttpSession session = pRequest.getSession();
        final String KEY_COUNTER = "counter"; //$NON-NLS-1$

        AtomicLong counter = (AtomicLong) session.getAttribute(KEY_COUNTER);
        if (counter == null) {
            counter = new AtomicLong(1);
            session.setAttribute(KEY_COUNTER, counter);
        } else {
            counter.incrementAndGet();
            session.setAttribute(KEY_COUNTER, counter);
        }

        pResponse.setContentType("text/plain"); //$NON-NLS-1$
        pResponse.getWriter().append("Counter value ---> " + counter.get()); //$NON-NLS-1$
    }
}
