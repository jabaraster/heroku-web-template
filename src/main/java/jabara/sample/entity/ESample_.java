/**
 * 
 */
package jabara.sample.entity;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * JPAエンティティを型に安全に問い合わせするための「メタモデル」と呼ばれるクラス. <br>
 * このクラスは手動で生成したが、eclipseの設定を変えれば自動生成も可能. <br>
 * 
 * @author jabaraster
 */
@StaticMetamodel(ESample.class)
public class ESample_ {
    public static volatile SingularAttribute<ESample, Long>   code;
    public static volatile SingularAttribute<ESample, String> name;
}
