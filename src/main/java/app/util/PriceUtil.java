package app.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class PriceUtil {

    private static final DecimalFormat formatter;

    static {
        // Always use US locale for currency formatting (change if needed)
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        formatter = new DecimalFormat("#,##0.00", symbols);
    }

    /**
     * Parses a string like "19.99" into 1999 (cents).
     */
    public static int parseToCents(String priceString) {
        if (priceString == null || priceString.isBlank()) {
            return 0;
        }
        priceString = priceString.replace(",", "").trim();
        double value = Double.parseDouble(priceString);
        return (int) Math.round(value * 100);
    }

    /**
     * Converts cents to formatted string like "$19.99"
     */
    public static String formatCents(int cents) {
        return "$" + formatter.format(cents / 100.0);
    }

    /**
     * Converts cents to plain numeric string (e.g., "19.99")
     */
    public static String formatCentsPlain(int cents) {
        return formatter.format(cents / 100.0);
    }
}
