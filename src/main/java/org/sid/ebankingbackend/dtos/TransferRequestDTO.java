package org.sid.ebankingbackend.dtos;

import lombok.Data;

@Data
public class TransferRequestDTO {
    private String idAccountSource;
    private double amount;
    private String idAccountDestination;

    // Getters and setters
}

