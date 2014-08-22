package co.uk.vonaq.cableguardian.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessagesParser {

    public static Tuple<MessageAction, Integer, String> parse(String message) {
        try {
            String[] tokens = message.split("\\s+");
            Integer lineNumber = Integer.parseInt(tokens[1]);
            String action = tokens[0];

            String description = "";
            if (tokens.length > 2) {
                description = message.split("\\s+", 3)[2]; //rest of the string
            }
            if (action.equalsIgnoreCase("ack")) {
                return new Tuple<>(MessageAction.ACK, lineNumber, null);
            } else if (action.equalsIgnoreCase("clr")) {
                return new Tuple<>(MessageAction.CLR, lineNumber, null);
            } else if (action.equalsIgnoreCase("sta")) {
                return new Tuple<>(MessageAction.STA, lineNumber, null);
            } else if (action.equalsIgnoreCase("+")) {
                return new Tuple<>(MessageAction.PLUS, lineNumber, null);
            } else if (action.equalsIgnoreCase("-")) {
                return new Tuple<>(MessageAction.MINUS, lineNumber, null);
            } else if (action.equalsIgnoreCase("addNode")) {
                return new Tuple<>(MessageAction.ADD, lineNumber, description);
            }
        } catch (NumberFormatException e) {
        } catch (Exception e) {
        }
        return null;
    }

    public static String parseFromField(String field) {
        try {
            final Pattern pattern = Pattern.compile("<(.+?)>");
            final Matcher matcher = pattern.matcher(field);
            matcher.find();
            return matcher.group(1);
        } catch (IllegalStateException h) {
            return "";
        }
    }

}
