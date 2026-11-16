package controller;

import java.util.ArrayList;

import model.Academia;
import model.Plano;

/**
 * Recebe os dados digitados na tela de planos, converte os textos
 * e chama o Model (Academia) para executar as operações.
 */
public class PlanoController {

    private final Academia academia = Academia.getInstancia();

    public void cadastrar(String nome, String descricao, String valorTexto) {
        academia.adicionarPlano(new Plano(nome, descricao, converterValor(valorTexto)));
    }

    public void atualizar(String nomeAtual, String novoNome, String descricao, String valorTexto) {
        academia.atualizarPlano(nomeAtual, novoNome, descricao, converterValor(valorTexto));
    }

    public void excluir(String nome) {
        academia.removerPlano(nome);
    }

    public Plano buscar(String nome) {
        return academia.buscarPlano(nome);
    }

    public ArrayList<Plano> listar() {
        return academia.getPlanos();
    }

    public int contarAlunos(Plano plano) {
        return academia.contarAlunosPorPlano(plano);
    }

    /** Converte textos como "99,90", "99.90" ou "1.500,00" para double. */
    static double converterValor(String texto) {
        String t = texto.replace("R$", "").trim();
        if (t.isEmpty()) {
            throw new IllegalArgumentException("Informe o valor.");
        }
        if (t.contains(",")) {
            t = t.replace(".", "").replace(",", ".");
        }
        try {
            return Double.parseDouble(t);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valor inválido. Use, por exemplo, 99,90.");
        }
    }
}
