package me.trayl.common.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRecentHitsRepository extends CrudRepository<UrlRecentHits, Long> {

}
