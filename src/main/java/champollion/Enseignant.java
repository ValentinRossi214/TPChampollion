package champollion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static champollion.TypeIntervention.*;

/**
 * Un enseignant est caractérisé par les informations suivantes : son nom, son adresse email, et son service prévu,
 * et son emploi du temps.
 */
public class Enseignant extends Personne {
    private Map<UE, ServicePrevu> enseignementsPrevus;
    private Map<UE, ArrayList<Intervention>> interventionsPlanifiees;
    public Enseignant(String nom, String email) {
        super(nom, email);
        enseignementsPrevus = new HashMap<>();
        interventionsPlanifiees = new HashMap<>();
    }

    public ArrayList<Intervention> getInterventionsPlanifiees(UE ue) {
        return interventionsPlanifiees.get(ue);
    }

    /**
     * Calcule le nombre total d'heures prévues pour cet enseignant en "heures équivalent TD" Pour le calcul : 1 heure
     * de cours magistral vaut 1,5 h "équivalent TD" 1 heure de TD vaut 1h "équivalent TD" 1 heure de TP vaut 0,75h
     * "équivalent TD"
     *
     * @return le nombre total d'heures "équivalent TD" prévues pour cet enseignant, arrondi à l'entier le plus proche
     *
     */
    public double heuresPrevues() {
        double totalHeuresPrevues = 0;

        for (UE ue : enseignementsPrevus.keySet()) {
            totalHeuresPrevues += heuresPrevuesPourUE(ue);
        }

        return totalHeuresPrevues;
    }

    /**
     * Calcule le nombre total d'heures prévues pour cet enseignant dans l'UE spécifiée en "heures équivalent TD" Pour
     * le calcul : 1 heure de cours magistral vaut 1,5 h "équivalent TD" 1 heure de TD vaut 1h "équivalent TD" 1 heure
     * de TP vaut 0,75h "équivalent TD"
     *
     * @param ue l'UE concernée
     * @return le nombre total d'heures "équivalent TD" prévues pour cet enseignant, arrondi à l'entier le plus proche
     *
     */
    public double heuresPrevuesPourUE(UE ue) {
        if(enseignementsPrevus.containsKey(ue)) {
            ServicePrevu servicePrevu = enseignementsPrevus.get(ue);
            return servicePrevu.getVolumeCM() * 1.5 + servicePrevu.getVolumeTD() + servicePrevu.getVolumeTP() * 0.75;
        } else {
            return 0;
        }
    }

    /**
     * Ajoute un enseignement au service prévu pour cet enseignant
     *
     * @param ue l'UE concernée
     * @param volumeCM le volume d'heures de cours magistral
     * @param volumeTD le volume d'heures de TD
     * @param volumeTP le volume d'heures de TP
     */
    public void ajouteEnseignement(UE ue, int volumeCM, int volumeTD, int volumeTP) {
        if (enseignementsPrevus.containsKey(ue)) {
            ServicePrevu servicePrevu = enseignementsPrevus.get(ue);
            servicePrevu.setVolumeCM(servicePrevu.getVolumeCM() + volumeCM);
            servicePrevu.setVolumeTD(servicePrevu.getVolumeTD() + volumeTD);
            servicePrevu.setVolumeTP(servicePrevu.getVolumeTP() + volumeTP);
        } else {
            enseignementsPrevus.put(ue, new ServicePrevu(volumeCM, volumeTD, volumeTP));
            interventionsPlanifiees.put(ue, new ArrayList<>());
        }
    }

    public void ajouteIntervention(Intervention intervention) throws Exception {
        if(intervention.getType() == CM) {
            if (intervention.getDuree() + heuresPlanifieesTypeInterventionPourUE(intervention.getUe(), CM) >
                enseignementsPrevus.get(intervention.getUe()).getVolumeCM()) {
                throw new Exception("Trop d'heures de CM planifiées pour cette UE");
            } else {
                interventionsPlanifiees.get(intervention.getUe()).add(intervention);
            }
        } else if(intervention.getType() == TD) {
            if (intervention.getDuree() + heuresPlanifieesTypeInterventionPourUE(intervention.getUe(), TD) >
                enseignementsPrevus.get(intervention.getUe()).getVolumeTD()) {
                throw new Exception("Trop d'heures de TD planifiées pour cette UE");
            } else {
            interventionsPlanifiees.get(intervention.getUe()).add(intervention);
            }
        } else {
            if (intervention.getDuree() + heuresPlanifieesTypeInterventionPourUE(intervention.getUe(), TP) >
                enseignementsPrevus.get(intervention.getUe()).getVolumeTP()) {
                throw new Exception("Trop d'heures de TP planifiées pour cette UE");
            } else {
                interventionsPlanifiees.get(intervention.getUe()).add(intervention);
            }
        }
    }

    public boolean enSousService() {
        return heuresPrevues() < 192;
    }

    public int resteAPlanifier(UE ue, TypeIntervention type) {
        if (!enseignementsPrevus.containsKey(ue)) {
            return 0;
        } else {
            if(type == CM) {
                return enseignementsPrevus.get(ue).getVolumeCM() - heuresPlanifieesTypeInterventionPourUE(ue, CM);
            } else if(type == TD) {
                return enseignementsPrevus.get(ue).getVolumeTD() - heuresPlanifieesTypeInterventionPourUE(ue, TD);
            } else {
                return enseignementsPrevus.get(ue).getVolumeTP() - heuresPlanifieesTypeInterventionPourUE(ue, TP);
            }
        }
    }

    private int heuresPlanifieesTypeInterventionPourUE(UE ue, TypeIntervention type) {
        int heuresPlanifiees = 0;

        for (Intervention intervention : interventionsPlanifiees.get(ue)) {
            if (intervention.getType() == type) {
                heuresPlanifiees += intervention.getDuree();
            }
        }

        return heuresPlanifiees;
    }
}
