package br.com.universidade.biblio;

import br.com.universidade.biblio.Usuario;
import br.com.universidade.biblio.Livro;
import br.com.universidade.biblio.Emprestimo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Biblioteca {
    private Usuario usuario;
    private Livro livro;
    private Utils utils;

    /// Ctor Padrão
    public Biblioteca() {
        this.usuario = new Usuario();
        this.livro = new Livro();
        this.utils = new Utils();
    }

    private boolean livrosIguais(Livro livro1, Livro livro2) {
        if(livro1.getTitulo().equalsIgnoreCase(livro2.getTitulo())
        && livro1.getAutor().equalsIgnoreCase(livro2.getAutor())
        && livro1.getAno() == livro2.getAno()) {
            // Livro foi encontrado no GD
            return true;
        }
        return false;
    }

    private boolean validarUsuario(Usuario u) {
        GerenciadorDeDados gd = new GerenciadorDeDados();
        if(gd.consultarUsuarioPorMatricula(u.getMatricula()) == null) {
            System.out.println("Usuário inexistente.");
            return false;
        }
        return true;
    }

    private boolean validarDataDeDevolucao(Date dataDeDevolucao) {
        if(!utils.validarTipoData(dataDeDevolucao) || dataDeDevolucao.before(new Date())) {
            System.out.println("Data de devolução inválida. Realize um empréstimo com a data após a data atual.");
            return false;
        }
        return true;
    }

    private boolean livroEstaRegistrado(Livro l) {
        GerenciadorDeDados gd = new GerenciadorDeDados();
        List<Livro> livros = gd.consultarTodosLivrosBanco();
        boolean livroRegistrado = false;
        
        for(Livro livro: livros) {
            if(livrosIguais(livro, l)) {
                // Livro foi encontrado no GD
                livroRegistrado = true;
                break;
            }
        }

        if(!livroRegistrado) {
            System.out.println("Livro não registrado na biblioteca.");
            return false;
        }

        return true;
    }

    private boolean livroEstaDisponivel(Livro l) {
        GerenciadorDeDados gd = new GerenciadorDeDados();
        List<Emprestimo> emprestimos = gd.consultarTodosEmprestimosBanco();
        int qtdLivrosEmprestados = 0;

        for(Emprestimo emp: emprestimos) {
            if(livrosIguais(emp.getLivro(), l)) {
                qtdLivrosEmprestados++;
            }
        }

        if(qtdLivrosEmprestados >= l.getQuantidade()) {
            System.out.println("Livro indisponível no momento.");
            return false;
        }

        return true;
    }

    private boolean validarLivro(Livro l) {
        if(!livroEstaRegistrado(l)) return false;
        if(!livroEstaDisponivel(l)) return false;
        return true;
    }

    private boolean validarEmprestimo(Emprestimo emprestimo) {
        if(!validarUsuario(emprestimo.getUsuario())) return false;
        if(!validarDataDeDevolucao(emprestimo.getDevolucao())) return false;
        if(!validarLivro(emprestimo.getLivro())) return false;
        return true;
    }

    /**
     * Registra um Empréstimo, também lida com as validações necessárias nos parâmetros passados
     * @param u Usuário que pegou o Livro emprestado
     * @param l Livro a ser Emprestado
     * @param dataDeDevolucao Prazo de Devolução
     * @return Instância do empréstimo ativo
    */
    public Emprestimo registrarEmprestimo(Usuario u, Livro l, Date dataDeDevolucao) {
        GerenciadorDeDados gd = new GerenciadorDeDados();
        Emprestimo emprestimo = new Emprestimo(u, l, new Date(), dataDeDevolucao);
        
        if(!validarEmprestimo(emprestimo)) {
            return null;
        }

        if(!gd.registrarEmprestimoBanco(emprestimo)) {
            System.out.println("Falha ao registrar empréstimo.");
            return null;
        } 

        // u.adicionarEmprestimo(l);
        return emprestimo;
    }

    /** 
     * Registra uma devolução, realizando validações nos parâmetros passados
     * @param e Instância do Empréstimo (que é devolvida pelo método registrarEmprestimo pra Main)
     * @param dataDevolvido Data em que o Livro foi devolvido
     * @return a Instância do Empréstimo atualizada com a devolução ou null caso a instância de empréstimo seja inválida
     * */
    public Emprestimo registrarDevolucao(Emprestimo e, Date dataDevolvido) {
        GerenciadorDeDados gd = new GerenciadorDeDados();
        if (!validarEmprestimo(e)) {
            return null;
        } else {
            // Precisa adicionar um desses dois membros + setter, para poder determinar se o empréstimo está ativo ou não
            // (e a devolução fazer algo)
            e.setDevolvido(true);
            // e.setDataDevolvido(new Date());

            // Ou também remover o empréstimo do banco de dados, ver qual é melhor (teria que mudar o tipo de retorno)
            //gd.removerEmprestimoBanco(e)
            return e;
        }
    }
    
    /**
     * Adiciona um Livro no Banco de Dados
     * @param livro Livro a ser registrado
     * @return true caso o Livro tenha sido registrado, false caso contrário.
    */
    public boolean adicionarLivro(Livro livro) {
        GerenciadorDeDados gd = new GerenciadorDeDados();
        if (!livroEstaRegistrado(livro)) {
            System.out.println("ERRO: Livro já adicionado");
            return false;
        } else {
            gd.registrarLivroBanco(livro);
            return true;
        }
    }

    /**
     * Lista os Livros presentes no Banco de Dados
     * @return A lista de Livros do Banco de Dados
     */
    public ArrayList<Livro> listarLivros() {
        // o método consultarTodosLivrosBanco()
        // já possui tratamento de erros 
        GerenciadorDeDados gd = new GerenciadorDeDados();
        // caso falhe retorna null
        return gd.consultarTodosLivrosBanco();
    }

    /**
     * Adiciona um Usuário no Banco de Dados
     * @return true se o Usuário foi registrado, false caso contrário.
     */
    public boolean adicionarUsuario() {
        GerenciadorDeDados gd = new GerenciadorDeDados();
        if (gd.consultarUsuarioPorMatricula(usuario.getMatricula()) != null){
            System.out.println("ERRO: Usuário já adicionado");
            return false;
        }

        if (gd.registrarUsuarioBanco(usuario)){
            return true;
        } else {
            System.out.println("Falha ao adicionar usuário");
            return false;
        }
    }

    /**
     * Lista os Usuários presentes no Banco de Dados
     * @return A lista de Usuários do Banco de Dados
     */
    public ArrayList<Usuario> listarUsuarios() { // Seguindo o mesmo padrao de listarLivros
        GerenciadorDeDados gd = new GerenciadorDeDados();
        return gd.consultarTodosUsuariosBanco();
    }
}
