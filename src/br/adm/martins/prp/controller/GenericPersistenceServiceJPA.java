package br.adm.martins.prp.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;


/**
 * A persistence generic service
 * @author martins
 * @param <T>
 */
@Stateless
//@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@SuppressWarnings("all")
public class GenericPersistenceServiceJPA implements GenericPersistenceService, Serializable {
   
    
	@PersistenceContext
    private EntityManager em;
    
    @Inject
    private transient Logger log;
    
    
    public <T> T create(T t) {
        this.em.persist(t);
        this.em.flush();
//        this.em.refresh(t);
        log.info("create object: " + t.getClass().getSimpleName());
        return t;
    }

    public <T> T find(T type, Object id) {
        log.info("retrive object: " + type.getClass().getSimpleName() + "- Id: " + id.toString());
        return (T) this.em.find(type.getClass(), id);
    }

    public <T> void delete(T type, Object id) {
       Object ref = this.em.getReference(type.getClass(), id);
       this.em.remove(ref);
       log.info("remove object: " + type.getClass().getSimpleName() + "- Id: " + id.toString());
    }
    
    public <T> void delete(T type) {
//        Object ref = this.em.getReference(type, id);
        log.info("remove object: " + type.getClass().getSimpleName());    	
    	type = this.em.merge(type);
        this.em.remove(type);
     }    
    

    public <T> T update(T type) {
        log.info("update object: " + type.getClass().getSimpleName());    	
        return this.em.merge(type);
    }
    
    public <T> List<T> findAll(Class type) {
    	log.info("retrive all " + type.getClass().getSimpleName());
		final Query query = this.em.createQuery("from " + type.getClass().getSimpleName());
		List<T> list = query.getResultList();
//		list = this.entityManager.createNamedQuery(this.entityClass.getSimpleName() + ".selectAll").getResultList();
        if (list == null) {
                list = new ArrayList<T>();
        }
        return list;
    	
    }
    
    public <T> List<T> findWithNamedQuery(String namedQueryName){
    	log.debug("findWithNamedQuery: " + namedQueryName);
        return this.em.createNamedQuery(namedQueryName).getResultList();
    }
    
    public <T> List<T> findWithNamedQuery(String namedQueryName, Map<String,Object> parameters){
        return findWithNamedQuery(namedQueryName, parameters, 0);
    }

    public <T> List<T> findWithNamedQuery(String queryName, int resultLimit) {
        return this.em.createNamedQuery(queryName).setMaxResults(resultLimit).getResultList();    
    }

    public <T> List<T> findByNativeQuery(String sql, Class<T> type) {
        return this.em.createNativeQuery(sql, type).getResultList();
    }
    
   
   public <T> List<T> findWithNamedQuery(String namedQueryName, Map<String,Object> parameters,int resultLimit){
        Set<Entry<String, Object>> rawParameters = parameters.entrySet();
        Query query = this.em.createNamedQuery(namedQueryName);
        if(resultLimit > 0)
            query.setMaxResults(resultLimit);
        for (Entry<String, Object> entry : rawParameters) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.getResultList();
    }


}