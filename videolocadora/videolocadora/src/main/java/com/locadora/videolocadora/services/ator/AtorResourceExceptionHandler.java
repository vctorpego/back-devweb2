package com.locadora.videolocadora.services.ator;

import com.locadora.videolocadora.exceptions.AtorNaoEncontradoException;
import com.locadora.videolocadora.exceptions.ErroPadrao;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class AtorResourceExceptionHandler {
    @ExceptionHandler(AtorNaoEncontradoException.class)
    public ResponseEntity<ErroPadrao>atorNaoEncontardoHandler(AtorNaoEncontradoException e, HttpServletRequest req){
        ErroPadrao err = new ErroPadrao();

        err.setError("Resource not found");
        err.setMessage(e.getMessage());
        err.setPath(req.getRequestURI());
        err.setStatus(HttpStatus.NOT_FOUND.value());
        err.setTimestamp(Instant.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);

    }
}
