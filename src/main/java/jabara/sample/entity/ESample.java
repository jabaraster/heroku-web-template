/**
 * 
 */
package jabara.sample.entity;

import jabara.jpa.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author jabaraster
 */
@Entity
public class ESample extends EntityBase<ESample> {
    private static final long serialVersionUID    = 5059894538997252116L;

    /**
     * 
     */
    public static final int   MAX_CHAR_COUNT_NAME = 200;

    /**
     * 
     */
    @Column(nullable = false)
    protected long            code;

    /**
     * 
     */
    @Column(nullable = false, length = MAX_CHAR_COUNT_NAME * 3)
    protected String          name;

    /**
     * @return the code
     */
    public long getCode() {
        return this.code;
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param pCode the code to set
     */
    public void setCode(final long pCode) {
        this.code = pCode;
    }

    /**
     * @param pName the name to set
     */
    public void setName(final String pName) {
        this.name = pName;
    }
}
