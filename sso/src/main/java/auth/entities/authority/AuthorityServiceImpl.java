package auth.entities.authority;

import auth.entities.authority.out.persistence.AuthorityRepository;
import auth.errors.Errors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {

    private final AuthorityRepository authorityRepository;

    @Transactional
    @Override
    public Authority create(Authority authority) {
        if (existsByName(authority.getName())) {
            throw Errors.authorityAlreadyExistsByName(authority.getName());
        }
        return authorityRepository.save(authority);
    }

    @Transactional
    @Override
    public Authority edit(Authority authority) {
        if (authority.getId() != null && !existsById(authority.getId())) {
            throw Errors.authorityNotExistsById(authority.getId());
        }
        if (existsByName(authority.getName()) &&
                !getByName(authority.getName()).equals(authority)) {
            throw Errors.authorityAlreadyExistsByName(authority.getName());
        }
        return authorityRepository.save(authority);
    }

    @Transactional(readOnly = true)
    @Override
    public Authority getById(String id) {
        return authorityRepository.findById(id)
                .orElseThrow(() -> Errors.authorityNotExistsById(id));
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(String id) {
        return authorityRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Authority getByName(String name) {
        return authorityRepository.findByName(name)
                .orElseThrow(() -> Errors.authorityNotExistsByName(name));
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByName(String name) {
        return authorityRepository.existsByName(name);
    }

    @Transactional(readOnly = true)
    @Override
    public long getCount() {
        return authorityRepository.count();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Authority> getAll(Pageable pageable) {
        return authorityRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Authority> search(String text, Pageable pageable) {
        return authorityRepository.search(text, pageable);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        authorityRepository.deleteById(getById(id).getId());
    }
}
