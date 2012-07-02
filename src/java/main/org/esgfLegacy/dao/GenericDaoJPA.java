/*****************************************************************************
 * Copyright © 2011 , UT-Battelle, LLC All rights reserved
 *
 * OPEN SOURCE LICENSE
 *
 * Subject to the conditions of this License, UT-Battelle, LLC (the
 * “Licensor”) hereby grants to any person (the “Licensee”) obtaining a copy
 * of this software and associated documentation files (the "Software"), a
 * perpetual, worldwide, non-exclusive, irrevocable copyright license to use,
 * copy, modify, merge, publish, distribute, and/or sublicense copies of the
 * Software.
 *
 * 1. Redistributions of Software must retain the above open source license
 * grant, copyright and license notices, this list of conditions, and the
 * disclaimer listed below.  Changes or modifications to, or derivative works
 * of the Software must be noted with comments and the contributor and
 * organization’s name.  If the Software is protected by a proprietary
 * trademark owned by Licensor or the Department of Energy, then derivative
 * works of the Software may not be distributed using the trademark without
 * the prior written approval of the trademark owner.
 *
 * 2. Neither the names of Licensor nor the Department of Energy may be used
 * to endorse or promote products derived from this Software without their
 * specific prior written permission.
 *
 * 3. The Software, with or without modification, must include the following
 * acknowledgment:
 *
 *    "This product includes software produced by UT-Battelle, LLC under
 *    Contract No. DE-AC05-00OR22725 with the Department of Energy.”
 *
 * 4. Licensee is authorized to commercialize its derivative works of the
 * Software.  All derivative works of the Software must include paragraphs 1,
 * 2, and 3 above, and the DISCLAIMER below.
 *
 *
 * DISCLAIMER
 *
 * UT-Battelle, LLC AND THE GOVERNMENT MAKE NO REPRESENTATIONS AND DISCLAIM
 * ALL WARRANTIES, BOTH EXPRESSED AND IMPLIED.  THERE ARE NO EXPRESS OR
 * IMPLIED WARRANTIES OF MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE,
 * OR THAT THE USE OF THE SOFTWARE WILL NOT INFRINGE ANY PATENT, COPYRIGHT,
 * TRADEMARK, OR OTHER PROPRIETARY RIGHTS, OR THAT THE SOFTWARE WILL
 * ACCOMPLISH THE INTENDED RESULTS OR THAT THE SOFTWARE OR ITS USE WILL NOT
 * RESULT IN INJURY OR DAMAGE.  The user assumes responsibility for all
 * liabilities, penalties, fines, claims, causes of action, and costs and
 * expenses, caused by, resulting from or arising out of, in whole or in part
 * the use, storage or disposal of the SOFTWARE.
 *
 *
 ******************************************************************************/

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
 * @author: Feiyi Wang (fwang2@ornl.gov)
 *
 *
 */
package org.esgfLegacy.dao;

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


    /**
     * the merge statement is to address removing "detached" object instance
     */

    @Transactional(rollbackFor = DataAccessException.class,
            readOnly = false, timeout = 30,
            propagation = Propagation.REQUIRED,
            isolation = Isolation.DEFAULT)
    public void delete(T object) throws DataAccessException {
        entityManager.remove(object);
    }

    @Transactional(rollbackFor = DataAccessException.class,
            readOnly = false, timeout = 30,
            propagation = Propagation.REQUIRED,
            isolation = Isolation.DEFAULT)
    public void delete(Long id) throws DataAccessException {
        T obj = get(id);
        entityManager.remove(obj);
    }

}
