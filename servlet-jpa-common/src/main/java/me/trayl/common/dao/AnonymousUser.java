package me.trayl.common.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;

import static java.time.ZoneOffset.UTC;

@Entity
@Table(
    indexes = {
        @Index(name = "unique_anonymous_user_cookie_id", columnList = "cookieId", unique = true)
    }
)
public class AnonymousUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String cookieId;

    @Column(nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(nullable = false)
    private ZonedDateTime lastUpdatedAt;

    @Column
    private Locale locale;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = false)
    private List<TraceableUrl> trackUrls;

    public AnonymousUser() {}
    public AnonymousUser(String cookieId) {
        this.cookieId = cookieId;
        this.createdAt = ZonedDateTime.now(UTC);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCookieId() {
        return cookieId;
    }

    public void setCookieId(String cookieId) {
        this.cookieId = cookieId;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(ZonedDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public List<TraceableUrl> getTrackUrls() {
        return trackUrls;
    }

    public void setTrackUrls(List<TraceableUrl> trackUrls) {
        this.trackUrls = trackUrls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnonymousUser)) return false;

        AnonymousUser that = (AnonymousUser) o;

        if (id != that.id) return false;
        if (!cookieId.equals(that.cookieId)) return false;
        if (!createdAt.equals(that.createdAt)) return false;
        if (lastUpdatedAt != null ? !lastUpdatedAt.equals(that.lastUpdatedAt) : that.lastUpdatedAt != null)
            return false;
        if (locale != null ? !locale.equals(that.locale) : that.locale != null) return false;
        return trackUrls != null ? trackUrls.equals(that.trackUrls) : that.trackUrls == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + cookieId.hashCode();
        result = 31 * result + createdAt.hashCode();
        result = 31 * result + (lastUpdatedAt != null ? lastUpdatedAt.hashCode() : 0);
        result = 31 * result + (locale != null ? locale.hashCode() : 0);
        result = 31 * result + (trackUrls != null ? trackUrls.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AnonymousUser{" +
                "id=" + id +
                ", cookieId='" + cookieId + '\'' +
                ", createdAt=" + createdAt +
                ", lastUpdatedAt=" + lastUpdatedAt +
                ", locale=" + locale +
                ", trackUrls=" + trackUrls +
                '}';
    }
}
