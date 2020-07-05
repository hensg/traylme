package me.trayl.common.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnonymousUserRepository extends CrudRepository<AnonymousUser, Long> {

    Optional<AnonymousUser> findByCookieId(String cookieId);
}
