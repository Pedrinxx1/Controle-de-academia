# Sistema de Gerenciamento de Academia

**Grupo 07 — Engenharia de Software (2º semestre) — UNICEPLAC**

Aplicação desktop em **Java puro**, com interface gráfica em **Swing**, arquitetura **MVC** e armazenamento **em memória** (ArrayList). O sistema controla alunos, planos e mensalidades de uma academia.

## Integrantes

- Nome do integrante 1
- Nome do integrante 2
- Nome do integrante 3
- Nome do integrante 4
- Nome do integrante 5

## Funcionalidades

1. **Cadastro de alunos**: matrícula, nome, idade e telefone.
2. **Cadastro de planos**: nome, descrição e valor mensal (com edição e exclusão).
3. **Associação de planos**: cada aluno pode ser vinculado a um plano (no cadastro ou pelo botão "Vincular plano").
4. **Consulta e manutenção**: listar, pesquisar (por parte da matrícula ou do nome), editar e excluir alunos.
5. **Registro de pagamentos**: registra a mensalidade paga com o mês e o ano de referência.
6. **Controle de pendências**: mostra os alunos que ainda não pagaram o mês/ano selecionado.
7. **Relatório gerencial**: total de alunos, alunos sem plano, quantidade de alunos por plano e total de pagamentos recebidos.

## Regras de negócio

- Não é permitido cadastrar duas vezes a mesma matrícula.
- Não são aceitos valores negativos ou zerados (no plano e no pagamento).
- Não é permitido registrar duas vezes o pagamento da mesma mensalidade (mesmo aluno, mesmo mês e ano).
- Só é possível registrar pagamento para alunos que possuem plano vinculado.
- Não é possível excluir um plano que ainda tenha alunos vinculados.
- Não são aceitos nomes de plano repetidos.
- Idade precisa estar entre 1 e 120 anos, e o telefone precisa ter entre 8 e 13 dígitos.
- Campos obrigatórios vazios mostram uma mensagem de erro (JOptionPane).

## Telas

| Tela | O que faz |
|---|---|
| Tela principal | Nome do sistema e botões de navegação |
| Cadastro de Alunos | Cadastrar, atualizar, excluir, vincular plano e pesquisar alunos |
| Cadastro de Planos | Cadastrar, atualizar e excluir planos |
| Controle de Mensalidades | Registrar pagamentos e ver as pendências do mês selecionado |
| Relatório Gerencial | Números gerais e quantidade de alunos por plano |

Todas as telas têm o botão **"Voltar ao menu"**, que retorna à tela principal.

## Organização do código (MVC)

```
src/
├── Main.java                     → inicia o sistema (e carrega dados de exemplo)
├── model/                        → dados e regras de negócio
│   ├── Aluno.java
│   ├── Plano.java
│   ├── Pagamento.java
│   └── Academia.java             → guarda as listas (ArrayList) e aplica as regras
├── controller/                   → liga a tela ao model e converte os dados digitados
│   ├── AlunoController.java
│   ├── PlanoController.java
│   ├── PagamentoController.java
│   └── RelatorioController.java
└── view/                         → telas Swing
    ├── TelaBase.java             → código comum às telas (voltar, mensagens, tabelas)
    ├── TelaPrincipal.java
    ├── TelaAlunos.java
    ├── TelaPlanos.java
    ├── TelaMensalidades.java
    └── TelaRelatorio.java
```

- **Model**: as classes `Aluno`, `Plano` e `Pagamento` usam atributos `private` com getters e setters que validam os valores (encapsulamento). A classe `Academia` guarda tudo em `ArrayList` e concentra as regras de negócio.
- **Controller**: recebe o que o usuário digitou (texto), converte para número quando precisa e chama o Model.
- **View**: só monta a interface e mostra as mensagens. Nenhuma regra de negócio fica nas telas.

**Por que os dados não somem ao fechar uma janela?** A classe `Academia` usa o padrão *Singleton*: existe uma única instância dela durante toda a execução, e todos os controllers usam essa mesma instância. Os dados só são apagados quando o programa é encerrado.

## Como executar

**Requisito:** Java 8 ou superior (JDK).

### No Eclipse

1. `File > Import > General > Existing Projects into Workspace`.
2. Selecione a pasta `GerenciamentoAcademia` e clique em **Finish**.
3. Abra `src/Main.java`, clique com o botão direito e escolha `Run As > Java Application`.

Se o projeto não for reconhecido, crie um *Java Project* novo e copie a pasta `src` para dentro dele.

### Pelo terminal

```bash
cd GerenciamentoAcademia
javac -encoding UTF-8 -d bin $(find src -name "*.java")
java -cp bin Main
```

No Windows (PowerShell), compile com:

```powershell
javac -encoding UTF-8 -d bin (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp bin Main
```

### Dados de exemplo

Ao iniciar, o sistema já carrega 3 planos, 4 alunos (um deles sem plano) e 1 pagamento do mês atual, para facilitar a apresentação. Para começar com o sistema vazio, apague a linha `carregarDadosDeExemplo();` no `Main.java`.

## Testes realizados

| # | Teste | Resultado esperado | Resultado |
|---|---|---|---|
| 1 | Cadastrar aluno com a matrícula 1001 (já existe) | Mensagem "Já existe um aluno com a matrícula 1001." | OK |
| 2 | Cadastrar plano com valor -5 | Mensagem "O valor mensal do plano deve ser maior que zero." | OK |
| 3 | Registrar o pagamento do mês atual da aluna Ana Souza (já pago) | Mensagem "A mensalidade ... já foi paga." | OK |
| 4 | Registrar pagamento do aluno Diego Rocha (sem plano) | Mensagem pedindo para vincular um plano | OK |
| 5 | Excluir o plano "Mensal" (tem aluno vinculado) | Mensagem informando que há alunos vinculados | OK |
| 6 | Abrir as pendências do mês atual | Bruno e Carla aparecem; Ana (pagou) e Diego (sem plano) não | OK |
| 7 | Pesquisar "an" na tela de alunos | Aparecem Ana Souza e outros nomes com "an" | OK |
| 8 | Cadastrar um aluno, fechar a tela e abrir de novo | O aluno continua na lista | OK |
