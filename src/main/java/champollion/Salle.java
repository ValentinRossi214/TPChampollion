package champollion;

import lombok.Getter;

@Getter
public class Salle {
    private String intitule;
    private int capacite;

    public Salle(String intitule, int capacite) {
        this.intitule = intitule;
        this.capacite = capacite;
    }
}
