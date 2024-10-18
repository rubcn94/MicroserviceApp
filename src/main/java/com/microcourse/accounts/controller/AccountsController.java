package com.microcourse.accounts.controller;

import com.microcourse.accounts.constants.AccountsConstants;
import com.microcourse.accounts.dto.CustomerDto;
import com.microcourse.accounts.dto.ResponseDto;
import com.microcourse.accounts.service.IAccountsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api", produces={MediaType.APPLICATION_JSON_VALUE})
@Validated
@AllArgsConstructor
// Apoyamos el tipo de datos de retorno como JSON.
// Ajustamos el marco de Spring Boot para que las respuestas de la API REST sean en formato JSON.
public class AccountsController {

    private IAccountsService iAccountsService;

    @PostMapping("/create")
    // Cualquiera que esté intentando invocar createAccount, necesita pasar la entrada con los datos del DTO (CustomerDto).
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {

        iAccountsService.createAccount(customerDto);
        // junto con el mensaje de éxito y el código de estado de la clase de constantes (AccountsConstants).
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }

    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto>fetchAccountDetails(@RequestParam
                                                              @Pattern(regexp = "(^$|[0-9]{10}$)", message = "Mobile number must be 10 digits")
                                                              String mobileNumber) {
            CustomerDto customerDto = iAccountsService.fetchAccount(mobileNumber);
            return ResponseEntity.status(HttpStatus.OK).body(customerDto);
        }
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccountDetails(@Valid @RequestBody CustomerDto customerDto) {
        boolean isUpdated = iAccountsService.updateAccount(customerDto);
        if(isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_UPDATE));
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccountDetails(@RequestParam
                                                            @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                            String mobileNumber) {
        boolean isDeleted = iAccountsService.deleteAccount(mobileNumber);
        if(isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_DELETE));
        }
    }
    }


