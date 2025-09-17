import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.ArrayList;

public class Usuario {
	private String nome;
	private String matricula;
	private String curso;
	 private List<Livro> listaDeEmprestimos = new ArrayList<>();
	
	// Construtor
	public Usuario(String nome, String curso) {
		this.nome = nome;
		this.matricula = geradorMatricula();
		this.curso = curso;
		this.listaDeEmprestimos = new Usuario[10]; 
	}

	private String geradorMatricula() {
        LocalDateTime agora = LocalDateTime.now();

		//formata o anos, mês, dia e hora
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuuMMddHH");
        String dataFormatada = formatter.format(agora);

        Random random = new Random();
        int numeroAleatorio = 1000 + random.nextInt(9000);

		//concatena a data formatada com o número aleatório
        return dataFormatada + numeroAleatorio;
    }

    public void adicionarEmprestimo(Livro livro){
        listaDeEmprestimos.add(livro);
    }

	//Setters
	public void setNome(String nome){
		this.nome = nome; 
	}
	public void setCurso(String curso){
		this.curso = curso;
	}

	//Getters
	public String getNome(){
		return this.nome;
	}

	public String getCurso(){
		return this.curso;
	}

	public String getMatricula(){
		return this.matricula;
	}

	public Usuario[] getEmprestimos(){
		return this.listaDeEmprestimos;
	}
}