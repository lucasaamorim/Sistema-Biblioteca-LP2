package br.universidade.biblio;

import br.universidade.biblio.Emprestimo;
import br.universidade.biblio.GerenciadorDeDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Relatorio {

  private GerenciadorDeDados db;

  public Relatorio(GerenciadorDeDados db) {
    this.db = db;
  }

  public List<Emprestimo> consultarTodosLivrosEmprestados() {
    return db.consultarTodosEmprestimosBanco();
  }

  public List<Emprestimo> consultarEmprestimosAtrasados() {
    Date hoje = new Date();
    List<Emprestimo> atrasados = new ArrayList<>();

    for (Emprestimo e : db.consultarTodosEmprestimosBanco()) {
      Date dataDevolucao = e.getDevolucao();

      if (dataDevolucao != null && dataDevolucao.before(hoje)) {
        atrasados.add(e);
      }
    }
    return atrasados;
  }

  public List<Usuario> consultarUsuariosComEmprestimosAtrasados() {
    List<Usuario> userList = new ArrayList<>();
    Date hoje = new Date();

    for (Emprestimo e : db.consultarTodosEmprestimosBanco()) {
      Date dataDevolucao = e.getDevolucao();

      if (dataDevolucao != null && dataDevolucao.before(hoje)) {
        Usuario u = e.getUsuario();
        if (!userList.contains(u)) { // evitar duplicados
          userList.add(u);
        }
      }
    }
    return userList;
  }
}
