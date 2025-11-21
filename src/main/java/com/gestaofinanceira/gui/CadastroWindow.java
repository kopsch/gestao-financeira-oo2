package com.gestaofinanceira.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import javax.swing.JRadioButton;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CadastroWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField txtUsuario;
	private JPasswordField passwordField;
	private static LoginWindow loginWindow;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CadastroWindow frame = new CadastroWindow(loginWindow);
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
	
	private void cancelar(LoginWindow loginWindow) {
		this.loginWindow = loginWindow;
		this.dispose();
		loginWindow.setVisible(true);
	}
	
	private void cadastrar(LoginWindow loginWindow) {
		this.loginWindow = loginWindow;
		this.dispose();
		loginWindow.setVisible(true);
	}
	
	public void iniciarComponentes() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 512, 283);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNomeCompleto = new JLabel("Nome completo:");
		lblNomeCompleto.setBounds(10, 44, 83, 14);
		contentPane.add(lblNomeCompleto);
		
		textField = new JTextField();
		textField.setBounds(128, 41, 350, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblDataNascimento = new JLabel("Data de Nascimento:");
		lblDataNascimento.setBounds(10, 86, 108, 14);
		contentPane.add(lblDataNascimento);
		
		JFormattedTextField txtDataNascimento = new JFormattedTextField();
		txtDataNascimento.setBounds(128, 83, 99, 20);
		contentPane.add(txtDataNascimento);
		
		JLabel lblSexo = new JLabel("Sexo:");
		lblSexo.setBounds(284, 85, 46, 14);
		contentPane.add(lblSexo);
		
		JRadioButton rdbtnMasculino = new JRadioButton("Masculino");
		rdbtnMasculino.setBounds(322, 81, 71, 23);
		contentPane.add(rdbtnMasculino);
		
		JRadioButton rdbtnFeminino = new JRadioButton("Feminino");
		rdbtnFeminino.setBounds(406, 81, 71, 23);
		contentPane.add(rdbtnFeminino);
		
		JLabel lblUsuario = new JLabel("Usuário:");
		lblUsuario.setBounds(9, 127, 46, 14);
		contentPane.add(lblUsuario);
		
		txtUsuario = new JTextField();
		txtUsuario.setBounds(59, 124, 168, 20);
		contentPane.add(txtUsuario);
		txtUsuario.setColumns(10);
		
		JLabel lblSenha = new JLabel("Senha:");
		lblSenha.setBounds(284, 127, 46, 14);
		contentPane.add(lblSenha);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(322, 124, 156, 20);
		contentPane.add(passwordField);
		
		JButton btnCadastrar = new JButton("Cadastrar");
		btnCadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cadastrar(loginWindow);
			}
		});
		btnCadastrar.setBounds(261, 184, 89, 23);
		contentPane.add(btnCadastrar);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelar(loginWindow);
			}
		});
		btnCancelar.setBounds(162, 184, 89, 23);
		contentPane.add(btnCancelar);
	}
	
	public CadastroWindow(LoginWindow loginWindow) {
		this.loginWindow = loginWindow;
		iniciarComponentes();
	}

	/*
	 * public CadastroWindow() { // TODO Auto-generated constructor stub }
	 */
}
