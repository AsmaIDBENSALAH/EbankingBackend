package org.sid.ebankingbackend.enteties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sid.ebankingbackend.enums.AccountStatus;

import java.util.Date;
import java.util.List;
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", length = 4)
@Data @NoArgsConstructor @AllArgsConstructor
@Entity
public class BankAccount {
    @Id
    private String id;
    private double balance;//solde
    private Date createdAt;
    @Enumerated(EnumType.STRING)//stocker sous forme de string parce que par defaut il est stocké sous forme de nombre 0 1 2 EnumType.ORDINAL
    private AccountStatus accountStatus;
    @ManyToOne//plusieurs bankAccount est pour un customer // relation bidirectionnel
    private Customer customer;
    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.LAZY) //  fetch = FetchType.EAGER pour chaque charge tout les operations de ce compte par defaut  lazy== il charge juste les autres attributs
    private List<AccountOperation> accountOperations;
}
