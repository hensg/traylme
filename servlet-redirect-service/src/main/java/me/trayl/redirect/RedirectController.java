package me.trayl.redirect;

import me.trayl.common.dao.TraceableUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class RedirectController {

    @Autowired
    private RedirectService redirectService;

    @Autowired
    private KafkaLogService kafkaLogService;

    @GetMapping(value = "/{traceableUrlPathVar}")
    public void redirect(@PathVariable String traceableUrlPathVar,
                         HttpServletResponse resp) {
        Optional<TraceableUrl> maybeTraceableUrl = this.redirectService.findOriginalUrl(traceableUrlPathVar);

        TraceableUrl traceableUrl = maybeTraceableUrl.orElseThrow(TraceableUrlNotRegisteredException::new);

        kafkaLogService.logAccess(traceableUrl.getId());

        resp.setStatus(302);
        resp.setHeader("Location", traceableUrl.getOriginalUrl().toString());

    }

    @ExceptionHandler(TraceableUrlNotRegisteredException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String urlNotRegistered(HttpServletResponse resp, TraceableUrlNotRegisteredException e) throws IOException {
        return e.getMessage();
    }
}
