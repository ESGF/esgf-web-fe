package org.esgf.service;

import java.util.List;

import org.esgf.dao.NewsEntityDao;
import org.esgf.domain.NewsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "newsService")
@Transactional
public class NewsServiceImpl implements NewsService {

    private NewsEntityDao newsEntityDao;

    @Transactional(readOnly = true)
    public NewsEntity getNewsEntity(Long id) throws DataAccessException {
        return newsEntityDao.get(id);
    }

    @Transactional(readOnly = true)
    public List<NewsEntity> getNewsEntityAll() throws DataAccessException {
        return newsEntityDao.getAll();
    }

    @Transactional(rollbackFor = DataAccessException.class,
            readOnly = false, timeout = 30,
            propagation = Propagation.SUPPORTS,
            isolation = Isolation.DEFAULT)
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
