package see.evaluator;

import see.exceptions.EvaluationException;
import see.tree.Node;

import java.util.Map;

public interface Evaluator {
	<T> T evaluate(Node<T> tree, Map<String, Object> context) throws EvaluationException;
}
