package me.trayl.anonymousapi.url;

import me.trayl.common.dao.UrlRecentHits;
import me.trayl.common.dao.UrlRecentHitsKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlRecentHitsService extends CrudRepository<UrlRecentHits, UrlRecentHitsKey> {

    List<UrlRecentHits> findByTraceableUrlId(long id);


}
