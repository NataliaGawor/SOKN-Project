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

import javax.validation.Valid;
import java.io.Serializable;
import java.util.Collection;

/**
 * Extend this class if you want Controller with working CRUD operations
 *
 * @param <T> - DTO class
 * @param <E> - Entity class
 * @param <K> - Primary key type of Entity class
 */
@Slf4j
public abstract class AbstractGenericController<T extends Serializable, E, K extends Serializable> {

    private final GenericService<E, K> service;
    private AuthenticationFacade authenticationFacade;

    AbstractGenericController(GenericService<E, K> service) {
        this.service = service;
    }

    @Autowired
    public void setAuthenticationFacade(AuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    /**
     * Generic end point for saving objects in database
     *
     * @param t DTO class that is mapped from JSON thanks to @RequestBody annotation
     *          If mapped successfully then @Valid checks if the validation requirements are met
     * @return - JSON with HTTP status 201
     * @throws OperationException when service throws an exceptions
     * @apiNote  @ApiOperation - it is only description for Swagger documentation
     */
    @ApiOperation(value = "Save object in database")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<E> create(@RequestBody @Valid T t) throws OperationException {
        // convert to entity class before using services
        final E saved = service.save(convertToEntity(t));

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     *  Generic end point for retrieving object from database by its id
     * @param id - object's id in database
     * @return object if exists with http 200 status, returns 404 status otherwise
     */
    @ApiOperation(value = "Retrieve object by ID")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<E> get(@PathVariable final K id) {
        final E e = service.retrieve(id);

        if (e != null) {
            return ResponseEntity.ok(e);
        }

        return ResponseEntity.notFound().build();
    }

    /**
     *
     * @return list of all objects in database
     */
    @ApiOperation(value = "Retrieve all objects")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<E>> getAll() {
        final Collection<E> all = service.getAll();

        return ResponseEntity.ok(all);
    }

    /**
     *
     * @param id - object's id in database
     * @return accepted if deleted successfully
     */
    @ApiOperation(value = "Delete object by ID")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<T> delete(@PathVariable final K id) {
        service.removeById(id);

        return ResponseEntity.accepted().build();
    }

    /**
     *
     * @param e - entity class
     * @return - returns converted DTO class
     */
    protected abstract T convertToDTO(final E e);

    /**
     *
     * @param t - DTO class
     * @return - returns converted Entity class
     */
    protected abstract E convertToEntity(final T t);

    /**
     *
     * @return - email of authenticated User
     */
    String getCredentials() {
        return authenticationFacade.getAuthentication().getName();
    }
}
