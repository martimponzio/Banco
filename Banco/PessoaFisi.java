package Banco;

public class PessoaFisi extends Cliente {
    private String cpf;

    public PessoaFisi(String id, String name, String cpf) {
        super(id, name);
        this.cpf = cpf;
    }
    
    public PessoaFisi(String name, String cpf) {
        super(name);
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }

    @Override
    public String toString() {
        return super.toString() + " (" + cpf + ")";
    }
    
}
