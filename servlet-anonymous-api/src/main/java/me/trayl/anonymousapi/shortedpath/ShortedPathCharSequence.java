package me.trayl.anonymousapi.shortedpath;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Arrays;

@Entity
@Table
public class ShortedPathCharSequence {

    final static int SHARDING_SIZE = 100;
    final static int FIRST_VALID_CHAR = 48; //num 0
    final static int LAST_VALID_CHAR = 122; //letter z


    /**
     * given that charsequence is incremental, and we need to lock to avoid race conditions,
     * let's use the first number for sharding purposes having more charsequences being generated
     * in parallel and an exclusive lock for each
     */
    @Id
    private int shardId;

    @Column(nullable = false)
    private char[] lastCharSequence;

    @Version
    private long version;

    public ShortedPathCharSequence() {}

    public ShortedPathCharSequence(int shardId, char[] lastCharSequence) {
        assert shardId < SHARDING_SIZE;
        this.shardId = shardId;
        this.lastCharSequence = lastCharSequence;
    }

    public static int getShardingSize() {
        return SHARDING_SIZE;
    }

    public static int getFirstValidChar() {
        return FIRST_VALID_CHAR;
    }

    public static int getLastValidChar() {
        return LAST_VALID_CHAR;
    }

    public int getShardId() {
        return shardId;
    }

    public void setShardId(int shardId) {
        this.shardId = shardId;
    }

    public char[] getLastCharSequence() {
        return lastCharSequence;
    }

    public void setLastCharSequence(char[] lastCharSequence) {
        this.lastCharSequence = lastCharSequence;
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
        if (!(o instanceof ShortedPathCharSequence)) return false;

        ShortedPathCharSequence that = (ShortedPathCharSequence) o;

        if (shardId != that.shardId) return false;
        if (version != that.version) return false;
        return Arrays.equals(lastCharSequence, that.lastCharSequence);
    }

    @Override
    public int hashCode() {
        int result = shardId;
        result = 31 * result + Arrays.hashCode(lastCharSequence);
        result = 31 * result + (int) (version ^ (version >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "ShortedPathCharSequence{" +
                "shardId=" + shardId +
                ", lastCharSequence=" + Arrays.toString(lastCharSequence) +
                ", version=" + version +
                '}';
    }
}
