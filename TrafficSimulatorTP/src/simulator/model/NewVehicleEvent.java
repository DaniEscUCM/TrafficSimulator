package simulator.model;

import java.util.LinkedList;
import java.util.List;

public class NewVehicleEvent extends Event {
	
	private String id;
	private int maxSpeed;
	private int	contClass;
	private List<String> itinerary;
	
	public NewVehicleEvent(int time, String id, int maxSpeed, int
			contClass, List<String> itinerary) {
			super(time);
			this.id=id;
			this.maxSpeed=maxSpeed;
			this.contClass=contClass;
			this.itinerary=itinerary;
	}

	@Override
	void execute(RoadMap map) {
		List<Junction> aux=new LinkedList<Junction>();
		for(String i:itinerary) {
			aux.add(map.getJunction(i));
		}
		Vehicle v=new Vehicle(id,maxSpeed,contClass,aux);
		map.addVehicle(v);
		v.moveToNextRoad();
	}
	
	@Override
	public String toString() {
		return "New Vehicle '"+id+"'";
	}

}
