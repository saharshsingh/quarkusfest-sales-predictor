package com.redhat;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import org.dmg.pmml.FieldName;
import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.EvaluatorUtil;
import org.jpmml.evaluator.FieldValue;
import org.jpmml.evaluator.InputField;
import org.jpmml.evaluator.LoadingModelEvaluatorBuilder;
import org.jpmml.evaluator.OutputField;
import org.jpmml.evaluator.TargetField;
import org.xml.sax.SAXException;

public class PmmlTest {

	public static void main(String[] args) throws IOException, SAXException, JAXBException {

		// record results here
		final long start = System.nanoTime();

		// Building a model evaluator from a PMML file
		final File file = new File("Walmart.pmml");
		final Evaluator evaluator = new LoadingModelEvaluatorBuilder().load(file).build();

		// Performing the self-check
		evaluator.verify();

		// Printing input (x1, x2, .., xn) fields
		final Map<String, ? extends InputField> inputFields = evaluator.getInputFields().stream()
				.collect(Collectors.toMap(i -> i.getName().getValue(), i -> i));
		System.out.println("Input fields: " + inputFields);

		// Printing primary result (y) field(s)
		final Map<String, ? extends TargetField> targetFields = evaluator.getTargetFields().stream()
				.collect(Collectors.toMap(i -> i.getName().getValue(), i -> i));
		System.out.println("Target field(s): " + targetFields);

		// Printing secondary result (eg. probability(y), decision(y)) fields
		final List<? extends OutputField> outputFields = evaluator.getOutputFields();
		System.out.println("Output fields: " + outputFields);

		for (int i = 1; i <= 111; i++) {
			final Map<FieldName, FieldValue> arguments = new LinkedHashMap<>();

			final InputField date = inputFields.get("date");
			arguments.put(date.getName(), date.prepare("101"));

			final InputField store = inputFields.get("store_nbr");
			arguments.put(store.getName(), store.prepare("1"));

			final InputField item = inputFields.get("item_nbr");
			arguments.put(item.getName(), item.prepare(i));

			final Map<String, ?> result = EvaluatorUtil.decodeAll(evaluator.evaluate(arguments));
			final Object units = result.get("units");
			System.out.println(i+"="+units);
		}

		// Print result
		final long stop = System.nanoTime();
		System.out.println("Nanos: " + (stop - start));

	}

}