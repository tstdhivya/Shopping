package com.app.shoppingzone.config;

import java.io.Serializable;

import org.springframework.data.repository.NoRepositoryBean;

/**
 * Extend this repository if you need to read as well as write data.
 * 
 * Having our own repository base classes allows us to only selectively expose
 * CRUD methods. I.e., a child of this class must explicitly declare the
 * additional methods that it wants - it won't get them automatically by
 * inheritance. Note that the methods declared here and in child repositories
 * must exactly match the method signatures defined in JpaRepository,
 * CrudRepository and PagingAndSortingRepository.
 */
@NoRepositoryBean
public interface WriteableRepository<T, ID extends Serializable> extends ReadOnlyRepository<T, ID> {

	<S extends T> S save(S entity);

	<S extends T> S saveAndFlush(S entity);

	<S extends T> Iterable<S> saveAll(Iterable<S> entities);

	void flush();

	void deleteById(ID id);

	void delete(T entity);

	void deleteAll(Iterable<? extends T> entities);

}
