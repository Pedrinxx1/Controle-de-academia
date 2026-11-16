package model;

/**
 * Representa um plano da academia (ex.: Mensal, Premium).
 * Atributos privados (encapsulamento) com validação nos setters.
 */
public class Plano {

    private String nome;
    private String descricao;
    private double valorMensal;

    public Plano(String nome, String descricao, double valorMensal) {
        setNome(nome);
        setDescricao(descricao);
        setValorMensal(valorMensal);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do plano é obrigatório.");
        }
        this.nome = nome.trim();
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = (descricao == null) ? "" : descricao.trim();
    }

    public double getValorMensal() {
        return valorMensal;
    }

    public void setValorMensal(double valorMensal) {
        // Regra de negócio: não aceitar valores negativos (nem zero)
        if (valorMensal <= 0) {
            throw new IllegalArgumentException("O valor mensal do plano deve ser maior que zero.");
        }
        this.valorMensal = valorMensal;
    }

    /** Usado pelo JComboBox para mostrar o nome do plano. */
    @Override
    public String toString() {
        return nome;
    }
}
