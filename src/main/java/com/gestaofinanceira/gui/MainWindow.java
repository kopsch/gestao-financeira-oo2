package com.gestaofinanceira.gui;

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.gestaofinanceira.dto.MovimentacaoDTO;
import com.gestaofinanceira.aggregator.MovimentacoesAggregator;
import com.gestaofinanceira.model.Conta;
import com.gestaofinanceira.model.Despesa;
import com.gestaofinanceira.model.Receita;
import com.gestaofinanceira.model.Usuario;
import com.gestaofinanceira.service.DespesaService;
import com.gestaofinanceira.service.ReceitaService;
import com.gestaofinanceira.service.RelatorioService;

public class MainWindow extends JFrame {

    private Usuario usuario;
    private Conta conta;

    private JTable tabela;
    private DefaultTableModel modelo;
    private MovimentacoesAggregator aggregator = new MovimentacoesAggregator();

    private JLabel lblSaldo; 
    private JComboBox<String> cbMes;
    private JComboBox<String> cbAno;

    public MainWindow(Usuario usuario, Conta conta) {
        this.usuario = usuario;
        this.conta = conta;

        setTitle("Painel Financeiro");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        carregarMovimentacoes();
    }

    private void initComponents() {
     
        JPanel painelTop = new JPanel(new GridLayout(2, 1, 10, 10));

        JPanel painelBotoes = new JPanel(new GridLayout(1, 2, 10, 10));
        JButton btnReceitas = new JButton("Abrir Receitas");
        btnReceitas.addActionListener(e -> new ReceitasWindow(usuario, conta, this).setVisible(true));

        JButton btnDespesas = new JButton("Abrir Despesas");
        btnDespesas.addActionListener(e -> new DespesasWindow(usuario, conta, this).setVisible(true));

        painelBotoes.add(btnReceitas);
        painelBotoes.add(btnDespesas);

        
        JPanel painelPeriodo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cbMes = new JComboBox<>(new String[]{"01","02","03","04","05","06","07","08","09","10","11","12"});
        cbAno = new JComboBox<>();
        int anoAtual = LocalDate.now().getYear();
        for(int i = anoAtual; i >= 2020; i--) cbAno.addItem(String.valueOf(i));

        JButton btnRelatorioMensal = new JButton("Relatório Mensal");
        btnRelatorioMensal.addActionListener(e -> gerarRelatorioMensal());

        JButton btnRelatorioAnual = new JButton("Relatório Anual");
        btnRelatorioAnual.addActionListener(e -> gerarRelatorioAnual());

        painelPeriodo.add(new JLabel("Mês:"));
        painelPeriodo.add(cbMes);
        painelPeriodo.add(new JLabel("Ano:"));
        painelPeriodo.add(cbAno);
        painelPeriodo.add(btnRelatorioMensal);
        painelPeriodo.add(btnRelatorioAnual);

        painelTop.add(painelBotoes);
        painelTop.add(painelPeriodo);

       
        modelo = new DefaultTableModel(new Object[] { "Tipo", "Categoria/Tipo", "Valor", "Data" }, 0);
        tabela = new JTable(modelo);
        tabela.setRowHeight(26);
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setPreferredSize(new Dimension(850, 350));

        
        lblSaldo = new JLabel("Saldo: R$ 0.00", SwingConstants.CENTER);

        add(painelTop, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(lblSaldo, BorderLayout.SOUTH);
    }

    public void carregarMovimentacoes() {
        modelo.setRowCount(0);

        List<MovimentacaoDTO> lista = aggregator.carregarMovimentacoesPorUsuario(usuario.getId());
        for (MovimentacaoDTO mov : lista) {
            modelo.addRow(new Object[] {
                mov.getTipoMovimentacao(),
                mov.getCategoriaOuTipo(),
                mov.getValor(),
                mov.getData()
            });
        }

        atualizarSaldo();
    }

    private void atualizarSaldo() {
        BigDecimal saldo = aggregator.calcularSaldo(usuario.getId());
        lblSaldo.setText("Saldo: R$ " + saldo);
    }

    
    private void gerarRelatorioMensal() {
        String mes = cbMes.getSelectedItem().toString();
        String ano = cbAno.getSelectedItem().toString();

        try {
            List<Receita> receitas = new ReceitaService().listarPorUsuario(usuario.getId());
            List<Despesa> despesas = new DespesaService().listarPorUsuario(usuario.getId());

            int mesInt = Integer.parseInt(mes);
            int anoInt = Integer.parseInt(ano);
            receitas.removeIf(r -> r.getData().getMonthValue() != mesInt || r.getData().getYear() != anoInt);
            despesas.removeIf(d -> d.getData().getMonthValue() != mesInt || d.getData().getYear() != anoInt);

            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Salvar Relatório Mensal");
            chooser.setSelectedFile(new java.io.File("relatorio_mensal"));
            if(chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

            String caminhoBase = chooser.getSelectedFile().getAbsolutePath();

            RelatorioService relatorioService = new RelatorioService();
            relatorioService.exportarRelatorioMensalPDF(receitas, despesas, mes + "/" + ano, caminhoBase + ".pdf");
            relatorioService.exportarRelatorioMensalExcel(receitas, despesas, mes + "/" + ano, caminhoBase + ".xls");

            JOptionPane.showMessageDialog(this, "Relatório mensal gerado em PDF e XLS!");
        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar relatório mensal: " + e.getMessage());
        }
    }

    private void gerarRelatorioAnual() {
        String ano = cbAno.getSelectedItem().toString();

        try {
            List<Receita> receitas = new ReceitaService().listarPorUsuario(usuario.getId());
            List<Despesa> despesas = new DespesaService().listarPorUsuario(usuario.getId());

            int anoInt = Integer.parseInt(ano);
            receitas.removeIf(r -> r.getData().getYear() != anoInt);
            despesas.removeIf(d -> d.getData().getYear() != anoInt);

            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Salvar Relatório Anual");
            chooser.setSelectedFile(new java.io.File("relatorio_anual"));
            if(chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

            String caminhoBase = chooser.getSelectedFile().getAbsolutePath();

            RelatorioService relatorioService = new RelatorioService();
            relatorioService.exportarRelatorioAnualPDF(receitas, despesas, ano, caminhoBase + ".pdf");
            relatorioService.exportarRelatorioAnualExcel(receitas, despesas, ano, caminhoBase + ".xls");

            JOptionPane.showMessageDialog(this, "Relatório anual gerado em PDF e XLS!");
        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar relatório anual: " + e.getMessage());
        }
    }

    public void atualizarDados() {
        carregarMovimentacoes();
    }

    public static void main(String[] args) {
        Usuario u = new Usuario();
        // coloquei o ID da conta e do usuário só para teste, nesse caso deveria receber esses 2 da interface de login
        u.setId(java.util.UUID.fromString("8f14e45f-ea9b-4b67-bc2a-9c1f7b9a1234"));

        Conta c = new Conta();
        c.setId(java.util.UUID.fromString("0c41e75b-32f4-49ea-a137-6102c11bb4a2"));

        SwingUtilities.invokeLater(() -> new MainWindow(u, c).setVisible(true));
    }
}
