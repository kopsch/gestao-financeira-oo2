package com.gestaofinanceira.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.mindrot.jbcrypt.BCrypt;

import com.gestaofinanceira.dao.UsuarioDAO;
import com.gestaofinanceira.model.Usuario;
import com.gestaofinanceira.service.UsuarioService;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsuario;
	private JPasswordField pwSenha;
	private UsuarioService usuarioService;
	private UsuarioDAO usuarioDAO;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginWindow frame = new LoginWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginWindow() {
		this.initComponents();
		this.usuarioService = new UsuarioService();
		this.usuarioDAO = new UsuarioDAO();
		

	}
	
	private void initComponents() {
		setTitle("Tela de Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 330, 205);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblUsuario = new JLabel("Usuário:");
		lblUsuario.setBounds(32, 33, 81, 24);
		contentPane.add(lblUsuario);
		
		txtUsuario = new JTextField();
		txtUsuario.setBounds(88, 35, 168, 20);
		contentPane.add(txtUsuario);
		txtUsuario.setColumns(10);
		
		JLabel lblSenha = new JLabel("Senha:");
		lblSenha.setBounds(32, 68, 73, 24);
		contentPane.add(lblSenha);
		
		pwSenha = new JPasswordField();
		pwSenha.setBounds(88, 68, 168, 20);
		contentPane.add(pwSenha);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				String nomeUsuario = txtUsuario.getText().trim();
			    String senhaDigitada = new String(pwSenha.getPassword()).trim();

			    if (nomeUsuario.isEmpty() || senhaDigitada.isEmpty()) {
			        JOptionPane.showMessageDialog(null, "Informe usuário e senha.");
			        return;
			    }
			    Usuario usuario = usuarioService.autenticar(nomeUsuario, senhaDigitada);
			    
			    Usuario u = usuarioDAO.buscarPorNomeUsuario(nomeUsuario);

			    if (usuario != null) {
			        JOptionPane.showMessageDialog(null, "Login realizado com sucesso!");
			    } else {
			        JOptionPane.showMessageDialog(null, "Usuário ou senha incorretos!");
			    }
			}
		});
		btnLogin.setBounds(57, 122, 93, 33);
		contentPane.add(btnLogin);
		
		JButton btnCadastrar = new JButton("Cadastrar");
		btnCadastrar.setBounds(175, 122, 93, 33);
		contentPane.add(btnCadastrar);
	}
}
