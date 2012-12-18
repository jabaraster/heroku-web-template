/**
 * 
 */
package jabara.sample.service.impl;

import jabara.jpa_guice.DaoBase;
import jabara.sample.entity.ESample;
import jabara.sample.service.SampleService;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

/**
 * @author jabaraster
 */
public class SampleServiceImpl extends DaoBase implements SampleService {
    private static final long serialVersionUID = 2363543705605502284L;

    /**
     * @see jabara.sample.service.SampleService#getAll()
     */
    @Override
    public List<ESample> getAll() {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<ESample> query = builder.createQuery(ESample.class);
        query.from(ESample.class);
        return em.createQuery(query).getResultList();
    }

}
