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
public class NewsEntityDaoJPA extends GenericDaoJPA<NewsEntity> {

    private final static Logger LOG = Logger.getLogger(NewsEntityDaoJPA.class);

    public NewsEntityDaoJPA() {
        super(NewsEntity.class);
    }

}
