package app.util;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class TextFieldUtil {

    public static void setIntegerOnly(TextField textField) {
        Pattern validIntegerText = Pattern.compile("\\d*");

        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (validIntegerText.matcher(newText).matches()) {
                return change;
            }
            return null;
        };

        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        textField.setTextFormatter(textFormatter);
    }

    public static void setCurrencyFormat(TextField textField) {
        // Only allow digits
        Pattern validInput = Pattern.compile("\\d*");

        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            String digitsOnly = newText.replaceAll("[^\\d]", "");
            if (validInput.matcher(digitsOnly).matches()) {
                return change;
            }
            return null;
        };

        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        textField.setTextFormatter(textFormatter);

        // Dollar format, US locale
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

        // Prevent infinite loop while formatting
        textField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.equals(oldVal)) {
                return;
            }

            String digits = newVal.replaceAll("[^\\d]", "");
            if (digits.isEmpty()) {
                textField.setText("");
                return;
            }

            double value = Double.parseDouble(digits) / 100.0;
            String formatted = currencyFormat.format(value);

            // Avoid unnecessary updates
            if (!formatted.equals(textField.getText())) {
                textField.setText(formatted);
                textField.positionCaret(formatted.length()); // Keep caret at end
            }
        });
    }

}
