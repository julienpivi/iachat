package com.test.iachat.json;

import com.fasterxml.jackson.databind.JsonNode;
import dev.langchain4j.data.document.Document;

import java.util.ArrayList;
import java.util.List;

public class ExtractorUtilities {


	public static List<Document> extractHospital(JsonNode root) {
		List<Document> documents = new ArrayList<>();
		for (JsonNode item : root.get("hopitaux")) {
			String idHospital = item.get("id").asText();
			String nameHospital  = item.get("nom").asText();
			String hopitalContent = "Hopital à controller : \n hopitalId: " + idHospital + "\n Nom hopital: "+ nameHospital;
			documents.add(Document.from(hopitalContent));
		}
		return documents;
	}

	public static List<Document> extractSonde(JsonNode root) {
		List<Document> documents = new ArrayList<>();
		for (JsonNode item : root.get("sondes")) {
			String id = item.get("id").asText();
			String echographeId = item.get("echographeId").asText();
			String type = item.get("type").asText();
			String dateMiseEnService = item.get("dateMiseEnService").asText();
			String rapportControleQualite = item.get("rapportControleQualite").asText();

			String content = "Sonde à contrôler :\n"
					+ "id: " + id + "\n"
					+ "echographeId: " + echographeId + "\n"
					+ "type: " + type + "\n"
					+ "dateMiseEnService: " + dateMiseEnService + "\n"
					+ "rapportControleQualite: " + rapportControleQualite;

			documents.add(Document.from(content));
		}
		return documents;
	}

	public static List<Document> extractUtilisations(JsonNode root) {
		List<Document> documents = new ArrayList<>();
		for (JsonNode item : root.get("utilisations")) {
			String id = item.get("id").asText();
			String echographeId = item.get("echographeId").asText();
			int heuresTotalesUtilisation = item.get("heuresTotalesUtilisation").asInt();
			int heuresUtilisationParSonde = item.get("heuresUtilisationParSonde").asInt();

			String content = "Utilisation à contrôler :\n"
					+ "id: " + id + "\n"
					+ "echographeId: " + echographeId + "\n"
					+ "heuresTotalesUtilisation: " + heuresTotalesUtilisation + "\n"
					+ "heuresUtilisationParSonde: " + heuresUtilisationParSonde;

			documents.add(Document.from(content));
		}
		return documents;
	}

	public static List<Document> extractExamens(JsonNode root) {
		List<Document> documents = new ArrayList<>();
		for (JsonNode item : root.get("examens")) {
			String id = item.get("id").asText();
			String echographeId = item.get("echographeId").asText();
			String medecinId = item.get("medecinId").asText();
			String application = item.get("application").asText();
			int dureeTotaleMinutes = item.get("dureeTotaleMinutes").asInt();
			int dureeEffectiveEchoMinutes = item.get("dureeEffectiveEchoMinutes").asInt();
			String dateExamen = item.get("dateExamen").asText();

			String content = "Examen à contrôler :\n"
					+ "id: " + id + "\n"
					+ "echographeId: " + echographeId + "\n"
					+ "medecinId: " + medecinId + "\n"
					+ "application: " + application + "\n"
					+ "dureeTotaleMinutes: " + dureeTotaleMinutes + "\n"
					+ "dureeEffectiveEchoMinutes: " + dureeEffectiveEchoMinutes + "\n"
					+ "dateExamen: " + dateExamen;

			documents.add(Document.from(content));
		}
		return documents;
	}



	public static List<Document> extractSonographe(JsonNode root) {
		List<Document> documents = new ArrayList<>();
		for (JsonNode item : root.get("echographes")) {
			String id = item.get("id").asText();
			String designation = item.get("designation").asText();
			String systemId = item.get("systemId").asText();
			String numeroSerie = item.get("numeroSerie").asText();
			String versionLogicielle = item.get("versionLogicielle").asText();
			String optionsDisponibles = item.get("optionsDisponibles").asText();
			String dateMiseEnService = item.get("dateMiseEnService").asText();
			String parametresReseaux = item.get("parametresReseaux").asText();
			String diagnostic = item.get("diagnostic").asText();
			int tempsVeille = item.get("tempsVeille").asInt();
			int nbExtinctions = item.get("nbExtinctions").asInt();
			int heuresSurBatterie = item.get("heuresSurBatterie").asInt();
			String hopitalId = item.get("hopitalId").asText();

			String content = "Échographe à contrôler :\n"
					+ "id: " + id + "\n"
					+ "designation: " + designation + "\n"
					+ "systemId: " + systemId + "\n"
					+ "numeroSerie: " + numeroSerie + "\n"
					+ "versionLogicielle: " + versionLogicielle + "\n"
					+ "optionsDisponibles: " + optionsDisponibles + "\n"
					+ "dateMiseEnService: " + dateMiseEnService + "\n"
					+ "parametresReseaux: " + parametresReseaux + "\n"
					+ "diagnostic: " + diagnostic + "\n"
					+ "tempsVeille: " + tempsVeille + "\n"
					+ "nbExtinctions: " + nbExtinctions + "\n"
					+ "heuresSurBatterie: " + heuresSurBatterie + "\n"
					+ "hopitalId: " + hopitalId;

			documents.add(Document.from(content));
		}
		return documents;
	}

	public static List<Document> extractDoctor(JsonNode root) {
		List<Document> documents = new ArrayList<>();
		for (JsonNode item : root.get("medecins")) {
			String id = item.get("id").asText();
			String nom = item.get("nom").asText();
			String specialite = item.get("specialite").asText();
			String hopitalId = item.get("hopitalId").asText();

			String medecinContent = "Médecin à contrôler :\n"
					+ "id: " + id + "\n"
					+ "nom: " + nom + "\n"
					+ "specialite: " + specialite + "\n"
					+ "hopitalId: " + hopitalId;

			documents.add(Document.from(medecinContent));
		}
		return documents;
	}


}
