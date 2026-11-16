package controller;

import java.util.ArrayList;

import model.Academia;
import model.Plano;

/**
 * Fornece os números do relatório gerencial.
 */
public class RelatorioController {

    private final Academia academia = Academia.getInstancia();

    public int getTotalAlunos() {
        return academia.getTotalAlunos();
    }

    public int getAlunosSemPlano() {
        return academia.contarAlunosSemPlano();
    }

    public int getQuantidadePagamentos() {
        return academia.getPagamentos().size();
    }

    public double getTotalRecebido() {
        return academia.getTotalRecebido();
    }

    public ArrayList<Plano> getPlanos() {
        return academia.getPlanos();
    }

    public int contarAlunos(Plano plano) {
        return academia.contarAlunosPorPlano(plano);
    }
}
