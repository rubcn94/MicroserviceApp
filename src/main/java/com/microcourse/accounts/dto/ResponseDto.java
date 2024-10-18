package com.microcourse.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor// no se generara si no tiene todos los args
public class ResponseDto {

    private String statusCode;

    private String statusMsg;
}
