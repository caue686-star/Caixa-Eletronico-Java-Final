package grupo_eclipse;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CaixaEletronico extends JFrame implements ICaixaEletronico {

    // Matriz de cédulas: [valor, quantidade disponível]
    private int[][] cedulas = {
        {100, 100},
        {50,  200},
        {20,  300},
        {10,  350},
        {5,   450},
        {2,   500}
    };

    private int cotaMinima = 0; // Valor mínimo que o caixa deve manter
    private ArrayList<String> historicoSaques = new ArrayList<>(); // Registro de saques realizados
    private JTextArea areaResultado; // Área de texto para exibir resultados
    private JLabel labelTotal; // Label com o total disponível no caixa

    public CaixaEletronico() {
        // Configurações básicas da janela
        setTitle("Caixa Eletrônico");
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Título principal da janela
        JLabel titulo = new JLabel("Caixa Eletrônico", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel painelCentral = new JPanel(new BorderLayout(10, 10));
        painelCentral.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // Painel com os botões de operação
        JPanel painelBotoes = new JPanel(new GridLayout(8, 1, 5, 5));
        painelBotoes.setBorder(BorderFactory.createTitledBorder("Operações"));

        // Seção do cliente
        JLabel lblCliente = new JLabel("── Módulo do Cliente ──", SwingConstants.CENTER);
        lblCliente.setFont(new Font("Arial", Font.BOLD, 12));
        painelBotoes.add(lblCliente);

        JButton btnSaque = new JButton("Efetuar Saque");
        painelBotoes.add(btnSaque);

        // Seção do administrador
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

        // Botão de saída com estilo destacado
        JButton btnSair = new JButton("Sair");
        btnSair.setBackground(new Color(200, 50, 50));
        btnSair.setForeground(Color.WHITE);
        btnSair.setFont(new Font("Arial", Font.BOLD, 13));

        JPanel painelSair = new JPanel(new BorderLayout());
        JLabel lblAmbos = new JLabel("── Módulo de Ambos ──", SwingConstants.CENTER);
        lblAmbos.setFont(new Font("Arial", Font.BOLD, 12));
        painelSair.add(lblAmbos, BorderLayout.NORTH);
        painelSair.add(btnSair, BorderLayout.CENTER);

        // Área de texto para exibir resultados das operações
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

        // Label no rodapé com o total atual do caixa
        labelTotal = new JLabel("Valor total no ca
