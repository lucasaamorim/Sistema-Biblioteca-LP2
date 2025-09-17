package br.universidade.biblio;

import java.io.IOException;
import java.util.Scanner;

/// @brief Define os diferentes estados possíveis da aplicação.
enum Estados {
    INDEFINIDO,
    NO_MENU_DE_OPCOES,
    EM_ERRO,
    CADASTRANDO_LIVRO,
    CADASTRANDO_USUARIO,
    FAZENDO_EMPRESTIMO,
    DEVOLVENDO_LIVRO,
    IMPRIMINDO_RELATORIO,
    SUCESSO,
    ENCERRANDO;
}

/// @brief Motor da aplicação.
public class Main {

    //=== Atributos.
    // private Biblioteca biblioteca; //!< Principal interface com o sistema.
    private Estados estadoAtual;    //!< Estado atual da máquina.
    private Scanner scanner;        //!< Mecanismo de leitura de estrada.
    private int opcaoDoUsuario;     //!< Opção escolhida no menu de opções.
    private String entrada;         //!< Linha de entrada do usuário contendo informações da funcionalidade escolhida.

    //=== Construtor.
    public Main() {

        // this.biblioteca = new Biblioteca();
        this.estadoAtual = Estados.INDEFINIDO;
        this.scanner = new Scanner(System.in);
        this.opcaoDoUsuario = 0;
        this.entrada = "";

    }

    //=== Métodos core.
    /// @brief Responsável por receber as entradas do usuário.
    public void processar() {

        if (estadoAtual == Estados.NO_MENU_DE_OPCOES) {
            opcaoDoUsuario = scanner.nextInt();
        }

        if (estadoAtual == Estados.EM_ERRO || estadoAtual == Estados.SUCESSO) {
            scanner.nextLine();
        }

        if (estadoAtual != Estados.NO_MENU_DE_OPCOES && estadoAtual != Estados.SUCESSO && estadoAtual != Estados.EM_ERRO) {
            entrada = scanner.nextLine();
        }

    }

    /// @brief Responsável por tratar as entradas e atualizar estado interno do sistema.
    public void atualizar() {

        if (estadoAtual == Estados.INDEFINIDO) {
            estadoAtual = Estados.NO_MENU_DE_OPCOES;
            return; // <-- Evita que a validação do estado "NO_MENU_DE_OPCOES"
                    // seja executada antes da renderização da interface.
        }

        if (estadoAtual == Estados.NO_MENU_DE_OPCOES) {
            /// @todo: Validar entrada e alterna estado.

            if (opcaoDoUsuario == 1 /* cadastrar usuário */) {
                
                estadoAtual = Estados.CADASTRANDO_USUARIO;

            } else if (opcaoDoUsuario == 2 /* cadastrar livro */) {
                
                estadoAtual = Estados.CADASTRANDO_LIVRO;

            } else if (opcaoDoUsuario == 3 /* fazer emprestimo */) {
                
                estadoAtual = Estados.FAZENDO_EMPRESTIMO;
                
            } else if (opcaoDoUsuario == 4 /* devolver livro */) {
                
                estadoAtual = Estados.DEVOLVENDO_LIVRO;
                
            } else if (opcaoDoUsuario == 5 /* imprimir relatorio */) {
                
                estadoAtual = Estados.IMPRIMINDO_RELATORIO;
                
            } else if (opcaoDoUsuario == 0 /* encerrando aplicação */) {
                
                estadoAtual = Estados.ENCERRANDO;
                
            } else {
                
                estadoAtual = Estados.EM_ERRO;
                
            }
            
            return; // <-- Evita que a validação do estado das transições acima
                    // seja executada antes da renderização da interface.
        }

        if (estadoAtual == Estados.EM_ERRO) {
            estadoAtual = Estados.NO_MENU_DE_OPCOES;
        }

        if (estadoAtual == Estados.CADASTRANDO_USUARIO) {
            /// @todo: Valida entrada e alterna o estado.
        }

        if (estadoAtual == Estados.CADASTRANDO_LIVRO) {
            /// @todo: Valida entrada e alterna o estado.
        }

        if (estadoAtual == Estados.FAZENDO_EMPRESTIMO) {
            /// @todo: Valida entrada e alterna o estado.
        }

        if (estadoAtual == Estados.DEVOLVENDO_LIVRO) {
            /// @todo: Valida entrada e alterna o estado.
        }

        if (estadoAtual == Estados.DEVOLVENDO_LIVRO) {
            /// @todo: Valida entrada e alterna o estado.
        }

    }

    /// @brief Responsável por desenhar e atualizar os elementos da interface gráfica.
    public void renderizar() {

        if (estadoAtual == Estados.NO_MENU_DE_OPCOES) {
            System.out.println("Menu de Opções:");
            System.out.println("1. Cadastrar Usuário");
            System.out.println("2. Cadastrar Livro");
            System.out.println("3. Fazer Empréstimo");
            System.out.println("4. Devolver Livro");
            System.out.println("5. Imprimir Relatório");
            System.out.println("0. Encerrar");
            System.out.print("->  ");
        }

        if (estadoAtual == Estados.EM_ERRO) {
            System.out.println("Ops! Algo deu errado. Tente novamente.");
            System.out.println("Pressione ENTER para continuar...");
        }

        if (estadoAtual == Estados.SUCESSO) {
            System.out.println("Operação realizada com sucesso!");
            System.out.println("Pressione ENTER para continuar...");
        }

        if (estadoAtual == Estados.CADASTRANDO_USUARIO) {
            System.out.println("Digite os dados do usuário a ser cadastrado (nome, matrícula, curso):");
            System.out.print("->  ");
        }

        if (estadoAtual == Estados.CADASTRANDO_LIVRO) {
            System.out.println("Digite os dados do livro a ser cadastrado (título, autor, ano):");
            System.out.print("->  ");
        }

        if (estadoAtual == Estados.FAZENDO_EMPRESTIMO) {
            /// @todo: Exibir tela de empréstimo.
        }

        if (estadoAtual == Estados.DEVOLVENDO_LIVRO) {
            /// @todo: Exibir tela de devolução de livro.
        }

        if (estadoAtual == Estados.IMPRIMINDO_RELATORIO) {
            /// @todo: Exibir tela de relatório.
        }

    }

    //=== Loop principal.
    public void executar() {

        while (estadoAtual != Estados.ENCERRANDO) {

            processar();
            atualizar();
            renderizar();

        }

    }

    //=== Método main.
    public static void main(String[] args) throws IOException {

        String caminho = "banco.json";
        GerenciadorDeDados gd = new GerenciadorDeDados(caminho);
        Main app = new Main();
        app.executar();

    }

}
