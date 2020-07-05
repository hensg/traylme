package me.trayl.anonymousapi.errors;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleNotValid(MethodArgumentNotValidException e) {
        List<FieldError> bindErrors = e.getBindingResult().getFieldErrors();
        ResponseBodyError.Builder builder = ResponseBodyError.Builder.newBuilder();
        for (FieldError fe: bindErrors) {
            builder.addError(fe.getField(), fe.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(builder.build());
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity handleInvalidFormat(InvalidFormatException invalidFormatExcep) {
        ResponseBodyError.Builder builder = ResponseBodyError.Builder.newBuilder();
        String fieldName = invalidFormatExcep.getPath().stream()
                .map(JsonMappingException.Reference::getFieldName).collect(Collectors.joining("."));
        builder.addError(fieldName, invalidFormatExcep.getCause().getMessage());
        return ResponseEntity.badRequest().body(builder.build());
    }
}
