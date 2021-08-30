package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public abstract class Table<T> extends AbstractTableModel implements TrafficSimObserver {
	private static final long serialVersionUID = 1L;
	
	private Controller _ctrl;
	private String[] _colNames;
	private List<T> _list;


	public Table(Controller ctrl, String[] colNames) {
		_ctrl=ctrl;				
		_ctrl.addObserver(this);
		_colNames=colNames;
		
	}

	public void update(List<T> list) {	
		_list=list;
		fireTableDataChanged();		
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	@Override
	public String getColumnName(int col) {
		return _colNames[col];
	}

	@Override
	public int getColumnCount() {
		return _colNames.length;
	}

	@Override
	public int getRowCount() {
		return _list == null ? 0 : _list.size();
	}

	public T getElem(int index) {
		return _list.get(index);
	}
	
	public abstract List<T> inputUpdate(RoadMap map, List<Event> events);
	

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		update(inputUpdate(map, events));
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(inputUpdate(map, events));
		

	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(inputUpdate(map, events));
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(inputUpdate(map, events));
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(inputUpdate(map, events));
	}

}
