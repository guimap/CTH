package View;

import java.awt.Color;
import java.util.Calendar;

import javax.swing.JPanel;

import com.toedter.calendar.JCalendar;

public class PainelCalendarioAulas extends JPanel{
	
	private JPanel painelCalendario;
	
	private JCalendar calendario;
	
	
	public PainelCalendarioAulas(JPanel painel){
		inicializaComponentes(painel);
		
	}
	
	public void inicializaComponentes(JPanel painel){
		
		
		painelCalendario = new JPanel();
		painelCalendario.setBounds(10, 150,400, 210);
		painelCalendario.setLayout(null);

		calendario = new JCalendar();
		calendario.setWeekdayForeground(Color.GRAY);
		calendario.setCalendar(Calendar.getInstance());
		calendario.setSize(painelCalendario.getWidth(),
				painelCalendario.getHeight());
		calendario.setTodayButtonVisible(true); // Mostrando o bot�o HOJE do
												// JCalendar
		calendario.setTodayButtonText("Hoje");
		calendario.getDayChooser().setAlwaysFireDayProperty(true);
		painelCalendario.add(calendario); // Adicionando o objeto

		painel.add(painelCalendario);

	}
	
	

}
