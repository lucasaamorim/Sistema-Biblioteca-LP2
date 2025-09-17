
import java.util.Date;

public class Utils {
    /**
     * Testa se a String é não vazia e tem um tamanho maior que 1
     * @param texto String a ser validada
     * @return Retorna verdadeiro se a string é válida e falso se não
     */
    public boolean validarTipoString(String texto) {
        if(texto == null || texto.length() <= 1) {
            //System.out.println("Texto inválido, insira novamente!");
            return false;
        }
        return true;
    }
    /**
     * Testa se a Date é não vazia
     * @param data Date a ser validada
     * @return Retorna verdadeiro se a Date é válida e falso se não
     */
    public boolean validarTipoData(Date data) {
        if(data == null) {  
            //System.out.println("Data inválida, insira novamente!");
            return false;
        }
        return true;
    }
    /**
     * Testa se o inteiro é maior que 0
     * @param numero inteiro a ser validado
     * @return Retorna verdadeiro se o inteiro for maior que 0, false se não
     */
    public boolean validarTipoInt(int numero) {
        if(numero <= 0) {
            //System.out.println("Numero inválido, insira novamente!");
            return false;
        }
        return true;  
    }
}