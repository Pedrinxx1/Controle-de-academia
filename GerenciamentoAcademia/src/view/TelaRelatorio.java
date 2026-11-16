package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import controller.RelatorioController;
import model.Plano;

/**
 * Relatório gerencial: total de alunos, alunos por plano e total de pagamentos recebidos.
 */
public class TelaRelatorio extends TelaBase {

    private final RelatorioController controller = new RelatorioController();

    private final JLabel lblTotalAlunos = new JLabel();
    private final JLabel lblSemPlano = new JLabel();
    private final JLabel lblPagamentos = new JLabel();
    private final JLabel lblTotalRecebido = new JLabel();

    private final DefaultTableModel modelo =
            criarModeloTabela("Plano", "Valor mensal", "Quantidade de alunos");

    public TelaRelatorio(JFrame principal) {
        super("Relatório Gerencial", principal);

        JPanel resumo = new JPanel(new GridLayout(0, 2, 6, 6));
        resumo.setBorder(BorderFactory.createTitledBorder("Resumo"));
        adicionarLinha(resumo, "Total de alunos:", lblTotalAlunos);
        adicionarLinha(resumo, "Alunos sem plano:", lblSemPlano);
        adicionarLinha(resumo, "Pagamentos registrados:", lblPagamentos);
        adicionarLinha(resumo, "Total de pagamentos recebidos:", lblTotalRecebido);

        JScrollPane tabela = new JScrollPane(new JTable(modelo));
        tabela.setBorder(BorderFactory.createTitledBorder("Alunos por plano"));

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rodape.add(criarBotao("Atualizar relatório", e -> carregarDados()));
        rodape.add(criarBotaoVoltar());

        add(resumo, BorderLayout.NORTH);
        add(tabela, BorderLayout.CENTER);
        add(rodape, BorderLayout.SOUTH);

        carregarDados();
        setSize(560, 450);
        setLocationRelativeTo(null);
    }

    private void adicionarLinha(JPanel painel, String rotulo, JLabel valor) {
        painel.add(new JLabel(rotulo));
        valor.setFont(valor.getFont().deriveFont(Font.BOLD));
        painel.add(valor);
    }

    private void carregarDados() {
        lblTotalAlunos.setText(String.valueOf(controller.getTotalAlunos()));
        lblSemPlano.setText(String.valueOf(controller.getAlunosSemPlano()));
        lblPagamentos.setText(String.valueOf(controller.getQuantidadePagamentos()));
        lblTotalRecebido.setText(formatarMoeda(controller.getTotalRecebido()));

        modelo.setRowCount(0);
        for (Plano p : controller.getPlanos()) {
            modelo.addRow(new Object[] { p.getNome(), formatarMoeda(p.getValorMensal()), controller.contarAlunos(p) });
        }
    }
}
