package org.sid.ebankingbackend.enteties;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;


    @OneToMany(mappedBy = "customer")
    // dans la classe bankAccount il ya un attribut qui s'appelle customer // creer une seule cle etranger
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    //jackson == api qui fait la serialisation Json == convertir les objets en format json
    // dire a jackson d'ingorer la serialisation de cet element car on a un prbm pendant l'affichage
    // == retourne tout les compte et pour chaque compte retourne le customer correspond une boucle infinie
    // il faut passer par dto pour eviter de modifier a chque fois les entites ==ce sont les bonnes pratiques
    // **** un exemple de dto(data transfer object) : j'ai une entitee qui une dizaine d'attributs alors que j'ai besoin juste d'afficher les 5 premiers
    // la solution est donc utilisé les dtos pour retourner seulement les attributs dont en a besoin ****
    // **** mapper c'est une couche qui permet de transferer un objet a un autre objet par exemple objet entité vers dto
    private List<BankAccount> bankAccounts;
}
