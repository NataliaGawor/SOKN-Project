package pl.sokn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sokn.entity.Authority;
import pl.sokn.repository.AuthorityRepository;
import pl.sokn.service.AuthorityService;

@Service
public class AuthorityServiceImpl extends AbstractGenericService<Authority, Long> implements AuthorityService {

    private final AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityServiceImpl(AuthorityRepository authorityRepository) {
        super(authorityRepository);
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Authority retrieve(String name) {
        return authorityRepository.findByRole(name);
    }
}
