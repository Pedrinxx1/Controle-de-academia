# 🏋️ Controle de Academia

Sistema desktop de **gerenciamento de academia**, desenvolvido em **Java (Swing)** com o padrão **MVC**, como atividade prática em grupo da faculdade.

## 🏫 Instituição

**Centro Universitário UNICEPLAC**

## 🎓 Curso

* Engenharia de Software
* Análise e Desenvolvimento de Sistemas

## 📚 Disciplina

**Elicitação e Programação**

## 👨‍🏫 Professor

**Prof. Hudson Neves**

## 👥 Equipe

**Grupo 07**

* Alexsander Sávio Santana da Silva
* Gustavo Viana Pereira
* Geovana Rosal Torres
* Heitor Rocha Moreira
* Kaio Rauan da Silva Matias
* Lara Isabela Lima
* Letícia Liz Benigno da Silva
* Pedro Henrique Eduardo Ribeiro Costa

---

## 📌 Funcionalidades

* **Alunos**: cadastro, listagem, pesquisa (por matrícula ou nome), edição, exclusão e vínculo de plano
* **Planos**: cadastro, listagem, edição e exclusão
* **Mensalidades**: registro de pagamentos e consulta de pendências por mês/ano
* **Relatório gerencial**: total de alunos, alunos sem plano, alunos por plano, quantidade e total de pagamentos recebidos

## 📏 Regras de negócio

* Não é permitido cadastrar dois alunos com a mesma matrícula
* Não é permitido cadastrar dois planos com o mesmo nome
* O valor do plano e o valor do pagamento devem ser maiores que zero
* A idade deve estar entre 1 e 120 anos
* O telefone deve ter entre 8 e 13 dígitos
* Um plano só pode ser excluído se não houver alunos vinculados a ele
* Só é possível registrar pagamento de aluno que tenha plano
* Não é permitido pagar duas vezes a mesma mensalidade (mesmo mês/ano)
* Ao excluir um aluno, os pagamentos já feitos continuam no histórico

---

## 🧱 Arquitetura (MVC)

| Camada | Pacote | Responsabilidade |
| --- | --- | --- |
| **Model** | `model` | Dados e regras de negócio (`Academia`, `Aluno`, `Plano`, `Pagamento`) |
| **View** | `view` | Telas Swing (`TelaPrincipal`, `TelaAlunos`, `TelaPlanos`, `TelaMensalidades`, `TelaRelatorio`, `TelaBase`) |
| **Controller** | `controller` | Recebe os dados das telas, converte e chama o Model (`AlunoController`, `PlanoController`, `PagamentoController`, `RelatorioController`) |

A classe `Academia` usa o padrão **Singleton** e guarda os dados **em memória** (`ArrayList`). Por isso, os dados permanecem enquanto o programa estiver aberto, mas **não são salvos** ao fechá-lo.

## 📂 Estrutura do projeto

```text
src/
├── Main.java
├── controller/
│   ├── AlunoController.java
│   ├── PagamentoController.java
│   ├── PlanoController.java
│   └── RelatorioController.java
├── model/
│   ├── Academia.java
│   ├── Aluno.java
│   ├── Pagamento.java
│   └── Plano.java
└── view/
    ├── TelaBase.java
    ├── TelaPrincipal.java
    ├── TelaAlunos.java
    ├── TelaPlanos.java
    ├── TelaMensalidades.java
    └── TelaRelatorio.java
```

---

## 💻 Tecnologias utilizadas

* **Java**
* **Java Swing** (interface gráfica)
* **Eclipse IDE**
* **Git**
* **GitHub**

---

## ▶️ Executando o sistema

1. Abra o **Eclipse IDE**.
2. Importe ou abra o projeto Java.
3. Localize a classe `Main`.
4. Clique com o botão direito sobre ela.
5. Selecione **Run As → Java Application**.
6. A tela principal do sistema será aberta.

> O sistema inicia com alguns **dados de exemplo** (3 planos e 4 alunos). Para começar vazio, remova a linha `carregarDadosDeExemplo();` da classe `Main`.

---

## 🎯 Objetivo

Este repositório tem finalidade **acadêmica e educacional**, aplicando conceitos de orientação a objetos, encapsulamento, validação de dados, padrão MVC e interface gráfica com Swing.

---

**Centro Universitário UNICEPLAC**
**Engenharia de Software | Análise e Desenvolvimento de Sistemas**
**Elicitação e Programação**
**Prof. Hudson Neves**
**Grupo 07**
