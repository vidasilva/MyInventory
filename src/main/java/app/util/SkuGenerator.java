package app.util;

import app.dao.ProductDAO;
import java.util.Random;

public class SkuGenerator {

    private static final Random random = new Random();
    private static final int MAX_ATTEMPTS = 10;

    /**
     * Generate a SKU with optional parts, retrying if it already exists in the
     * DB.
     *
     * @param parts Components like category, color, size, etc.
     * @return A unique SKU string
     */
    public static String generateUnique(ProductDAO productDAO, String... parts) {
        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            String sku = generate(parts);
            if (productDAO.findBySku(sku) == null) {
                return sku;
            }
        }
        throw new RuntimeException("❌ Failed to generate unique SKU after " + MAX_ATTEMPTS + " attempts.");
    }

    /**
     * Generate a SKU from any combination of parts.
     */
    public static String generate(String... parts) {
        StringBuilder sku = new StringBuilder();

        for (String part : parts) {
            if (part != null && !part.isBlank()) {
                if (sku.length() > 0) {
                    sku.append("-");
                }
                sku.append(clean(part).toUpperCase());
            }
        }

        sku.append("-").append(generateSuffix());
        return sku.toString();
    }

    private static String clean(String input) {
        return input.trim().replaceAll("[^a-zA-Z0-9]", "");
    }

    private static String generateSuffix() {
        return String.valueOf(1000 + random.nextInt(9000));
    }
}
