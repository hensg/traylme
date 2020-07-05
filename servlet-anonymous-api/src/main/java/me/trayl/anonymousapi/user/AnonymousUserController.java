package me.trayl.anonymousapi.user;

import me.trayl.anonymousapi.user.url.TraceableUrlDto;
import me.trayl.anonymousapi.user.url.TraceableUrlService;
import me.trayl.anonymousapi.shortedpath.ShortedPathCharSequence;
import me.trayl.anonymousapi.shortedpath.ShortedPathCharSequenceService;
import me.trayl.common.dao.AnonymousUser;
import me.trayl.common.dao.TraceableUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequestMapping("/anon_users/")
public class AnonymousUserController {

    @Autowired
    private AnonymousUserService anonymousUserService;

    @Autowired
    private TraceableUrlService trackUrlService;

    @Autowired
    private ShortedPathCharSequenceService shortedPathCharSequenceService;

    @PostMapping("{anonUserId}/urls")
    @ResponseBody
    public TraceableUrl trackUrl(@Valid @RequestBody TraceableUrlDto form,
                                 @PathVariable String anonUserId) throws MalformedURLException {

        String originalUrl = form.getOriginalUrl();
        AnonymousUser anon = anonymousUserService.findOrCreateAnonUser(anonUserId); //block
        ShortedPathCharSequence shorted = shortedPathCharSequenceService.genenerateNewId(); //block

        return trackUrlService.addUrl(shorted, originalUrl, anon);
    }

    @GetMapping("{anonUserId}/urls")
    @ResponseBody
    public List<TraceableUrl> getAnonymousUserTraceableUrls(@PathVariable String anonUserId) {
        return trackUrlService.findByCookieId(anonUserId);
    }

}
