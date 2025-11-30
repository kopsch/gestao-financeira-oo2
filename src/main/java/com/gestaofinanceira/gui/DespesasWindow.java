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

import com.gestaofinanceira.model.*;
import com.gestaofinanceira.service.DespesaService;
import com.gestaofinanceira.service.CategoriaDespesaService;

public class DespesasWindow extends JFrame {

    private final DespesaService despesaService = new DespesaService();
    private final CategoriaDespesaService categoriaService = new CategoriaDespesaService();

    private Usuario usuario;
    private Conta conta;

    private JTable tabela;
    private DefaultTableModel modelo;

    private JComboBox<TipoDespesa> cbTipo;
    private JComboBox<CategoriaDespesa> cbCategoria;
    private JTextField txtValor;
    private JTextField txtDescricao;
    private JFormattedTextField txtData;
    private MainWindow mainWindow;

    public DespesasWindow(Usuario usuario, Conta conta, MainWindow mainWindow) {
        this.usuario = usuario;
        this.conta = conta;
        this.mainWindow = mainWindow;

        setTitle("Cadastro de Despesas");
        setSize(750, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        carregarDespesas();
    }

    private void initComponents() {
        JPanel painelFormulario = new JPanel(new GridLayout(6, 2, 10, 10));

        cbTipo = new JComboBox<>(TipoDespesa.values());
        txtValor = new JTextField();
        txtDescricao = new JTextField();

        cbCategoria = new JComboBox<>();
        carregarCategorias();

        txtData = new JFormattedTextField(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        txtData.setValue(LocalDate.now().toString());

        painelFormulario.add(new JLabel("Tipo de Despesa:"));
        painelFormulario.add(cbTipo);

        painelFormulario.add(new JLabel("Categoria:"));
        painelFormulario.add(cbCategoria);

        painelFormulario.add(new JLabel("Valor:"));
        painelFormulario.add(txtValor);

        painelFormulario.add(new JLabel("Descrição:"));
        painelFormulario.add(txtDescricao);

        painelFormulario.add(new JLabel("Data (yyyy-MM-dd):"));
        painelFormulario.add(txtData);

        JButton btnSalvar = new JButton("Salvar Despesa");
        btnSalvar.addActionListener(e -> salvarDespesa());

        painelFormulario.add(new JLabel());
        painelFormulario.add(btnSalvar);

        modelo = new DefaultTableModel(new Object[] { "ID", "Tipo", "Categoria", "Valor", "Data", "Descrição" }, 0);
        tabela = new JTable(modelo);
        tabela.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setPreferredSize(new Dimension(680, 300));

        add(painelFormulario, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    private void carregarCategorias() {
        cbCategoria.removeAllItems();

        List<CategoriaDespesa> categorias = categoriaService.listarPorUsuario(usuario.getId());

        for (CategoriaDespesa c : categorias) {
            cbCategoria.addItem(c);
        }
    }

    private void carregarDespesas() {
        modelo.setRowCount(0);

        List<Despesa> despesas = despesaService.listarPorUsuario(usuario.getId());

        for (Despesa d : despesas) {

            String tipo = (d.getTipoDespesa() != null) ? d.getTipoDespesa().name() : "(sem tipo)";

            modelo.addRow(new Object[] {
                d.getId(),
                tipo,
                (d.getCategoria() != null ? d.getCategoria().getNome() : "(sem categoria)"),
                d.getValor(),
                d.getData(),
                d.getDescricao()
            });
        }
    }

    private void salvarDespesa() {
        try {
            if (cbTipo.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Escolha um tipo de despesa.");
                return;
            }

            if (cbCategoria.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Escolha uma categoria.");
                return;
            }

            Despesa d = new Despesa();
            d.setId(UUID.randomUUID());
            d.setUsuario(usuario);
            d.setConta(conta);
            d.setTipoDespesa((TipoDespesa) cbTipo.getSelectedItem());
            d.setCategoria((CategoriaDespesa) cbCategoria.getSelectedItem());
            d.setValor(new BigDecimal(txtValor.getText()));
            d.setDescricao(txtDescricao.getText());
            d.setData(LocalDate.parse(txtData.getText()));

            despesaService.salvar(d);

            JOptionPane.showMessageDialog(this, "Despesa salva com sucesso!");
            carregarDespesas();
            if (mainWindow != null) {
                mainWindow.atualizarDados();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar despesa: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {

        Usuario u = new Usuario();
        u.setId(UUID.fromString("8f14e45f-ea9b-4b67-bc2a-9c1f7b9a1234"));

        Conta c = new Conta();
        c.setId(UUID.fromString("0c41e75b-32f4-49ea-a137-6102c11bb4a2"));

        
    }
}