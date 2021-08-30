package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;

public abstract class ToolDialog<LIST,BOX> extends JDialog {
	
	private static final long serialVersionUID = 1822421172041234283L;
	
	private JComboBox<BOX> theBox;
	private JList<LIST> theList;
	private JSpinner ticks;
	private JPanel buttons;
	static ControlPanel _cont;
	
	public ToolDialog(String title, String text, String opLabel,BOX [] op, JFrame frame,ControlPanel cont) {
		super(frame,true);
		setTitle(title);
		_cont=cont;
		Image iconoPropio = Toolkit.getDefaultToolkit().getImage("resources/icons/lights.png");
		setIconImage(iconoPropio);
		
		JPanel mainPanel = new JPanel(new GridLayout(3, 1));
		mainPanel.setPreferredSize(new Dimension(500,120));
		setContentPane(mainPanel);
		
		JTextArea message=new JTextArea(text);
		message.setLineWrap(true);
		message.setWrapStyleWord(true);
		message.setEditable(false);
		add(message,BorderLayout.PAGE_START);
		
		buttons=new JPanel(new GridLayout(1,3));
		mainPanel.add(buttons);
		
		//JPanel aux=new JPanel();
		theList=new JList<LIST>();
		theList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		//aux.add(new JScrollPane(theList));
		buttons.add(new JScrollPane(theList),BorderLayout.LINE_START);
		
		theBox=new JComboBox<BOX>(op);
		buttons.add( new JLabel(opLabel+": "));
		buttons.add(theBox);
		theBox.setMaximumSize(new Dimension(100,20));
		theBox.setSize(new Dimension(100,20));
		
		ticks = new JSpinner(new SpinnerNumberModel(10, 1, 1000, 1));
		buttons.add( new JLabel("Ticks: "));
		buttons.add(ticks, BorderLayout.LINE_START);
		ticks.setMaximumSize(new Dimension(100,20));
		ticks.setSize(new Dimension(100,20));
		
		
		JPanel save=new JPanel();
		mainPanel.add(save);
		
		JButton cancel=new JButton("cancel");
		cancel.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				reset();
			}
		});
		save.add(cancel,BorderLayout.CENTER);
		
		JButton ok=new JButton("ok");
		ok.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				action();
				setVisible(false);
				reset();
			}
		});
		save.add(ok,BorderLayout.CENTER);
		
		pack();
	}
	
	abstract public void action();
	
	public int getNumListIndices() {
		return theList.getSelectedIndices().length;
	}
	
	@SuppressWarnings("unchecked")
	public BOX getSelectedbox() {
		return (BOX)theBox.getSelectedItem();
	}
	
	public Integer getTicks() {
		return (Integer) ticks.getValue();
	}
	
	public void setList(List<LIST> list) {		
		theList.setListData(new Vector<LIST>(list));
	}
	
	public boolean getSelected() {
		return theList.getSelectedIndices().length>0;
	}
	
	public void reset() {
		theBox.setSelectedIndex(0);
		theList.clearSelection();
		ticks.setValue(10);
	}
}
