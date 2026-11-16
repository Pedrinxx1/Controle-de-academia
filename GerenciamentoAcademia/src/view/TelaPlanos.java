package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import controller.PlanoController;
import model.Plano;

/**
 * Tela de planos: cadastro, listagem, edição e exclusão.
 */
public class TelaPlanos extends TelaBase {

    private final PlanoController controller = new PlanoController();

    private final JTextField txtNome = new JTextField();
    private final JTextField txtDescricao = new JTextField();
    private final JTextField txtValor = new JTextField();

    private final DefaultTableModel modelo =
            criarModeloTabela("Nome", "Descrição", "Valor mensal", "Alunos vinculados");
    private final JTable tabela = new JTable(modelo);

    private String nomeSelecionado; // null = nenhum plano selecionado

    public TelaPlanos(JFrame principal) {
        super("Cadastro de Planos", principal);

        JPanel formulario = criarFormulario(
                new String[] { "Nome do plano:", "Descrição:", "Valor mensal (R$):" },
                new JComponent[] { txtNome, txtDescricao, txtValor });

        JPanel botoes = new JPanel(new FlowLayout());
        botoes.add(criarBotao("Cadastrar", e -> cadastrar()));
        botoes.add(criarBotao("Atualizar", e -> atualizar()));
        botoes.add(criarBotao("Excluir", e -> excluir()));
        botoes.add(criarBotao("Limpar", e -> limpar()));

        JPanel topo = new JPanel();
        topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));
        topo.add(formulario);
        topo.add(botoes);

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

        atualizarTabela();
        setSize(700, 480);
        setLocationRelativeTo(null);
    }

    private void cadastrar() {
        try {
            controller.cadastrar(txtNome.getText(), txtDescricao.getText(), txtValor.getText());
            mostrarInfo("Plano cadastrado com sucesso.");
            limpar();
            atualizarTabela();
        } catch (IllegalArgumentException ex) {
            mostrarErro(ex.getMessage());
        }
    }

    private void atualizar() {
        if (nomeSelecionado == null) {
            mostrarErro("Selecione um plano na tabela para atualizar.");
            return;
        }
        try {
            controller.atualizar(nomeSelecionado, txtNome.getText(), txtDescricao.getText(), txtValor.getText());
            mostrarInfo("Plano atualizado.");
            limpar();
            atualizarTabela();
        } catch (IllegalArgumentException ex) {
            mostrarErro(ex.getMessage());
        }
    }

    private void excluir() {
        if (nomeSelecionado == null) {
            mostrarErro("Selecione um plano na tabela para excluir.");
            return;
        }
        if (!confirmar("Deseja realmente excluir o plano \"" + nomeSelecionado + "\"?")) {
            return;
        }
        try {
            controller.excluir(nomeSelecionado);
            mostrarInfo("Plano excluído.");
            limpar();
            atualizarTabela();
        } catch (IllegalArgumentException ex) {
            mostrarErro(ex.getMessage());
        }
    }

    private void preencherFormulario() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return;
        }
        Plano plano = controller.buscar((String) modelo.getValueAt(linha, 0));
        if (plano == null) {
            return;
        }
        nomeSelecionado = plano.getNome();
        txtNome.setText(plano.getNome());
        txtDescricao.setText(plano.getDescricao());
        txtValor.setText(String.format(java.util.Locale.forLanguageTag("pt-BR"), "%.2f", plano.getValorMensal()));
    }

    private void limpar() {
        nomeSelecionado = null;
        txtNome.setText("");
        txtDescricao.setText("");
        txtValor.setText("");
        tabela.clearSelection();
    }

    private void atualizarTabela() {
        modelo.setRowCount(0);
        for (Plano p : controller.listar()) {
            modelo.addRow(new Object[] { p.getNome(), p.getDescricao(), formatarMoeda(p.getValorMensal()),
                    controller.contarAlunos(p) });
        }
    }
}
