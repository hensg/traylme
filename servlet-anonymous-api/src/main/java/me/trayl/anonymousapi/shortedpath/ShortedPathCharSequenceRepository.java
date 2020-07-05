package me.trayl.anonymousapi.shortedpath;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;

@Repository
interface ShortedPathCharSequenceRepository extends CrudRepository<ShortedPathCharSequence, Integer> {

    /**
     * lock by shardid
     */
    @Lock(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
    @Override
    Optional<ShortedPathCharSequence> findById(Integer integer);
}
