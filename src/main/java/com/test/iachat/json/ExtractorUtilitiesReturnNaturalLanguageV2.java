package com.test.iachat.json;

import com.fasterxml.jackson.databind.JsonNode;
import dev.langchain4j.data.document.Document;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExtractorUtilitiesReturnNaturalLanguageV2 {

	private static DateTimeFormatter dateFormat =  DateTimeFormatter.ofPattern("d MMMM uuuu", Locale.ENGLISH);

	public static List<Document> extractHospital(JsonNode root) {
		List<Document> documents = new ArrayList<>();
		String numberOfHospitalContent = "we have "+root.size()+" hospitals";
		documents.add(Document.from(numberOfHospitalContent));
		int index = 1;
		for (JsonNode item : root.get("hopitaux")) {
			String idHospital = item.get("id").asText();
			String hopitalContent = "hospital number "+index+" is called "+idHospital;
			documents.add(Document.from(hopitalContent));
			index++;
		}
		return documents;
	}

	public static List<Document> extractSonde(JsonNode root) {
		List<Document> documents = new ArrayList<>();
		String probeNumber = "we have "+root.size()+" probes";
		documents.add(Document.from(probeNumber));
		int index = 1;
		for (JsonNode item : root.get("sondes")) {
			String id = item.get("id").asText();
			String echographeId = item.get("echographeId").asText();
			String type = item.get("type").asText();

			String dateMiseEnService = item.get("dateMiseEnService").asText();
			LocalDate date = LocalDate.parse(dateMiseEnService);

			String rapportControleQualite = item.get("rapportControleQualite").asText();

			String content = "probe number"+index+" is called "+id
					+ ", linked to ultra sound scanner named "+ echographeId
					+ ", its type is " + type
					+ ", start up the " +  date.format(dateFormat)
					+ ", its control quality is  " + rapportControleQualite;

			documents.add(Document.from(content));
			index++;
		}
		return documents;
	}

	public static List<Document> extractUtilisations(JsonNode root) {
		List<Document> documents = new ArrayList<>();
		String probeNumber = "we have "+root.size()+" uses";
		documents.add(Document.from(probeNumber));
		int index = 1;
		for (JsonNode item : root.get("utilisations")) {
			String echographeId = item.get("echographeId").asText();
			int heuresTotalesUtilisation = item.get("heuresTotalesUtilisation").asInt();

			String content = "use number "+index
					+ "for the ultra sound scanner named " + echographeId
					+ ", has been used during " + heuresTotalesUtilisation + "hours.";

			documents.add(Document.from(content));
			index++;
		}
		return documents;
	}

	public static List<Document> extractExamens(JsonNode root) {
		List<Document> documents = new ArrayList<>();

		String count = "we have "+root.size()+" examinations";
		documents.add(Document.from(count));

		int index = 1;
		for (JsonNode item : root.get("examens")) {
			String id = item.get("id").asText();
			String echographeId = item.get("echographeId").asText();
			String medecinId = item.get("medecinId").asText();
			String application = item.get("application").asText();
			int dureeTotaleMinutes = item.get("dureeTotaleMinutes").asInt();
			int dureeEffectiveEchoMinutes = item.get("dureeEffectiveEchoMinutes").asInt();
			String dateExamen = item.get("dateExamen").asText();
			LocalDate date = LocalDate.parse(dateExamen);

			String content = "Examination number "+index+" is called "+id
					+ ", has been done with the ultra sound scanner named " + echographeId
					+ ", has been made by the doctor named " + medecinId
					+ ", in the " + application +" context"
					+ ", has lasted " + dureeTotaleMinutes + " minutes"
					+ ", has lasted with probes " + dureeEffectiveEchoMinutes + " minutes"
					+ ", at the date of " + date.format(dateFormat);

			System.out.println(content);
			documents.add(Document.from(content));
			index++;
		}
		return documents;
	}



	public static List<Document> extractSonographe(JsonNode root) {

		List<Document> documents = new ArrayList<>();

		String count = "we have "+root.size()+" ultra sound scanners";
		documents.add(Document.from(count));

		int index = 1;
		for (JsonNode item : root.get("echographes")) {
			String id = item.get("id").asText();
			String systemId = item.get("systemId").asText();
			String numeroSerie = item.get("numeroSerie").asText();
			String versionLogicielle = item.get("versionLogicielle").asText();
			String optionsDisponibles = item.get("optionsDisponibles").asText();
			String dateMiseEnService = item.get("dateMiseEnService").asText();
			LocalDate date = LocalDate.parse(dateMiseEnService);

			String parametresReseaux = item.get("parametresReseaux").asText();
			int nbExtinctions = item.get("nbExtinctions").asInt();

			String hopitalId = item.get("hopitalId").asText();

			String content = "The ultra sound scanner number "+index
					+ " is called " + id
					+ ", has the system id " + systemId
					+ ", has the series number " + numeroSerie
					+ ", is in the software version number " + versionLogicielle
					+ ", is equiped with : " + optionsDisponibles
					+ ", start-up date " + date.format(dateFormat)
					+ ", network parameters " + parametresReseaux
					+ ", has been shutdown "+ nbExtinctions+" times"
					+ ", is in the hospital named " + hopitalId;

			System.out.println(content);
			documents.add(Document.from(content));
			index++;
		}
		return documents;
	}

	public static List<Document> extractDoctor(JsonNode root) {
		List<Document> documents = new ArrayList<>();

		String count = "we have "+root.size()+" doctors";
		documents.add(Document.from(count));

		int index = 1;
		for (JsonNode item : root.get("medecins")) {
			String id = item.get("id").asText();
			String specialite = item.get("specialite").asText();
			String hopitalId = item.get("hopitalId").asText();

			String medecinContent = "Doctor number "+index
					+ " named " + id
					+ ", has for speciality " + specialite
					+ ", in the hospital named " + hopitalId;

			documents.add(Document.from(medecinContent));
		}
		return documents;
	}


}
