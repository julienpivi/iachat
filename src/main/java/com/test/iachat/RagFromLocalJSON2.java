package com.test.iachat;

import ai.djl.util.ClassLoaderUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.iachat.json.ExtractorUtilitiesReturnJson;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.injector.DefaultContentInjector;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RagFromLocalJSON2 {
	static String MODEL_NAME = "llama3.2"; // try other local ollama model names
	static String BASE_URL   = "http://localhost:11434"; // local ollama base url

	public static void main(String[] args) throws IOException, URISyntaxException {

		/**
		 * rag with a structured file like a JSON.
		 */
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(
				new File(ClassLoaderUtils.getResource("samples/echographes-structured2.json").toURI()));

		List<Document> documents = new ArrayList<>();

		documents.add(Document.from("you're here to help our ultrasound scanner fleet."));
		documents.add(Document.from("ultrasound scanner fleet management is our business."));

		//Normalisation + chunkisation
		documents.addAll(ExtractorUtilitiesReturnJson.extractHospital(root));
		documents.addAll(ExtractorUtilitiesReturnJson.extractDoctor(root));
		documents.addAll(ExtractorUtilitiesReturnJson.extractSonographe(root));
		documents.addAll(ExtractorUtilitiesReturnJson.extractSonde(root));
		documents.addAll(ExtractorUtilitiesReturnJson.extractExamens(root));
		documents.addAll(ExtractorUtilitiesReturnJson.extractUtilisations(root));


		//Vectorisation
		EmbeddingModel embeddingModel = OllamaEmbeddingModel.builder()
				.baseUrl(BASE_URL)
				.modelName(MODEL_NAME)
				.build();

		InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

		for (Document document : documents) {
			TextSegment textSegment = document.toTextSegment();
			Response<Embedding> embed = embeddingModel.embed(textSegment);
			embeddingStore.add(embed.content(), textSegment);
		}


		ContentRetriever retriever = EmbeddingStoreContentRetriever.builder()
				.embeddingStore(embeddingStore)          // votre InMemoryEmbeddingStore<TextSegment>
				.embeddingModel(embeddingModel)          // l’instance que vous avez créée plus haut
				.minScore(0.8) // Seuil de similarité minimale
				.build();

		ChatModel model = OllamaChatModel.builder()
				.baseUrl(BASE_URL)
				.modelName(MODEL_NAME)
				.build();

		RetrievalAugmentor retrievalAugmentor = defineAugmentatorTorestricFromOurDatabase(retriever);

		Assistant assistant = AiServices.builder(Assistant.class)
				.chatModel(model)
				//.chatMemory(MessageWindowChatMemory.withMaxMessages(10))
				.retrievalAugmentor(retrievalAugmentor)
				.build();


		//Renforcement
		String startingMessage = "Are you ready? Answer using only the information provided.";
		assistant.chat(startingMessage);

		String intialMessage = "Que voulez vous savoir sur votre flotte d'échographe ?";
		System.out.println(intialMessage);
		Scanner scanner = new Scanner(System.in);

		while (true) {
			String line = scanner.nextLine();
			String answer = assistant.chat(line);
			System.out.println(answer);
		}

	}

	private static RetrievalAugmentor defineAugmentatorTorestricFromOurDatabase(ContentRetriever retriever) {
		return  DefaultRetrievalAugmentor.builder()
				.contentRetriever(retriever)
				.contentInjector(DefaultContentInjector.builder()
						.promptTemplate(

								PromptTemplate.from(
										"{{userMessage}}\n\n" +
												"Please answer **only** from the following information :\n" +
												"{{contents}}"
								)
						).build())
				.build();
	}

	interface Assistant {
		String chat(String userMessage);
	}
}
