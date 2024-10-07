package org.sid.ebankingbackend.dtos;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.sid.ebankingbackend.enums.AccountStatus;

import java.util.Date;
import java.util.List;

@Data
public class SavingBankAccountDTO extends BankAccountDTO{


    private double balance;//solde
    private Date createdAt;

    private AccountStatus accountStatus;
    private CustomerDTO customerDTO;
    private double interestRate;

}
