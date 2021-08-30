package simulator.factories;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event> {

	public SetContClassEventBuilder() {
		super("set_cont_class");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time;
		List<Pair<String,Integer>> cs= new LinkedList<Pair<String,Integer>>();
		Pair<String,Integer> aux;
		if(data.has("time") && data.has("info")) {
			time=data.getInt("time");
			for(int i=0;i<data.getJSONArray("info").length();i++) {
				aux=new Pair<String,Integer>(data.getJSONArray("info").getJSONObject(i).getString("vehicle"), 
						 (Integer) data.getJSONArray("info").getJSONObject(i).get("class"));
				cs.add(aux);
			}
			return new NewSetContClassEvent(time, cs);
		}
		else {
			return null;			
		}
	}

}
