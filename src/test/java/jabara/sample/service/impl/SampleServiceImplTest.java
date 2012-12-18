/**
 * 
 */
package jabara.sample.service.impl;

import jabara.sample.entity.ESample;
import jabara.sample.service.SampleService;
import jabara.sample.service.DI;

import org.junit.Test;

/**
 * 
 * @author jabaraster
 */
public class SampleServiceImplTest {

    /**
     * Test method for {@link jabara.sample.service.impl.SampleServiceImpl#getAll()}.
     */
    @SuppressWarnings("static-method")
    @Test
    public void _getAll() {
        for (final ESample sample : DI.getObject(SampleService.class).getAll()) {
            System.out.println(sample);
        }
    }
}
