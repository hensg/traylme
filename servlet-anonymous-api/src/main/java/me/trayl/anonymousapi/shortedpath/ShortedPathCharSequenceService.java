package me.trayl.anonymousapi.shortedpath;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static me.trayl.anonymousapi.shortedpath.ShortedPathCharSequence.FIRST_VALID_CHAR;
import static me.trayl.anonymousapi.shortedpath.ShortedPathCharSequence.LAST_VALID_CHAR;

@Service
public class ShortedPathCharSequenceService {

    @Autowired
    private ShortedPathCharSequenceRepository shortedPathCharSeqRepo;

    private static final char[] FIRST_CHAR_SEQUENCE = new char[] {
            FIRST_VALID_CHAR,FIRST_VALID_CHAR,FIRST_VALID_CHAR,FIRST_VALID_CHAR
    };

    @Transactional
    public ShortedPathCharSequence genenerateNewId() {
        int shardId = (int) (Thread.currentThread().getId() % ShortedPathCharSequence.SHARDING_SIZE);
        ShortedPathCharSequence dao = shortedPathCharSeqRepo
                .findById(shardId)
                .map(this::incrementDaoLastCharSequence)
                .orElse(new ShortedPathCharSequence(shardId, FIRST_CHAR_SEQUENCE));

        return shortedPathCharSeqRepo.save(dao);
    }

    private ShortedPathCharSequence incrementDaoLastCharSequence(ShortedPathCharSequence shortedPathCharSequenceDao) {
        char[] newCharSeq = incrementCharSequence(shortedPathCharSequenceDao.getLastCharSequence());
        shortedPathCharSequenceDao.setLastCharSequence(newCharSeq);
        return shortedPathCharSequenceDao;
    }

    private char[] incrementCharSequence(char[] charSeq) {
        if (charSeq.length == 0) {
            return FIRST_CHAR_SEQUENCE;
        }

        int charSeqLength = charSeq.length;
        boolean incremented = false;

        int i = charSeqLength - 1;

        while (!incremented) {
            if (i == -1) {
                i++;
                charSeqLength++;
                char[] charSeqPlus1Space = new char[charSeqLength];
                System.arraycopy(charSeq, 0, charSeqPlus1Space, 1, charSeqLength - 1);
                charSeq = charSeqPlus1Space;
            }
            char newValue = incChar(charSeq[i]);
            incremented = newValue > charSeq[i];
            charSeq[i] = newValue;
            i--;
        }
        return charSeq;
    }

    private char incChar(final char value) {
        switch (value) {
            case 57: return 65;
            case 90: return 92;
            case LAST_VALID_CHAR: return FIRST_VALID_CHAR;
            default: return (char)(value + 1);
        }
    }
}
