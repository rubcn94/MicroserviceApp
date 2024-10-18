package com.microcourse.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponseDto {

    private String apiPath;//donde/quien

    private HttpStatus errorCode;//porque

    private String errorMessage;//que

    private LocalDateTime errorTime;//cuando



}
