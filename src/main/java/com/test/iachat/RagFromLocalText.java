package com.test.iachat;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.ClassPathDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.util.List;
import java.util.Scanner;

public class RagFromLocalText {
	static String MODEL_NAME = "llama3.2"; // try other local ollama model names
	static String BASE_URL   = "http://localhost:11434"; // local ollama base url

	public static void main(String[] args) throws IOException, URISyntaxException {
		ChatModel model = OllamaChatModel.builder()
				.baseUrl(BASE_URL)
				.modelName(MODEL_NAME)
				.build();

		/**
		 * rag with local documents with an original story
		 */

		PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:*.txt");
		List<Document> documents =
				ClassPathDocumentLoader.loadDocuments("samples", pathMatcher);

		InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
		EmbeddingStoreIngestor.ingest(documents, embeddingStore);

		Assistant assistant = AiServices.builder(Assistant.class)
				.chatModel(model)
				.chatMemory(MessageWindowChatMemory.withMaxMessages(10))
				.contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
				.build();

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
