import java.time.LocalDate;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import controller.AlunoController;
import controller.PagamentoController;
import controller.PlanoController;
import view.TelaPrincipal;

/**
 * Classe principal: inicia a aplicação.
 */
public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // se não conseguir, usa o visual padrão do Swing
        }

        carregarDadosDeExemplo(); // pode remover esta linha para iniciar o sistema vazio

        SwingUtilities.invokeLater(() -> new TelaPrincipal().setVisible(true));
    }

    /** Alguns dados iniciais para facilitar a demonstração. */
    private static void carregarDadosDeExemplo() {
        PlanoController planos = new PlanoController();
        planos.cadastrar("Mensal", "Acesso à musculação", "99,90");
        planos.cadastrar("Premium", "Musculação + aulas coletivas", "149,90");
        planos.cadastrar("Estudante", "Desconto para estudantes", "79,90");

        AlunoController alunos = new AlunoController();
        alunos.cadastrar("1001", "Ana Souza", "22", "(61) 99999-1111", planos.buscar("Mensal"));
        alunos.cadastrar("1002", "Bruno Lima", "30", "(61) 98888-2222", planos.buscar("Premium"));
        alunos.cadastrar("1003", "Carla Mendes", "19", "(61) 97777-3333", planos.buscar("Estudante"));
        alunos.cadastrar("1004", "Diego Rocha", "41", "(61) 96666-4444", null);

        LocalDate hoje = LocalDate.now();
        new PagamentoController().registrar(alunos.buscar("1001"), hoje.getMonthValue(), hoje.getYear(), "99,90");
    }
}
