package com.test.iachat;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

import java.util.Scanner;

public class EndlessConsoleConversation {

	static String MODEL_NAME = "llama3.2"; // try other local ollama model names
	static String BASE_URL   = "http://localhost:11434"; // local ollama base url

	public static void main(String[] args) {
		ChatModel model = OllamaChatModel.builder()
				.baseUrl(BASE_URL)
				.modelName(MODEL_NAME)
				.build();

		// Création d’un scanner pour lire depuis la console
		Scanner scanner = new Scanner(System.in);

		while (true) {
			String line = scanner.nextLine();
			String answer = model.chat(line);
			System.out.println(answer);
		}

	}

}
