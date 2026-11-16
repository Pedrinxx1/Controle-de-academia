package view;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 * Classe base das telas secundárias.
 * Cuida do que é comum a todas: voltar ao menu, mensagens (JOptionPane) e formatação.
 */
public abstract class TelaBase extends JFrame {

    private static final NumberFormat MOEDA = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));

    protected TelaBase(String titulo, final JFrame telaPrincipal) {
        super(titulo);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        // Ao fechar esta tela (pelo X ou pelo botão Voltar), a tela principal reaparece
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                telaPrincipal.setVisible(true);
            }
        });
    }

    protected JButton criarBotao(String texto, ActionListener acao) {
        JButton botao = new JButton(texto);
        botao.addActionListener(acao);
        return botao;
    }

    protected JButton criarBotaoVoltar() {
        return criarBotao("Voltar ao menu", e -> dispose());
    }

    /** Monta um formulário com rótulos à esquerda e campos à direita. */
    protected JPanel criarFormulario(String[] rotulos, JComponent[] campos) {
        JPanel painel = new JPanel(new GridLayout(0, 2, 6, 6));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        for (int i = 0; i < rotulos.length; i++) {
            painel.add(new JLabel(rotulos[i]));
            painel.add(campos[i]);
        }
        return painel;
    }

    /** Modelo de tabela que não permite editar as células diretamente. */
    protected static DefaultTableModel criarModeloTabela(String... colunas) {
        return new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };
    }

    protected void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    protected void mostrarInfo(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    protected boolean confirmar(String mensagem) {
        return JOptionPane.showConfirmDialog(this, mensagem, "Confirmação",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    public static String formatarMoeda(double valor) {
        return MOEDA.format(valor);
    }
}
