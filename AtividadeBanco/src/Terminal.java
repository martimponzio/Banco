package AtividadeBanco.src;

import javax.swing.*;
import java.util.List;

public class Terminal {

    private Banco banco;

    public void run() {
        banco = new Banco("PME International Bank");
        Cliente atualCliente = null;
        Conta atualConta = null;

        JOptionPane.showMessageDialog(null, banco.getName(), "Banco", JOptionPane.INFORMATION_MESSAGE);

        while (true) {
            String prompt = "Banco PME International\n\n";
            prompt += atualCliente == null ? "Nenhum cliente selecionado." : atualCliente.getName();
            prompt += atualConta == null ? "" : String.format(" | Conta: %s [Saldo: %.2f]", atualConta.getId(), atualConta.getSaldo());

            String[] options = {
                "Criar Cliente", "Listar Clientes", "Selecionar Cliente", "Criar Conta",
                "Listar Contas", "Selecionar Conta", "Depositar", "Sacar",
                "Listar Todas as Contas", "Render", "Excluir Cliente", "Sair"
            };

            int escolha = JOptionPane.showOptionDialog(null, prompt, "Menu", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (escolha == -1) {
                int confirmar = JOptionPane.showConfirmDialog(null, "Deseja realmente sair?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (confirmar == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null, "Encerrando o programa!", "Fim", JOptionPane.INFORMATION_MESSAGE);
                    return;
                } else {
                    continue;
                }
            }

            try {
                switch (escolha) {
                    case 0:
                        atualCliente = createCustomer();
                        banco.addCliente(atualCliente);
                        atualConta = null;
                        break;
                    case 1:
                        listCustomers();
                        break;
                    case 2:
                        List<Cliente> clientes = banco.getClientes();
                        if (clientes.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Nenhum cliente cadastrado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                            break;
                        }

                        String[] opcoesSelecao = clientes.stream()
                                .map(c -> String.format("%s - [%s]", c.getName(), c.getId()))
                                .toArray(String[]::new);

                        String escolhaCliente = (String) JOptionPane.showInputDialog(
                                null,
                                "Selecione o cliente:",
                                "Selecionar Cliente",
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                opcoesSelecao,
                                opcoesSelecao[0]
                        );

                        if (escolhaCliente != null) {
                            String clienteIdSelecionado = escolhaCliente.substring(escolhaCliente.indexOf("[") + 1, escolhaCliente.indexOf("]"));
                            atualCliente = banco.getCliente(clienteIdSelecionado);
                            atualConta = null;
                        }
                        break;
                    case 3:
                        if (atualCliente == null)
                            throw new BancoException("D05", "Cliente não selecionado");

                        atualConta = createAccount(atualCliente);
                        atualCliente.addConta(atualConta);
                        banco.addConta(atualConta);
                        break;
                    case 4:
                        if (atualCliente == null)
                            throw new BancoException("D05", "Cliente não selecionado");
                        listAccounts(atualCliente.getContas());
                        break;
                    case 5:
                        if (atualCliente == null)
                            throw new BancoException("D05", "Cliente não selecionado");
                        String contaId = JOptionPane.showInputDialog("Digite o código da conta:");
                        atualConta = atualCliente.getConta(contaId);
                        break;
                    case 6:
                        if (atualConta == null)
                            throw new BancoException("D06", "Conta não selecionada");
                        double valorDeposito = inputValue("Digite o valor para depositar:");
                        atualConta.depositar(valorDeposito);
                        break;
                    case 7:
                        if (atualConta == null)
                            throw new BancoException("D06", "Conta não selecionada");
                        double valorSaque = inputValue("Digite o valor para sacar:");
                        atualConta.sacar(valorSaque);
                        break;
                    case 8:
                        listAccounts(banco.getContas());
                        break;
                    case 9:
                        banco.getContas().stream().filter(c -> c instanceof Rendimento)
                                .forEach(c -> ((Rendimento) c).render());
                        JOptionPane.showMessageDialog(null, "Contas renderizadas com sucesso!", "Render", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case 10:
                        List<Cliente> clientesParaExcluir = banco.getClientes();
                        if (clientesParaExcluir.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Nenhum cliente cadastrado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                            break;
                        }

                        String[] opcoesExclusao = clientesParaExcluir.stream()
                                .map(c -> String.format("%s - [%s]", c.getName(), c.getId()))
                                .toArray(String[]::new);

                        String escolhaExclusao = (String) JOptionPane.showInputDialog(
                                null,
                                "Selecione o cliente para excluir:",
                                "Excluir Cliente",
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                opcoesExclusao,
                                opcoesExclusao[0]
                        );

                        if (escolhaExclusao != null) {
                            String clienteIdExcluir = escolhaExclusao.substring(escolhaExclusao.indexOf("[") + 1, escolhaExclusao.indexOf("]"));
                            try {
                                banco.removeCliente(clienteIdExcluir);
                                if (atualCliente != null && atualCliente.getId().equals(clienteIdExcluir)) {
                                    atualCliente = null;
                                    atualConta = null;
                                }
                                JOptionPane.showMessageDialog(null, "Cliente excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                            } catch (BancoException be) {
                                JOptionPane.showMessageDialog(null, be.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        break;
                    case 11:
                        JOptionPane.showMessageDialog(null, "Encerrando o programa!", "Fim", JOptionPane.INFORMATION_MESSAGE);
                        return;
                }
            } catch (BancoException be) {
                JOptionPane.showMessageDialog(null, be.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void listCustomers() {
        StringBuilder sb = new StringBuilder("Clientes:\n");
        banco.getClientes().forEach(c -> sb.append(c.toString()).append("\n"));
        JOptionPane.showMessageDialog(null, sb.toString(), "Clientes", JOptionPane.INFORMATION_MESSAGE);
    }

    private void listAccounts(List<Conta> contas) {
        StringBuilder sb = new StringBuilder("Contas:\n");
        contas.forEach(c -> sb.append(c.toString()).append("\n"));
        JOptionPane.showMessageDialog(null, sb.toString(), "Contas", JOptionPane.INFORMATION_MESSAGE);
    }

    private Cliente createCustomer() {
        String name = JOptionPane.showInputDialog("Nome do Cliente:");
        String tipo = JOptionPane.showInputDialog("Tipo de cliente: Física (f) ou Jurídica (j)?");

        if (tipo.equalsIgnoreCase("f")) {
            String cpf;
            do {
                cpf = JOptionPane.showInputDialog("CPF:");
                if (Util.isCpf(cpf)) break;
                JOptionPane.showMessageDialog(null, "CPF inválido", "Erro", JOptionPane.ERROR_MESSAGE);
            } while (true);
            return new PessoaFisica(name, cpf);
        } else {
            String cnpj = JOptionPane.showInputDialog("CNPJ:");
            return new PessoaJuridica(name, cnpj);
        }
    }

    private Conta createAccount(Cliente cliente) {
        String tipo = JOptionPane.showInputDialog("Tipo de conta: (P)oupança, (C)orrente ou (I)nvestimento?").toLowerCase();
        switch (tipo) {
            case "p":
                return new ContaPoupanca(cliente);
            case "c":
                return new ContaCorrente(cliente);
            default:
                return new ContaInvestimento(cliente);
        }
    }

    private double inputValue(String message) {
        while (true) {
            try {
                return Double.parseDouble(JOptionPane.showInputDialog(message));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Valor inválido. Tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}