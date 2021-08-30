package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewCityRoad;
import simulator.model.Weather;

public class NewCityRoadEventBuilder extends NewRoadEventBuilder {

	public NewCityRoadEventBuilder() {
		super("new_city_road");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		if(super.isCorrect(data)) {	
			return new NewCityRoad(getTime(), getId(), getSrcJun(), getDestJunc(), 
					getLength(), getCo2Limit(), getMaxSpeed(), Weather.valueOf(super.getWeather().toUpperCase()));
		}
		else {
			return null;
		}		
	}

}
