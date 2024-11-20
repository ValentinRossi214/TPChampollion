
package champollion;

import lombok.Getter;

/**
 *  Une Personne    , classe abstraite, sera spécifiée par des classes concrètes comme Étudiant ou Enseignant
 */
@Getter
public abstract class Personne {
    private final String nom;
    private final String email;

    /**
     * Constructeur de la classe Personne.
     * @param nom le nom de la personne
     * @param email l'adresse email de la personne
     */
    protected Personne(String nom, String email) {
        this.nom = nom;
        this.email = email;
    }
}
