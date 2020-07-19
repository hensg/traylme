package me.trayl.common.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TraceableUrlRepository extends CrudRepository<TraceableUrl, Long> {

    Optional<TraceableUrl> findByShortedPath(String shortedPath);

    List<TraceableUrl> findByAnonymousUserCookieId(String cookieId);
}
