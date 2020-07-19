package me.trayl.common.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Version;
import java.time.ZonedDateTime;

@Entity
@Table
@IdClass(UrlRecentHitsKey.class)
public class UrlRecentHits {

    @Id
    private long traceableUrlId;

    @Id
    private ZonedDateTime accessDate;

    @Column
    private long counter;

    @Version
    private long version;

    public UrlRecentHits() {
    }

    public UrlRecentHits(long traceableUrlId, ZonedDateTime accessDate) {
        this.traceableUrlId = traceableUrlId;
        this.accessDate = accessDate;
        this.counter = 0;
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

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UrlRecentHits)) return false;

        UrlRecentHits that = (UrlRecentHits) o;

        if (traceableUrlId != that.traceableUrlId) return false;
        if (counter != that.counter) return false;
        if (version != that.version) return false;
        return accessDate != null ? accessDate.equals(that.accessDate) : that.accessDate == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (traceableUrlId ^ (traceableUrlId >>> 32));
        result = 31 * result + (accessDate != null ? accessDate.hashCode() : 0);
        result = 31 * result + (int) (counter ^ (counter >>> 32));
        result = 31 * result + (int) (version ^ (version >>> 32));
        return result;
    }
}
