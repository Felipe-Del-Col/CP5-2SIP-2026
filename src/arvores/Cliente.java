package arvores;

public class Cliente {

    private String nome;
    private String cpf;
    private String whatsapp;
    private double totalGasto;
    private boolean aptoOferta;

    public Cliente(String nome, String cpf, String whatsapp, double totalGasto) {
        this.nome = nome;
        this.cpf = cpf;
        this.whatsapp = whatsapp;
        this.totalGasto = totalGasto;

        // Todo cliente começa apto a receber oferta
        this.aptoOferta = true;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public double getTotalGasto() {
        return totalGasto;
    }

    public boolean isAptoOferta() {
        return aptoOferta;
    }

    public void setAptoOferta(boolean aptoOferta) {
        this.aptoOferta = aptoOferta;
    }

    public void apresentar() {

        System.out.println("Nome: " + nome);
        System.out.println("CPF: " + cpf);
        System.out.println("WhatsApp: " + whatsapp);

        System.out.printf(
                "Valor Total em Compras: R$ %.2f%n",
                totalGasto
        );

        System.out.println(
                "Apto para Oferta: "
                        + (aptoOferta ? "Sim" : "Nao")
        );
    }

    @Override
    public String toString() {

        return "Nome: " + nome
                + " | CPF: " + cpf
                + " | WhatsApp: " + whatsapp
                + " | Total gasto: R$ "
                + String.format("%.2f", totalGasto)
                + " | Apto: "
                + (aptoOferta ? "Sim" : "Nao");
    }
}