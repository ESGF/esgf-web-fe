package org.esgf.dao;

import org.apache.log4j.Logger;
import org.esgf.domain.NewsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author fwang2
 *
 */
@Repository("newsEntityDao")
public class NewsEntityDaoJPA extends GenericDaoJPA<NewsEntity> implements
        NewsEntityDao {
    private JpaTemplate jpaTemplate;

    private final static Logger LOG = Logger.getLogger(NewsEntityDaoJPA.class);

    public NewsEntityDaoJPA() {
        super(NewsEntity.class);
    }

    @Autowired
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        this.jpaTemplate = jpaTemplate;
    }

    @Transactional
    public void save(NewsEntity news) throws DataAccessException {
        jpaTemplate.merge(news);
    }
}
