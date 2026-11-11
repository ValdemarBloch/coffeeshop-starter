package ek.osnb.demo.coffeeshop.assistant;


import jakarta.validation.Valid;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assistant")
class AssistantController {

    private final ChatClient chatClient;

    AssistantController(ChatClient.Builder builder, ChatMemory chatMemory) {
        this.chatClient = builder
                .defaultSystem("""
                        You are the assistant of a small coffee shop that sells coffee beans
                        and brewing equipment. Answer briefly and friendly.
                        Only answer questions about the shop, its products and the user's orders.
                        """)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @PostMapping("/chat")
    ChatReply chat(@Valid @RequestBody ChatRequest request, Authentication authentication) {
        String reply = chatClient.prompt()
                .user(request.message())
                // One conversation per user: the email is the conversation id
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, authentication.getName()))
                .call()
                .content();

        return new ChatReply(reply);
    }
}
