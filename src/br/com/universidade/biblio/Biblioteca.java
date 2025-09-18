package br.universidade.biblio;

import br.universidade.biblio.Usuario;
import br.universidade.biblio.Livro;
import br.universidade.biblio.Emprestimo;

import java.util.List;
import java.util.Date;
import java.io.IOException;

public class Biblioteca {
    private Utils utils;

    /// Ctor Padrão
    public Biblioteca() {
        //FIXME: Construtores padrão para o Usuário e Livro não foram criados
        this.utils = new Utils();
    }

    private boolean validarUsuario(Usuario u) {
        try {
            GerenciadorDeDados gd = new GerenciadorDeDados("banco.json");
            if(gd.consultarUsuarioPorMatricula(u.getMatricula()) == null) {
                System.out.println("Usuário inexistente.");
                return false;
            }
            return true;
        } catch(IOException ie) {
            System.out.println("Não foi possível instanciar o gerenciador de dados");
            return false;
        }
    }

    private boolean validarDataDeDevolucao(Date dataDeDevolucao) {
        if(!utils.validarTipoData(dataDeDevolucao) || dataDeDevolucao.before(new Date())) {
            System.out.println("Data de devolução inválida. Realize um empréstimo com a data após a data atual.");
            return false;
        }
        return true;
    }

    private boolean livroEstaRegistrado(Livro l) {
        try {
            GerenciadorDeDados gd = new GerenciadorDeDados("banco.json");
            List<Livro> livros = gd.consultarTodosLivrosBanco();
            boolean livroRegistrado = false;
            
            for(Livro livro: livros) {
                if(gd.livrosIguais(livro, l)) {
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
        } catch(IOException ie) {
            System.out.println("Não foi possível instanciar o gerenciador de dados");
            return false;
        }
    }
    
    private boolean livroEstaDisponivel(Livro l) {
        try {
            GerenciadorDeDados gd = new GerenciadorDeDados("banco.json");
            List<Emprestimo> emprestimos = gd.consultarTodosEmprestimosBanco();
            int qtdLivrosEmprestados = 0;
    
            for(Emprestimo emp: emprestimos) {
                if(livrosIguais(emp.getLivro(), l)) {
                    qtdLivrosEmprestados++;
                }
            }
            //FIXME: Método (ainda) não existe
            if(qtdLivrosEmprestados >= l.getQuantidade()) {
                System.out.println("Livro indisponível no momento.");
                return false;
            }
    
            return true;
        } catch(IOException ie) {
            System.out.println("Não foi possível instanciar o gerenciador de dados");
            return false;
        }
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
        try {
            GerenciadorDeDados gd = new GerenciadorDeDados("banco.json");
            // FIXME: Construtor inválido
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
        } catch(IOException ie) {
            System.out.println("Não foi possível instanciar o gerenciador de dados");
            return null;
        }
    }

    /** 
     * Registra uma devolução, realizando validações nos parâmetros passados
     * @param e Instância do Empréstimo (que é devolvida pelo método registrarEmprestimo pra Main)
     * @param dataDevolvido Data em que o Livro foi devolvido
     * @return a Instância do Empréstimo atualizada com a devolução ou null caso a instância de empréstimo seja inválida
     * */
    public Emprestimo registrarDevolucao(Emprestimo e, Date dataDevolvido) {
        try {
            GerenciadorDeDados gd = new GerenciadorDeDados("banco.json");
            if (!validarEmprestimo(e)) {
                return null;
            } else {
                // TODO: Precisa adicionar um desses dois membros + setter, para poder determinar se o empréstimo está ativo ou não
                // (e a devolução fazer algo)
                e.setDevolvido(true);
                // e.setDataDevolvido(new Date());
    
                // Ou também remover o empréstimo do banco de dados, ver qual é melhor (teria que mudar o tipo de retorno)
                //gd.removerEmprestimoBanco(e)
                return e;
            }
        } catch(IOException ie) {
            System.out.println("Não foi possível instanciar o gerenciador de dados");
        }
    }
    
    /**
     * Adiciona um Livro no Banco de Dados
     * @param livro Livro a ser registrado
     * @return true caso o Livro tenha sido registrado, false caso contrário.
    */
    public boolean adicionarLivro(Livro livro) {
        try {
            GerenciadorDeDados gd = new GerenciadorDeDados("banco.json");
            if (!livroEstaRegistrado(livro)) {
                System.out.println("ERRO: Livro já adicionado");
                return false;
            } else {
                gd.registrarLivroBanco(livro);
                return true;
            }
        } catch(IOException ie) {
            System.out.println("Não foi possível instanciar o gerenciador de dados");
            return false;
        }
    }

    /**
     * Lista os Livros presentes no Banco de Dados
     * @return A lista de Livros do Banco de Dados
     */
    public List<Livro> listarLivros() {
        // o método consultarTodosLivrosBanco()
        // já possui tratamento de erros 
        try {
            GerenciadorDeDados gd = new GerenciadorDeDados("banco.json");
            // caso falhe retorna null
            return gd.consultarTodosLivrosBanco();
        } catch(IOException ie) {
            System.out.println("Não foi possível instanciar o gerenciador de dados");
            return null;
        }
    }

    /**
     * Adiciona um Usuário no Banco de Dados
     * @return true se o Usuário foi registrado, false caso contrário.
     */
    public boolean adicionarUsuario(Usuario usuario) {
        try {
            GerenciadorDeDados gd = new GerenciadorDeDados("banco.json");
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
        } catch(IOException ie) {
            System.out.println("Não foi possível instanciar o gerenciador de dados");
            return false;
        }
    }

    /**
     * Lista os Usuários presentes no Banco de Dados
     * @return A lista de Usuários do Banco de Dados
     */
    public List<Usuario> listarUsuarios() { // Seguindo o mesmo padrao de listarLivros
        try {
            GerenciadorDeDados gd = new GerenciadorDeDados("banco.json");
            return gd.consultarTodosUsuariosBanco();
        } catch(IOException ie) {
            System.out.println("Não foi possível instanciar o gerenciador de dados");
            return null;
        }
    }
}
