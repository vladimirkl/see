package see.functions.compare;

import see.functions.Function;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

public class Geq implements Function<List<Comparable>, Boolean> {
    @Override
    public Boolean apply(List<Comparable> input) {
        checkArgument(input.size() == 2, "Geq takes only two arguments");

        boolean value = input.get(0).compareTo(input.get(1)) >= 0;

        return value;
    }

    @Override
    public String toString() {
        return ">=";
    }
}
