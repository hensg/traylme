package me.trayl.anonymousapi.url;

import me.trayl.common.dao.UrlRecentHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/url_recent_hits")
public class UrlRecentHitsController {

    @Autowired
    UrlRecentHitsService urlRecentHitsService;

    @GetMapping("{traceableUrlId}")
    @ResponseBody
    public Iterable<UrlRecentHits> get(@PathVariable long traceableUrlId) {
        return urlRecentHitsService.findByTraceableUrlId(traceableUrlId);
    }

}
