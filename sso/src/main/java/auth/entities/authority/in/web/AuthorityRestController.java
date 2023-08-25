package auth.entities.authority.in.web;

import auth.Endpoints;
import auth.docs.openapi.*;
import auth.entities.authority.Authority;
import auth.entities.authority.AuthorityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Endpoints.AUTHORITIES)
@Tag(name = "Authority")
@RequiredArgsConstructor
public class AuthorityRestController {

    private final AuthorityService authorityService;

    @GetMapping
    @RestDocumentedGetMappingResponsePageRequireAuthorization
    ResponseEntity<Page<AuthorityProjection>> get(@NotDocumentedSchema Pageable pageable,
                                                  @RequestParam(required = false) String term) {
        Page<Authority> authorities = term == null ? authorityService.getAll(pageable) :
                authorityService.search(term, pageable);
        return ResponseEntity.ok(authorities.map(AuthorityProjection::from));
    }

    @GetMapping("/{id}")
    @RestDocumentedGetMappingRequireAuthorization
    ResponseEntity<AuthorityProjection> getById(@PathVariable String id) {
        return ResponseEntity.ok(AuthorityProjection.from(authorityService.getById(id)));
    }

    @PostMapping
    @RestDocumentedPostMappingRequireAuthorization
    ResponseEntity<Void> create(@RequestBody @Validated AuthorityCreateRequest authorityCreateRequest) {
        authorityService.create(authorityCreateRequest.getAuthority());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @RestDocumentedPutMappingRequireAuthorization
    ResponseEntity<Void> edit(@PathVariable String id, @RequestBody @Validated AuthorityEditRequest authorityEditRequest) {
        Authority authority = authorityService.getById(id);
        authorityService.edit(authorityEditRequest.getModifiedAuthority(authority));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @RestDocumentedDeleteMappingRequireAuthorization
    ResponseEntity<Void> delete(@PathVariable String id) {
        authorityService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
