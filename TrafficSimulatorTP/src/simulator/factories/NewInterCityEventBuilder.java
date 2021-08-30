package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewInterCityRoad;
import simulator.model.Weather;

public class NewInterCityEventBuilder extends NewRoadEventBuilder {

	public NewInterCityEventBuilder() {
		super("new_inter_city_road");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		if(super.isCorrect(data)) {	
			return new NewInterCityRoad(getTime(), getId(), getSrcJun(), getDestJunc(), 
					getLength(), getCo2Limit(), getMaxSpeed(), Weather.valueOf(super.getWeather().toUpperCase()));
		}
		else {
			return null;
		}		
	}


}
