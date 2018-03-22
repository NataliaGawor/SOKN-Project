package pl.sokn.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sokn.exception.OperationException;
import pl.sokn.security.information.AuthenticationFacade;
import pl.sokn.service.GenericService;
import pl.sokn.service.helper.SendEmailService;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.Collection;

@Slf4j
public abstract class AbstractGenericController<T extends Serializable, E, K extends Serializable> {

    private final GenericService<E, K> service;
    SendEmailService emailService;
    private AuthenticationFacade authenticationFacade;

    AbstractGenericController(GenericService<E, K> service) {
        this.service = service;
    }

    @Autowired
    public void setEmailService(SendEmailService emailService) {
        this.emailService = emailService;
    }

    @Autowired
    public void setAuthenticationFacade(AuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    @ApiOperation(value = "Save object in database")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<E> create(@RequestBody @Valid T t) throws OperationException {
        final E saved = service.save(convertToEntity(t));

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @ApiOperation(value = "Retrieve object by ID")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<E> get(@PathVariable final K id) {
        final E e = service.retrieve(id);

        if (e != null) {
            return ResponseEntity.ok(e);
        }

        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Retrieve all objects")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<E>> getAll() {
        final Collection<E> all = service.getAll();

        return ResponseEntity.ok(all);
    }

    @ApiOperation(value = "Delete object by ID")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<T> delete(@PathVariable final K id) {
        service.removeById(id);

        return ResponseEntity.accepted().build();
    }

    protected abstract T convertToDTO(final E e);

    protected abstract E convertToEntity(final T t);

    String getCredentials() {
        return authenticationFacade.getAuthentication().getName();
    }
}
