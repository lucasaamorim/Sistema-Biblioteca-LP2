package br.universidade.biblio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class GerenciadorDeDados {
    private final Path dbPath;
    private final Gson gson;

    private static class Database {
        List<Usuario> USUARIO = new ArrayList<>();
        List<Livro>  LIVRO   = new ArrayList<>();
        List<Emprestimo> EMPRESTIMO = new ArrayList<>();
    }

    public GerenciadorDeDados(String caminhoArquivoJson) throws IOException {
        this.dbPath = Path.of(caminhoArquivoJson);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        garantirArquivoInicial();
    }

    public synchronized boolean registrarLivroBanco(Livro livro) {
        try {
            Database db = carregar();
            for (Livro existente : db.LIVRO) {
                if (igualLivro(existente, livro)) {
                    existente.setQuantidade(existente.getQuantidade() + livro.getQuantidade());
                    salvar(db);
                    return true;
                }
            }
            db.LIVRO.add(livro);
            salvar(db);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Retorna o último livro cadastrado
    public synchronized Livro consultarLivroBanco() {
        try {
            Database db = carregar();
            if (db.LIVRO.isEmpty()) return null;
            return db.LIVRO.get(db.LIVRO.size() - 1);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Verifica se o livro com o título passado para este metodo existe. Se existir retorna o livro, caso contrário lança a excessão
    public synchronized Livro consultarLivroPorTitulo(String titulo) {
        try {
            Database db = carregar();
            if (db.LIVRO.isEmpty()) return null;
            for(Livro existente : db.LIVRO) {
                if(titulo.equalsIgnoreCase(existente.getTitulo())){
                    return existente;
                }
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Retorna a lista de todos os livros salvos no json
    public synchronized List<Livro> consultarTodosLivrosBanco() {
        try {
            Database db = carregar();
            if (db.LIVRO.isEmpty()) return null;
            return db.LIVRO;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* Metodo que compara dois objetos do tipo Livro, usado no metodo de registro
    para identificar se é um livro novo ou se já existe algum salvo para acrescentar mais um do mesmo no json */
    public boolean igualLivro(Livro a, Livro b) {
        return a.getTitulo().equalsIgnoreCase(b.getTitulo())
                && a.getAutor().equalsIgnoreCase(b.getAutor())
                && a.getAno() == b.getAno();
    }

    public synchronized boolean registrarUsuarioBanco(Usuario usuario) {
        try {
            Database db = carregar();
            for (Usuario existente : db.USUARIO) {
                if (igualUsuario(existente, usuario)) {
                    return false;
                }
            }
            db.USUARIO.add(usuario);
            salvar(db);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Retorna o último usuário cadastrado
    public synchronized Usuario consultarUsuarioBanco() {
        try {
            Database db = carregar();
            if (db.USUARIO.isEmpty()) return null;
            return db.USUARIO.get(db.USUARIO.size() - 1);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Verifica se o usuário com a matrícula passada para este metodo existe. Se existir retorna o usuário, caso contrário lança a excessão
    public synchronized Usuario consultarUsuarioPorMatricula(String matricula) {
        try {
            Database db = carregar();
            if (db.USUARIO.isEmpty()) return null;
            for(Usuario existente : db.USUARIO) {
                if(matricula.equalsIgnoreCase(existente.getMatricula())){
                    return existente;
                }
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Retorna a lista de todos os usuários salvos no json
    public synchronized List<Usuario> consultarTodosUsuariosBanco() {
        try {
            Database db = carregar();
            if (db.USUARIO.isEmpty()) return null;
            return db.USUARIO;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* Metodo que compara dois objetos do tipo Usuario, usado no metodo de registro
    para identificar se é um usuário novo ou se já está salvo no json */
    public boolean igualUsuario(Usuario a, Usuario b) {
        return a.getNome().equalsIgnoreCase(b.getNome())
                && a.getMatricula().equalsIgnoreCase(b.getMatricula())
                && a.getCurso().equalsIgnoreCase(b.getCurso());
    }


    public synchronized boolean registrarEmprestimoBanco(Emprestimo emprestimo) {
        try {
            Database db = carregar();
            for (Emprestimo existente : db.EMPRESTIMO) {
                if (igualEmprestimo(existente, emprestimo)) {
                    return false;
                }
            }
            db.EMPRESTIMO.add(emprestimo);
            salvar(db);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Retorna o último empréstimo salvo no json
    public synchronized Emprestimo consultarEmprestimoBanco() {
        try {
            Database db = carregar();
            if (db.EMPRESTIMO.isEmpty()) return null;
            return db.EMPRESTIMO.get(db.EMPRESTIMO.size() - 1);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Verifica se o emprestimo com o id passado para este metodo existe. Se existir retorna o empréstimo, caso contrário lança a excessão
    // public synchronized Emprestimo consultarEmprestimoPorID(int id) {
    //     try {
    //         Database db = carregar();
    //         if (db.EMPRESTIMO.isEmpty()) return null;
    //         for(Emprestimo existente : db.EMPRESTIMO) {
    //             if(id == existente.getId()){
    //                 return existente;
    //             }
    //         }
    //         return null;
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //         return null;
    //     }
    // }

    //Retorna a lista de todos os empréstimos salvos no json
    public synchronized List<Emprestimo> consultarTodosEmprestimosBanco() {
        try {
            Database db = carregar();
            if (db.EMPRESTIMO.isEmpty()) return null;
            return db.EMPRESTIMO;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* Metodo que compara dois objetos do tipo Emprestimo, usado no metodo de registro
    para identificar se é um empréstimo novo ou se já está salvo no json */
    public boolean igualEmprestimo(Emprestimo a, Emprestimo b) {
        return a.getDataEmprestimo().equals(b.getDataEmprestimo())
                && a.getDataDevolucao().equals(b.getDataDevolucao());
    }

    private void garantirArquivoInicial() throws IOException {
        if (Files.notExists(dbPath)) {
            Database vazio = new Database();
            String json = gson.toJson(vazio);
            Files.writeString(dbPath, json, StandardCharsets.UTF_8);
        }
    }

    private Database carregar() throws IOException {
        String conteudo = Files.readString(dbPath, StandardCharsets.UTF_8);
        Type tipo = new TypeToken<Database>(){}.getType();
        Database db = gson.fromJson(conteudo, tipo);
        if (db == null) db = new Database();
        if (db.LIVRO == null) db.LIVRO = new ArrayList<>();
        if (db.USUARIO == null) db.USUARIO = new ArrayList<>();
        if (db.EMPRESTIMO == null) db.EMPRESTIMO = new ArrayList<>();
        return db;
    }

    private void salvar(Database db) throws IOException {
        String json = gson.toJson(db);
        Files.writeString(dbPath, json, StandardCharsets.UTF_8);
    }
}
