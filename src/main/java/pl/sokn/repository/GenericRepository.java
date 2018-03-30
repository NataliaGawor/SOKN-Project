package pl.sokn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Extend this interface when you want create new repository
 * It extends JpaRepository that provides us basic operation on entities such like: save, findById, delete, findAll
 *
 * @param <E> entity class
 * @param <K> primary key type
 */
@NoRepositoryBean
public interface GenericRepository<E, K extends Serializable> extends JpaRepository<E, K> {
}
