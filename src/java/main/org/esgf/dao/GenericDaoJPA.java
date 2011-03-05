/** 
 * 
 * Core Persistence Implementation (Pure JPA)
 *
 * For JpaTemplate (which is a Hibernate API) approach
 * 
 *      We can use
 *          private JpaTemplate jpaTemplate;
 *      
 *      and inject a bean as the following
 *   
 *   @Autowired
 *   public void setJpaTemplate(JpaTemplate jpaTemplate) {
 *       this.jpaTemplate = jpaTemplate;
 *   }
 *
 *   @Transactional(rollbackFor = DataAccessException.class,
 *           readOnly = false, timeout = 30,
 *           propagation = Propagation.REQUIRED,
 *           isolation = Isolation.DEFAULT)
 *   public void save(T object) throws DataAccessException {
 *       jpaTemplate.merge(object);
 *   }
 *   
 *
 * author: fwang2@ornl.gov
 * 
 * 
 */
package org.esgf.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.esgf.domain.DomainObject;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("unchecked")
@Transactional
public class GenericDaoJPA<T extends DomainObject> implements GenericDao<T> {

    private Class<T> type;
    private EntityManager entityManager;

    public GenericDaoJPA(Class<T> type) {
        super();
        this.type = type;
    }

    
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public T get(Long id) {
        return (T) entityManager.find(type, id);
    }

    @Transactional(readOnly = true)
    public List<T> getAll() {
        return entityManager.createQuery(
                "select obj from " + type.getName() + " obj").getResultList();
    }

    @Transactional(rollbackFor = DataAccessException.class,
            readOnly = false, timeout = 30,
            propagation = Propagation.REQUIRED,
            isolation = Isolation.DEFAULT)    
    public void save(T object) throws DataAccessException {

        entityManager.persist(object);
    }
    

    public void delete(T object) throws DataAccessException {
        entityManager.remove(object);
    }

}
