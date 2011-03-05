package org.esgf.service;

import java.util.List;

import org.esgf.dao.NewsEntityDao;
import org.esgf.domain.NewsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service(value = "newsService")
public class NewsServiceImpl implements NewsService {

    private NewsEntityDao newsEntityDao;

    public NewsEntity getNewsEntity(Long id) throws DataAccessException {
        return newsEntityDao.get(id);
    }

    public List<NewsEntity> getNewsEntityAll() throws DataAccessException {
        return newsEntityDao.getAll();
    }

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
