package View;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.BoundedRangeModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import org.eclipse.swt.internal.ole.win32.ISpecifyPropertyPages;

import Controller.ConfigController;
import Controller.CriptografiaConfigEmail;
import Controller.EmailControllerV3;
import Model.Login;
import Model.MensagemEmail;
import Model.ModelTableEmail;
import Model.UsuarioEmail;

import com.itextpdf.text.Font;
import com.towel.swing.img.JImagePanel;

public class Principal extends JFrame {

	private JMenuBar menuBarra;
	private JMenu menuArquivo, menuAgendamento, menuRelatorio;
	private JMenuItem itSair, itCadastroCliente, itCadastroFuncionario,
			itCalendario, itAgendamento, itCadastroPacote, itCadastroCarro,
			itConfiguraEmail, itFazerLogoff;
	private JLabel redefinirSenha;
	private boolean painelMostrando = false;
	private JPanel painelInformativo, painelLateralGuia;
	private JButton btAbrirInformativo;
	private JToolBar barraLateral;
	private JScrollPane sp;
	private PainelCalendarioAgendamento painelCalendario;

	private int POSXButoon;
	protected JTree jtreeAtalhos;

	protected EmailControllerV3 email;
	protected JPanel painelEmail;
	protected static JTable jTableEmails;
	private List<String> listaEmails;
	private HashMap<String, List<String>> mapEmails;
	protected static Login loginUser;
	
	protected static JTextField txtBuscaEmail;
	private MensagemEmail mensagem;

	private Map<String, List<MensagemEmail>> arquivosEmail;

	protected static JButton btRefreshItens;
	protected static JButton btAbrirMenuLateral;
	protected static JFrame minhaFrame; // Frame para setar a dialogs
	
	
	private JScrollPane spTbEmail;
	private Thread gerenciaEmal;
	private final int WIDTH_TAMANHO = 301;

	private boolean hasEmailReady = false;
	

	public static boolean finished = false;
	public static boolean carregado;
	protected static boolean isFrameInstrutorOpen, isFrameClienteOpen,
			isFrameCadastroPacote, isFrameAgendamento, isFrameCarro,
			isViewConfiguraEmail, isPainelEmailShow;

	public Principal(Login usuario) {

		try {

			minhaFrame = this;
			this.loginUser = usuario;
			this.painelEmail = null;

			inicializaComponentes();
			definirEventos();
			new Thread(new ConfiguraEmail(btRefreshItens, usuario,
					jtreeAtalhos, btAbrirMenuLateral, barraLateral,
					WIDTH_TAMANHO, jTableEmails, painelEmail, this, spTbEmail))
					.start();

			isFrameClienteOpen = isFrameInstrutorOpen = isFrameCadastroPacote = isFrameAgendamento = isFrameCarro = isViewConfiguraEmail = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void inicializaComponentes() throws IOException {
		// Definindo o layout e a imagem de fundo
		java.awt.Font fonteP = ConfigController.definePrincipalFont(15f,
				Font.NORMAL);
		setLayout(null);
		
		
		JDesktopPane fundoDaPrincipal = new JDesktopPane();
		setContentPane(fundoDaPrincipal);
		
		setContentPane(new DesktopPaneCustom());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension scrnsize = toolkit.getScreenSize();
		setSize(scrnsize);

		setIconImage(ConfigController.defineIcon());

		//
		menuBarra = new JMenuBar();
		menuBarra.setFont(fonteP);
		//

		menuArquivo = new JMenu("Arquivo");
		menuAgendamento = new JMenu("Agendamento");
		menuRelatorio = new JMenu("Relatorio");
		//
		itSair = new JMenuItem("Sair");
		itCadastroCliente = new JMenuItem("Cadastro Aluno");
		itCadastroFuncionario = new JMenuItem("Cadastro Funcionario");
		itCalendario = new JMenuItem("Calendario");
		itAgendamento = new JMenuItem("Agendar Aula");
		itCadastroPacote = new JMenuItem("Cadastro Pacote");
		itCadastroCarro = new JMenuItem("Cadastro Carro");
		itConfiguraEmail = new JMenuItem("Configurar E-mail");
		itFazerLogoff = new JMenuItem("Fazer Logoff");
		//
		menuArquivo.add(itCadastroCliente);
		menuArquivo.add(itCadastroFuncionario);
		menuArquivo.add(itCadastroPacote);
		menuArquivo.add(itCadastroCarro);
		menuArquivo.add(itConfiguraEmail);

		menuArquivo.add(itFazerLogoff);
		menuArquivo.add(itSair);
		menuAgendamento.add(itAgendamento);
		//
		menuBarra.add(menuArquivo);
		menuBarra.add(menuAgendamento);
		menuBarra.add(menuRelatorio);

		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Inicio");

		DefaultMutableTreeNode favItens = new DefaultMutableTreeNode(
				"Favoritos");
		root.add(favItens);

		DefaultMutableTreeNode tarefaItens = new DefaultMutableTreeNode(
				"Tarefas");
		root.add(tarefaItens);

		DefaultMutableTreeNode dmEmail = new DefaultMutableTreeNode("E-mail");

		jtreeAtalhos = new JTree(root);
		jtreeAtalhos.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		jtreeAtalhos.setCellRenderer(new MeuModeloTree());
		jtreeAtalhos.expandRow(60);
		jtreeAtalhos.setFont(fonteP);


		barraLateral = new JToolBar();
		barraLateral.setEnabled(false);
		barraLateral.setLayout(null);
		barraLateral.setSize(170, minhaFrame.getHeight() - 60);
		barraLateral.setLocation(-(barraLateral.getWidth()), 0);
		barraLateral.setVisible(false);

		sp = new JScrollPane(jtreeAtalhos);
		sp.setBounds(barraLateral.getX(), 0, 170, barraLateral.getHeight());
		barraLateral.add(sp);

		add(barraLateral);

		
		
		btAbrirMenuLateral = new JButton(">>");
		btAbrirMenuLateral.setFocusCycleRoot(true);
		btAbrirMenuLateral.setBounds(5, 10, 45, 30);
		add(btAbrirMenuLateral);

		POSXButoon = btAbrirMenuLateral.getX();

		java.awt.Point p = new Point(minhaFrame.getWidth() - 405, 100);
		painelCalendario = new PainelCalendarioAgendamento(p);/*
															 * Definindo ele no
															 * canto esquerdo da
															 * tela
															 */
		painelCalendario.setFont(fonteP);

		

		painelEmail = new JPanel(); // INICANDO O PAINEL
		painelEmail.setLayout(new GridLayout(1, 1));
		
		jTableEmails = new JTable(new ModelTableEmail(new ArrayList<String>()));
		jTableEmails.setFont(fonteP);
		jTableEmails.setRowHeight(20);
		spTbEmail = new JScrollPane(jTableEmails);
		spTbEmail.setBackground(Color.yellow);
//		spTbEmail.setBounds(5,txtBuscaEmail.getHeight()+txtBuscaEmail.getY(),painelEmail.getWidth()-5,25);
		painelEmail.add(new PainelItensEmail(new ArrayList<String>(), spTbEmail, jTableEmails,painelEmail.getSize()));
		

		add(painelCalendario);

		btRefreshItens = new JButton(new ImageIcon(getClass().getResource(
				"/Resources/icons").getPath()
				+ "/load.gif"));
		btRefreshItens.setContentAreaFilled(false);
		btRefreshItens.setSize(40, 40);
		btRefreshItens.setLocation(
				(btAbrirMenuLateral.getX() + btAbrirMenuLateral.getWidth()),
				btAbrirMenuLateral.getY());
		btRefreshItens.setToolTipText("Atualizando");
		btRefreshItens.setVisible(false);
		add(btRefreshItens);

		getGlassPane().setVisible(true);
		setJMenuBar(menuBarra);
		setLocationRelativeTo(null);
		setTitle("Karol Habilitados v 1.3.1");
		// setResizable(false);
		setVisible(true);

	}

	//
	public void definirEventos() {

		btAbrirMenuLateral.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				long time = (1 / 2) + (1 / 3);

				if (!painelMostrando) { // ABRINDO O PAINEL

					painelMostrando = true;
					Thread t = new Thread(new Runnable() {

						@Override
						public void run() {
							int XAtual = sp.getX(); // Local atual do view
							int XFinal = 0; // O Destino final que deve estar.
							// int XPainel = painelEmail.getX();
							int posXButton = btAbrirMenuLateral.getX();
							int posXBtRefresh = btRefreshItens.getX();
							barraLateral.setVisible(true);
							while (XAtual <= XFinal) { // Enquanto o atual n
														// chegar no final
								try {

									XAtual++;
									posXButton++;
									posXBtRefresh++;

									btRefreshItens.setLocation(posXBtRefresh,
											btRefreshItens.getY());
									sp.setLocation(XAtual, sp.getY());
									barraLateral.setLocation(XAtual,
											barraLateral.getY());
									btAbrirMenuLateral.setLocation(posXButton,
											btAbrirMenuLateral.getY());
									Thread.sleep(time);
								} catch (InterruptedException ex) {
									Logger.getLogger(Principal.class.getName())
											.log(Level.SEVERE, null, ex);
								}
							}
							if (isPainelEmailShow) {
								barraLateral.add(painelEmail);
							}

							btAbrirMenuLateral.setText("<<");
						}
					});
					t.start();

				} else { // FECHANDO

					barraLateral.remove(painelEmail);
					barraLateral.revalidate();

					painelMostrando = false;
					Thread t = new Thread(new Runnable() {

						@Override
						public void run() {

							int XAtual = sp.getX(); // Local atual do view
							int XFinal = -(barraLateral.getWidth()); // O
																		// Destino
																		// final
																		// que
																		// deve
																		// estar.
							int posXButton = barraLateral.getWidth();
							int posXBtRefresh = btRefreshItens.getX();

							while (XAtual >= XFinal) { // Enquanto o atual n
														// chegar no final
								try {
									XAtual--;
									posXButton--;
									posXBtRefresh--;

									btRefreshItens.setLocation(posXBtRefresh,
											btRefreshItens.getY());
									sp.setLocation(XAtual, sp.getY());
									barraLateral.setLocation(XAtual,
											barraLateral.getY());
									btAbrirMenuLateral.setLocation(posXButton,
											btAbrirMenuLateral.getY());
									Thread.sleep(time);

								} catch (InterruptedException ex) {
									Logger.getLogger(Principal.class.getName())
											.log(Level.SEVERE, null, ex);
								}
							}
							btAbrirMenuLateral.setLocation(posXButton + 5,
									btAbrirMenuLateral.getY());
							barraLateral.setVisible(false);
							btAbrirMenuLateral.setText(">>");
						}
					});
					t.start();
				}
			}
		});

		itSair.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(rootPane, "deseja sair?",
						"Confirmar saida?", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE) == 0) {
					System.exit(0);
				}
			}
		});

		itCadastroCliente.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isFrameClienteOpen) {
					try {
					isFrameClienteOpen = true;
					JInternalFrame internalFrame = new FormCadastroCliente();
					getContentPane().add(internalFrame);
					internalFrame.setSelected(true);
					} catch (PropertyVetoException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		itCadastroFuncionario.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!Principal.isFrameInstrutorOpen) {
					try {
						Principal.isFrameInstrutorOpen = true; // difinindo que ja tem uma
													// janela aberta
					JInternalFrame internalFrame = new FormCadastroInstrutor();
					getContentPane().add(internalFrame);
					internalFrame.setSelected(true);
					} catch (PropertyVetoException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}
		});

		itAgendamento.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isFrameAgendamento) { // Se não houver uma instancia
											// crida, eu crio uma nova.
					try {
					isFrameAgendamento = true; // definindo que ja tem uma
												// janela aberta
					JInternalFrame internalFrame = new FormAgendamento();
					getContentPane().add(internalFrame); // Adiciono a
																	// Internal
																	// Frame na
																	// tela
					internalFrame.setSelected(true);
					} catch (PropertyVetoException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});

		itCadastroCarro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isFrameCarro) {
					try {
					isFrameCarro = true;
					JInternalFrame internalFrame = new FormCadastroCarro();
					getContentPane().add(internalFrame);
					internalFrame.setSelected(true);
					} catch (PropertyVetoException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});

		itCadastroPacote.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!isFrameCadastroPacote) { // defino se não houver uma
												// instancia já criada, eu
					
					try {
					isFrameCadastroPacote = true; // difinindo que ja tem uma janela aberta
					JInternalFrame internalFrame = new FormCadastroPacote();
					getContentPane().add(internalFrame);
					internalFrame.setSelected(true);
					} catch (PropertyVetoException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		});

		itConfiguraEmail.addActionListener(e -> {
			if (!isViewConfiguraEmail) {
				try {
				isViewConfiguraEmail = true;
				JInternalFrame internalFrame = new ViewConfigEmail(loginUser);
				getContentPane().add(internalFrame);
				internalFrame.setSelected(true);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		itFazerLogoff.addActionListener(e -> {
			this.dispose();
			// Thread.currentThread().stop();

				new TelaLogin();
				minhaFrame = null;

			});

	}

}

/**
 * 
 * INNERS CLASS
 * 
 * @author gprodrigues
 *
 */

class MeuModeloTree extends DefaultTreeCellRenderer {

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus); // To change body of generated
								// methods, choose Tools |
								// Templates.
								// if(value != null){
		DefaultMutableTreeNode no = (DefaultMutableTreeNode) value;
		String texto = no.getUserObject().toString();
		System.out.println();
		if (texto.equals("Inicio")) {
			ImageIcon img = new ImageIcon(getClass().getClassLoader()
					.getResource("Resources/icons/inicio.png"));
			setIcon(img);
			setToolTipText(texto);

		} else if (texto.contains("E-mail")) {
			ImageIcon img = new ImageIcon(getClass().getClassLoader()
					.getResource("Resources/icons/email.png"));
			setIcon(img);
			setToolTipText(texto);
		}
		if (texto.contains("Favoritos")) {
			ImageIcon img = new ImageIcon(getClass().getClassLoader()
					.getResource("Resources/icons/" + texto + ".png"));
			setIcon(img);
			setToolTipText(texto);
		}
		if (texto.contains("Tarefas")) {
			ImageIcon img = new ImageIcon(getClass().getClassLoader()
					.getResource("Resources/icons/" + texto + ".png"));
			setIcon(img);
			setToolTipText(texto);
		} else {
		}

		// }
		// }
		return this;

	}

}

// Thread Para Configurar e baixar os E-mails
class ConfiguraEmail implements Runnable {
	private JButton btAbrirNav;
	private static JButton btRefresh;
	private File arquivoEmailMap;
	private List<File> lsArquivoTemp;
	private EmailControllerV3 emailC;
	private Login user;
	private JTree tree;
	private JToolBar barrNav;

	private int WIDTH_TAMANHO;

	private JTable table;
	private JPanel pnEmail;

	private JFrame frame;

	protected static boolean isShowing;
	private int totalEmails;
	private UsuarioEmail confEmail;
	private JScrollPane spTbEmail;
	protected static boolean isLodingEmail,isJtreeAumentou;
	private List<String> lstItens;
	protected String nameFolder;

	public ConfiguraEmail(JButton bt, Login u, JTree j, JButton btAbrirNav,
			JToolBar barrNav, int WIDTH_TAMANHO, JTable tb, JPanel pn,
			Principal principal, JScrollPane sp) {

		isShowing = false;
		isJtreeAumentou = false;
		this.lstItens = new ArrayList<String>();
		this.btRefresh = bt;
		this.spTbEmail = sp;
		this.user = u;
		this.tree = j;
		this.table = tb;
		this.pnEmail = pn;
		this.btAbrirNav = btAbrirNav;
		this.barrNav = barrNav;
		this.frame = principal;
		this.WIDTH_TAMANHO = WIDTH_TAMANHO;

		String diretorio = System.getProperty("user.home");
		String sep = System.getProperty("file.separator");
		
		

		String nameFolder = user.getUsuario() + "@emailConfig"; // e digo o nome
																// padr�o dos
																// arquivos de
																// e-mail's
		
		File fileConfigEmail = new File(diretorio+sep+nameFolder); // Pego o
																   // diretorio
																   // que se
																   // encontra
																   // o arquivo
		
		confEmail = new CriptografiaConfigEmail().unCrypt(// e tento localizo-lo
															// e
															// descriptografa-lo
				nameFolder);

	}

	private void defineJtreeView() throws MessagingException {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Inicio");

		DefaultMutableTreeNode favItens = new DefaultMutableTreeNode(
				"Favoritos");
		root.add(favItens);

		DefaultMutableTreeNode tarefaItens = new DefaultMutableTreeNode(
				"Tarefas");
		root.add(tarefaItens);

		DefaultMutableTreeNode dmEmail = new DefaultMutableTreeNode("E-mail"); // Recrio
																				// todo
																				// a
																				// JTree
																				// com
																				// os
																				// itens

		DefaultMutableTreeNode dmEnviarEmail = new DefaultMutableTreeNode(
				"Enviar E-mail");
		
		dmEmail.add(dmEnviarEmail);
		List<String> folders = emailC.getFolders();
		folders.forEach(fo -> {
			DefaultMutableTreeNode dm = new DefaultMutableTreeNode(fo);
			dmEmail.add(dm);
		});

		root.add(dmEmail);
		DefaultTreeModel model = new DefaultTreeModel(root);

		tree.setModel(model);

	}

	private void defineEventsItensEmail() {
		tree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent evt) {
				System.out.println(evt.getNewLeadSelectionPath());
				System.out.println("eh nois carai");
				
				String[] itens = evt.getNewLeadSelectionPath().toString()
						.split(",");

				nameFolder = itens[itens.length - 1]; /*
													 * Pego o ultimo nome da
													 * arvore de arquivos
													 */

				nameFolder = nameFolder.replace(']', ' '); /*
															 * e retiro os
															 * caracteres
															 */
				nameFolder = nameFolder.replace(" ", "");

				// Testa se �
				// pasta
				// INBOX

				System.out.println(nameFolder);

				try {
					System.out.println(isShowing);
					System.out.println(isJtreeAumentou);
					if (!isShowing) {
						Principal.isPainelEmailShow = true;
						isShowing = true;
						java.awt.Dimension d2 = barrNav.getSize();
						barrNav.setSize(d2.width + WIDTH_TAMANHO, d2.height);
						table.setRowHeight(20);
						btAbrirNav.setLocation(btAbrirNav.getX()
								+ WIDTH_TAMANHO, btAbrirNav.getY());
						btRefresh.setLocation(btAbrirNav.getX()
								+ btAbrirNav.getWidth(), btAbrirNav.getY());
						
						
						if(!isJtreeAumentou){
							isJtreeAumentou = true;
						pnEmail.setSize(WIDTH_TAMANHO,
								barrNav.getHeight() - 80);
						pnEmail.add(new PainelItensEmail(new ArrayList<String>(), spTbEmail, table,pnEmail.getSize()));
						pnEmail.setLocation(tree.getWidth() + 10, 0);
						pnEmail.setBackground(Color.white);
						barrNav.add(pnEmail);
						}
						
					}
					
					
					if ("EnviarE-mail".equalsIgnoreCase(nameFolder)) { //Tem que ser junto, pois existe a valida��o de retirar todos os Espa�os
						
						
						
						pnEmail.removeAll();
						pnEmail.add(new PainelSendNewEmail(pnEmail.getSize(), emailC, pnEmail, barrNav,new  Dimension(barrNav.getSize().width - WIDTH_TAMANHO, barrNav.getSize().height)));
						barrNav.revalidate();
						barrNav.repaint();
						pnEmail.revalidate();
						pnEmail.repaint();
						System.out.println("Enviar E-mail@@");
						
					} else {
						lstItens = emailC.pegaItens(nameFolder);
						totalEmails = lstItens.size();
						table.setModel(new ModelTableEmail(lstItens));
						pnEmail.removeAll();
						pnEmail.add(new PainelItensEmail(lstItens, spTbEmail, table,pnEmail.getSize()));
						pnEmail.revalidate();
						barrNav.revalidate();
						barrNav.repaint();
						pnEmail.repaint();
						Principal.minhaFrame.revalidate();
						Principal.minhaFrame.repaint();
					}
					
					

				} catch (Exception er) {
					er.printStackTrace();
				}
			}
			// }

		});

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (e.getClickCount() == 2) {
					try {
						int index = table.getSelectedRow();
						System.out.println("INDEX PRINCIPAL - " + index);
						// index = ((ModelTableEmail) table.getModel())
						// .getIdEmail(index);
						System.out.println("INDEX PELA TABLE -" + index);
						String selectedFolder = tree.getSelectionPath()
								.toString();
						System.out.println(selectedFolder);
						String[] vt = selectedFolder.split(",");
						selectedFolder = vt[vt.length - 1];
						selectedFolder = selectedFolder.replaceAll("]", "");
						selectedFolder = selectedFolder.trim();
						MensagemEmail em = emailC.pegaEmail(selectedFolder,
								index);
						frame.getContentPane().add(new ViewEmail(em, emailC));
						// arquivosEmail = email.getEmails();

						System.out.println("cliq");
					} catch (Exception exc) {
						exc.printStackTrace();
					}

				}
			}
		});

		spTbEmail.getVerticalScrollBar().addAdjustmentListener(
				new AdjustmentListener() {

					@Override
					public void adjustmentValueChanged(AdjustmentEvent e) {
						JScrollBar bar = spTbEmail.getVerticalScrollBar();
						int total = bar.getValue() + bar.getHeight();

						if (total == bar.getMaximum()) {
							if (!isLodingEmail) {
								btRefresh.setVisible(true);
								JButton b = btRefresh;
								new Thread(
										() -> {
											try {
												ConfiguraEmail.isLodingEmail = true;
												List<String> ls = emailC
														.pegaItens(
																nameFolder,
																lstItens.size(),
																lstItens.size() + 30,
																lstItens);

												table.setModel(new ModelTableEmail(
														ls));
												ConfiguraEmail.btRefresh
														.setVisible(false);
												ConfiguraEmail.isLodingEmail = false;
												// Principal.minhaFrame.revalidate();
												// Principal.minhaFrame.repaint();
											} catch (Exception e1) {
												// TODO Auto-generated catch
												// block
												e1.printStackTrace();
											}
										}).start();
							}
						} else {
							System.out.println(total + " / " + bar.getMaximum());
						}

					}
				});

	}

	public void run() {
		try {

			if (confEmail != null) { // Se no construtor conseguiu localizar o
										// arquivo ent�o eu tento autentica-lo
				System.out.println("Tem config");
				// File log = new File(getClass().getClassLoader().getResource(
				// "Resources/FilesConfig/log.txt").toURI()); //Pego o diretorio
				// que se encontra o arquivo
				// InputStream in =
				// getClass().getResourceAsStream("/Resources/FilesConfig/log.txt");

				LocalDate inicio = LocalDate.now();

				btRefresh.setVisible(true);
				emailC = new EmailControllerV3(confEmail);
				defineJtreeView();
				defineEventsItensEmail();
				LocalDate fim = LocalDate.now();
				Thread threadUpdateEmail = new Thread(new CheckNewMessages(
						table, tree, pnEmail, emailC));
				threadUpdateEmail.start();
				Principal.minhaFrame.revalidate();
			} else {
				System.out.println("N Tem config  - PARTIU");
			}
		} catch (MessagingException e) {
			e.printStackTrace();

		} finally {
			btRefresh.setVisible(false);
		}
	}
}







class CheckNewMessages implements Runnable {

	private JTable jtable;
	private EmailControllerV3 email;
	private JTree jtree;
	private JPanel painel;
	private Map<String, List<MensagemEmail>> arquivosEmail;

	public CheckNewMessages(JTable Table, JTree jtreeAtalhos, JPanel painel,
			EmailControllerV3 emailC) {
		this.painel = painel;
		this.jtable = Table;
		this.jtree = jtreeAtalhos;
		this.email = emailC;

	}

	//
	@Override
	public void run() {
		try {
			while (true) {
				int atual = email.countMessage();
				if (email.hasNewEmail(atual)) {
					List<String> ls = email.getNewEmails();
					int indiceSelected = jtable.getSelectedRow();
					jtable.setModel(new ModelTableEmail(ls));
					indiceSelected = ls.size() - atual;
					jtable.setRowSelectionInterval(indiceSelected,
							indiceSelected);
				}

				Thread.sleep(60 * 1000);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
	}
	//
	
	
}

class PainelItensEmail extends JPanel{
	private JScrollPane sp;
	private JTable tb;
	private JTextField txtBusca;
	public PainelItensEmail(List<String>ls,JScrollPane sp,JTable tb,Dimension s){
		this.tb = tb;
		this.sp = sp;
		setSize(s);
		setBackground(new Color(0, 0, 0, 30));
		initComponents();
		
	}

	private void initComponents() {
		setLayout(null);
		txtBusca = new JTextField();
		txtBusca.setBounds(5, 5, this.getWidth()-15, 25);
		add(txtBusca);
		System.out.println(sp);
		System.out.println(txtBusca);
		sp = new JScrollPane(Principal.jTableEmails);
		sp.setBounds(5, txtBusca.getY()+txtBusca.getHeight(), this.getWidth()-15, getHeight()-txtBusca.getHeight()-txtBusca.getY());
		add(sp);
		
		setVisible(true);
	}
	
	//----------
	
	
	
	
	
	
}

class DesktopPaneCustom extends JDesktopPane{
	public void paintComponent(Graphics g) {  
        super.paintComponent(g);
        try {
        BufferedImage imagem;
		
			imagem = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Resources/imgs/logo fundo.png"));
		
        if(imagem != null)  {  
            g.drawImage(imagem, 0, 0, this.getWidth(), this.getHeight(), this);   
        }  
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    } 
}


