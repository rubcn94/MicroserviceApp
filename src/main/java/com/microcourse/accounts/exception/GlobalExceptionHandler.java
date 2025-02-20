package com.microcourse.accounts.exception;

import com.microcourse.accounts.dto.ErrorResponseDto; // DTO que encapsula la información del error
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus; // Enumeración para los códigos de estado HTTP
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity; // Clase que permite personalizar las respuestas HTTP
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice; // Anotación que convierte esta clase en un manejador global de excepciones
import org.springframework.web.bind.annotation.ExceptionHandler; // Anotación que permite manejar excepciones específicas
import org.springframework.web.context.request.WebRequest; // Proporciona información sobre la solicitud web que causó la excepción
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime; // Clase para manejar fechas y horas
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
// Esta anotación permite que esta clase maneje excepciones en toda la aplicación,
// sin necesidad de duplicar el manejo de errores en cada controlador.
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    // Este método maneja todas las excepciones no controladas (excepciones genéricas) que no tienen un manejador específico.
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception exception,
                                                                  WebRequest webRequest) {
        // Creamos un objeto `ErrorResponseDto` que encapsula los detalles del error.
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false), // Descripción de la solicitud que provocó el error.
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage(),
                LocalDateTime.now()
        );

        // Devolvemos una respuesta con el objeto `ErrorResponseDto` y el estado HTTP 500.
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    // Este método sobrescribe el manejo de excepciones cuando los argumentos de los métodos no son válidos.
    // Esto se utiliza principalmente en validaciones, por ejemplo, cuando los datos de entrada no cumplen con las restricciones definidas (como @NotNull, @Size, etc.).
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        // Creamos un mapa para almacenar los errores de validación, donde la clave será el nombre del campo que falló
        // y el valor será el mensaje de validación.
        Map<String, String> validationErrors = new HashMap<>();

        // Obtenemos la lista de errores de validación del objeto `BindingResult` contenido en la excepción.
        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

        // Iteramos a través de la lista de errores y extraemos el nombre del campo y el mensaje de validación.
        validationErrorList.forEach((error) -> {
            String fieldName = ((FieldError) error).getField(); // Nombre del campo que falló la validación.
            String validationMsg = error.getDefaultMessage();   // Mensaje de error asociado a esa validación.
            validationErrors.put(fieldName, validationMsg);     // Guardamos el error en el mapa.
        });

        // Devolvemos una respuesta con el mapa de errores de validación y el estado HTTP 400 (Bad Request).
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    // Este método maneja específicamente la excepción.
    // Cuando se lanza esta excepción en cualquier parte de la aplicación, este método será invocado.
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                                 WebRequest webRequest) {
        // Se crea un objeto ErrorResponseDto que encapsula toda la información del error.
        // Este objeto será enviado como respuesta al cliente que hizo la solicitud.

        // Parámetros de ErrorResponseDto:
        // 1. `webRequest.getDescription(false)` - Proporciona la descripción de la solicitud que causó el error,
        //    sin incluir detalles de los parámetros (debido al valor `false`).
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false), // Descripción de la solicitud
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                LocalDateTime.now()
        );

        // Devuelve un objeto ResponseEntity que encapsula el objeto ErrorResponseDto y el código de estado HTTP.
        // El cliente recibirá esta respuesta con la información del error y el código de estado.
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(CustomerAlreadyExistsException.class)

    public ResponseEntity<ErrorResponseDto> handleCustomerAlreadyExistsException(CustomerAlreadyExistsException exception,
                                                                                 WebRequest webRequest) {
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }
}
