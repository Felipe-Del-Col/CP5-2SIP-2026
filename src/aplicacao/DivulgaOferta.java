package aplicacao;
/*
* Nome: Felipe Del Col
* RM: 556176
*/
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import arvores.AbbInt;
import arvores.Cliente;


import java.util.LinkedList;
import java.util.Queue;


public class DivulgaOferta {

    public static void main(String[] args) {

        Scanner le = new Scanner(System.in);

        /*
         * ABB principal de cadastro.
         *
         * Organização:
         * CPF
         */
        AbbInt cadastro = new AbbInt();

        /*
         * ABB utilizada somente para a oferta.
         *
         * Organização:
         * valor total gasto.
         */
        AbbInt oferta = new AbbInt();

        /*
         * Fila de clientes que serão contatados.
         *
         * O primeiro será o cliente com maior gasto.
         */
        Queue<Cliente> filaOferta = new LinkedList<>();

        int opcao;
        int op;

        String nome;
        String cpf;
        String whatsapp;
        double totalGasto;

        boolean arquivoCadastrado = false;

        do {

            System.out.println();
            System.out.println("==========================================");
            System.out.println("       SISTEMA DE DIVULGACAO DE OFERTAS");
            System.out.println("==========================================");

            System.out.println("0 - Encerrar o programa");

            System.out.println("1 - Inscricao de clientes por leitura de arquivo");

            System.out.println("2 - Inscricao de cliente");

            System.out.println("3 - Oferta de novo produto/promocao");

            System.out.println("4 - Entrar no submenu");

            System.out.println("5 - Remover cliente do cadastro");

            System.out.println("==========================================");

            System.out.print("Opcao: ");

            opcao = le.nextInt();

            switch (opcao) {

                // =================================================
                // OPÇÃO 0
                // =================================================

                case 0 -> {

                    System.out.println();
                    System.out.println("==========================================");

                    System.out.println("CLIENTES QUE NAO ACEITARAM OFERTAS");

                    System.out.println("==========================================");

                    cadastro.apresentarNaoAceitaram();

                    System.out.println();
                    System.out.println("Programa encerrado.");
                }

                // =================================================
                // OPÇÃO 1
                // =================================================

                case 1 -> {

                    if (!arquivoCadastrado) {

                        cadastrarBackupDeClientes(cadastro);

                        arquivoCadastrado = true;

                        System.out.println();

                        System.out.println("Clientes do arquivo cadastrados com sucesso!");

                    } else {

                        System.out.println();

                        System.out.println("Arquivo ja cadastrado!");
                    }
                }

                // =================================================
                // OPÇÃO 2
                // =================================================

                case 2 -> {

                    System.out.println();
                    System.out.println("===== INSCRICAO DE CLIENTE =====");

                    le.nextLine();

                    System.out.print("Digite nome: ");

                    nome = le.nextLine();

                    System.out.print("Digite CPF: ");

                    cpf = le.next();

                    System.out.print("WhatsApp: ");

                    whatsapp = le.next();

                    System.out.print("Informe total gasto do cliente R$: ");

                    totalGasto = le.nextDouble();

                    Cliente cliente = new Cliente(nome, cpf, whatsapp, totalGasto);

                    /*
                     * Inserção na ABB de cadastro
                     * usando CPF.
                     */
                    cadastro.inserirPorCpf(
                            cliente
                    );

                    System.out.println();

                    System.out.println("Cliente cadastrado com sucesso!");
                }

                // =================================================
                // OPÇÃO 3
                // =================================================

                case 3 -> {

                    System.out.println();
                    System.out.println("===== NOVA OFERTA/PROMOCAO =====");

                    System.out.print("Informe o valor minimo de gasto: R$ ");

                    double limite = le.nextDouble();

                    /*
                     * A ABB de oferta deve ser utilizada
                     * somente para a geração da fila.
                     *
                     * Portanto, garantimos que ela esteja vazia.
                     */
                    oferta.esvaziar();

                    /*
                     * A fila também deve começar vazia.
                     */
                    filaOferta.clear();

                    /*
                     * Percorre a ABB de cadastro.
                     *
                     * São selecionados somente:
                     *
                     * gasto >= limite
                     * E
                     * apto para oferta
                     *
                     * Os clientes selecionados são
                     * inseridos na ABB de oferta
                     * usando o GASTO como organização.
                     */
                    cadastro.gerarAbbOferta(oferta, limite);

                    /*
                     * Percorre a ABB de oferta
                     * e gera a fila:
                     *
                     * maior gasto
                     * ↓
                     * menor gasto
                     */
                    oferta.gerarFilaOferta(filaOferta);

                    if (filaOferta.isEmpty()) {
                        System.out.println();
                        System.out.println("Nenhum cliente esta apto para receber esta oferta.");

                    } else {

                        System.out.println();
                        System.out.println("Fila de clientes gerada com sucesso!");

                        /*
                         * Processa a fila inteira.
                         */
                        while (
                                !filaOferta.isEmpty()
                        ) {

                            Cliente cliente = filaOferta.poll();

                            System.out.println();
                            System.out.println("------------------------------------------");

                            System.out.println("CLIENTE A SER CONTATADO");

                            System.out.println("------------------------------------------");

                            cliente.apresentar();

                            System.out.println();
                            System.out.println("Oferta especial disponivel!");

                            System.out.println("1 - Aceitar oferta");

                            System.out.println("2 - Nao aceitar oferta");

                            System.out.print("Resposta: ");

                            int resposta = le.nextInt();

                            if (resposta == 1) {

                                /*
                                 * Cliente aceitou.
                                 *
                                 * O status é atualizado
                                 * por CPF através da ABB.
                                 */
                                cadastro.atualizaApto(cliente.getCpf(), false);

                                System.out.println();

                                System.out.println("Oferta aceita pelo cliente!");

                            } else if (resposta == 2) {

                                /*
                                 * Se recusou, o registro
                                 * permanece inalterado.
                                 */
                                System.out.println();

                                System.out.println("Cliente nao aceitou a oferta.");

                                System.out.println("Registro permanece inalterado.");

                            } else {

                                System.out.println();

                                System.out.println("Resposta invalida.");

                                System.out.println("Registro permanece inalterado.");
                            }
                        }
                    }

                    /*
                     * A ABB de oferta é utilizada
                     * exclusivamente para gerar a fila.
                     *
                     * Depois disso ela é esvaziada.
                     */
                    oferta.esvaziar();

                    System.out.println();

                    System.out.println("Processamento da oferta finalizado.");
                }

                // =================================================
                // OPÇÃO 4
                // =================================================

                case 4 -> {

                    do {

                        System.out.println();
                        System.out.println("==========================================");

                        System.out.println("                 SUBMENU");

                        System.out.println("==========================================");

                        System.out.println("1 - Consulta cliente buscando pelo CPF");

                        System.out.println("2 - Apresenta o total de gasto de todos os clientes");

                        System.out.println("3 - Quantidade de clientes com gasto acima de um valor");

                        System.out.println("4 - Voltar ao menu principal");

                        System.out.println("==========================================");

                        System.out.print("Opcao: ");

                        op = le.nextInt();

                        switch (op) {

                            // =====================================
                            // CONSULTA
                            // =====================================

                            case 1 -> {

                                System.out.print("Informe CPF para consulta: ");

                                cpf = le.next();

                                Cliente cliente = cadastro.consultaPorCpf(cpf);

                                if (cliente != null) {

                                    System.out.println();
                                    System.out.println("===== CLIENTE ENCONTRADO =====");

                                    cliente.apresentar();

                                } else {

                                    System.out.println();
                                    System.out.println("Cliente nao encontrado.");
                                }
                            }

                            // =====================================
                            // SOMATÓRIO
                            // =====================================

                            case 2 -> {

                                double soma = cadastro.somaGastos();

                                System.out.println();

                                System.out.printf("Somatorio de todos os gastos: R$ %.2f%n", soma);
                            }

                            // =====================================
                            // CONTAGEM
                            // =====================================

                            case 3 -> {

                                System.out.print("Informe o valor limite: R$ ");

                                double limite = le.nextDouble();

                                int quantidade = cadastro.contaGastoAcima(limite);

                                System.out.println();

                                System.out.println("Quantidade de clientes com gasto acima de R$ " + String.format("%.2f", limite)+ ": " + quantidade);
                            }

                            // =====================================
                            // VOLTAR
                            // =====================================

                            case 4 -> {

                                System.out.println();

                                System.out.println("Retornando ao menu principal...");
                            }

                            default -> {

                                System.out.println();

                                System.out.println("Opcao invalida!");
                            }
                        }

                    } while (op != 4);
                }

                // =================================================
                // OPÇÃO 5
                // =================================================

                case 5 -> {

                    System.out.println();

                    System.out.print("Informe CPF do cliente que deseja remover: "
                    );

                    cpf = le.next();

                    Cliente cliente = cadastro.consultaPorCpf(cpf);

                    if (cliente != null) {

                        cadastro.removeCliente(cpf);

                        System.out.println();

                        System.out.println("Cliente removido com sucesso!");

                    } else {

                        System.out.println();

                        System.out.println("Cliente nao encontrado.");
                    }
                }

                default -> {

                    System.out.println();

                    System.out.println("Opcao invalida!");
                }
            }

        } while (opcao != 0);

        le.close();
    }

    // =========================================================
    // LEITURA DO ARQUIVO
    // =========================================================

    /*
     * Lê os clientes do arquivo:
     *
     * src/arquivos/backupClientes.txt
     *
     * Formato:
     *
     * Nome,CPF,WhatsApp,TotalGasto
     */
    public static void cadastrarBackupDeClientes(AbbInt cadastro) {

        String caminhoDoArquivo = "src/arquivos/backupClientes.txt";

        try {

            File arquivo = new File(caminhoDoArquivo);

            Scanner leArq = new Scanner(arquivo);

            while (leArq.hasNextLine()) {

                String linha = leArq.nextLine();

                if (
                        linha.trim().isEmpty()
                ) {

                    continue;
                }

                String[] partes = linha.split(",");

                String nome = partes[0].trim();

                String cpf = partes[1].trim();

                String whatsapp = partes[2].trim();

                double totalGasto = Double.parseDouble(partes[3].trim());

                Cliente cliente = new Cliente(nome, cpf, whatsapp, totalGasto);

                /*
                 * O arquivo alimenta a ABB
                 * de cadastro, portanto a
                 * organização é por CPF.
                 */
                cadastro.inserirPorCpf(cliente);
            }

            leArq.close();

        } catch (
                FileNotFoundException e
        ) {

            System.out.println();
            System.out.println("Arquivo nao encontrado: " + e.getMessage());
        }
    }
}
