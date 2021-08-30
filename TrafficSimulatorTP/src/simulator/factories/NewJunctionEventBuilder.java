package simulator.factories;

import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event> {
	
	Factory<LightSwitchingStrategy> lssFactory;
	Factory<DequeuingStrategy> dqsFactory;

	public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> lssFactory, Factory<DequeuingStrategy> dqsFactory) {
		super("new_junction");
		this. lssFactory= lssFactory;
		this.dqsFactory=dqsFactory;		
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time,x,y;
		String id;	
		LightSwitchingStrategy lsStrategy;
		DequeuingStrategy dqStrategy;
		if(data.has("time") && data.has("id") && data.has("coor") && data.has("ls_strategy") && data.has("dq_strategy")) {
			time=data.getInt("time");
			id=data.getString("id");
			x=data.getJSONArray("coor").getInt(0);
			y=data.getJSONArray("coor").getInt(1);
			lsStrategy=this.lssFactory.createInstance(data.getJSONObject("ls_strategy"));
			dqStrategy=this.dqsFactory.createInstance(data.getJSONObject("dq_strategy"));
			return new NewJunctionEvent(time, id,lsStrategy , dqStrategy, x, y);
		}
		else {
			return null;
		}
	}

}
