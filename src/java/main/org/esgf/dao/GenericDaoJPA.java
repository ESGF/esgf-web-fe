package org.esgf.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.esgf.domain.DomainObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("unchecked")
@Transactional

public class GenericDaoJPA<T extends DomainObject> implements GenericDao<T> {

    private Class<T> type;

    private EntityManager entityManager;
    private JpaTemplate jpaTemplate;

    public GenericDaoJPA(Class<T> type) {
        super();
        this.type = type;
    }

    @Autowired
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        this.jpaTemplate = jpaTemplate;
    }

    @Transactional(rollbackFor = DataAccessException.class,
            readOnly = false, timeout = 30,
            propagation = Propagation.REQUIRED,
            isolation = Isolation.DEFAULT)
    public void save(T object) throws DataAccessException {
        jpaTemplate.merge(object);
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

    /*
    public void save(T object) throws DataAccessException {

        entityManager.persist(object);
    }
    */

    public void delete(T object) throws DataAccessException {
        entityManager.remove(object);
    }

    /*
    public void indexEntity(T object) {
        FullTextEntityManager fullTextEntityManager = Search
                .getFullTextEntityManager(entityManager);
        fullTextEntityManager.index(object);
    }

    public void indexAllItems() {
        FullTextEntityManager fullTextEntityManager = Search
                .getFullTextEntityManager(entityManager);
        List results = fullTextEntityManager.createQuery(
                "from " + type.getCanonicalName()).getResultList();
        int counter = 0, numItemsInGroup = 10;
        Iterator resultsIt = results.iterator();
        while (resultsIt.hasNext()) {
            fullTextEntityManager.index(resultsIt.next());
            if (counter++ % numItemsInGroup == 0) {
                fullTextEntityManager.flushToIndexes();
                fullTextEntityManager.clear();
            }
        }
    }
    */
}
