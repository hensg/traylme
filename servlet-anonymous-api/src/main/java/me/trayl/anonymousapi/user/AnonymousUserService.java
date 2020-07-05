package me.trayl.anonymousapi.user;

import me.trayl.common.dao.AnonymousUser;
import me.trayl.common.dao.AnonymousUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

import static java.time.ZoneOffset.UTC;

@Service
public class AnonymousUserService {

    @Autowired
    private AnonymousUserRepository anonymousUserRepository;

    public AnonymousUser findOrCreateAnonUser(String cookieId) {
        AnonymousUser anonymousUserDao = anonymousUserRepository
                .findByCookieId(cookieId)
                .orElse(new AnonymousUser(cookieId));

        anonymousUserDao.setLastUpdatedAt(ZonedDateTime.now(UTC));

        return anonymousUserRepository.save(anonymousUserDao);
    }
}
