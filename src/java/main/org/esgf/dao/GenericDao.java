/**
 * Core Persistence Interface
 * 
 * @author: fwang2@ornl.gov
 *
 */

package org.esgf.dao;


import java.util.List;

import org.esgf.domain.DomainObject;

public interface GenericDao<T extends DomainObject> {

    public T get(Long id);

    public List<T> getAll();

    public void save(T object);

    public void delete(T object);

}
