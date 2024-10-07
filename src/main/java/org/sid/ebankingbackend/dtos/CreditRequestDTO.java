package org.sid.ebankingbackend.dtos;

import lombok.Data;

@Data
public class CreditRequestDTO {

        private String idAccount;
        private double amount;
        private String description;


}
