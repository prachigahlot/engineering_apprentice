package lab;

public class ConfigurationStateInspector {

    public record InspectionResult(
            String name,
            String type,
            Object before,
            Object after,
            Object returnedValue) {
    }

    public static InspectionResult inspect(
            String name,
            String type,
            String initialRawValue,
            String updatedRawValue) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Name must not be empty.");
        }

        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException(
                    "Type must not be empty.");
        }

        Object initialValue = parseValue(type, initialRawValue);
        Object nextValue = parseValue(type, updatedRawValue);

        Object currentValue = initialValue;
        currentValue = nextValue;

        return new InspectionResult(
                name, type, initialValue, currentValue, currentValue);
    }

    private static Object parseValue(String type, String rawValue) {
        if (rawValue == null) {
            throw new IllegalArgumentException(
                    "Value must not be null.");
        }

        return switch (type) {
            case "int" -> parseInteger(rawValue);
            case "boolean" -> parseBoolean(rawValue);
            case "string" -> rawValue;
            default -> throw new IllegalArgumentException(
                    "Unsupported type: " + type);
        };
    }

    private static Integer parseInteger(String rawValue) {
        try {
            int value = Integer.parseInt(rawValue);

            if (value < 0 || value > 100) {
                throw new IllegalArgumentException(
                        "Integer must be between 0 and 100.");
            }

            return value;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "Invalid integer: " + rawValue, exception);
        }
    }

    private static Boolean parseBoolean(String rawValue) {
        if (!rawValue.equals("true") && !rawValue.equals("false")) {
            throw new IllegalArgumentException(
                    "Boolean must be true or false.");
        }

        return Boolean.parseBoolean(rawValue);
    }

    public static void main(String[] args) {
        if (args.length != 4) {
            System.err.println(
                    "Usage: <name> <type> <initial-value> <updated-value>");
            System.exit(2);
        }

        try {
            InspectionResult result = inspect(
                    args[0], args[1], args[2], args[3]);

            System.out.println("Configuration state inspection");
            System.out.println("name: " + result.name());
            System.out.println("type: " + result.type());
            System.out.println("before: " + result.before());
            System.out.println("after: " + result.after());
            System.out.println(
                    "returned value: " + result.returnedValue());

        } catch (IllegalArgumentException exception) {
            System.err.println("Error: " + exception.getMessage());
            System.exit(1);
        }
    }
}
