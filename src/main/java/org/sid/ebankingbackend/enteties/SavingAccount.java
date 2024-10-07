package org.sid.ebankingbackend.enteties;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@DiscriminatorValue("SA")
@Data @AllArgsConstructor @NoArgsConstructor
public class SavingAccount extends BankAccount{
    private double interestRate;// is the amount of interest due per period
}
/*héritage = une hiérarchie ==> 3 solution parce qu'il n'y a pas d'héritage il y'a juste des relation entre les tables =====> Mapping Héritage
 3 stratégies:

    1-Single Table == une seule table qui contient tout les attributs (id, Type, createdAt, balance, status, currency, overDraft, interestRate)
               ajouter une colonne type qui s'appelle "discriminateur column" pour indiquer est ce qu'il s'agit d'un compte courant CC ou compte epargne CE
    ==> performance eleve car il est simple de chercher dans une seul table
    ==> inconvenient pour chaque ligne il y'a une colonne qui ne sert a rien
    @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
    @DiscriminatorColumn(name = "TYPE", length = 4)
    pour les sous clase en ajoute les annotations suivants
    @Entity
    @DiscriminatorValue("SA")


    2-Table per class == chaque classe fille correspond a une table
    CurrentAcount (id, date, status, currency, overdraft, custID)
    SavingAcount (id, date, status, currency, rate, custID)
    ==> pas de colonne null
    ==> plusieurs tables // structures presque se repéte  date, status, currency, custID
    @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
     ajouter abstract pour ne pas creer la table de la classe mere

    3-Joined Table === creer 3 tables
    Account (id, date, status, currency, custID)
    CurrentAcount (AccId, overdraft)
    SavingAcount (AccId, rate)
    @Inheritance(strategy = InheritanceType.JOINED)

 */