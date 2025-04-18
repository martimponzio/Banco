package AtividadeBanco.src;

public final class Util {

    private Util() {}

    public static final boolean isCpf(String cpf) {
        if (cpf == null) return false;
        cpf = cpf.replaceAll("[^0-9]", "");
      
        return cpf.length() == 11;
    }
    
}
