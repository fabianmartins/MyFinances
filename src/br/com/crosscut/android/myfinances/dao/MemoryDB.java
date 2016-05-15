package br.com.crosscut.android.myfinances.dao;

import java.util.HashMap;
import java.util.HashSet;

public class MemoryDB {
	
	@SuppressWarnings("rawtypes")
	private final HashMap<Class,HashSet<Object>> memoryDB = new HashMap<Class,HashSet<Object>>();
	
	public void save(Object object) throws DAOException {
		HashSet<Object> base = memoryDB.get(object.getClass());
		base.add(object);
	}
	public void saveOrUpdate(Object object) throws DAOException {
		HashSet<Object> base = memoryDB.get(object.getClass());
		base.remove(object);
		base.add(object);
	}
	public void update(Object object) throws DAOException {
		saveOrUpdate(object);
	}
	
	public void delete(Object object) throws DAOException {
		HashSet<Object> base = memoryDB.get(object.getClass());
		base.remove(object);
	}
	
	public Object selectById(Class clazz, long id) throws DAOException {
		return null;
		
	}
}
