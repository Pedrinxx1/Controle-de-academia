package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import controller.AlunoController;
import model.Aluno;
import model.Plano;

/**
 * Tela de alunos: cadastro, listagem, pesquisa, edição, exclusão e vínculo de plano.
 */
public class TelaAlunos extends TelaBase {

    private static final String SEM_PLANO = "(sem plano)";

    private final AlunoController controller = new AlunoController();

    private final JTextField txtMatricula = new JTextField();
    private final JTextField txtNome = new JTextField();
    private final JTextField txtIdade = new JTextField();
    private final JTextField txtTelefone = new JTextField();
    private final JComboBox<Object> cbPlano = new JComboBox<>();
    private final JTextField txtPesquisa = new JTextField(20);

    private final DefaultTableModel modelo =
            criarModeloTabela("Matrícula", "Nome", "Idade", "Telefone", "Plano");
    private final JTable tabela = new JTable(modelo);

    private String matriculaSelecionada; // null = nenhum aluno selecionado

    public TelaAlunos(JFrame principal) {
        super("Cadastro de Alunos", principal);

        carregarPlanos();

        JPanel formulario = criarFormulario(
                new String[] { "Matrícula:", "Nome:", "Idade:", "Telefone:", "Plano:" },
                new JComponent[] { txtMatricula, txtNome, txtIdade, txtTelefone, cbPlano });

        JPanel botoes = new JPanel(new FlowLayout());
        botoes.add(criarBotao("Cadastrar", e -> cadastrar()));
        botoes.add(criarBotao("Atualizar", e -> atualizar()));
        botoes.add(criarBotao("Excluir", e -> excluir()));
        botoes.add(criarBotao("Vincular plano", e -> vincularPlano()));
        botoes.add(criarBotao("Limpar", e -> limpar()));

        JPanel pesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pesquisa.add(new JLabel("Pesquisar (matrícula ou nome):"));
        pesquisa.add(txtPesquisa);
        pesquisa.add(criarBotao("Pesquisar", e -> pesquisar()));
        pesquisa.add(criarBotao("Mostrar todos", e -> {
            txtPesquisa.setText("");
            atualizarTabela(controller.listar());
        }));

        JPanel topo = new JPanel();
        topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));
        topo.add(formulario);
        topo.add(botoes);
        topo.add(pesquisa);

        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                preencherFormulario();
            }
        });

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rodape.add(criarBotaoVoltar());

        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(rodape, BorderLayout.SOUTH);

        atualizarTabela(controller.listar());
        setSize(780, 600);
        setLocationRelativeTo(null);
    }

    private void carregarPlanos() {
        cbPlano.addItem(SEM_PLANO);
        for (Plano p : controller.listarPlanos()) {
            cbPlano.addItem(p);
        }
    }

    private Plano planoSelecionado() {
        Object item = cbPlano.getSelectedItem();
        return (item instanceof Plano) ? (Plano) item : null;
    }

    private void cadastrar() {
        try {
            controller.cadastrar(txtMatricula.getText(), txtNome.getText(), txtIdade.getText(),
                    txtTelefone.getText(), planoSelecionado());
            mostrarInfo("Aluno cadastrado com sucesso.");
            limpar();
            atualizarTabela(controller.listar());
        } catch (IllegalArgumentException ex) {
            mostrarErro(ex.getMessage());
        }
    }

    private void atualizar() {
        if (matriculaSelecionada == null) {
            mostrarErro("Selecione um aluno na tabela para atualizar.");
            return;
        }
        try {
            controller.atualizar(matriculaSelecionada, txtNome.getText(), txtIdade.getText(),
                    txtTelefone.getText(), planoSelecionado());
            mostrarInfo("Dados do aluno atualizados.");
            limpar();
            atualizarTabela(controller.listar());
        } catch (IllegalArgumentException ex) {
            mostrarErro(ex.getMessage());
        }
    }

    private void excluir() {
        if (matriculaSelecionada == null) {
            mostrarErro("Selecione um aluno na tabela para excluir.");
            return;
        }
        if (!confirmar("Deseja realmente excluir o aluno de matrícula " + matriculaSelecionada + "?")) {
            return;
        }
        try {
            controller.excluir(matriculaSelecionada);
            mostrarInfo("Aluno excluído.");
            limpar();
            atualizarTabela(controller.listar());
        } catch (IllegalArgumentException ex) {
            mostrarErro(ex.getMessage());
        }
    }

    private void vincularPlano() {
        if (matriculaSelecionada == null) {
            mostrarErro("Selecione um aluno na tabela e escolha o plano.");
            return;
        }
        try {
            controller.vincularPlano(matriculaSelecionada, planoSelecionado());
            mostrarInfo("Plano vinculado ao aluno.");
            limpar();
            atualizarTabela(controller.listar());
        } catch (IllegalArgumentException ex) {
            mostrarErro(ex.getMessage());
        }
    }

    private void pesquisar() {
        List<Aluno> resultado = controller.pesquisar(txtPesquisa.getText());
        atualizarTabela(resultado);
        if (resultado.isEmpty()) {
            mostrarInfo("Nenhum aluno encontrado.");
        }
    }

    /** Ao clicar numa linha da tabela, os dados vão para o formulário. */
    private void preencherFormulario() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return;
        }
        Aluno aluno = controller.buscar((String) modelo.getValueAt(linha, 0));
        if (aluno == null) {
            return;
        }
        matriculaSelecionada = aluno.getMatricula();
        txtMatricula.setText(aluno.getMatricula());
        txtMatricula.setEditable(false); // matrícula não pode ser alterada
        txtNome.setText(aluno.getNome());
        txtIdade.setText(String.valueOf(aluno.getIdade()));
        txtTelefone.setText(aluno.getTelefone());
        if (aluno.getPlano() == null) {
            cbPlano.setSelectedIndex(0);
        } else {
            cbPlano.setSelectedItem(aluno.getPlano());
        }
    }

    private void limpar() {
        matriculaSelecionada = null;
        txtMatricula.setEditable(true);
        txtMatricula.setText("");
        txtNome.setText("");
        txtIdade.setText("");
        txtTelefone.setText("");
        cbPlano.setSelectedIndex(0);
        tabela.clearSelection();
    }

    private void atualizarTabela(List<Aluno> alunos) {
        modelo.setRowCount(0);
        for (Aluno a : alunos) {
            modelo.addRow(new Object[] { a.getMatricula(), a.getNome(), a.getIdade(), a.getTelefone(),
                    a.getPlano() == null ? SEM_PLANO : a.getPlano().getNome() });
        }
    }
}
