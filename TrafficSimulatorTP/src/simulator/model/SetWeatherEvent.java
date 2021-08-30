package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class SetWeatherEvent extends Event {
	
	private List<Pair<String,Weather>> ws;

	public SetWeatherEvent(int time, List<Pair<String,Weather>> ws) {
		super(time);
		if(ws.equals(null)) {
			throw new IllegalArgumentException("No new weather found");
		}
		else {
			this.ws=ws;
		}
	}
	
	@Override
	void execute(RoadMap map) {
		for( Pair<String,Weather> s:ws) {
			if(!map.getRoad(s.getFirst()).equals(null)) {
				map.getRoad(s.getFirst()).setEnvironment(s.getSecond());
			}
			else {
				throw new IllegalArgumentException("No existant road: "+s.getFirst());
			}
		}

	}
	
	@Override
	public String toString() {
		String s="Change Weather: [";
		for( Pair<String,Weather> a:ws) {
			s+="("+a.getFirst()+","+a.getSecond().toString()+")";
		}
		s+="]";
		return s;
	}

}
