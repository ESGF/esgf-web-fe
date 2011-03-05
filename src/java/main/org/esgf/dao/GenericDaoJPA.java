package org.esgf.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.esgf.domain.DomainObject;
import org.springframework.dao.DataAccessException;

@SuppressWarnings("unchecked")
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


    public T get(Long id) {
        return (T) entityManager.find(type, id);
    }

    public List<T> getAll() {
        return entityManager.createQuery(
                "select obj from " + type.getName() + " obj").getResultList();
    }

    public void save(T object) throws DataAccessException {

        entityManager.persist(object);
    }

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
