package feira.solucao;

import feira.solucao.cupom.ImpressoraCupom;
import feira.solucao.cupom.ImpressoraTermica;
import feira.solucao.desconto.*;
import feira.solucao.domain.Pedido;
import feira.solucao.domain.Produto;
import feira.solucao.entrega.CalculadoraPrazoEntrega;
import feira.solucao.entrega.EntregaExpressa;
import feira.solucao.entrega.EntregaNormal;
import feira.solucao.notificacao.NotificadorPedido;
import feira.solucao.notificacao.NotificadorWhatsApp;
import feira.solucao.pagamento.*;
import feira.solucao.relatorio.ExportadorCsvPedido;
import feira.solucao.relatorio.ExportadorRelatorioPedido;
import feira.solucao.repository.PedidoRepository;
import feira.solucao.repository.PedidoRepositoryMemoria;
import feira.solucao.service.FinalizadorPedidoService;
import feira.solucao.service.PedidoFinalizado;
import java.util.Arrays;

public class SolucaoMain {
    public static void main(String[] args) {
        PedidoRepository repository = new PedidoRepositoryMemoria();

        CalculadoraDesconto calculadoraDesconto = new CalculadoraDesconto(Arrays.asList(
                new SemDesconto(),
                new DescontoClienteFiel(),
                new DescontoQueimaEstoque(),
                new DescontoDomingo()));

        ServicoPagamento servicoPagamento = new ServicoPagamento(Arrays.asList(
                new PagamentoPix(),
                new PagamentoCartao(),
                new PagamentoBoleto()));

        ImpressoraCupom impressora = new ImpressoraTermica();
        NotificadorPedido notificador = new NotificadorWhatsApp();
        ExportadorRelatorioPedido exportador = new ExportadorCsvPedido();

        FinalizadorPedidoService finalizador = new FinalizadorPedidoService(
                repository,
                calculadoraDesconto,
                servicoPagamento,
                impressora,
                notificador,
                exportador);

        Pedido pedido = new Pedido("Maria da Feira");
        pedido.adicionarItem(new Produto("Tomate", 8.0), 2);
        pedido.adicionarItem(new Produto("Cenoura", 6.5), 3);

        PedidoFinalizado resultado = finalizador.finalizar(
                pedido,
                "CLIENTE_FIEL",
                "PIX",
                "85999990000");

        System.out.println("Total bruto: R$ " + resultado.getTotalBruto());
        System.out.println("Total líquido: R$ " + resultado.getTotalLiquido());
        System.out.println("CSV:\n" + resultado.getRelatorioCsv());

        CalculadoraPrazoEntrega entregaNormal = new EntregaNormal();
        CalculadoraPrazoEntrega entregaExpressa = new EntregaExpressa();
        System.out.println("Prazo normal (30km): " + entregaNormal.calcularPrazoDias(30) + " dia(s)");
        System.out.println("Prazo expresso (30km): " + entregaExpressa.calcularPrazoDias(30) + " dia(s)");
    }
}