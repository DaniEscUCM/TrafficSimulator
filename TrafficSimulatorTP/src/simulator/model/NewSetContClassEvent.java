package simulator.model;
import java.util.List;
import simulator.misc.Pair;

public class NewSetContClassEvent extends Event {
	
	private List<Pair<String,Integer>> cs;
	
	public NewSetContClassEvent(int time, List<Pair<String,Integer>> cs) {
		super(time);
		this.cs=cs;
	}

	@Override
	void execute(RoadMap map) {
		for( Pair<String,Integer> s:cs) {
			if(!map.getVehicle(s.getFirst()).equals(null)) {
				map.getVehicle(s.getFirst()).setContaminationClass(s.getSecond());
			}
			else {
				throw new IllegalArgumentException("No existant road: "+s.getFirst());
			}
		}
	}
	
	@Override
	public	String toString() {
		String s="Change CO2 class: [";
		for( Pair<String,Integer> a:cs) {
			s+="("+a.getFirst()+","+a.getSecond().toString()+")";
		}
		s+="]";
		return s;
	}

}
