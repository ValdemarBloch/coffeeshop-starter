package ek.osnb.demo.coffeeshop.assistant;

import ek.osnb.demo.coffeeshop.catalog.CatalogApi;
import ek.osnb.demo.coffeeshop.catalog.ProductDetailsInfo;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class CatalogTools {

    private final CatalogApi catalog;

    CatalogTools(CatalogApi catalog) {
        this.catalog = catalog;
    }

    @Tool(description = "Search the shop's products by part of their name or origin, e.g. 'kenya'. "
            + "Returns name, description, category, origin, roast level, price and stock.")
    List<ProductDetailsInfo> searchProducts(
            @ToolParam(description = "Part of the product name or origin. Leave empty to list every product",
                    required = false) String text) {
        return catalog.searchProducts(null, text);
    }

    @Tool(description = "Find coffee beans by roast level. "
            + "Use this for questions like 'do you have light roasts?' or 'I like dark coffee'.")
    List<ProductDetailsInfo> findByRoastLevel(
            @ToolParam(description = "The roast level: LIGHT, MEDIUM or DARK") String roastLevel) {
        return catalog.searchProducts(null, null).stream()
                .filter(product -> roastLevel.equalsIgnoreCase(product.roastLevel()))
                .toList();
    }
}
