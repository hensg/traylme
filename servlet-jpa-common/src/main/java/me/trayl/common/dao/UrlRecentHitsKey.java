package me.trayl.common.dao;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class UrlRecentHitsKey implements Serializable {

    private long traceableUrlId;
    private ZonedDateTime accessDate;

    public UrlRecentHitsKey() {
    }

    public UrlRecentHitsKey(long traceableUrlId, ZonedDateTime accessDate) {
        this.traceableUrlId = traceableUrlId;
        this.accessDate = accessDate;
    }

    public UrlRecentHitsKey(long traceableUrlId) {
        this.traceableUrlId = traceableUrlId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UrlRecentHitsKey)) return false;

        UrlRecentHitsKey that = (UrlRecentHitsKey) o;

        if (traceableUrlId != that.traceableUrlId) return false;
        return accessDate != null ? accessDate.equals(that.accessDate) : that.accessDate == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (traceableUrlId ^ (traceableUrlId >>> 32));
        result = 31 * result + (accessDate != null ? accessDate.hashCode() : 0);
        return result;
    }
}
