package simulator.factories;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event> {

	public SetWeatherEventBuilder() {
		super("set_weather");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time;
		List<Pair<String,Weather>> ws=new LinkedList<Pair<String,Weather>>();
		Pair<String,Weather> aux;
		if(data.has("time") && data.has("info")) {
			time=data.getInt("time");
			for(int i=0;i<data.getJSONArray("info").length();i++) {
				aux=new Pair<String, Weather>(data.getJSONArray("info").getJSONObject(i).getString("road"), 
						Weather.valueOf(data.getJSONArray("info").getJSONObject(i).getString("weather").toUpperCase()));	
				ws.add(aux);
			}
			return new SetWeatherEvent(time, ws);
		}
		else {
			return null;			
		}
	}

}
