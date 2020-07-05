package me.trayl.anonymousapi.user.url;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class TraceableUrlDto {

    @NotEmpty(message = "Missing URL to be tracked")
    @Pattern(
        regexp = "^(https?:\\/\\/)?\\w+\\..{1,2083}",
        message = "The URL seems to be invalid, please have a look"
    )
    private String originalUrl;

    @Override
    public String toString() {
        return "UrlDto{" +
                "originalUrl=" + originalUrl +
                '}';
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

}
