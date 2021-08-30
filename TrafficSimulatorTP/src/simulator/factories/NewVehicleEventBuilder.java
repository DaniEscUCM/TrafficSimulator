package simulator.factories;


import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event> {

	public NewVehicleEventBuilder() {
		super("new_vehicle");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time,maxSpeed,cclas;
		String id,aux;
		List<String> itinerary=new LinkedList<String>();
		if(data.has("time") && data.has("id") && data.has("itinerary") && data.has("maxspeed") && data.has("class")) {
			time=data.getInt("time");
			id=data.getString("id");
			maxSpeed=data.getInt("maxspeed");
			cclas=data.getInt("class");
			for(int i=0;i<data.getJSONArray("itinerary").length();i++) {
				aux=data.getJSONArray("itinerary").getString(i);
				itinerary.add(aux);
			}
			return new NewVehicleEvent(time, id, maxSpeed, cclas, itinerary);
			
		}else {		
			return null;
		}
	}

}
