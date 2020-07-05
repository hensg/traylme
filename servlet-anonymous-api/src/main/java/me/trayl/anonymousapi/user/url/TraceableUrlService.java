package me.trayl.anonymousapi.user.url;

import me.trayl.common.dao.AnonymousUser;
import me.trayl.common.dao.TraceableUrl;
import me.trayl.anonymousapi.shortedpath.ShortedPathCharSequence;
import me.trayl.common.dao.TraceableUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Service
public class TraceableUrlService {

    @Autowired
    private TraceableUrlRepository trackUrlRepository;

    public TraceableUrl addUrl(ShortedPathCharSequence newShortedPath, String originalUrlStr,
                               AnonymousUser anonUser) throws MalformedURLException {
        URL originalUrl = originalUrlStr.contains("://") ? new URL(originalUrlStr) : new URL("http://" + originalUrlStr);

        String shortedPath = newShortedPath.getShardId() + String.valueOf(newShortedPath.getLastCharSequence());
        TraceableUrl dao = new TraceableUrl(shortedPath, originalUrl, anonUser);
        return trackUrlRepository.save(dao);
    }


    public List<TraceableUrl> findByCookieId(String cookieId) {
        return  trackUrlRepository.findByAnonymousUserCookieId(cookieId, Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}
