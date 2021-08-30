package simulator.view;

import javax.swing.JFrame;

public class ChangeCO2Dialog extends ToolDialog<String,Integer> {
	
	private static final long serialVersionUID = 1L;
	
	private static String titulo="Change CO2";
	private static String texto="Schedule an event to change the CO2 class of a vehicle after a given number od simulation ticks from now.";
	private static String box="CO2";
	private static Integer[] clas=new Integer[] {0,1,2,3,4,5,6,7,8,9,10};

	public ChangeCO2Dialog(JFrame frame, ControlPanel cont) {
		super(titulo, texto, box,clas, frame, cont);
		_cont=cont;
	}

	@Override
	public void action() {
		_cont.changeCo2();		
	}

}
