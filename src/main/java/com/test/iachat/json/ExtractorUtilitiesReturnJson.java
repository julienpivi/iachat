package com.test.iachat.json;

import com.fasterxml.jackson.databind.JsonNode;
import dev.langchain4j.data.document.Document;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExtractorUtilitiesReturnJson {
	private static DateTimeFormatter dateFormat =  DateTimeFormatter.ofPattern("d MMMM uuuu", Locale.ENGLISH);

	public static List<Document> extractHospital(JsonNode root) {
		List<Document> documents = new ArrayList<>();
		String numberOfHospitalContent = "we have "+root.size()+" hospitals";
		documents.add(Document.from(numberOfHospitalContent));
		for (JsonNode item : root.get("hopitaux")) {
			documents.add(Document.from(item.toPrettyString()));
		}
		return documents;
	}

	public static List<Document> extractSonde(JsonNode root) {
		List<Document> documents = new ArrayList<>();
		String probeNumber = "we have "+root.size()+" probes";
		documents.add(Document.from(probeNumber));
		for (JsonNode item : root.get("sondes")) {
			documents.add(Document.from(item.toPrettyString()));
		}
		return documents;
	}

	public static List<Document> extractUtilisations(JsonNode root) {
		List<Document> documents = new ArrayList<>();
		String probeNumber = "we have "+root.size()+" uses";
		documents.add(Document.from(probeNumber));
		for (JsonNode item : root.get("utilisations")) {
			documents.add(Document.from(item.toPrettyString()));
		}
		return documents;
	}

	public static List<Document> extractExamens(JsonNode root) {
		List<Document> documents = new ArrayList<>();

		String count = "we have "+root.size()+" examinations";
		documents.add(Document.from(count));

		for (JsonNode item : root.get("examens")) {
			documents.add(Document.from(item.toPrettyString()));
		}

		return documents;
	}



	public static List<Document> extractSonographe(JsonNode root) {

		List<Document> documents = new ArrayList<>();

		String count = "we have "+root.size()+" ultra sound scanners";
		documents.add(Document.from(count));

		for (JsonNode item : root.get("echographes")) {
			documents.add(Document.from(item.toPrettyString()));
		}

		return documents;
	}

	public static List<Document> extractDoctor(JsonNode root) {
		List<Document> documents = new ArrayList<>();

		String count = "we have "+root.size()+" doctors";
		documents.add(Document.from(count));

		for (JsonNode item : root.get("medecins")) {
			documents.add(Document.from(item.toPrettyString()));
		}

		return documents;
	}
}
