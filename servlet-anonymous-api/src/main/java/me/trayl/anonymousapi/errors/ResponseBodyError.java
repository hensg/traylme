package me.trayl.anonymousapi.errors;

import java.time.ZonedDateTime;
import java.util.*;

import static java.time.ZoneOffset.UTC;

public class ResponseBodyError {

    private final Map<String, List<String>> errors;
    private final ZonedDateTime dateTime;

    private ResponseBodyError(Map<String, List<String>> errors, ZonedDateTime dateTime) {
        this.errors = errors;
        this.dateTime = dateTime;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public static class Builder {

        private Map<String, List<String>> errors;
        private ZonedDateTime dateTime;

        private Builder() {
            this.errors = new HashMap<>();
            this.dateTime = ZonedDateTime.now(UTC);
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder addError(String fieldName, String errorMessage) {
            List<String> errorsForFieldX = this.errors.getOrDefault(fieldName, new LinkedList<>());
            errorsForFieldX.add(errorMessage);
            this.errors.putIfAbsent(fieldName, errorsForFieldX);
            return this;
        }

        public ResponseBodyError build() {
            return new ResponseBodyError(errors, dateTime);
        }
    }
}
