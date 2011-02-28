package org.esgf.dao;

/**
 *
 * author: fwang2@ornl.gov
 *
 */

import java.util.List;

import org.esgf.domain.DomainObject;

public interface GenericDao<T extends DomainObject> {

    public T get(Long id);

    public List<T> getAll();

    public void save(T object);

    public void delete(T object);

    public void indexEntity(T object);

    public void indexAllItems();

}
