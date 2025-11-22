package com.gestaofinanceira.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MovimentacoesWindow extends JFrame {

    private JPanel contentPane;
    private JComboBox<String> cbTipoMov;
    private JComboBox<String> cbCategoria;
    private JTextField tfValor;
    private JFormattedTextField tfData;
    private JTextField tfDescricao;
    private JTable table;
    private JLabel lblSaldo;

    private BigDecimal saldoAtual = BigDecimal.ZERO;

    public MovimentacoesWindow() {
        setTitle("Gestão Financeira");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 750, 520);

        initComponents();
        initListeners();
        initLayout();

        preencherDataAtual();
        carregarCategorias();
    }

    private void initComponents() {
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        cbTipoMov = new JComboBox<>(new String[]{"Receita", "Despesa"});
        cbCategoria = new JComboBox<>();

        tfValor = new JTextField();

        try {
            MaskFormatter mask = new MaskFormatter("##/##/####");
            tfData = new JFormattedTextField(mask);
        } catch (ParseException e) {
            tfData = new JFormattedTextField();
        }

        tfDescricao = new JTextField();

        table = new JTable();
        table.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Tipo", "Categoria", "Valor", "Data", "Descrição"}
        ));

        lblSaldo = new JLabel("Saldo Atual: R$ 0,00");
        lblSaldo.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblSaldo.setHorizontalAlignment(SwingConstants.RIGHT);
    }

    private void initListeners() {
        cbTipoMov.addActionListener(e -> carregarCategorias());
    }

    private void initLayout() {
        JLabel lblTipoMov = new JLabel("Movimentação:");
        lblTipoMov.setBounds(10, 10, 120, 20);
        contentPane.add(lblTipoMov);

        cbTipoMov.setBounds(130, 10, 150, 22);
        contentPane.add(cbTipoMov);

        JLabel lblCategoria = new JLabel("Categoria:");
        lblCategoria.setBounds(10, 45, 120, 20);
        contentPane.add(lblCategoria);

        cbCategoria.setBounds(130, 45, 150, 22);
        contentPane.add(cbCategoria);

        JLabel lblValor = new JLabel("Valor (R$):");
        lblValor.setBounds(10, 80, 120, 20);
        contentPane.add(lblValor);

        tfValor.setBounds(130, 80, 150, 22);
        contentPane.add(tfValor);

        JLabel lblData = new JLabel("Data:");
        lblData.setBounds(10, 115, 120, 20);
        contentPane.add(lblData);

        tfData.setBounds(130, 115, 150, 22);
        contentPane.add(tfData);

        JLabel lblDescricao = new JLabel("Descrição:");
        lblDescricao.setBounds(10, 150, 120, 20);
        contentPane.add(lblDescricao);

        tfDescricao.setBounds(130, 150, 300, 22);
        contentPane.add(tfDescricao);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(this::salvarMov);
        btnSalvar.setBounds(10, 185, 130, 30);
        contentPane.add(btnSalvar);

        JButton btnLimpar = new JButton("Limpar Campos");
        btnLimpar.addActionListener(e -> limparCampos());
        btnLimpar.setBounds(150, 185, 150, 30);
        contentPane.add(btnLimpar);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 230, 710, 220);
        contentPane.add(scrollPane);

        lblSaldo.setBounds(380, 455, 340, 20);
        contentPane.add(lblSaldo);
    }

    private void preencherDataAtual() {
        String hoje = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        tfData.setText(hoje);
    }

    private void carregarCategorias() {
        cbCategoria.removeAllItems();
        String tipo = cbTipoMov.getSelectedItem().toString();

        if (tipo.equals("Receita")) {
            cbCategoria.addItem("Salário");
            cbCategoria.addItem("Rendimento");
            cbCategoria.addItem("Extra");
            cbCategoria.addItem("Outros");
        } else {
            cbCategoria.addItem("Fixa");
            cbCategoria.addItem("Variável");
        }
    }

    private void salvarMov(ActionEvent e) {
        String tipo = cbTipoMov.getSelectedItem().toString();
        String categoria = cbCategoria.getSelectedItem().toString();
        String valorStr = tfValor.getText().replace(",", ".");
        String data = tfData.getText();
        String desc = tfDescricao.getText();

        if (valorStr.isBlank()) {
            JOptionPane.showMessageDialog(this, "Digite um valor.");
            return;
        }

        BigDecimal valor = new BigDecimal(valorStr);

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.addRow(new Object[]{tipo, categoria, valor, data, desc});

        if (tipo.equals("Receita"))
            saldoAtual = saldoAtual.add(valor);
        else
            saldoAtual = saldoAtual.subtract(valor);

        atualizarSaldo();
        limparCampos();
        preencherDataAtual();
    }

    private void limparCampos() {
        tfValor.setText("");
        tfDescricao.setText("");
    }

    private void atualizarSaldo() {
        lblSaldo.setText("Saldo Atual: R$ " + saldoAtual);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MovimentacoesWindow frame = new MovimentacoesWindow();
            frame.setVisible(true);
        });
    }
}