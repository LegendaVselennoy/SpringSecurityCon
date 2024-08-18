package org.example.springguru.beer.exception.advice;

import org.example.springguru.beer.model.dto.BeerDTO;
import org.springframework.http.ResponseEntity;

//@ControllerAdvice
public class ExceptionController {

//    @ExceptionHandler(NotfoundException.class)
    public ResponseEntity<BeerDTO> handleNotFoundException() {
        return ResponseEntity.notFound().build();
    }
}
