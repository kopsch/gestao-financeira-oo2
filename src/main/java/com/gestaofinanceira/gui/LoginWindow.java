package com.gestaofinanceira.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField pswSenha;

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
		this.iniciarComponentes();
	}
	
	private void abrirCadastro() {
		CadastroWindow cadastro = new CadastroWindow(this);
		cadastro.setVisible(true);
		
		this.setVisible(false);
	}
	
	public void iniciarComponentes() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblLogin = new JLabel("Login");
		lblLogin.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblLogin.setBounds(188, 44, 77, 21);
		contentPane.add(lblLogin);
		
		JLabel lblUsername = new JLabel("Usuário:");
		lblUsername.setBounds(112, 95, 54, 14);
		contentPane.add(lblUsername);
		
		textField = new JTextField();
		textField.setBounds(179, 92, 148, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblSenha = new JLabel("Senha:");
		lblSenha.setBounds(112, 142, 46, 14);
		contentPane.add(lblSenha);
		
		pswSenha = new JPasswordField();
		pswSenha.setBounds(179, 139, 148, 20);
		contentPane.add(pswSenha);
		
		JButton btnLogin = new JButton("Entrar");
		btnLogin.setBounds(123, 196, 89, 23);
		contentPane.add(btnLogin);
		
		JButton btnCadastro = new JButton("Cadastre-se");
		
		
		
		btnCadastro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirCadastro();
			}
		});
		btnCadastro.setBounds(221, 196, 97, 23);
		contentPane.add(btnCadastro);
	}
}
