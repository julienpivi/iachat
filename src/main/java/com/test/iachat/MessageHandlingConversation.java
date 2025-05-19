package com.test.iachat;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

public class MessageHandlingConversation {

	static String MODEL_NAME = "llama3.2"; // try other local ollama model names
	static String BASE_URL   = "http://localhost:11434"; // local ollama base url

	public static void main(String[] args) {
		ChatModel model = OllamaChatModel.builder()
				.baseUrl(BASE_URL)
				.modelName(MODEL_NAME)
				.build();
		//Little conversation
		UserMessage firstUserMessage = UserMessage.from("Hello, my name is Klaus");
		System.out.println(firstUserMessage);
		AiMessage firstAiMessage = model.chat(firstUserMessage).aiMessage(); // Hi Klaus, how can I help you?
		System.out.println(firstAiMessage.toString());

		UserMessage secondUserMessage = UserMessage.from("What is my name?");
		System.out.println(secondUserMessage);
		AiMessage secondAiMessage = model.chat(firstUserMessage, firstAiMessage, secondUserMessage).aiMessage(); // Klaus
		System.out.println(secondAiMessage.toString());

	}

}
