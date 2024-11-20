package champollion;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.Date;

public class Intervention {
    @Getter
    private TypeIntervention type;
    @Getter
    private Date debut;
    @Getter
    private int duree;
    @Getter
    private Salle salle;
    private int heureDebut;
    @Getter
    private boolean annulee = false;
    @Getter
    private Enseignant enseignant;
    @Getter
    private UE ue;

    public Intervention(TypeIntervention type, Date debut, int duree, Salle salle, int heureDebut, Enseignant enseignant, UE ue) {
        this.type = type;
        this.debut = debut;
        this.duree = duree;
        this.salle = salle;
        this.heureDebut = heureDebut;
        this.enseignant = enseignant;
        this.ue = ue;
    }
}
