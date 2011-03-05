package org.esgf.dao;

import org.apache.log4j.Logger;
import org.esgf.domain.NewsEntity;
import org.springframework.stereotype.Repository;

/**
 *
 * @author fwang2
 *
 */
@Repository("newsEntityDao")
public class NewsEntityDaoJPA extends GenericDaoJPA<NewsEntity> implements
        NewsEntityDao {

    private final static Logger LOG = Logger.getLogger(NewsEntityDaoJPA.class);

    public NewsEntityDaoJPA() {
        super(NewsEntity.class);
    }


}
