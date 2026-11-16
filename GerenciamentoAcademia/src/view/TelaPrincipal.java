package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.function.Supplier;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Tela principal: mostra o nome do sistema e os botões de navegação.
 */
public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        super("Academia - Sistema de Gerenciamento");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel titulo = new JLabel("Sistema de Gerenciamento de Academia", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 20f));
        JLabel subtitulo = new JLabel("Grupo 07 - Engenharia de Software", SwingConstants.CENTER);

        JPanel topo = new JPanel(new GridLayout(2, 1));
        topo.add(titulo);
        topo.add(subtitulo);

        JPanel botoes = new JPanel(new GridLayout(0, 1, 8, 8));
        adicionarBotao(botoes, "Cadastro de Alunos", () -> new TelaAlunos(this));
        adicionarBotao(botoes, "Cadastro de Planos", () -> new TelaPlanos(this));
        adicionarBotao(botoes, "Controle de Mensalidades", () -> new TelaMensalidades(this));
        adicionarBotao(botoes, "Relatório Gerencial", () -> new TelaRelatorio(this));

        JButton sair = new JButton("Sair");
        sair.addActionListener(e -> System.exit(0));
        botoes.add(sair);

        JPanel conteudo = new JPanel(new BorderLayout(10, 20));
        conteudo.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        conteudo.add(topo, BorderLayout.NORTH);
        conteudo.add(botoes, BorderLayout.CENTER);
        setContentPane(conteudo);

        setSize(460, 420);
        setLocationRelativeTo(null);
    }

    /** Cria um botão que abre outra tela e esconde a principal. */
    private void adicionarBotao(JPanel painel, String texto, Supplier<JFrame> criarTela) {
        JButton botao = new JButton(texto);
        botao.addActionListener(e -> {
            criarTela.get().setVisible(true);
            setVisible(false);
        });
        painel.add(botao);
    }
}
