package model;

import java.util.ArrayList;

/**
 * Guarda todos os dados do sistema em memória (ArrayList) e aplica as regras de negócio.
 *
 * Usa o padrão Singleton: existe UMA única instância durante toda a execução.
 * Por isso os dados continuam disponíveis mesmo quando o usuário fecha e reabre uma janela.
 */
public class Academia {

    private static Academia instancia;

    private final ArrayList<Aluno> alunos = new ArrayList<>();
    private final ArrayList<Plano> planos = new ArrayList<>();
    private final ArrayList<Pagamento> pagamentos = new ArrayList<>();

    private Academia() {
    }

    public static Academia getInstancia() {
        if (instancia == null) {
            instancia = new Academia();
        }
        return instancia;
    }

    // ===================== PLANOS =====================

    public void adicionarPlano(Plano plano) {
        if (buscarPlano(plano.getNome()) != null) {
            throw new IllegalArgumentException("Já existe um plano com o nome \"" + plano.getNome() + "\".");
        }
        planos.add(plano);
    }

    public Plano buscarPlano(String nome) {
        if (nome == null) {
            return null;
        }
        for (Plano p : planos) {
            if (p.getNome().equalsIgnoreCase(nome.trim())) {
                return p;
            }
        }
        return null;
    }

    public void atualizarPlano(String nomeAtual, String novoNome, String descricao, double valor) {
        Plano plano = obterPlano(nomeAtual);
        Plano mesmoNome = buscarPlano(novoNome);
        if (mesmoNome != null && mesmoNome != plano) {
            throw new IllegalArgumentException("Já existe um plano com o nome \"" + novoNome.trim() + "\".");
        }
        // Cria um objeto temporário só para validar tudo antes de alterar o original
        Plano validado = new Plano(novoNome, descricao, valor);
        plano.setNome(validado.getNome());
        plano.setDescricao(validado.getDescricao());
        plano.setValorMensal(validado.getValorMensal());
    }

    public void removerPlano(String nome) {
        Plano plano = obterPlano(nome);
        int vinculados = contarAlunosPorPlano(plano);
        if (vinculados > 0) {
            throw new IllegalArgumentException("Não é possível excluir: existem " + vinculados
                    + " aluno(s) vinculado(s) a este plano.");
        }
        planos.remove(plano);
    }

    public ArrayList<Plano> getPlanos() {
        return new ArrayList<>(planos); // devolve uma cópia para proteger a lista original
    }

    private Plano obterPlano(String nome) {
        Plano p = buscarPlano(nome);
        if (p == null) {
            throw new IllegalArgumentException("Plano não encontrado.");
        }
        return p;
    }

    // ===================== ALUNOS =====================

    public void adicionarAluno(Aluno aluno) {
        // Regra de negócio: impedir matrícula duplicada
        if (buscarAluno(aluno.getMatricula()) != null) {
            throw new IllegalArgumentException("Já existe um aluno com a matrícula " + aluno.getMatricula() + ".");
        }
        validarPlanoCadastrado(aluno.getPlano());
        alunos.add(aluno);
    }

    public Aluno buscarAluno(String matricula) {
        if (matricula == null) {
            return null;
        }
        for (Aluno a : alunos) {
            if (a.getMatricula().equalsIgnoreCase(matricula.trim())) {
                return a;
            }
        }
        return null;
    }

    /** Pesquisa por parte da matrícula ou do nome (sem diferenciar maiúsculas). Vazio = todos. */
    public ArrayList<Aluno> pesquisarAlunos(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return getAlunos();
        }
        String t = termo.trim().toLowerCase();
        ArrayList<Aluno> resultado = new ArrayList<>();
        for (Aluno a : alunos) {
            if (a.getMatricula().toLowerCase().contains(t) || a.getNome().toLowerCase().contains(t)) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    public void atualizarAluno(String matricula, String nome, int idade, String telefone, Plano plano) {
        Aluno aluno = obterAluno(matricula);
        Aluno validado = new Aluno(matricula, nome, idade, telefone, plano); // valida antes de alterar
        validarPlanoCadastrado(plano);
        aluno.setNome(validado.getNome());
        aluno.setIdade(validado.getIdade());
        aluno.setTelefone(validado.getTelefone());
        aluno.setPlano(plano);
    }

    public void vincularPlano(String matricula, Plano plano) {
        Aluno aluno = obterAluno(matricula);
        validarPlanoCadastrado(plano);
        aluno.setPlano(plano);
    }

    /** Remove o aluno. Os pagamentos já feitos continuam no histórico financeiro. */
    public void removerAluno(String matricula) {
        alunos.remove(obterAluno(matricula));
    }

    public ArrayList<Aluno> getAlunos() {
        return new ArrayList<>(alunos);
    }

    private Aluno obterAluno(String matricula) {
        Aluno a = buscarAluno(matricula);
        if (a == null) {
            throw new IllegalArgumentException("Aluno não encontrado.");
        }
        return a;
    }

    private void validarPlanoCadastrado(Plano plano) {
        if (plano != null && !planos.contains(plano)) {
            throw new IllegalArgumentException("O plano selecionado não está cadastrado.");
        }
    }

    // ===================== PAGAMENTOS =====================

    public void registrarPagamento(String matricula, int mes, int ano, double valor) {
        Aluno aluno = obterAluno(matricula);
        if (aluno.getPlano() == null) {
            throw new IllegalArgumentException("O aluno não possui plano vinculado. "
                    + "Vincule um plano antes de registrar o pagamento.");
        }
        // Regra de negócio: impedir pagamento duplicado da mesma mensalidade
        if (possuiPagamento(aluno, mes, ano)) {
            throw new IllegalArgumentException("A mensalidade " + Pagamento.formatarReferencia(mes, ano)
                    + " do aluno " + aluno.getNome() + " já foi paga.");
        }
        pagamentos.add(new Pagamento(aluno, mes, ano, valor));
    }

    public boolean possuiPagamento(Aluno aluno, int mes, int ano) {
        for (Pagamento p : pagamentos) {
            if (p.getAluno() == aluno && p.ehReferenteA(mes, ano)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Pagamento> getPagamentos() {
        return new ArrayList<>(pagamentos);
    }

    /** Alunos com plano vinculado que ainda não pagaram o mês/ano informado. */
    public ArrayList<Aluno> listarPendentes(int mes, int ano) {
        ArrayList<Aluno> pendentes = new ArrayList<>();
        for (Aluno a : alunos) {
            if (a.getPlano() != null && !possuiPagamento(a, mes, ano)) {
                pendentes.add(a);
            }
        }
        return pendentes;
    }

    // ===================== RELATÓRIO =====================

    public int getTotalAlunos() {
        return alunos.size();
    }

    public int contarAlunosSemPlano() {
        int total = 0;
        for (Aluno a : alunos) {
            if (a.getPlano() == null) {
                total++;
            }
        }
        return total;
    }

    public int contarAlunosPorPlano(Plano plano) {
        int total = 0;
        for (Aluno a : alunos) {
            if (a.getPlano() == plano) {
                total++;
            }
        }
        return total;
    }

    public double getTotalRecebido() {
        double total = 0;
        for (Pagamento p : pagamentos) {
            total += p.getValor();
        }
        return total;
    }
}
