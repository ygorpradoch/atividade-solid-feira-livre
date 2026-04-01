package feira.solucao.desconto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalculadoraDesconto {
    private final Map<String, PoliticaDesconto> politicasPorCodigo = new HashMap<>();

    public CalculadoraDesconto(List<PoliticaDesconto> politicas) {
        for (PoliticaDesconto politica : politicas) {
            this.politicasPorCodigo.put(politica.codigo().toUpperCase(), politica);
        }
    }

    public double aplicar(String codigoDesconto, double totalBruto) {
        if (codigoDesconto == null) throw new IllegalArgumentException("Código de desconto é obrigatório");
        
        PoliticaDesconto politica = politicasPorCodigo.get(codigoDesconto.toUpperCase());
        if (politica == null) throw new IllegalArgumentException("Desconto não suportado: " + codigoDesconto);
        
        return politica.aplicar(totalBruto);
    }
}