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
}
