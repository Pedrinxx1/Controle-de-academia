package model;

import java.time.LocalDate;

/**
 * Representa o pagamento de uma mensalidade (mês/ano de referência).
 * Depois de criado, um pagamento não é alterado (todos os atributos são final).
 */
public class Pagamento {

    private final Aluno aluno;
    private final String nomePlano; // guarda o plano da época do pagamento
    private final int mes;
    private final int ano;
    private final double valor;
    private final LocalDate dataRegistro;

    public Pagamento(Aluno aluno, int mes, int ano, double valor) {
        if (aluno == null) {
            throw new IllegalArgumentException("Selecione um aluno.");
        }
        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("Mês de referência inválido.");
        }
        if (ano < 2000 || ano > 2100) {
            throw new IllegalArgumentException("Ano de referência inválido.");
        }
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser maior que zero.");
        }
        this.aluno = aluno;
        this.nomePlano = (aluno.getPlano() == null) ? "-" : aluno.getPlano().getNome();
        this.mes = mes;
        this.ano = ano;
        this.valor = valor;
        this.dataRegistro = LocalDate.now();
    }

    public Aluno getAluno() {
        return aluno;
    }

    public String getNomePlano() {
        return nomePlano;
    }

    public int getMes() {
        return mes;
    }

    public int getAno() {
        return ano;
    }

    public double getValor() {
        return valor;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    /** Retorna a referência no formato MM/AAAA. */
    public String getReferencia() {
        return formatarReferencia(mes, ano);
    }

    public boolean ehReferenteA(int mes, int ano) {
        return this.mes == mes && this.ano == ano;
    }

    public static String formatarReferencia(int mes, int ano) {
        return String.format("%02d/%d", mes, ano);
    }
}
