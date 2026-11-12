package ek.osnb.demo.coffeeshop.assistant;

import ek.osnb.demo.coffeeshop.catalog.CatalogApi;
import ek.osnb.demo.coffeeshop.catalog.ProductDetailsInfo;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

// Værktøjer som kun staff får (se AssistantController)
@Component
class StaffTools {

    private final CatalogApi catalog;

    StaffTools(CatalogApi catalog) {
        this.catalog = catalog;
    }

    @Tool(description = "List the products that are running low (10 or fewer in stock), lowest stock first. "
            + "Use this when staff ask what is running low or sold out.")
    List<ProductDetailsInfo> lowStockProducts() {
        return catalog.searchProducts(null, null).stream()
                .filter(product -> product.stock() <= 10)
                .sorted(Comparator.comparingInt(ProductDetailsInfo::stock))
                .toList();
    }
}
