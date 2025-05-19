package com.test.iachat;

import ai.djl.util.ClassLoaderUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.iachat.json.ExtractorUtilities;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.model.output.Response;
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

public class RagFromLocalJSON {
	static String MODEL_NAME = "llama3.2"; // try other local ollama model names
	static String BASE_URL   = "http://localhost:11434"; // local ollama base url

	public static void main(String[] args) throws IOException, URISyntaxException {

		/**
		 * rag with a structured file like a JSON.
		 */


		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(
				new File(ClassLoaderUtils.getResource("samples/echographes-structured.json").toURI()));

		List<Document> documents = new ArrayList<>();

		documents.addAll(ExtractorUtilities.extractHospital(root));
		documents.addAll(ExtractorUtilities.extractDoctor(root));
		documents.addAll(ExtractorUtilities.extractSonographe(root));
		documents.addAll(ExtractorUtilities.extractSonde(root));
		documents.addAll(ExtractorUtilities.extractExamens(root));
		documents.addAll(ExtractorUtilities.extractUtilisations(root));


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
				.build();

		ChatModel model = OllamaChatModel.builder()
				.baseUrl(BASE_URL)
				.modelName(MODEL_NAME)
				.build();

		Assistant assistant = AiServices.builder(Assistant.class)
				.chatModel(model)
				.chatMemory(MessageWindowChatMemory.withMaxMessages(10))
				.contentRetriever(retriever)
				.build();

		String startingMessage = "Es-tu prêt ?";
		System.out.println(startingMessage);
		String response = assistant.chat(startingMessage);
		System.out.println(response);

		Scanner scanner = new Scanner(System.in);

		while (true) {
			String line = scanner.nextLine();
			String answer = assistant.chat(line);
			System.out.println(answer);
		}

	}

	interface Assistant {
		String chat(String userMessage);
	}
}
