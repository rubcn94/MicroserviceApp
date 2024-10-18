package com.microcourse.accounts.service.impl;

import com.microcourse.accounts.constants.AccountsConstants;
import com.microcourse.accounts.dto.AccountsDto;
import com.microcourse.accounts.dto.CustomerDto;
import com.microcourse.accounts.entity.Accounts;
import com.microcourse.accounts.entity.Customer;
import com.microcourse.accounts.exception.ResourceNotFoundException;
import com.microcourse.accounts.mapper.AccountsMapper;
import com.microcourse.accounts.mapper.CustomerMapper;
import com.microcourse.accounts.repository.AccountsRepository;
import com.microcourse.accounts.repository.CustomerRepository;
import com.microcourse.accounts.service.IAccountsService;
import com.microcourse.accounts.exception.CustomerAlreadyExistsException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements IAccountsService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    /**
     * @param customerDto - CustomerDto Object
     */
    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        /*adicional*/ Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if(optionalCustomer.isPresent()) {
                    throw new CustomerAlreadyExistsException("Customer already registred with mobile number "
                            + customerDto.getMobileNumber());

        }
        Customer savedCustomer = customerRepository.save(customer);
        accountsRepository.save(createNewAccount(savedCustomer));
    }


    /**
     * @param customer - Customer Object
     * @return the new account details
     */
    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);

        return newAccount;
    }
    /**
     * @param mobileNumber - Input Mobile Number
     * @return Accounts Details based on a given mobileNumber
     */
    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));
        return customerDto;
    }

    /**
     * Actualiza los detalles de la cuenta y el cliente relacionados.
     *
     * @param customerDto - Objeto CustomerDto que contiene los datos actualizados del cliente y la cuenta.
     * @return booleano que indica si la actualización de los detalles de la cuenta fue exitosa o no.
     */
    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        // Inicializamos una bandera para indicar si la actualización fue exitosa.
        boolean isUpdated = false;

        // Obtenemos los detalles de la cuenta asociados al cliente a través de CustomerDto.
        AccountsDto accountsDto = customerDto.getAccountsDto();

        // Si los detalles de la cuenta no son nulos, procedemos con la actualización.
        if (accountsDto != null) {
            // Intentamos encontrar la cuenta en la base de datos usando el número de cuenta proporcionado en accountsDto.
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    // Si no se encuentra la cuenta, lanzamos una excepción personalizada indicando que el recurso no fue encontrado.
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
            );

            // Usamos el mapeador para copiar los datos actualizados del DTO a la entidad Accounts.
            AccountsMapper.mapToAccounts(accountsDto, accounts);

            // Guardamos los cambios en la entidad Accounts en la base de datos.
            accounts = accountsRepository.save(accounts);

            // Una vez que se ha actualizado la cuenta, obtenemos el ID del cliente asociado.
            Long customerId = accounts.getCustomerId();

            // Buscamos el cliente en la base de datos usando el customerId.
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    // Si no se encuentra el cliente, lanzamos una excepción personalizada indicando que el recurso no fue encontrado.
                    () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
            );

            // Usamos el mapeador para actualizar los datos del cliente desde CustomerDto a la entidad Customer.
            CustomerMapper.mapToCustomer(customerDto, customer);

            // Guardamos los cambios en la entidad Customer en la base de datos.
            customerRepository.save(customer);

            // Si todo el proceso se realizó correctamente, cambiamos el valor de isUpdated a true.
            isUpdated = true;
        }

        // Devolvemos el resultado de la operación de actualización.
        return isUpdated;
    }

    /**
     * Elimina una cuenta y su cliente asociado utilizando el número de móvil.
     *
     * @param mobileNumber - Número de móvil proporcionado para identificar al cliente y su cuenta.
     * @return booleano que indica si la eliminación de los detalles de la cuenta fue exitosa o no.
     */
    @Override
    public boolean deleteAccount(String mobileNumber) {
        // Busca al cliente en la base de datos usando el número de móvil proporcionado.
        // Si no se encuentra, lanza una excepción personalizada `ResourceNotFoundException`.
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                // Excepción que indica que no se encontró el cliente con el número de móvil proporcionado.
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );

        // Elimina todas las cuentas asociadas al cliente, usando el `customerId` del cliente encontrado.
        accountsRepository.deleteByCustomerId(customer.getCustomerId());

        // Elimina el registro del cliente de la base de datos utilizando el `customerId`.
        customerRepository.deleteById(customer.getCustomerId());

        // Devuelve `true` para indicar que la operación de eliminación fue exitosa.
        return true;
    }


}