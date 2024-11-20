package champollion;

import org.junit.jupiter.api.*;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class ChampollionJUnitTest {
	Enseignant untel;
	UE uml, java;

	Salle b101;
		
	@BeforeEach
	public void setUp() {
		untel = new Enseignant("untel", "untel@gmail.com");
		uml = new UE("UML");
		java = new UE("Programmation en java");
		b101 = new Salle("B101", 50);
	}
	

	@Test
	public void testNouvelEnseignantSansService() {
		assertEquals(0, untel.heuresPrevues(),
                        "Un nouvel enseignant doit avoir 0 heures prévues");
	}
	
	@Test
	public void testAjouteHeures() {
                // 10h TD pour UML
		untel.ajouteEnseignement(uml, 0, 10, 0);

		assertEquals(10, untel.heuresPrevuesPourUE(uml),
                        "L'enseignant doit maintenant avoir 10 heures prévues pour l'UE 'uml'");

		// 20h TD pour UML
        untel.ajouteEnseignement(uml, 0, 20, 0);
                
		assertEquals(10 + 20, untel.heuresPrevuesPourUE(uml),
                         "L'enseignant doit maintenant avoir 30 heures prévues pour l'UE 'uml'");		
		
	}

	@Test
	public void testHeuresPrevues() {
		untel.ajouteEnseignement(uml, 0, 10, 0);
		untel.ajouteEnseignement(java, 0, 20, 0);

		assertEquals(10 + 20, untel.heuresPrevues(),
						"L'enseignant doit maintenant avoir 30 heures prévues");
	}

	@Test
	public void testHeuresPrevuesPourUEInconnue() {
		assertEquals(0, untel.heuresPrevuesPourUE(uml),
						"L'enseignant doit avoir 0 heures prévues pour l'UE 'uml'");
	}

	@Test
	public void testHeuresPrevuesPourUE() {
		untel.ajouteEnseignement(uml, 0, 10, 0);
		assertEquals(10, untel.heuresPrevuesPourUE(uml),
						"L'enseignant doit avoir 10 heures prévues pour l'UE 'uml'");
	}

	@Test
	public void testHeuresPrevuesPourUEAvecCM() {
		untel.ajouteEnseignement(uml, 10, 10, 0);
		assertEquals(10 * 1.5 + 10 + 0, untel.heuresPrevuesPourUE(uml),
						"L'enseignant doit avoir 25 heures prévues pour l'UE 'uml'");
	}

	@Test
	public void testHeuresPrevuesPourUEAvecTP() {
		untel.ajouteEnseignement(uml, 0, 0, 10);
		assertEquals(0 + 0 + 10 * 0.75, untel.heuresPrevuesPourUE(uml),
						"L'enseignant doit avoir 7.5 heures prévues pour l'UE 'uml'");
	}

	@Test
	public void testHeuresPrevuesPourUEAvecCMTP() {
		untel.ajouteEnseignement(uml, 10, 0, 10);
		assertEquals(10 * 1.5 + 0 + 10 * 0.75, untel.heuresPrevuesPourUE(uml),
						"L'enseignant doit avoir 22.5 heures prévues pour l'UE 'uml'");
	}

	@Test
	public void testHeuresPrevuesPourUEAvecCMTPTD() {
		untel.ajouteEnseignement(uml, 10, 10, 10);
		assertEquals(10 * 1.5 + 10 + 10 * 0.75, untel.heuresPrevuesPourUE(uml),
						"L'enseignant doit avoir 35 heures prévues pour l'UE 'uml'");
	}

	@Test
	public void testHeuresPrevuesPourDifferentsUE() {
		untel.ajouteEnseignement(uml, 10, 10, 10);
		untel.ajouteEnseignement(java, 20, 20, 20);
		assertEquals(10 * 1.5 + 10 + 10 * 0.75 + 20 * 1.5 + 20 + 20 * 0.75, untel.heuresPrevues(),
						"L'enseignant doit avoir 105 heures prévues");
	}

	@Test
	public void testAjoutIntervention() {
		try {
			untel.ajouteEnseignement(uml, 0, 10, 0);
			Intervention intervention = new Intervention(TypeIntervention.TD, null, 10, b101, 0, untel, uml);
			untel.ajouteIntervention(intervention);

			assertTrue(untel.getInterventionsPlanifiees(uml).contains(intervention),
					"L'intervention ajoutée doit être dans la liste des interventions de l'enseignant");
		} catch (Exception e) {
			fail("Exception inattendue: " + e.getMessage());
		}
	}

	@Test
	public void testAjoutInterventionDepassementCM() {
		try {
			untel.ajouteEnseignement(uml, 10, 0, 0);
			Intervention intervention = new Intervention(TypeIntervention.CM, null, 10 + 5, b101, 0, untel, uml);
			untel.ajouteIntervention(intervention);

			fail("L'ajout d'une intervention de type CM doit lever une exception");
		} catch (Exception e) {
			assertEquals("Trop d'heures de CM planifiées pour cette UE", e.getMessage(),
					"Le message de l'exception doit être 'Trop d'heures de CM planifiées pour cette UE'");
		}
	}

	@Test
	public void testAjoutInterventionDepassementTD() {
		try {
			untel.ajouteEnseignement(uml, 0, 10, 0);
			Intervention intervention = new Intervention(TypeIntervention.TD, null, 10 + 5, b101, 0, untel, uml);
			untel.ajouteIntervention(intervention);

			fail("L'ajout d'une intervention de type TD doit lever une exception");
		} catch (Exception e) {
			assertEquals("Trop d'heures de TD planifiées pour cette UE", e.getMessage(),
					"Le message de l'exception doit être 'Trop d'heures de TD planifiées pour cette UE'");
		}
	}

	@Test
	public void testAjoutInterventionDepassementTP() {
		try {
			untel.ajouteEnseignement(uml, 0, 0, 10);
			Intervention intervention = new Intervention(TypeIntervention.TP, null, 10 + 5, b101, 0, untel, uml);
			untel.ajouteIntervention(intervention);

			fail("L'ajout d'une intervention de type TP doit lever une exception");
		} catch (Exception e) {
			assertEquals("Trop d'heures de TP planifiées pour cette UE", e.getMessage(),
					"Le message de l'exception doit être 'Trop d'heures de TP planifiées pour cette UE'");
		}
	}

	@Test
	public void testResteAPlanifier() {
		try {
			untel.ajouteEnseignement(uml, 20, 20, 20);
			untel.ajouteIntervention(new Intervention(TypeIntervention.CM, null, 10, b101, 0, untel, uml));
			untel.ajouteIntervention(new Intervention(TypeIntervention.TD, null, 10, b101, 0, untel, uml));
			untel.ajouteIntervention(new Intervention(TypeIntervention.TP, null, 10, b101, 0, untel, uml));

			assertEquals(20 - 10, untel.resteAPlanifier(uml, TypeIntervention.CM),
					"L'enseignant doit avoir 10 heures à planifier en CM UML");
			assertEquals(20 - 10, untel.resteAPlanifier(uml, TypeIntervention.TD),
					"L'enseignant doit avoir 10 heures à planifier en TD UML");
			assertEquals(20 - 10, untel.resteAPlanifier(uml, TypeIntervention.TP),
					"L'enseignant doit avoir 10 heures à planifier en TP UML");
		} catch (Exception e) {
			fail("Exception inattendue: " + e.getMessage());
		}
	}

	@Test
	public void testResteAPlanifierUENonEnseignee() {
		assertEquals(0, untel.resteAPlanifier(uml, TypeIntervention.CM),
				"L'enseignant ne doit pas avoir d'heures à planifier pour une UE non enseignée");
	}

	@Test
	public void testEstEnSousService() {
		try {
			untel.ajouteEnseignement(uml, 20, 20, 20);
			untel.ajouteIntervention(new Intervention(TypeIntervention.CM, null, 10, b101, 0, untel, uml));
			untel.ajouteIntervention(new Intervention(TypeIntervention.TD, null, 10, b101, 0, untel, uml));
			untel.ajouteIntervention(new Intervention(TypeIntervention.TP, null, 10, b101, 0, untel, uml));

			assertTrue(untel.enSousService(),
					"L'enseignant doit être en sous-service");
		} catch (Exception e) {
			fail("Exception inattendue: " + e.getMessage());
		}
	}

	@Test
	public void testPasEnSousService() {
		try {
			untel.ajouteEnseignement(uml, 100, 100, 100);
			untel.ajouteIntervention(new Intervention(TypeIntervention.CM, null, 100, null, 0, untel, uml));
			untel.ajouteIntervention(new Intervention(TypeIntervention.TD, null, 100, null, 0, untel, uml));
			untel.ajouteIntervention(new Intervention(TypeIntervention.TP, null, 100, null, 0, untel, uml));

			assertFalse(untel.enSousService(),
					"L'enseignant ne doit pas être en sous-service");
		} catch (Exception e) {
			fail("Exception inattendue: " + e.getMessage());
		}
	}
}
