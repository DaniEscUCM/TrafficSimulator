package simulator.model;

import java.util.LinkedList;
import java.util.List;

public class MoveAllStrategy implements DequeuingStrategy {

	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		List<Vehicle> aux=new LinkedList<Vehicle>();
		for(Vehicle i:q) {
			aux.add(i);
		}
		return aux;
	}

}
