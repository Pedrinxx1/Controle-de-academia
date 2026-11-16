package controller;

import java.util.ArrayList;

import model.Academia;
import model.Aluno;
import model.Plano;

/**
 * Recebe os dados da tela de alunos, converte os textos
 * e chama o Model (Academia) para executar as operações.
 */
public class AlunoController {

    private final Academia academia = Academia.getInstancia();

    public void cadastrar(String matricula, String nome, String idade, String telefone, Plano plano) {
        academia.adicionarAluno(new Aluno(matricula, nome, converterIdade(idade), telefone, plano));
    }

    public void atualizar(String matricula, String nome, String idade, String telefone, Plano plano) {
        academia.atualizarAluno(matricula, nome, converterIdade(idade), telefone, plano);
    }

    public void excluir(String matricula) {
        academia.removerAluno(matricula);
    }

    public void vincularPlano(String matricula, Plano plano) {
        if (plano == null) {
            throw new IllegalArgumentException("Selecione um plano para vincular ao aluno.");
        }
        academia.vincularPlano(matricula, plano);
    }

    public Aluno buscar(String matricula) {
        return academia.buscarAluno(matricula);
    }

    public ArrayList<Aluno> listar() {
        return academia.getAlunos();
    }

    public ArrayList<Aluno> pesquisar(String termo) {
        return academia.pesquisarAlunos(termo);
    }

    public ArrayList<Plano> listarPlanos() {
        return academia.getPlanos();
    }

    private int converterIdade(String texto) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("A idade deve ser um número inteiro.");
        }
    }
}
