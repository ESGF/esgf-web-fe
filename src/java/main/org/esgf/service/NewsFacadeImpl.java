package org.esgf.service;

import org.esgf.dao.NewsEntityDao;
import org.esgf.domain.NewsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "newsFacade")
@Transactional
public class NewsFacadeImpl implements NewsFacade {

    private NewsEntityDao newsEntityDao;

    @Transactional(readOnly = true)
    public NewsEntity getNewsEntity(Long id) throws DataAccessException {
        return newsEntityDao.get(id);
    }

    @Transactional(rollbackFor = DataAccessException.class, readOnly = false, timeout = 30, propagation = Propagation.SUPPORTS, isolation = Isolation.DEFAULT)
    public void saveNewsEntity(NewsEntity news) throws DataAccessException {
        newsEntityDao.save(news);

    }

    @Autowired
    public void setNewsEntityDao(NewsEntityDao newsEntityDao) {
        this.newsEntityDao = newsEntityDao;
    }

    public NewsEntityDao getNewsEntityDao() {
        return newsEntityDao;
    }

}
