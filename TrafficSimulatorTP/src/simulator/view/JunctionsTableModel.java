package simulator.view;

import java.util.List;

import javax.swing.JOptionPane;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.RoadMap;
import simulator.model.Vehicle;

public class JunctionsTableModel extends Table<Junction> {
	
	private static final long serialVersionUID = 1L;
	
	private static String[] _colNames = {"Id","Green","Queues"};


	public JunctionsTableModel(Controller ctrl) {
		super(ctrl,_colNames);
		
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {
		case 0:
			s = super.getElem(rowIndex).getId();
			break;
		case 1:
			int g=super.getElem(rowIndex).getGreenLight();
			s=g==-1?"NONE":super.getElem(rowIndex).getInRoads().get(g).getId();
			break;
		case 2:
			int i=0;
			s="";
			for( List<Vehicle > l:super.getElem(rowIndex).getWaitingCars()) {
				s+=super.getElem(rowIndex).getInRoads().get(i).getId();
				s+=":[";
				for(Vehicle v:l) {
					if(!v.equals(l.get(0))) {s+=",";}
					s+=v.getId();
				}
				s+="]";
				i++;
			}
			break;
		
		}
		return s;
	}
	

	@Override
	public void onError(String err) {
		JOptionPane.showMessageDialog(null,err , "Error Junction Table", JOptionPane.ERROR_MESSAGE);

	}

	@Override
	public List<Junction> inputUpdate(RoadMap map, List<Event> events) {
		return map.getJunctions();
	}
}
