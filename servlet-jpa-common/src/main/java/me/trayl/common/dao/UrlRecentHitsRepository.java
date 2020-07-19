package me.trayl.common.dao;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.time.ZonedDateTime;
import java.util.Optional;


@Repository
public interface UrlRecentHitsRepository extends CrudRepository<UrlRecentHits, UrlRecentHitsKey> {

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    Optional<UrlRecentHits> findByTraceableUrlIdAndAccessDate(long traceableUrlId, ZonedDateTime accessDate);
}
