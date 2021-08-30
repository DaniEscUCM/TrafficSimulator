package simulator.view;

import java.util.List;

import javax.swing.JOptionPane;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;

public class RoadsTableModel extends Table<Road> {

	private static final long serialVersionUID = 1L;
	
	private static String[] _colNames = {"Id","Lenght","Weather","Max. Speed","Speed Limit","Total CO2","CO2 Limit"};


	public RoadsTableModel(Controller ctrl) {
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
			s= super.getElem(rowIndex).getLength();
			break;
		case 2:
			s = super.getElem(rowIndex).getEnvironment();
			break;
		case 3:
			s =  super.getElem(rowIndex).getMaxSpeed();
			break;
		case 4:
			s =  super.getElem(rowIndex).getAcSpeed();
			break;
		case 5:
			s = super.getElem(rowIndex).getTotalContamination();
			break;
		case 6:
			s = super.getElem(rowIndex).getContAlarm();
			break;
		
		}
		return s;
	}
	
	@Override
	public void onError(String err) {
		JOptionPane.showMessageDialog(null, err, "Error Roads Table", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public List<Road> inputUpdate(RoadMap map, List<Event> events) {
		return map.getRoads();
	}
	
	

}
