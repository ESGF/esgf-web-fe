package org.esgf.dao;

/**
 *
 * author: fwang2@ornl.gov
 *
 */

import java.util.List;

import org.esgf.domain.NewsEntity;
import org.springframework.dao.DataAccessException;

public interface NewsEntityDao extends GenericDao<NewsEntity> {

    public NewsEntity getNewsEntityByTitle(String title)
            throws DataAccessException;

    public List<NewsEntity> getAllNews() throws DataAccessException;

}
