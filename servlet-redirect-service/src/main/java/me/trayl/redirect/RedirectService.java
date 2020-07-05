package me.trayl.redirect;

import me.trayl.common.dao.TraceableUrl;
import me.trayl.common.dao.TraceableUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RedirectService {

    @Autowired
    private TraceableUrlRepository urlRepository;

    public Optional<TraceableUrl> findOriginalUrl(String shortedPath) {
        return this.urlRepository.findByShortedPath(shortedPath);
    }

}
