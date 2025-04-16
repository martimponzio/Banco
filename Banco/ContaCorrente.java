package Banco;

public class ContaCorrente extends Conta {

    private double limite = 0;

    public ContaCorrente(Cliente cliente) {
        this(cliente, 10);
    }

    public ContaCorrente(Cliente cliente, double limite) {
        super(cliente);
        this.limite = limite;
    }

   
    @Override
    public void sacar(double valor) {
        if (valor < 0) {
            throw new RuntimeException("Saque negativo");
        }
        if (valor <= (saldo + limite)) {
            this.saldo -= valor;
        } else {
            throw new RuntimeException("Limite excedido");
        }
    }

    
}
