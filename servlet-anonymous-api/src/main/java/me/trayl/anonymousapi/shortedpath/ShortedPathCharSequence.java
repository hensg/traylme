package me.trayl.anonymousapi.shortedpath;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ShortedPathCharSequence {

    final static int SHARDING_SIZE = 10;
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

    public ShortedPathCharSequence(int shardId, char[] lastCharSequence) {
        assert shardId < SHARDING_SIZE;
        this.shardId = shardId;
        this.lastCharSequence = lastCharSequence;
    }
}
