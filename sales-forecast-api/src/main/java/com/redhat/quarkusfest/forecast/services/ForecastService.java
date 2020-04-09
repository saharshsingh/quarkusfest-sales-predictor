package com.redhat.quarkusfest.forecast.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.xml.bind.JAXBException;

import org.dmg.pmml.FieldName;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.EvaluatorUtil;
import org.jpmml.evaluator.FieldValue;
import org.jpmml.evaluator.InputField;
import org.jpmml.evaluator.LoadingModelEvaluatorBuilder;
import org.xml.sax.SAXException;

import com.redhat.quarkusfest.forecast.model.StoreForecast;

@ApplicationScoped
public class ForecastService {

	private final ItemsService itemsService;
	private final Evaluator evaluator;
	private final Map<String, ? extends InputField> inputFields;

	@Inject
	public ForecastService(ItemsService itemsService,
			@ConfigProperty(name = "forecastapi.pmml.filepath") String pmmlFilePath)
					throws IOException, SAXException, JAXBException {

		this.itemsService = itemsService;

		final File pmmlFile = new File(pmmlFilePath);
		evaluator = new LoadingModelEvaluatorBuilder().load(pmmlFile).build();
		evaluator.verify();

		// create lookup maps for model input fields
		inputFields = evaluator.getInputFields().stream()
				.collect(Collectors.toMap(i -> i.getName().getValue(), i -> i));
	}

	public List<StoreForecast> generate(String date, int storeID) {

		final List<StoreForecast> forecasts = new ArrayList<>();

		final List<Integer> itemIDs = itemsService.getItemIDs();
		for (final int itemID : itemIDs) {
			final Map<FieldName, FieldValue> arguments = new LinkedHashMap<>();

			final InputField dateField = inputFields.get("date");
			arguments.put(dateField.getName(), dateField.prepare(ForecastService.transformDate(date)));

			final InputField store = inputFields.get("store_nbr");
			arguments.put(store.getName(), store.prepare(storeID));

			final InputField item = inputFields.get("item_nbr");
			arguments.put(item.getName(), item.prepare(itemID));

			final Map<String, ?> result = EvaluatorUtil.decodeAll(evaluator.evaluate(arguments));
			final int units = ((Double) result.get("units")).intValue();

			forecasts.add(new StoreForecast(date, storeID, itemID, units));
		}

		return forecasts;
	}

	private static String transformDate(String inputDate) {
		final StringBuilder date = new StringBuilder(inputDate);
		final int trimTill = date.charAt(5) == '0' ? 6 : 5;
		date.delete(7, 8);
		date.delete(0, trimTill);
		return date.toString();
	}

}
