package org.esgf.dao.jpa;

import org.apache.log4j.Logger;
import org.esgf.domain.NewsEntity;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author fwang2
 * 
 */
@Repository("newsEntityDao")
public class NewsEntityDaoJpa extends GenericDaoJpa<NewsEntity> {

    private final static Logger LOG = Logger.getLogger(NewsEntityDaoJpa.class);

    public NewsEntityDaoJpa() {
        super(NewsEntity.class);
    }

}
