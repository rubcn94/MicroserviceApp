package com.microcourse.accounts.service;

import com.microcourse.accounts.dto.CustomerDto;

public interface IAccountsService {    /**
 *
 * @param customerDto - CustomerDto Object
 */
void createAccount(CustomerDto customerDto);

/**
 *
 * @param mobileNumber - Customer Object
 * @return the new account details
 */
CustomerDto fetchAccount(String mobileNumber);

/**
 *
 * @param customerDto - CustomerDto Object
 * @return boolean indicating if the account was updated or not
 */
boolean updateAccount(CustomerDto customerDto);

/**
 *
 * @param mobileNumber - Customer Object
 * @return boolean indicating if the account was deleted or not
 */
boolean deleteAccount(String mobileNumber);
}
