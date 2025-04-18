package AtividadeBanco.src;

public class Excluir {

    public void removeCliente(String clienteId) throws BancoException {
        
        Banco banco = new Banco(clienteId); 
        Cliente cliente = banco.getCliente(clienteId); 

        if (cliente == null) {
            throw new BancoException("D07", "Cliente não encontrado!");
        }

        
        for (Conta conta : cliente.getContas()) {
            cliente.removeConta(conta.getId());
        }
        banco.removeCliente(clienteId); 
    }
}