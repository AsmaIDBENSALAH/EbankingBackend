package org.sid.ebankingbackend.dtos;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.sid.ebankingbackend.enteties.Customer;
import org.sid.ebankingbackend.enums.AccountStatus;

import java.util.Date;
@Data
public class BankAccountDTO {

   private String id;
   private String type;
}
