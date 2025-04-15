package Banco;

public class PessoaJuri extends Cliente {
    private String cnpj;

    public PessoaJuri(String name, String cnpj) {
        super(name);
        this.cnpj = cnpj;
    }

    public PessoaJuri(String id, String name, String cnpj) {
        super(id, name);
        this.cnpj = cnpj;
    }

    public String getCnpj() {
        return cnpj;
    }
}
