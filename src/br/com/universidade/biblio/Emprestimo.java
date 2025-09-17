package br.universidade.biblio;

import java.util.Date;

public class Emprestimo{
    private Livro livro_emprestado;
    private Usuario usuario_emprestado;
    private Date data_emprestimo;
    private Date data_devolucao;
    
    private Utils utilidade;

    public void registrarEmprestimo(Livro livro, Usuario usr, Date data){

        if(utilidade.validarTipoData(data)){
            data_emprestimo = data;
        }
        else{
            System.out.println("Data invalida");
            return;
        }

        livro_emprestado = livro;
        usuario_emprestado = usr;
    }

    public void registrarDevolucao(Date data){

        if(utilidade.validarTipoData(data)){
            data_devolucao = data;
        }
        else{
            System.out.println("Data invalida");
        }

    }

    public void getInfo(){
        System.out.print("Livro emprestado: ");
        System.out.println(livro_emprestado);

        System.out.print("Usuário Responsável: ");
        System.out.println(usuario_emprestado);
        
        System.out.print("Data de Emprestimo: ");
        System.out.println(data_emprestimo);

        // data devolução pode não estar declarada caso o livro ainda 
        // não foi devolvido
        if(data_devolucao != null){
            System.out.print("Data de devolução: ");
            System.out.println(data_devolucao);
        }
        else{
            System.out.println("Devolução pendente");
        }
    }
    public Date getEmprestimo(){
        return data_emprestimo;
    }
    public Date getDevolucao(){
        return data_devolucao;
    }
    public Livro getLivro(){
        return livro_emprestado;
    }
    public Usuario getUsuario(){
        return usuario_emprestado;
    }
};

