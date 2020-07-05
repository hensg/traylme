package me.trayl.redirect;

public class TraceableUrlNotRegisteredException extends RuntimeException {

    TraceableUrlNotRegisteredException() {
        super("URL not found :(");
    }
}
