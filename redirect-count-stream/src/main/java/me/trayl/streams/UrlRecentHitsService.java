package me.trayl.streams;

import me.trayl.common.dao.TraceableUrl;
import me.trayl.common.dao.TraceableUrlRepository;
import me.trayl.common.dao.UrlRecentHits;
import me.trayl.common.dao.UrlRecentHitsKey;
import me.trayl.common.dao.UrlRecentHitsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static java.time.ZoneOffset.UTC;

@Service
public class UrlRecentHitsService {

    @Autowired
    TraceableUrlRepository traceableUrlRepository;

    @Autowired
    UrlRecentHitsRepository urlRecentHitsRepository;

    @Transactional
    public void updateHitCount(long id, Instant date, long aggCountWin, int attempt) {
        try {
            ZonedDateTime accessDate = date.atZone(UTC).truncatedTo(ChronoUnit.DAYS);
            UrlRecentHits hits = urlRecentHitsRepository
                    .findByTraceableUrlIdAndAccessDate(id, accessDate)
                    .orElse(new UrlRecentHits(id, accessDate));
            hits.setCounter(hits.getCounter() + aggCountWin);
            urlRecentHitsRepository.save(hits);

        } catch (OptimisticLockException e) {
            try {
                Thread.sleep(500 * 2 ^ attempt);
            } catch (InterruptedException interruptedException) {
                //intentionally silencied
            }
            e.printStackTrace();
            updateHitCount(id, date, aggCountWin, attempt + 1);
        }
    }
}
