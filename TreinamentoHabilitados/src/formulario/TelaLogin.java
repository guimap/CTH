package formulario;

import java.awt.Color;
import java.awt.Font;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.towel.swing.img.JImagePanel;

import Model.Repository.LoginRepository;
import principal.FazendoLogin;
import modelo.Login;

public class TelaLogin extends JFrame {

	private JButton btOk,btSair;
	private JLabel lbUsuario, lbSenha;
	private JTextField tfUsuario;
	private Font fonte;
	private JPasswordField jpSenha;
	private Point point;
	private JFrame minhaFrame; //Variavel da minha frame, necessaria para o efeito de arrastar a tela
	//private FazendoLogin fazendoLogin = new FazendoLogin();

	public TelaLogin() {
		try {
			
			inicializaComponentes();
			eventos();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void inicializaComponentes() throws IOException {
		// Font
		// depois
		
		minhaFrame = this;
		point = new Point();
		
		//JOptionPane.showConfirmDialog(null, ));
		
		
		getContentPane().setLayout(null);
		setSize(300, 150);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setTitle("Login");
		setResizable(false);
		setUndecorated(true);
		
		// Button
		btOk = new JButton("Logar");
		btOk.setSize(80, 30);
		btOk.setLocation(100, 90);
		add(btOk);

		btSair = new JButton("Sair");
		btSair.setSize(80, 30);
		btSair.setLocation(200, 90);
		add(btSair);
		
		// Label
		lbUsuario = new JLabel("Usu�rio");
		lbUsuario.setSize(55, 10);
		lbUsuario.setLocation(20, 25);
		add(lbUsuario);

		lbSenha = new JLabel("Senha");
		lbSenha.setSize(60, 10);
		lbSenha.setLocation(20, 55);
		add(lbSenha);

		// TextField
		tfUsuario = new JTextField();
		tfUsuario.setSize(160, 30);
		tfUsuario.setLocation(100, 22);
		add(tfUsuario);

		jpSenha = new JPasswordField();
		jpSenha.setSize(160, 30);
		jpSenha.setLocation(100, 52);
		add(jpSenha);

		//
		
		
		
		setVisible(true);

	}

	public void eventos() {
		btOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Login l = new Login();
					l.setUsuario(tfUsuario.getText().trim());
					l.setSenha(String.valueOf(jpSenha.getPassword()));
					LoginRepository autentica = new LoginRepository();
					
					JTextArea a = new JTextArea();
					
					
					if(autentica.isAutentica(l)){
						new Principal();
						minhaFrame.dispose();
					}else {
						JOptionPane.showConfirmDialog(null, "n�o autenticado");
					}
				
				}catch (Exception e1){
					e1.printStackTrace();
				
				}
			}
			
		});
		
		btSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);				
			}
		});
		
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				System.out.println("clique presses");
				point.x = e.getX();//Pego a posi��o a localiza��o da frame quando clicado
		        point.y = e.getY();
		      }
		});
		
		this.addMouseMotionListener(new MouseMotionAdapter() {
		      public void mouseDragged(MouseEvent e) {
		    	  System.out.println("clique  drag");
		    	  Point p = minhaFrame.getLocation();//pego a localiza��o da frame no ato de arrastar
		          minhaFrame.setLocation(p.x + e.getX() - point.x, p.y + e.getY() - point.y); // e ent�o eu subtraio a localiza��o dela, mais a onde eu cliquei 
		        }
		      });
	}

}
