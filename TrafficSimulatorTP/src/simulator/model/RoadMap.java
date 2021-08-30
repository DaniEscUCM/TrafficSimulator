package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class RoadMap {
	
	//Lists
	List<Junction> juncList;
	List<Road> roadList;
	List<Vehicle> vehiList;
	//Maps
	Map<String,Junction> juncMap;
	Map<String,Road> roadMap;
	Map<String,Vehicle> vehiMap;
	
	public RoadMap() {
		this.juncList = new LinkedList<Junction>();
		this.roadList = new LinkedList<Road>();
		this.vehiList =new LinkedList<Vehicle>();
		this.juncMap = new HashMap<String, Junction>();
		this.roadMap = new HashMap<String, Road>();
		this.vehiMap = new HashMap<String, Vehicle>();
	}
	
	void addJunction(Junction j) {
		if(!juncList.contains(j)) {
			juncList.add(j);
			juncMap.put(j._id, j);
		}		
	}
	
	void addRoad(Road r){
		if(!roadList.contains(r)) {
			roadList.add(r);
			roadMap.put(r._id, r);
			r.getOrigin().addOutGoingRoad(r);
			r.getDestiny().addIncommingRoad(r);
		}
	}
	
	void addVehicle(Vehicle v) {
		if(!vehiList.contains(v)) {
			vehiList.add(v);
			vehiMap.put(v._id, v);
		}
	}
	
	public Junction getJunction (String id) {
		Junction junc=juncMap.get(id);	
		return junc;		
	}
	
	public Road getRoad(String id) {
		Road road=roadMap.get(id); 
		return road;
	}
	
	public Vehicle getVehicle(String id) {
		Vehicle vehi=vehiMap.get(id);
		return vehi;
	}
	
	public List<Junction>getJunctions(){
		List<Junction> resulJunctions=Collections.unmodifiableList(new ArrayList<>(this.juncList));
		return resulJunctions;
	}
	
	public List<Road>getRoads(){
		List<Road> resul=Collections.unmodifiableList(new ArrayList<>(this.roadList));
		return resul;
	}
	
	public List<Vehicle>getVehicles() 	{
		 List<Vehicle> resul=Collections.unmodifiableList(new ArrayList<>(this.vehiList));
		return resul;	 
	 }
	
	void reset() {
		this.juncList.clear();
		this.juncMap.clear();
		this.roadList.clear();
		this.roadMap.clear();
		this.vehiList.clear();
		this.vehiMap.clear();
	}
	
	public JSONObject report() {
		JSONObject roadMap=new JSONObject();//no creo que funcione
		JSONArray aux=new JSONArray();
		JSONArray aux1=new JSONArray();
		JSONArray aux2=new JSONArray();
		
		for(Road i:roadList) {
			aux1.put(i.report());
		}
		roadMap.put("roads", aux1);
		
		for(Vehicle i:vehiList) {
			aux2.put(i.report());
		}
		roadMap.put("vehicles", aux2);
		
		for(Junction i:juncList) {
			aux.put(i.report());
		}
		roadMap.put("junctions", aux);
		
		return roadMap;
	}
}
