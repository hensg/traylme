package me.trayl.common.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Table
public class UrlRecentHits {

    @Id
    private long traceableUrlId;

    @Column
    private ZonedDateTime accessDate;

    @Column
    private long counter;

    public UrlRecentHits() {
    }

    public long getTraceableUrlId() {
        return traceableUrlId;
    }

    public void setTraceableUrlId(long traceableUrlId) {
        this.traceableUrlId = traceableUrlId;
    }

    public ZonedDateTime getAccessDate() {
        return accessDate;
    }

    public void setAccessDate(ZonedDateTime accessDate) {
        this.accessDate = accessDate;
    }

    public long getCounter() {
        return counter;
    }

    public void setCounter(long counter) {
        this.counter = counter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UrlRecentHits)) return false;

        UrlRecentHits that = (UrlRecentHits) o;

        if (traceableUrlId != that.traceableUrlId) return false;
        if (counter != that.counter) return false;
        return accessDate.equals(that.accessDate);
    }

    @Override
    public int hashCode() {
        int result = (int) (traceableUrlId ^ (traceableUrlId >>> 32));
        result = 31 * result + accessDate.hashCode();
        result = 31 * result + (int) (counter ^ (counter >>> 32));
        return result;
    }
}
