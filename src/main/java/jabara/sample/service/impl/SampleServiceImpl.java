/**
 * 
 */
package jabara.sample.service.impl;

import jabara.jpa.entity.EntityBase_;
import jabara.jpa_guice.DaoBase;
import jabara.sample.entity.ESample;
import jabara.sample.service.SampleService;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * @author jabaraster
 */
public class SampleServiceImpl extends DaoBase implements SampleService {
    private static final long serialVersionUID = 2363543705605502284L;

    /**
     * @see jabara.sample.service.SampleService#countAll()
     */
    @Override
    public int countAll() {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<Long> query = builder.createQuery(Long.class);
        final Root<ESample> root = query.from(ESample.class);

        query.select(builder.count(root.get(EntityBase_.id)));

        return em.createQuery(query).getSingleResult().intValue();
    }

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

    /**
     * このメソッドは、クラスが{@link DaoBase}を継承していて、かつpublicなのでトランザクションが自動ではられます.
     * 
     * @see jabara.sample.service.SampleService#insert(long, java.lang.String)
     */
    @Override
    public ESample insert(final long pCode, final String pName) {
        final ESample newSample = new ESample();
        newSample.setCode(pCode);
        newSample.setName(pName);
        getEntityManager().persist(newSample);
        return newSample;
    }

}
