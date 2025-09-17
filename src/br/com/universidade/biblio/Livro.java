package br.universidade.biblio;

public class Livro {
  // Atributos
  private String titulo;
  private String autor;
  private int ano;
  private boolean disponivel;

  // Validação de dados
  private void validarDados(String titulo, String autor, int ano) {
    if (titulo == null || titulo.trim().isEmpty())
      throw new IllegalArgumentException("Título não pode ser nulo ou vazio");
    if (autor == null || autor.trim().isEmpty())
      throw new IllegalArgumentException("Autor não pode ser nulo ou vazio");
    if (ano <= 0)
      throw new IllegalArgumentException("Ano deve ser um valor positivo");
  }

  // Construtores
  public Livro(String titulo, String autor, int ano, boolean disponivel) {
    validarDados(titulo, autor, ano);

    this.titulo = titulo;
    this.autor = autor;
    this.ano = ano;
    this.disponivel = disponivel;
  }

  public Livro(String titulo, String autor, int ano) {
    this(titulo, autor, ano, true);
  }

  // Getters e Setters
  public void setDisponivel(boolean disponivel) {
    this.disponivel = disponivel;
  }

  public String getTitulo() {
    return titulo;
  }

  public String getAutor() {
    return autor;
  }

  public int getAno() {
    return ano;
  }

  public boolean isDisponivel() {
    return disponivel;
  }
}
