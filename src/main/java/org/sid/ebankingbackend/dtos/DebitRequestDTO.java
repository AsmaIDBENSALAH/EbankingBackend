package org.sid.ebankingbackend.dtos;

import lombok.Data;

@Data
public class DebitRequestDTO {
    private String idAccount;
    private double amount;
    private String description;

}
