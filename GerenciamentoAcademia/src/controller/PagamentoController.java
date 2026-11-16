package controller;

import java.util.ArrayList;

import model.Academia;
import model.Aluno;
import model.Pagamento;

/**
 * Controla o registro de mensalidades e a consulta de pendências.
 */
public class PagamentoController {

    private final Academia academia = Academia.getInstancia();

    public void registrar(Aluno aluno, int mes, int ano, String valorTexto) {
        if (aluno == null) {
            throw new IllegalArgumentException("Selecione um aluno.");
        }
        academia.registrarPagamento(aluno.getMatricula(), mes, ano, PlanoController.converterValor(valorTexto));
    }

    public ArrayList<Pagamento> listarPagamentos() {
        return academia.getPagamentos();
    }

    public ArrayList<Aluno> listarPendentes(int mes, int ano) {
        return academia.listarPendentes(mes, ano);
    }

    public ArrayList<Aluno> listarAlunos() {
        return academia.getAlunos();
    }
}
