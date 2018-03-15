package pl.sokn.service;

import pl.sokn.entity.Authority;

public interface AuthorityService extends GenericService<Authority, Long> {
    Authority retrieve(final String name);
}
