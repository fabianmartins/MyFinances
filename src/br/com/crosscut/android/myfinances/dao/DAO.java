package br.com.crosscut.android.myfinances.dao;


public interface DAO<T> {
	void save(T object) throws DAOException;
	T saveOrUpdate(T object) throws DAOException;
	void update(T object) throws DAOException;
	void delete(T object) throws DAOException;
	T selectById(long id) throws DAOException;
}
