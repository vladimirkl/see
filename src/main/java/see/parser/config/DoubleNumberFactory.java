package see.parser.config;

public class DoubleNumberFactory implements NumberFactory {
    @Override
    public Number getNumber(String string) {
        return Double.parseDouble(string);
    }

    @Override
    public Number getNumber(Number number) {
        return number.doubleValue();
    }

    @Override
    public Character getDecimalSeparator() {
        return '.';
    }
}
