package model;

/**
 * Representa um aluno da academia.
 * A matrícula é a identificação única e não pode ser alterada depois do cadastro.
 */
public class Aluno {

    private String matricula;
    private String nome;
    private int idade;
    private String telefone;
    private Plano plano; // pode ser null enquanto o aluno não tiver plano vinculado

    public Aluno(String matricula, String nome, int idade, String telefone, Plano plano) {
        setMatricula(matricula);
        setNome(nome);
        setIdade(idade);
        setTelefone(telefone);
        setPlano(plano);
    }

    public String getMatricula() {
        return matricula;
    }

    // privado: a matrícula só é definida no cadastro
    private void setMatricula(String matricula) {
        if (matricula == null || matricula.trim().isEmpty()) {
            throw new IllegalArgumentException("A matrícula é obrigatória.");
        }
        this.matricula = matricula.trim();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do aluno é obrigatório.");
        }
        this.nome = nome.trim();
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        if (idade < 1 || idade > 120) {
            throw new IllegalArgumentException("Informe uma idade válida (entre 1 e 120 anos).");
        }
        this.idade = idade;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            throw new IllegalArgumentException("O telefone é obrigatório.");
        }
        int digitos = telefone.replaceAll("\\D", "").length();
        if (digitos < 8 || digitos > 13) {
            throw new IllegalArgumentException("Telefone inválido. Informe entre 8 e 13 dígitos.");
        }
        this.telefone = telefone.trim();
    }

    public Plano getPlano() {
        return plano;
    }

    public void setPlano(Plano plano) {
        this.plano = plano;
    }

    /** Usado pelo JComboBox da tela de mensalidades. */
    @Override
    public String toString() {
        return matricula + " - " + nome;
    }
}
