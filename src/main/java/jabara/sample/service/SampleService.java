/**
 * 
 */
package jabara.sample.service;

import jabara.sample.entity.ESample;
import jabara.sample.service.impl.SampleServiceImpl;

import java.util.List;

import com.google.inject.ImplementedBy;

/**
 * @author jabaraster
 */
@ImplementedBy(SampleServiceImpl.class)
public interface SampleService {

    /**
     * @return 全ての永続化された{@link ESample}オブジェクト.
     */
    List<ESample> getAll();

    /**
     * @param pCode
     * @param pName
     * @return 永続化されたエンティティ.
     */
    ESample insert(long pCode, String pName);
}
