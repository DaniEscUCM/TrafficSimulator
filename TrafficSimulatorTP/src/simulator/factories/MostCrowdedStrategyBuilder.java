package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.MostCrowdedStrategy;

public class MostCrowdedStrategyBuilder extends Builder<LightSwitchingStrategy> {
	
	public MostCrowdedStrategyBuilder(){
		super("most_crowded_lss");
	}
	
	@Override
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		return data.has("timeslot")? new   MostCrowdedStrategy((int) data.get("timeslot")):new   MostCrowdedStrategy(1);
	}

}
