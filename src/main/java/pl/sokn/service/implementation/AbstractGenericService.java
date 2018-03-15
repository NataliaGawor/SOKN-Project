package pl.sokn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import pl.sokn.exception.OperationException;
import pl.sokn.repository.GenericRepository;
import pl.sokn.service.GenericService;
import pl.sokn.service.helper.JSONCreatorService;
import pl.sokn.service.helper.SendEmailService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

public abstract class AbstractGenericService<E, K extends Serializable> implements GenericService<E, K> {
    private final GenericRepository<E, K> repository;
    SendEmailService emailService;
    JSONCreatorService jsonService;

    AbstractGenericService(GenericRepository<E, K> repository) {
        this.repository = repository;
    }

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    public void setJsonService(JSONCreatorService jsonService) {
        this.jsonService = jsonService;
    }

    @Autowired
    public void setEmailService(SendEmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public E save(E object) throws OperationException {
        return repository.save(object);
    }

    @Override
    public List<E> getAll() {
        return repository.findAll();
    }

    @Override
    public E retrieve(K id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void update(E object) {
        repository.save(object);
    }

    @Override
    public void remove(E object) throws OperationException {
        repository.delete(object);
    }

    @Override
    public void removeById(K id) {
        repository.deleteById(id);
    }

    @Override
    public boolean doesExist(K id) {
        return repository.findById(id).orElse(null) != null;
    }

    String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + "3000" + request.getContextPath();
    }
}
