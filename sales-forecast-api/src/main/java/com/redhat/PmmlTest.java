package com.redhat;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        // Building a model evaluator from a PMML file
        File file = new File("Walmart.pmml");
        Evaluator evaluator = new LoadingModelEvaluatorBuilder().load(file).build();

        // Performing the self-check
        evaluator.verify();

        // Printing input (x1, x2, .., xn) fields
        List<? extends InputField> inputFields = evaluator.getInputFields();
        System.out.println("Input fields: " + inputFields);

        // Printing primary result (y) field(s)
        List<? extends TargetField> targetFields = evaluator.getTargetFields();
        System.out.println("Target field(s): " + targetFields);

        // Printing secondary result (eg. probability(y), decision(y)) fields
        List<? extends OutputField> outputFields = evaluator.getOutputFields();
        System.out.println("Output fields: " + outputFields);

        // Reading a record from the data source
        Map<String, String> inputRecord = new HashMap<>();
        inputRecord.put("date", "101");
        inputRecord.put("store_nbr", "1");
        inputRecord.put("item_nbr", "9");
        
        // Mapping the record field-by-field from data source schema to PMML schema
        Map<FieldName, FieldValue> arguments = new LinkedHashMap<>();
        for (InputField inputField : inputFields) {
            FieldName inputName = inputField.getName();
            Object rawValue = inputRecord.get(inputName.getValue());

            // Transforming an arbitrary user-supplied value to a known-good PMML value
            FieldValue inputValue = inputField.prepare(rawValue);

            arguments.put(inputName, inputValue);
        }

        // Evaluating the model with known-good arguments
        Map<FieldName, ?> results = evaluator.evaluate(arguments);

        // Decoupling results from the JPMML-Evaluator runtime environment
        Map<String, ?> resultRecord = EvaluatorUtil.decodeAll(results);

        // Print result
        System.out.println(resultRecord);
    }

}