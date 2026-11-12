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
    private final CatalogTools catalogTools;
    private final StaffTools staffTools;

    AssistantController(ChatClient.Builder builder, ChatMemory chatMemory, CatalogTools catalogTools,
                        StaffTools staffTools) {

        this.catalogTools = catalogTools;
        this.staffTools = staffTools;

        this.chatClient = builder
                .defaultSystem("""
                        You are the assistant of a small coffee shop that sells coffee beans
                        and brewing equipment. Answer briefly and friendly.
                        Only answer questions about the shop, its products and the user's orders.
                        
                        Always use the tools to look up products, prices and stock.
                        Never make up products, prices, stock or orders.
                        If a tool finds nothing, say that you could not find it.
                        You cannot change anything: you cannot place, cancel or change orders.
                        """)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();

    }

    @PostMapping("/chat")
    ChatReply chat(@Valid @RequestBody ChatRequest request, Authentication authentication) {
        String reply = chatClient.prompt()
                .user(request.message())
                .tools(toolsFor(authentication))
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, authentication.getName()))
                .call()
                .content();

        return new ChatReply(reply);
    }
    private Object [] toolsFor(Authentication authentication) {
        if (isStaff(authentication)) {
            return new Object[]{catalogTools, staffTools};
        }
        return new Object[]{catalogTools};

    }
    private boolean isStaff(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STAFF"));
    }
}
