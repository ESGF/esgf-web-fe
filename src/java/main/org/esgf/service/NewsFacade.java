package org.esgf.service;

import org.esgf.domain.NewsEntity;
import org.springframework.dao.DataAccessException;

public interface NewsFacade {

    public NewsEntity getNewsEntity(Long id) throws DataAccessException;

    public void saveNewsEntity(NewsEntity news) throws DataAccessException;

}
