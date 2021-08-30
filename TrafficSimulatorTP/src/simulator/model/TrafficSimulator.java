package simulator.model;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable <TrafficSimObserver> {
	private RoadMap roadMap;
	private List<Event> events;
	private int time;
	private List<TrafficSimObserver> trafficSimObserver;
	
	public TrafficSimulator() {
		roadMap=new RoadMap();
		time=0;
		events=new SortedArrayList<Event>();
		trafficSimObserver= new LinkedList<TrafficSimObserver>();
	}
	
	public void addEvent(Event e) {
		events.add(e);
		onEventAdded(roadMap, events, e, time);
	}
	
	public void advance() {
		time++;
		onAdvanceStart(roadMap, events, time);
		try {
			while(!events.isEmpty() &&events.get(0)._time==this.time) {
				
					events.get(0).execute(this.roadMap);
					events.remove(0);	
				
			}
			
			if(this.roadMap.getJunctions().size()>0) {
				for(Junction j:this.roadMap.getJunctions()) {
					j.advance(time);
				}
			}
			if(this.roadMap.getRoads().size()>0) {
				for(Road r:this.roadMap.getRoads()) {
					r.advance(time);
				}
			}
			onAdvanceEnd(roadMap, events, time);
		}
		catch(Exception e) {
			onError(e.getMessage());
		}
	}
	
	public void reset() {
		roadMap.reset();
		time=0;
		events.clear();
		onReset(roadMap, events, time);
	}
	
	public JSONObject report() {
		JSONObject resul= new JSONObject();
		resul.put("time", time);
		resul.put("state", roadMap.report());
		return resul;
	}

	@Override
	public void addObserver(TrafficSimObserver o) {
		if(!trafficSimObserver.contains(o)) {
			trafficSimObserver.add(o);
			onRegister(roadMap, events, time);
		}
		
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		trafficSimObserver.remove(o);		
	}
	void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		for(TrafficSimObserver o:trafficSimObserver) {
			o.onAdvanceStart(roadMap, events, time);
		}
	}
	void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		for(TrafficSimObserver o:trafficSimObserver) {
			o.onAdvanceEnd(roadMap, events, time);
		}
	}
	void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		for(TrafficSimObserver o:trafficSimObserver) {
			o.onEventAdded(roadMap, events,e, time);
		}
	}
	void onReset(RoadMap map, List<Event> events, int time) {
		for(TrafficSimObserver o:trafficSimObserver) {
			o.onReset(roadMap, events, time);
		}
	}
	void onRegister(RoadMap map, List<Event> events, int time) {
		for(TrafficSimObserver o:trafficSimObserver) {
			o.onRegister(roadMap, events, time);
		}
	}
	public void onError(String err) {
		for(TrafficSimObserver o:trafficSimObserver) {
			o.onError(err);
		}
	}
	
}
