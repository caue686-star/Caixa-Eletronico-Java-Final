package grupo_eclipse;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CaixaEletronico extends JFrame implements ICaixaEletronico {

    private int[][] cedulas = {
        {100, 100},
        {50,  200},
        {20,  300},
        {10,  350},
        {5,   450},
        {2,   500}
    };

    private int cotaMinima = 0;
    private ArrayList<String> historicoSaques = new ArrayList<>();
    private JTextArea areaResultado;
    private JLabel labelTotal;

    public CaixaEletronico() {
        setTitle("Caixa Eletrônico");
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("Caixa Eletrônico", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel painelCentral = new JPanel(new BorderLayout(10, 10));
        painelCentral.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        JPanel painelBotoes = new JPanel(new GridLayout(8, 1, 5, 5));
        painelBotoes.setBorder(BorderFactory.createTitledBorder("Operações"));

        JLabel lblCliente = new JLabel("── Módulo do Cliente ──", SwingConstants.CENTER);
        lblCliente.setFont(new Font("Arial", Font.BOLD, 12));
        painelBotoes.add(lblCliente);

        JButton btnSaque = new JButton("Efetuar Saque");
        painelBotoes.add(btnSaque);

        JLabel lblAdmin = new JLabel("── Módulo do Administrador ──", SwingConstants.CENTER);
        lblAdmin.setFont(new Font("Arial", Font.BOLD, 12));
        painelBotoes.add(lblAdmin);

        JButton btnRelatorio  = new JButton("Relatório de Cédulas");
        JButton btnValorTotal = new JButton("Valor Total Disponível");
        JButton btnReposicao  = new JButton("Reposição de Cédulas");
        JButton btnCotaMinima = new JButton("Cota Mínima");
        painelBotoes.add(btnRelatorio);
        painelBotoes.add(btnValorTotal);
        painelBotoes.add(btnReposicao);
        painelBotoes.add(btnCotaMinima);

        JButton btnSair = new JButton("Sair");
        btnSair.setBackground(new Color(200, 50, 50));
        btnSair.setForeground(Color.WHITE);
        btnSair.setFont(new Font("Arial", Font.BOLD, 13));

        JPanel painelSair = new JPanel(new BorderLayout());
        JLabel lblAmbos = new JLabel("── Módulo de Ambos ──", SwingConstants.CENTER);
        lblAmbos.setFont(new Font("Arial", Font.BOLD, 12));
        painelSair.add(lblAmbos, BorderLayout.NORTH);
        painelSair.add(btnSair, BorderLayout.CENTER);

        areaResultado = new JTextArea(10, 30);
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaResultado.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JScrollPane scroll = new JScrollPane(areaResultado);
        scroll.setBorder(BorderFactory.createTitledBorder("Resultado"));

        painelCentral.add(painelBotoes, BorderLayout.NORTH);
        painelCentral.add(scroll, BorderLayout.CENTER);
        painelCentral.add(painelSair, BorderLayout.SOUTH);

        add(painelCentral, BorderLayout.CENTER);

        labelTotal = new JLabel("Valor total no caixa: R$ " + calcularTotal(), SwingConstants.CENTER);
        labelTotal.setFont(new Font("Arial", Font.PLAIN, 13));
        labelTotal.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        add(labelTotal, BorderLayout.SOUTH);

        btnSaque.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this,
                "Digite o valor do saque (R$):", "Efetuar Saque", JOptionPane.PLAIN_MESSAGE);
            if (input != null && !input.trim().isEmpty()) {
                try {
                    int valor = Integer.parseInt(input.trim());
                    String resultado = sacar(valor);
                    areaResultado.setText(resultado);
                    atualizarLabelTotal();
                } catch (NumberFormatException ex) {
                    areaResultado.setText("Valor inválido. Digite um número inteiro.");
                }
            }
        });

        btnRelatorio.addActionListener(e -> areaResultado.setText(pegaRelatorioCedulas()));

        btnValorTotal.addActionListener(e -> areaResultado.setText(pegaValorTotalDisponivel()));

        btnReposicao.addActionListener(e -> {
            String[] valores = {"100", "50", "20", "10", "5", "2"};
            String cedula = (String) JOptionPane.showInputDialog(this,
                "Escolha o valor da cédula:", "Reposição de Cédulas",
                JOptionPane.PLAIN_MESSAGE, null, valores, valores[0]);
            if (cedula != null) {
                String qtd = JOptionPane.showInputDialog(this,
                    "Digite a quantidade a repor:", "Reposição de Cédulas", JOptionPane.PLAIN_MESSAGE);
                if (qtd != null && !qtd.trim().isEmpty()) {
                    try {
                        int quantidade = Integer.parseInt(qtd.trim());
                        String resultado = reposicaoCedulas(Integer.parseInt(cedula), quantidade);
                        areaResultado.setText(resultado);
                        atualizarLabelTotal();
                    } catch (NumberFormatException ex) {
                        areaResultado.setText("Valor inválido.");
                    }
                }
            }
        });

        btnCotaMinima.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this,
                "Digite a cota mínima (R$):", "Cota Mínima", JOptionPane.PLAIN_MESSAGE);
            if (input != null && !input.trim().isEmpty()) {
                try {
                    int minimo = Integer.parseInt(input.trim());
                    areaResultado.setText(armazenaCotaMinima(minimo));
                } catch (NumberFormatException ex) {
                    areaResultado.setText("Valor inválido.");
                }
            }
        });

        btnSair.addActionListener(e -> {
            StringBuilder extrato = new StringBuilder();
            extrato.append("======= EXTRATO DE SAQUES =======\n");
            if (historicoSaques.isEmpty()) {
                extrato.append("Nenhum saque realizado.\n");
            } else {
                for (String s : historicoSaques) {
                    extrato.append(s).append("\n");
                }
            }
            extrato.append("=================================\n");
            extrato.append(pegaValorTotalDisponivel());
            JOptionPane.showMessageDialog(this, extrato.toString(),
                "Extrato Final", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        });
    }

    @Override
    public String pegaValorTotalDisponivel() {
        return "Valor total disponível no caixa: R$ " + calcularTotal();
    }

    @Override
    public String sacar(Integer valor) {
        int totalAtual = calcularTotal();

        if (cotaMinima > 0 && totalAtual <= cotaMinima) {
            return "Caixa Vazio: Chame o Operador";
        }

        if (valor <= 0) {
            return "Valor de saque inválido.";
        }

        int restante = valor;
        int totalCedulas = 0;
        int[] ceduasUsadas = new int[cedulas.length];

        for (int i = 0; i < cedulas.length; i++) {
            int valorCedula = cedulas[i][0];
            int qtdDisponivel = cedulas[i][1];

            if (valorCedula <= restante && qtdDisponivel > 0) {
                int qtdNecessaria = restante / valorCedula;
                int qtdUsada = Math.min(qtdNecessaria, qtdDisponivel);

                if (totalCedulas + qtdUsada > 30) {
                    qtdUsada = 30 - totalCedulas;
                }

                ceduasUsadas[i] = qtdUsada;
                restante -= qtdUsada * valorCedula;
                totalCedulas += qtdUsada;

                if (totalCedulas >= 30) break;
            }
        }

        if (restante > 0) {
            return "Saque não realizado por falta de cédulas.";
        }

        if (cotaMinima > 0 && (totalAtual - valor) < cotaMinima) {
            return "Caixa Vazio: Chame o Operador\n(Saque bloqueado: abaixo da cota mínima)";
        }

        StringBuilder resultado = new StringBuilder();
        resultado.append("Saque de R$ ").append(valor).append(" realizado!\n");
        resultado.append("─────────────────────────────\n");
        resultado.append("Cédulas entregues:\n");

        for (int i = 0; i < cedulas.length; i++) {
            if (ceduasUsadas[i] > 0) {
                resultado.append("  ").append(ceduasUsadas[i])
                         .append("x R$ ").append(cedulas[i][0]).append("\n");
                cedulas[i][1] -= ceduasUsadas[i];
            }
        }

        resultado.append("─────────────────────────────\n");
        resultado.append("Total de cédulas entregues: ").append(totalCedulas).append("\n");
        resultado.append(pegaValorTotalDisponivel());

        historicoSaques.add("Saque: R$ " + valor + " | Cédulas: " + totalCedulas);

        return resultado.toString();
    }

    @Override
    public String pegaRelatorioCedulas() {
        StringBuilder sb = new StringBuilder();
        sb.append("RELATÓRIO DE CÉDULAS\n");
        sb.append("─────────────────────────────\n");
        sb.append(String.format("%-15s %-15s%n", "Valor (R$)", "Quantidade"));
        sb.append("─────────────────────────────\n");
        for (int[] c : cedulas) {
            sb.append(String.format("  R$ %-12d %-15d%n", c[0], c[1]));
        }
        sb.append("─────────────────────────────\n");
        sb.append(pegaValorTotalDisponivel());
        return sb.toString();
    }

    @Override
    public String reposicaoCedulas(Integer cedula, Integer quantidade) {
        if (quantidade <= 0) {
            return "Quantidade inválida.";
        }
        for (int[] c : cedulas) {
            if (c[0] == cedula) {
                c[1] += quantidade;
                return "Reposição realizada!\n"
                     + quantidade + "x cédulas de R$ " + cedula + " adicionadas.\n"
                     + "Nova quantidade: " + c[1] + " cédulas de R$ " + cedula + "\n"
                     + pegaValorTotalDisponivel();
            }
        }
        return "Cédula de R$ " + cedula + " não encontrada no caixa.";
    }

    @Override
    public String armazenaCotaMinima(Integer minimo) {
        this.cotaMinima = minimo;
        int totalAtual = calcularTotal();

        if (totalAtual <= cotaMinima) {
            return "Caixa Vazio: Chame o Operador\n"
                 + "Cota mínima definida: R$ " + minimo + "\n"
                 + "Total atual: R$ " + totalAtual;
        }

        return "Cota mínima definida: R$ " + minimo + "\n"
             + "Total atual no caixa: R$ " + totalAtual;
    }

    private int calcularTotal() {
        int total = 0;
        for (int[] c : cedulas) {
            total += c[0] * c[1];
        }
        return total;
    }

    private void atualizarLabelTotal() {
        labelTotal.setText("Valor total no caixa: R$ " + calcularTotal());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CaixaEletronico janela = new CaixaEletronico();
            janela.setVisible(true);
        });
    }
}
