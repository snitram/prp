package br.adm.martins.prp.controller;

import java.util.List;
import java.util.Map;

public interface GenericPersistenceService {

	public <T> T create(T t);

	public <T> T find(T type, Object id);

	public <T> T update(T t);

	public <T> void delete(T type, Object id);

	public <T> void delete(T type);

	public <T> List<T> findAll(T type);
	
	public <T> List<T> findWithNamedQuery(String queryName);

	public <T> List<T> findWithNamedQuery(String queryName, int resultLimit);

	public <T> List<T> findWithNamedQuery(String namedQueryName, Map<String,Object> parameters);

	public <T> List<T> findWithNamedQuery(String namedQueryName, Map<String,Object> parameters,
			int resultLimit);
}