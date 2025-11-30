package com.gestaofinanceira.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.gestaofinanceira.model.Receita;
import com.gestaofinanceira.model.TipoReceita;
import com.gestaofinanceira.model.Usuario;
import com.gestaofinanceira.model.Conta;
import com.gestaofinanceira.service.ReceitaService;

public class ReceitasWindow extends JFrame {

    private final ReceitaService service = new ReceitaService();

    private Usuario usuario;
    private Conta conta;

    private JTable tabela;
    private DefaultTableModel modelo;

    private JComboBox<TipoReceita> cbTipo;
    private JTextField txtValor;
    private JTextField txtDescricao;
    private JFormattedTextField txtData;
    private MainWindow mainWindow;

    public ReceitasWindow(Usuario usuario, Conta conta, MainWindow mainWindow) {
        this.usuario = usuario;
        this.conta = conta;
        this.mainWindow = mainWindow;

        setTitle("Cadastro de Receitas");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        carregarReceitas();
    }

    private void initComponents() {
        JPanel painelFormulario = new JPanel(new GridLayout(5, 2, 10, 10));

        cbTipo = new JComboBox<>(TipoReceita.values());
        txtValor = new JTextField();
        txtDescricao = new JTextField();

        txtData = new JFormattedTextField(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        txtData.setValue(LocalDate.now().toString());

        painelFormulario.add(new JLabel("Tipo:"));
        painelFormulario.add(cbTipo);

        painelFormulario.add(new JLabel("Valor:"));
        painelFormulario.add(txtValor);

        painelFormulario.add(new JLabel("Descrição:"));
        painelFormulario.add(txtDescricao);

        painelFormulario.add(new JLabel("Data (yyyy-MM-dd):"));
        painelFormulario.add(txtData);

        JButton btnSalvar = new JButton("Salvar Receita");
        btnSalvar.addActionListener(e -> salvarReceita());

        painelFormulario.add(new JLabel());
        painelFormulario.add(btnSalvar);

        modelo = new DefaultTableModel(new Object[] { "ID", "Tipo", "Valor", "Data", "Descrição" }, 0);
        tabela = new JTable(modelo);
        tabela.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setPreferredSize(new Dimension(650, 300));

        add(painelFormulario, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    private void carregarReceitas() {
        modelo.setRowCount(0);

        List<Receita> receitas = service.listarPorUsuario(usuario.getId());

        for (Receita r : receitas) {
            modelo.addRow(new Object[] {
                r.getId(),
                r.getTipo(),
                r.getValor(),
                r.getData(),
                r.getDescricao()
            });
        }
    }

    private void salvarReceita() {
        try {
            Receita r = new Receita();
            r.setId(UUID.randomUUID());
            r.setUsuario(usuario);
            r.setConta(conta);
            r.setTipo((TipoReceita) cbTipo.getSelectedItem());
            r.setValor(new BigDecimal(txtValor.getText()));
            r.setDescricao(txtDescricao.getText());
            r.setData(LocalDate.parse(txtData.getText()));

            service.salvar(r);

            JOptionPane.showMessageDialog(this, "Receita salva com sucesso!");
            carregarReceitas();
            if (mainWindow != null) {
                mainWindow.atualizarDados();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar receita: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Teste igual da main
    public static void main(String[] args) {
        Usuario u = new Usuario();
        u.setId(UUID.fromString("8f14e45f-ea9b-4b67-bc2a-9c1f7b9a1234"));     

        Conta c = new Conta();
        c.setId(UUID.fromString("0c41e75b-32f4-49ea-a137-6102c11bb4a2"));

    }
}