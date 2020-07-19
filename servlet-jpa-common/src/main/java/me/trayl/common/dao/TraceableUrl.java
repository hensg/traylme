package me.trayl.common.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.List;

import static java.time.ZoneOffset.UTC;

@Entity
@Table(
    indexes = {
        @Index(name = "traceable_url_shorted_path_idx", columnList = "shortedPath")
    }
)
public class TraceableUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String shortedPath;

    @Column(nullable = false, length = 2100)
    private URL originalUrl;

    @Column(nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private AnonymousUser anonymousUser;

    public TraceableUrl() {}
    public TraceableUrl(String shortedPath, URL originalUrl, AnonymousUser anonUser) {
        this.shortedPath = shortedPath;
        this.originalUrl = originalUrl;
        this.anonymousUser = anonUser;
        this.createdAt = ZonedDateTime.now(UTC);
    }

    public TraceableUrl(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getShortedPath() {
        return shortedPath;
    }

    public void setShortedPath(String shortedPath) {
        this.shortedPath = shortedPath;
    }

    public URL getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(URL originalUrl) {
        this.originalUrl = originalUrl;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public AnonymousUser getAnonymousUser() {
        return anonymousUser;
    }

    public void setAnonymousUser(AnonymousUser anonymousUser) {
        this.anonymousUser = anonymousUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TraceableUrl)) return false;

        TraceableUrl that = (TraceableUrl) o;

        if (id != that.id) return false;
        if (!shortedPath.equals(that.shortedPath)) return false;
        if (!originalUrl.equals(that.originalUrl)) return false;
        if (!createdAt.equals(that.createdAt)) return false;
        return anonymousUser.equals(that.anonymousUser);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + shortedPath.hashCode();
        result = 31 * result + originalUrl.hashCode();
        result = 31 * result + createdAt.hashCode();
        result = 31 * result + anonymousUser.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TraceableUrl{" +
                "id=" + id +
                ", shortedPath='" + shortedPath + '\'' +
                ", originalUrl=" + originalUrl +
                ", createdAt=" + createdAt +
                ", anonymousUser=" + anonymousUser +
                '}';
    }
}
