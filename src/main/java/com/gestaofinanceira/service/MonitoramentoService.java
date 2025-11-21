package com.gestaofinanceira.service;

import com.gestaofinanceira.model.Conta;
import com.gestaofinanceira.model.Meta;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class MonitoramentoService implements Runnable {

    private final ContaService contaService;
    private final MetaService metaService;
    private volatile boolean ativo;
    
    private final List<MonitoramentoListener> ouvintes = new ArrayList<>();

    public MonitoramentoService() {
        this.contaService = new ContaService();
        this.metaService = new MetaService();
        this.ativo = true;
    }

    public void adicionarOuvinte(MonitoramentoListener listener) {
        this.ouvintes.add(listener);
    }

    public void parar() {
        this.ativo = false;
    }

    @Override
    public void run() {
        try {
            while (ativo) {
                verificarSaldosNegativos();
                verificarPrazosMetas();
                Thread.sleep(60000);
            }
        } catch (InterruptedException e) {
            notificar("Monitoramento interrompido.");
            Thread.currentThread().interrupt();
        }
    }

    private void notificar(String mensagem) {
        System.out.println(mensagem);

        for (MonitoramentoListener listener : ouvintes) {
            listener.onAlerta(mensagem);
        }
    }

    private void verificarSaldosNegativos() {
        try {
            List<Conta> contas = contaService.listarTodos();
            for (Conta conta : contas) {
                if (conta.getSaldo().compareTo(BigDecimal.ZERO) < 0) {
                    String msg = "[ALERTA CRÍTICO] A conta " + conta.getNomeDoBanco() + 
                                 " (Ag: " + conta.getAgencia() + ") está negativada! Saldo: R$ " + conta.getSaldo();
                    notificar(msg);
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao verificar saldos: " + e.getMessage());
        }
    }

    private void verificarPrazosMetas() {
        try {
            List<Meta> metas = metaService.listarTodos();
            LocalDate hoje = LocalDate.now();

            for (Meta meta : metas) {
                long diasParaVencer = ChronoUnit.DAYS.between(hoje, meta.getDataLimite());

                if (diasParaVencer >= 0 && diasParaVencer <= 7) {
                    String msg = "[LEMBRETE] A meta '" + meta.getDescricao() + 
                                 "' vence em " + diasParaVencer + " dias! (Data: " + meta.getDataLimite() + ")";
                    notificar(msg);
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao verificar metas: " + e.getMessage());
        }
    }
}