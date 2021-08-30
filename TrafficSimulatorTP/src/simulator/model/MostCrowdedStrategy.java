package simulator.model;

import java.util.List;

public final class MostCrowdedStrategy implements LightSwitchingStrategy {
	
	private int timeSlot;
	
	public MostCrowdedStrategy(int timeSlot) {
		this.timeSlot=timeSlot;
	}

	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,int currTime) {
		int bigger=-1,pos=-1;
		if(roads.isEmpty()) {
			return -1;
		}
		else if(currGreen==-1) {
			for(int i=0;i<qs.size();i++) {
				if(bigger<qs.get(i).size()) {bigger=qs.get(i).size();pos=i;}
			}
			return pos;
		}
		else if(currTime-lastSwitchingTime<timeSlot) {
			return currTime;
		}
		else {
			for(int i=0;i<qs.size();i++) {
				if(bigger<qs.get((i+currGreen+1)%qs.size()).size()) {bigger=qs.get(i).size();pos=(i+currGreen+1)%qs.size();}//tal vez modulo no este bien
			}
			return pos;
		}
		
	}

}
