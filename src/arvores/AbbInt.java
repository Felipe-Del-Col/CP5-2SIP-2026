package arvores;

public class AbbInt {

    /*
     * Nó da árvore.
     *
     * A classe original armazenava um int.
     * Agora o nó armazena um objeto Cliente.
     */
    private class No {
        Cliente dado;
        No esq;
        No dir;

        public No(Cliente dado) {
            this.dado = dado;
            this.esq = null;
            this.dir = null;
        }
    }

    /*
     * Raiz da ABB.
     */
    public No root = null;

    // =========================================================
    // 1) INSERÇÃO USANDO CPF
    // =========================================================

    /*
     * Insere um cliente na ABB usando o CPF
     * como critério de organização.
     */
    private No inserirPorCpf(No p, Cliente cliente) {
        if (p == null) {
            p = new No(cliente);
        } else if (
                cliente.getCpf().compareTo(p.dado.getCpf()) < 0
        ) {
            p.esq = inserirPorCpf(p.esq, cliente);

        } else {

            p.dir = inserirPorCpf(p.dir, cliente);
        }

        return p;
    }

    /*
     * Método público para inserir cliente por CPF.
     */
    public void inserirPorCpf(Cliente cliente) {

        root = inserirPorCpf(root, cliente);
    }

    // =========================================================
    // 2) INSERÇÃO USANDO VALOR DE GASTO
    // =========================================================

    /*
     * Insere um cliente na ABB usando
     * o total gasto como critério.
     */
    private No inserirPorGasto(No p, Cliente cliente) {

        if (p == null) {
            p = new No(cliente);
        } else if (cliente.getTotalGasto() < p.dado.getTotalGasto()
        ) {
            p.esq = inserirPorGasto(p.esq, cliente);

        } else {
            p.dir = inserirPorGasto(p.dir, cliente);
        }

        return p;
    }

    /*
     * Método público para inserir cliente por gasto.
     */
    public void inserirPorGasto(Cliente cliente) {

        root = inserirPorGasto(root, cliente);
    }

    // =========================================================
    // 3) CONSULTA POR CPF
    // =========================================================

    /*
     * Consulta um cliente pelo CPF.
     *
     * Este método deve ser utilizado na ABB
     * de cadastro, que está organizada por CPF.
     */
    private Cliente consultaCpf(No p, String cpf) {
        if (p == null) {
            return null;
        }
        int comparacao = cpf.compareTo(p.dado.getCpf());

        if (comparacao == 0) {

            return p.dado;

        } else if (comparacao < 0) {

            return consultaCpf(p.esq, cpf);

        } else {

            return consultaCpf(p.dir, cpf);
        }
    }

    /*
     * Método público de consulta.
     */
    public Cliente consultaPorCpf(String cpf) {

        return consultaCpf(root, cpf);
    }

    // =========================================================
    // 4) SOMATÓRIO DOS GASTOS
    // =========================================================

    /*
     * Percorre toda a árvore e soma
     * o total gasto de todos os clientes.
     */
    private double somaGastos(No p) {
        if (p == null) {
            return 0;
        }

        return p.dado.getTotalGasto() + somaGastos(p.esq) + somaGastos(p.dir);
    }

    /*
     * Método público para somatório.
     */
    public double somaGastos() {

        return somaGastos(root);
    }

    // =========================================================
    // 5) CONTAGEM ACIMA DE UM LIMITE
    // =========================================================

    /*
     * Conta quantos clientes possuem
     * gasto acima do limite.
     */
    private int contaGastoAcima(No p, double limite
    ) {

        if (p == null) {
            return 0;
        }

        int contador = 0;

        if (p.dado.getTotalGasto() > limite) {
            contador++;
        }
        contador += contaGastoAcima(p.esq, limite);

        contador += contaGastoAcima(p.dir, limite);

        return contador;
    }

    /*
     * Método público.
     */
    public int contaGastoAcima(
            double limite
    ) {

        return contaGastoAcima(root, limite);
    }

    // =========================================================
    // 6) ATUALIZA STATUS DO CLIENTE
    // =========================================================

    /*
     * Busca o cliente pelo CPF e altera
     * seu status de aptidão para oferta.
     */
    public boolean atualizaApto(String cpf, boolean apto) {
        Cliente cliente = consultaPorCpf(cpf);
        if (cliente != null) {
            cliente.setAptoOferta(apto);
            return true;
        }
        return false;
    }

    // =========================================================
    // 7) GERAÇÃO DA ABB DE OFERTA
    // =========================================================

    /*
     * Percorre a ABB de cadastro.
     *
     * Para cada cliente:
     *
     * 1. Verifica se está apto;
     * 2. Verifica se o gasto é maior ou igual ao limite;
     * 3. Insere na ABB de oferta usando o gasto como
     *    critério de organização.
     */
    private void gerarAbbOferta(No p, AbbInt abbOferta, double limite) {
        if (p != null) {
            gerarAbbOferta(p.esq, abbOferta, limite);
            Cliente cliente = p.dado;
            if (
                    cliente.isAptoOferta() && cliente.getTotalGasto() >= limite
            ) {

                abbOferta.inserirPorGasto(cliente
                );
            }

            gerarAbbOferta(p.dir, abbOferta, limite);
        }
    }

    /*
     * Método público para gerar a ABB de oferta.
     */
    public void gerarAbbOferta(AbbInt abbOferta, double limite) {
        gerarAbbOferta(root, abbOferta, limite);
    }

    // =========================================================
    // 8) GERAÇÃO DA FILA
    // =========================================================

    /*
     * Percurso:
     *
     * DIREITA
     * RAIZ
     * ESQUERDA
     *
     * Como a ABB de oferta está organizada
     * pelo valor gasto, o resultado será:
     *
     * MAIOR GASTO
     *
     * ...
     *
     * MENOR GASTO
     */
    private void gerarFilaOferta(No p, java.util.Queue<Cliente> fila) {

        if (p != null) {
            gerarFilaOferta(p.dir, fila);

            fila.add(p.dado);

            gerarFilaOferta(p.esq, fila);
        }
    }

    /*
     * Método público para gerar a fila.
     */
    public void gerarFilaOferta(java.util.Queue<Cliente> fila) {
        gerarFilaOferta(root, fila);
    }

    // =========================================================
    // REMOÇÃO POR CPF
    // =========================================================

    /*
     * Remove cliente da ABB de cadastro usando CPF.
     */
    private No removeCliente(No p, String cpf) {
        if (p == null) {
            return null;
        }
        int comparacao = cpf.compareTo(p.dado.getCpf());

        if (comparacao < 0) {
            p.esq = removeCliente(p.esq, cpf);

        } else if (comparacao > 0) {

            p.dir = removeCliente(p.dir, cpf);

        } else {

            /*
             * Nó sem filhos.
             */
            if (
                    p.esq == null && p.dir == null
            ) {

                return null;
            }

            /*
             * Somente filho direito.
             */
            if (p.esq == null) {

                return p.dir;
            }

            /*
             * Somente filho esquerdo.
             */
            if (p.dir == null) {

                return p.esq;
            }

            /*
             * Dois filhos.
             *
             * Procura o menor elemento
             * da subárvore direita.
             */
            No sucessor = p.dir;

            while (
                    sucessor.esq != null
            ) {

                sucessor = sucessor.esq;
            }

            p.dado = sucessor.dado;

            p.dir = removeCliente(p.dir, sucessor.dado.getCpf()
            );
        }

        return p;
    }

    /*
     * Método público de remoção.
     */
    public void removeCliente(String cpf
    ) {

        root = removeCliente(root, cpf);
    }

    // =========================================================
    // APRESENTAR EM ORDEM
    // =========================================================

    /*
     * Percurso em ordem da ABB.
     */
    private void mostrarEmOrdem(No p) {

        if (p != null) {

            mostrarEmOrdem(p.esq);

            p.dado.apresentar();

            System.out.println();

            mostrarEmOrdem(p.dir);
        }
    }

    /*
     * Método público.
     */
    public void mostrarEmOrdem() {

        mostrarEmOrdem(root);
    }

    // =========================================================
    // CLIENTES QUE NÃO ACEITARAM OFERTA
    // =========================================================

    /*
     * Apresenta clientes que continuam aptos.
     *
     * Se um cliente recusou uma oferta,
     * seu status permanece true.
     *
     * Se nunca aceitou nenhuma oferta,
     * também permanece true.
     */
    private void apresentarNaoAceitaram(No p) {

        if (p != null) {

            apresentarNaoAceitaram(p.esq);

            if (
                    p.dado.isAptoOferta()
            ) {

                p.dado.apresentar();

                System.out.println();
            }

            apresentarNaoAceitaram(p.dir);
        }
    }

    /*
     * Método público.
     */
    public void apresentarNaoAceitaram() {

        apresentarNaoAceitaram(root);
    }

    // =========================================================
    // ESVAZIAR ABB
    // =========================================================

    /*
     * A ABB de oferta é utilizada apenas
     * durante a geração da fila.
     *
     * Depois que a fila é gerada,
     * ela deve ser esvaziada.
     */
    public void esvaziar() {

        root = null;
    }
}