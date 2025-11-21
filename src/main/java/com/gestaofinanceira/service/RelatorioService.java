package com.gestaofinanceira.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.gestaofinanceira.model.Despesa;
import com.gestaofinanceira.model.Receita;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class RelatorioService {

    private static final DateTimeFormatter FORMATTER_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void exportarRelatorioMensalPDF(List<Receita> receitas, List<Despesa> despesas, String mesReferencia,
            String caminhoArquivo) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(caminhoArquivo));
            document.open();

            adicionarCabecalhoPDF(document, "Relatório Mensal", mesReferencia);

            adicionarSubtituloPDF(document, "1. Detalhamento de Receitas");
            PdfPTable tabelaRec = new PdfPTable(3);
            tabelaRec.setWidthPercentage(100);
            tabelaRec.addCell(criarCelulaNegrito("Data"));
            tabelaRec.addCell(criarCelulaNegrito("Descrição"));
            tabelaRec.addCell(criarCelulaNegrito("Valor (R$)"));

            BigDecimal totalReceitas = BigDecimal.ZERO;
            for (Receita r : receitas) {
                tabelaRec.addCell(r.getData().format(FORMATTER_DATA));
                tabelaRec.addCell(r.getDescricao());
                tabelaRec.addCell(r.getValor().toString());
                totalReceitas = totalReceitas.add(r.getValor());
            }
            document.add(tabelaRec);
            adicionarTotalPDF(document, "Total Receitas", totalReceitas);

            adicionarSubtituloPDF(document, "2. Detalhamento de Despesas");
            PdfPTable tabelaDesp = new PdfPTable(4);
            tabelaDesp.setWidthPercentage(100);
            tabelaDesp.addCell(criarCelulaNegrito("Data"));
            tabelaDesp.addCell(criarCelulaNegrito("Descrição"));
            tabelaDesp.addCell(criarCelulaNegrito("Categoria"));
            tabelaDesp.addCell(criarCelulaNegrito("Valor (R$)"));

            BigDecimal totalDespesas = BigDecimal.ZERO;
            for (Despesa d : despesas) {
                tabelaDesp.addCell(d.getData().format(FORMATTER_DATA));
                tabelaDesp.addCell(d.getDescricao());
                tabelaDesp.addCell(d.getCategoria().getNome());
                tabelaDesp.addCell(d.getValor().toString());
                totalDespesas = totalDespesas.add(d.getValor());
            }
            document.add(tabelaDesp);
            adicionarTotalPDF(document, "Total Despesas", totalDespesas);

            document.add(new Paragraph("------------------------------------------------"));
            BigDecimal saldo = totalReceitas.subtract(totalDespesas);
            Paragraph pSaldo = new Paragraph("SALDO DO MÊS: R$ " + saldo,
                    new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
            pSaldo.setAlignment(Element.ALIGN_CENTER);
            document.add(pSaldo);

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Erro PDF Mensal: " + e.getMessage(), e);
        }
    }

    public void exportarRelatorioMensalExcel(List<Receita> receitas, List<Despesa> despesas, String mesReferencia,
            String caminhoArquivo) {
        try (HSSFWorkbook workbook = new HSSFWorkbook()) {
            HSSFSheet sheet = workbook.createSheet("Mensal");
            HSSFCellStyle boldStyle = criarEstiloNegrito(workbook);

            int rowNum = 0;

            rowNum = adicionarCabecalhoExcel(sheet, boldStyle, "RELATÓRIO MENSAL", mesReferencia, rowNum);

            rowNum = criarLinhaTitulo(sheet, boldStyle, rowNum, "RECEITAS DETALHADAS");
            HSSFRow headRec = sheet.createRow(rowNum++);
            headRec.createCell(0).setCellValue("Data");
            headRec.createCell(1).setCellValue("Descrição");
            headRec.createCell(2).setCellValue("Valor");
            aplicarEstilo(headRec, boldStyle, 3);

            BigDecimal totalRec = BigDecimal.ZERO;
            for (Receita r : receitas) {
                HSSFRow row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(r.getData().format(FORMATTER_DATA));
                row.createCell(1).setCellValue(r.getDescricao());
                row.createCell(2).setCellValue(r.getValor().doubleValue());
                totalRec = totalRec.add(r.getValor());
            }
            rowNum = adicionarTotalExcel(sheet, boldStyle, rowNum, "TOTAL RECEITAS:", totalRec);

            rowNum = criarLinhaTitulo(sheet, boldStyle, rowNum, "DESPESAS DETALHADAS");
            HSSFRow headDesp = sheet.createRow(rowNum++);
            headDesp.createCell(0).setCellValue("Data");
            headDesp.createCell(1).setCellValue("Descrição");
            headDesp.createCell(2).setCellValue("Categoria");
            headDesp.createCell(3).setCellValue("Valor");
            aplicarEstilo(headDesp, boldStyle, 4);

            BigDecimal totalDesp = BigDecimal.ZERO;
            for (Despesa d : despesas) {
                HSSFRow row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(d.getData().format(FORMATTER_DATA));
                row.createCell(1).setCellValue(d.getDescricao());
                row.createCell(2).setCellValue(d.getCategoria().getNome());
                row.createCell(3).setCellValue(d.getValor().doubleValue());
                totalDesp = totalDesp.add(d.getValor());
            }
            rowNum = adicionarTotalExcel(sheet, boldStyle, rowNum, "TOTAL DESPESAS:", totalDesp);

            HSSFRow rowSaldo = sheet.createRow(rowNum++);
            rowSaldo.createCell(0).setCellValue("SALDO FINAL:");
            rowSaldo.createCell(1).setCellValue(totalRec.subtract(totalDesp).doubleValue());
            rowSaldo.getCell(0).setCellStyle(boldStyle);

            salvarArquivoExcel(workbook, caminhoArquivo);
        } catch (Exception e) {
            throw new RuntimeException("Erro Excel Mensal: " + e.getMessage(), e);
        }
    }

    public void exportarRelatorioAnualPDF(List<Receita> receitas, List<Despesa> despesas, String anoReferencia,
            String caminhoArquivo) {
        Map<String, BigDecimal> receitasPorTipo = agruparReceitasPorTipo(receitas);
        Map<String, BigDecimal> despesasPorCategoria = agruparDespesasPorCategoria(despesas);

        BigDecimal totalReceitas = calcularTotalReceitas(receitas);
        BigDecimal totalDespesas = calcularTotalDespesas(despesas);

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(caminhoArquivo));
            document.open();

            adicionarCabecalhoPDF(document, "Relatório Anual Consolidado", anoReferencia);

            adicionarSubtituloPDF(document, "1. Resumo Geral");
            document.add(new Paragraph("Total Entradas: R$ " + totalReceitas));
            document.add(new Paragraph("Total Saídas:   R$ " + totalDespesas));
            document.add(new Paragraph("Saldo Anual:    R$ " + totalReceitas.subtract(totalDespesas)));
            document.add(new Paragraph(" "));

            adicionarSubtituloPDF(document, "2. Receitas por Tipo");
            PdfPTable tableRec = new PdfPTable(2);
            tableRec.setWidthPercentage(100);
            tableRec.addCell(criarCelulaNegrito("Tipo de Receita"));
            tableRec.addCell(criarCelulaNegrito("Total (R$)"));

            for (Map.Entry<String, BigDecimal> entry : receitasPorTipo.entrySet()) {
                tableRec.addCell(entry.getKey());
                tableRec.addCell(entry.getValue().toString());
            }
            document.add(tableRec);
            document.add(new Paragraph(" "));

            adicionarSubtituloPDF(document, "3. Despesas por Categoria");
            PdfPTable tableDesp = new PdfPTable(2);
            tableDesp.setWidthPercentage(100);
            tableDesp.addCell(criarCelulaNegrito("Categoria"));
            tableDesp.addCell(criarCelulaNegrito("Total (R$)"));

            for (Map.Entry<String, BigDecimal> entry : despesasPorCategoria.entrySet()) {
                tableDesp.addCell(entry.getKey());
                tableDesp.addCell(entry.getValue().toString());
            }
            document.add(tableDesp);

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Erro PDF Anual: " + e.getMessage(), e);
        }
    }

    public void exportarRelatorioAnualExcel(List<Receita> receitas, List<Despesa> despesas, String anoReferencia,
            String caminhoArquivo) {
        Map<String, BigDecimal> receitasPorTipo = agruparReceitasPorTipo(receitas);
        Map<String, BigDecimal> despesasPorCategoria = agruparDespesasPorCategoria(despesas);

        BigDecimal totalReceitas = calcularTotalReceitas(receitas);
        BigDecimal totalDespesas = calcularTotalDespesas(despesas);

        try (HSSFWorkbook workbook = new HSSFWorkbook()) {
            HSSFSheet sheet = workbook.createSheet("Anual");
            HSSFCellStyle boldStyle = criarEstiloNegrito(workbook);

            int rowNum = 0;
            rowNum = adicionarCabecalhoExcel(sheet, boldStyle, "RELATÓRIO ANUAL", anoReferencia, rowNum);

            rowNum = criarLinhaTitulo(sheet, boldStyle, rowNum, "RESUMO GERAL");
            HSSFRow rowResumo1 = sheet.createRow(rowNum++);
            rowResumo1.createCell(0).setCellValue("Total Receitas:");
            rowResumo1.createCell(1).setCellValue(totalReceitas.doubleValue());

            HSSFRow rowResumo2 = sheet.createRow(rowNum++);
            rowResumo2.createCell(0).setCellValue("Total Despesas:");
            rowResumo2.createCell(1).setCellValue(totalDespesas.doubleValue());

            HSSFRow rowResumo3 = sheet.createRow(rowNum++);
            rowResumo3.createCell(0).setCellValue("SALDO ANUAL:");
            rowResumo3.createCell(1).setCellValue(totalReceitas.subtract(totalDespesas).doubleValue());
            rowResumo3.getCell(0).setCellStyle(boldStyle);

            rowNum = criarLinhaTitulo(sheet, boldStyle, rowNum, "RECEITAS POR TIPO");
            HSSFRow headRec = sheet.createRow(rowNum++);
            headRec.createCell(0).setCellValue("Tipo");
            headRec.createCell(1).setCellValue("Valor Total");
            aplicarEstilo(headRec, boldStyle, 2);

            for (Map.Entry<String, BigDecimal> entry : receitasPorTipo.entrySet()) {
                HSSFRow row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(entry.getKey());
                row.createCell(1).setCellValue(entry.getValue().doubleValue());
            }

            rowNum = criarLinhaTitulo(sheet, boldStyle, rowNum, "DESPESAS POR CATEGORIA");
            HSSFRow headDesp = sheet.createRow(rowNum++);
            headDesp.createCell(0).setCellValue("Categoria");
            headDesp.createCell(1).setCellValue("Valor Total");
            aplicarEstilo(headDesp, boldStyle, 2);

            for (Map.Entry<String, BigDecimal> entry : despesasPorCategoria.entrySet()) {
                HSSFRow row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(entry.getKey());
                row.createCell(1).setCellValue(entry.getValue().doubleValue());
            }

            salvarArquivoExcel(workbook, caminhoArquivo);
        } catch (Exception e) {
            throw new RuntimeException("Erro Excel Anual: " + e.getMessage(), e);
        }
    }

    private void adicionarCabecalhoPDF(Document doc, String titulo, String referencia) throws DocumentException {
        Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph pTitulo = new Paragraph(titulo + " - " + referencia, fontTitulo);
        pTitulo.setAlignment(Element.ALIGN_CENTER);
        doc.add(pTitulo);

        Font fontData = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
        Paragraph pData = new Paragraph("Data de Emissão: " + LocalDate.now().format(FORMATTER_DATA), fontData);
        pData.setAlignment(Element.ALIGN_RIGHT);
        doc.add(pData);
        doc.add(new Paragraph(" "));
    }

    private void adicionarSubtituloPDF(Document doc, String texto) throws DocumentException {
        doc.add(new Paragraph(texto, new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD)));
        doc.add(new Paragraph(" "));
    }

    private PdfPCell criarCelulaNegrito(String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        return cell;
    }

    private void adicionarTotalPDF(Document doc, String label, BigDecimal valor) throws DocumentException {
        Paragraph p = new Paragraph(label + ": R$ " + valor);
        p.setAlignment(Element.ALIGN_RIGHT);
        doc.add(p);
        doc.add(new Paragraph(" "));
    }

    private int adicionarCabecalhoExcel(HSSFSheet sheet, HSSFCellStyle style, String titulo, String referencia,
            int rowNum) {
        HSSFRow rowTitulo = sheet.createRow(rowNum++);
        rowTitulo.createCell(0).setCellValue(titulo + ": " + referencia);
        rowTitulo.getCell(0).setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 3));

        HSSFRow rowData = sheet.createRow(rowNum++);
        rowData.createCell(0).setCellValue("Emitido em: " + LocalDate.now().format(FORMATTER_DATA));
        return ++rowNum;
    }

    private int criarLinhaTitulo(HSSFSheet sheet, HSSFCellStyle style, int rowNum, String texto) {
        rowNum++;
        HSSFRow row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(texto);
        row.getCell(0).setCellStyle(style);
        return rowNum;
    }

    private int adicionarTotalExcel(HSSFSheet sheet, HSSFCellStyle style, int rowNum, String label, BigDecimal total) {
        HSSFRow row = sheet.createRow(rowNum++);
        row.createCell(2).setCellValue(label);
        row.createCell(3).setCellValue(total.doubleValue());
        row.getCell(2).setCellStyle(style);
        return rowNum;
    }

    private HSSFCellStyle criarEstiloNegrito(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    private void aplicarEstilo(HSSFRow row, HSSFCellStyle style, int cols) {
        for (int i = 0; i < cols; i++)
            row.getCell(i).setCellStyle(style);
    }

    private void salvarArquivoExcel(HSSFWorkbook workbook, String caminho) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(caminho)) {
            workbook.write(fileOut);
        }
    }

    private Map<String, BigDecimal> agruparDespesasPorCategoria(List<Despesa> despesas) {
        Map<String, BigDecimal> mapa = new HashMap<>();
        for (Despesa d : despesas) {
            String cat = d.getCategoria().getNome();
            mapa.put(cat, mapa.getOrDefault(cat, BigDecimal.ZERO).add(d.getValor()));
        }
        return mapa;
    }

    private Map<String, BigDecimal> agruparReceitasPorTipo(List<Receita> receitas) {
        Map<String, BigDecimal> mapa = new HashMap<>();
        for (Receita r : receitas) {
            String tipo = r.getTipo().name();
            mapa.put(tipo, mapa.getOrDefault(tipo, BigDecimal.ZERO).add(r.getValor()));
        }
        return mapa;
    }

    private BigDecimal calcularTotalReceitas(List<Receita> receitas) {
        return receitas.stream().map(Receita::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularTotalDespesas(List<Despesa> despesas) {
        return despesas.stream().map(Despesa::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}