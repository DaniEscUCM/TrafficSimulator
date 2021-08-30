package simulator.view;

import java.util.List;

import javax.swing.JOptionPane;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.Vehicle;

public class VehiclesTableModel extends Table<Vehicle>{
	
	private static final long serialVersionUID = 1L;
	
	private static String[] _colNames = {"Id","Location","Iterinary","CO2 Class","Max. Speed","Speed","Total CO2","Distance"};
	
	public VehiclesTableModel(Controller ctrl) {
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
			switch(super.getElem(rowIndex).getStatus()) {
			case PENDING:
				s="Pending";
				break;
			case TRAVELING:
				s=super.getElem(rowIndex).getImIn().getId()+":"+super.getElem(rowIndex).getLocation();
				break;
			case WAITING:
				s="Waiting:"+super.getElem(rowIndex).getImIn().getDestiny();
				break;
			case ARRIVED:
				s="Arrived";
				break;
			}
			break;
		case 2:
			s = super.getElem(rowIndex).getItinerary();
			break;
		case 3:
			s = super.getElem(rowIndex).getContaminationGrate();
			break;
		case 4:
			s = super.getElem(rowIndex).getMaxSpeed();
			break;
		case 5:
			s = super.getElem(rowIndex).getAcSpeed();
			break;
		case 6:
			s = super.getElem(rowIndex).getContaminationTotal();
			break;
		case 7:
			s = super.getElem(rowIndex).getTotalDistance();
			break;
		}
		return s;
	}
	

	@Override
	public void onError(String err) {
		JOptionPane.showMessageDialog(null, err, "Error Vehicles Table", JOptionPane.ERROR_MESSAGE);

	}

	@Override
	public List<Vehicle> inputUpdate(RoadMap map, List<Event> events) {
		return map.getVehicles();
	}
	

}
