package com.example.ClienteRest.exception;

import com.example.ClienteRest.dtos.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExcetionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> manejarValidaciones(MethodArgumentNotValidException ex){
        List<String> detalles = new ArrayList<>();

        for(FieldError error: ex.getBindingResult().getFieldErrors()){
            detalles.add(error.getField()+ ":" + error.getDefaultMessage());
        }

        ErrorDto errorDTO = new ErrorDto(
                "Error en la validacion de datos de entrada",
                detalles,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDto> manejarRecursoNoEncontrado(ResourceNotFoundException ex){

        List<String> detalles = new ArrayList<>();

        detalles.add(ex.getMessage());

        ErrorDto errorDto = new ErrorDto(
                "Recurso no encontrado",
                detalles,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<ErrorDto> manejarAccionesNoAutorizadas(UnauthorizedActionException ex){

        List<String> detalles = new ArrayList<>();

        detalles.add(ex.getMessage());

        ErrorDto errorDto = new ErrorDto(
                "Acción no autorizada",
                detalles,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorDto, HttpStatus.FORBIDDEN);

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDto> manejarDatosLoginIncorrectos(BadCredentialsException ex){

        List<String> detalles = new ArrayList<>();

        detalles.add(ex.getMessage());

        ErrorDto errorDto = new ErrorDto(
                "Credenciales incorrectas",
                detalles,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> manejarExcepcionGlobalGenerica(Exception ex){

        List<String> detalles = new ArrayList<>();

        detalles.add(ex.getLocalizedMessage());

        ErrorDto errorDto = new ErrorDto(
                "Ocurrio un error interno en el servidor",
                detalles,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);

    }

}
