package simulator.view;

import java.util.List;

import javax.swing.JOptionPane;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;

public class EventsTableModel extends Table<Event>{

	private static final long serialVersionUID = 1L;
	
	private static String[] _colNames = {"Time","Desc."};


	public EventsTableModel(Controller ctrl) {
		super(ctrl,_colNames);		
	}
	
	@Override 
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {
		case 0:
			s = super.getElem(rowIndex).getTime();
			break;
		case 1:
			s= super.getElem(rowIndex).toString();
			break;
		}
		return s;
	}

	
	@Override
	public void onError(String err) {
		JOptionPane.showMessageDialog(null, err, "Error Events Table", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public List<Event> inputUpdate(RoadMap map, List<Event> events) {
		return events;
	}
}
