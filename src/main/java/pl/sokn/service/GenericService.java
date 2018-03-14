package pl.sokn.service;

import pl.sokn.exception.OperationException;

import java.io.Serializable;
import java.util.List;

public interface GenericService<E, K extends Serializable> {
    E save(E dto) throws OperationException;
    List<E> getAll();
    E retrieve(K id);
    void update(E dto);
    void remove(E dto) throws OperationException;
    void removeById(K id);
    boolean doesExist(K id);
}
