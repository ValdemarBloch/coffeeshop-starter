package ek.osnb.demo.coffeeshop.assistant;


import ek.osnb.demo.coffeeshop.customer.CustomerApi;
import ek.osnb.demo.coffeeshop.customer.CustomerInfo;
import jakarta.validation.Valid;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/assistant")
class AssistantController {

    private final ChatClient chatClient;
    private final CatalogTools catalogTools;
    private final StaffTools staffTools;
    private final OrderTools orderTools;
    private final CustomerApi customerApi;

    AssistantController(ChatClient.Builder builder, ChatMemory chatMemory, CatalogTools catalogTools,
                        StaffTools staffTools, OrderTools orderTools, CustomerApi customerApi) {

        this.catalogTools = catalogTools;
        this.staffTools = staffTools;
        this.orderTools = orderTools;
        this.customerApi = customerApi;

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
        Long customerId = findCustomerId(authentication);

        var prompt = chatClient.prompt()
                .user(request.message())
                .tools(toolsFor(authentication, customerId))
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, authentication.getName()));

        if (customerId != null) {
            prompt = prompt.toolContext(Map.of("customerId", customerId));
        }

        return new ChatReply(prompt.call().content());
    }
    private Long findCustomerId(Authentication authentication) {
        return customerApi.findCustomerByEmail(authentication.getName())
                .map(CustomerInfo::id)
                .orElse(null);
    }

    private Object[] toolsFor(Authentication authentication, Long customerId) {
        if (isStaff(authentication)) {
            return new Object[]{catalogTools, staffTools};
        }
        if (customerId != null) {
            return new Object[]{catalogTools, orderTools};
        }
        return new Object[]{catalogTools};
    }
    private boolean isStaff(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STAFF"));
    }
}
