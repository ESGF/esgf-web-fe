package org.esgf.service;

import java.util.List;

import org.esgf.domain.NewsEntity;
import org.springframework.dao.DataAccessException;

public interface NewsService {

    public NewsEntity getNewsEntity(Long id) throws DataAccessException;

    public List<NewsEntity> getNewsEntityAll() throws DataAccessException;

    public void saveNewsEntity(NewsEntity news) throws DataAccessException;

    public void removeNewsEntity(Long id) throws DataAccessException;

}
