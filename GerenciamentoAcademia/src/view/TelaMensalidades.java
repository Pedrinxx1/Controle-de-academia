package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import controller.PagamentoController;
import model.Aluno;
import model.Pagamento;

/**
 * Tela de mensalidades: registro de pagamentos e controle de pendências.
 */
public class TelaMensalidades extends TelaBase {

    private static final String[] MESES = { "01 - Janeiro", "02 - Fevereiro", "03 - Março", "04 - Abril",
            "05 - Maio", "06 - Junho", "07 - Julho", "08 - Agosto", "09 - Setembro", "10 - Outubro",
            "11 - Novembro", "12 - Dezembro" };
    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final PagamentoController controller = new PagamentoController();

    private final JComboBox<Aluno> cbAluno = new JComboBox<>();
    private final JComboBox<String> cbMes = new JComboBox<>(MESES);
    private final JComboBox<Integer> cbAno = new JComboBox<>();
    private final JTextField txtValor = new JTextField();

    private final DefaultTableModel modeloPagamentos =
            criarModeloTabela("Matrícula", "Aluno", "Plano", "Referência", "Valor", "Registrado em");
    private final DefaultTableModel modeloPendentes =
            criarModeloTabela("Matrícula", "Nome", "Telefone", "Plano", "Valor devido");
    private final JLabel lblPendencias = new JLabel();

    public TelaMensalidades(JFrame principal) {
        super("Controle de Mensalidades", principal);

        for (Aluno a : controller.listarAlunos()) {
            cbAluno.addItem(a);
        }
        int anoAtual = LocalDate.now().getYear();
        for (int ano = anoAtual - 2; ano <= anoAtual + 1; ano++) {
            cbAno.addItem(ano);
        }
        cbAno.setSelectedItem(anoAtual);
        cbMes.setSelectedIndex(LocalDate.now().getMonthValue() - 1);

        cbAluno.addActionListener(e -> sugerirValor());
        cbMes.addActionListener(e -> atualizarPendencias());
        cbAno.addActionListener(e -> atualizarPendencias());

        JPanel formulario = criarFormulario(
                new String[] { "Aluno:", "Mês de referência:", "Ano de referência:", "Valor pago (R$):" },
                new JComponent[] { cbAluno, cbMes, cbAno, txtValor });

        JPanel botoes = new JPanel(new FlowLayout());
        botoes.add(criarBotao("Registrar pagamento", e -> registrar()));

        JPanel topo = new JPanel();
        topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));
        topo.add(formulario);
        topo.add(botoes);

        JPanel painelPendencias = new JPanel(new BorderLayout());
        painelPendencias.add(lblPendencias, BorderLayout.NORTH);
        painelPendencias.add(new JScrollPane(new JTable(modeloPendentes)), BorderLayout.CENTER);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Pagamentos registrados", new JScrollPane(new JTable(modeloPagamentos)));
        abas.addTab("Pendências do mês selecionado", painelPendencias);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rodape.add(criarBotaoVoltar());

        add(topo, BorderLayout.NORTH);
        add(abas, BorderLayout.CENTER);
        add(rodape, BorderLayout.SOUTH);

        sugerirValor();
        atualizarPagamentos();
        atualizarPendencias();
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private int mesSelecionado() {
        return cbMes.getSelectedIndex() + 1;
    }

    private int anoSelecionado() {
        return (Integer) cbAno.getSelectedItem();
    }

    /** Preenche o valor com a mensalidade do plano do aluno escolhido. */
    private void sugerirValor() {
        Aluno aluno = (Aluno) cbAluno.getSelectedItem();
        if (aluno != null && aluno.getPlano() != null) {
            txtValor.setText(String.format(Locale.forLanguageTag("pt-BR"), "%.2f", aluno.getPlano().getValorMensal()));
        } else {
            txtValor.setText("");
        }
    }

    private void registrar() {
        try {
            controller.registrar((Aluno) cbAluno.getSelectedItem(), mesSelecionado(), anoSelecionado(),
                    txtValor.getText());
            mostrarInfo("Pagamento registrado com sucesso.");
            atualizarPagamentos();
            atualizarPendencias();
        } catch (IllegalArgumentException ex) {
            mostrarErro(ex.getMessage());
        }
    }

    private void atualizarPagamentos() {
        modeloPagamentos.setRowCount(0);
        for (Pagamento p : controller.listarPagamentos()) {
            modeloPagamentos.addRow(new Object[] { p.getAluno().getMatricula(), p.getAluno().getNome(),
                    p.getNomePlano(), p.getReferencia(), formatarMoeda(p.getValor()),
                    p.getDataRegistro().format(DATA) });
        }
    }

    private void atualizarPendencias() {
        List<Aluno> pendentes = controller.listarPendentes(mesSelecionado(), anoSelecionado());
        modeloPendentes.setRowCount(0);
        for (Aluno a : pendentes) {
            modeloPendentes.addRow(new Object[] { a.getMatricula(), a.getNome(), a.getTelefone(),
                    a.getPlano().getNome(), formatarMoeda(a.getPlano().getValorMensal()) });
        }
        lblPendencias.setText("  " + pendentes.size() + " aluno(s) sem pagamento em "
                + Pagamento.formatarReferencia(mesSelecionado(), anoSelecionado())
                + " (alunos sem plano não têm mensalidade).");
    }
}
