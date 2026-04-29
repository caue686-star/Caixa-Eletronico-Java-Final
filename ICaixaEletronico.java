package grupo_eclipse;

public interface ICaixaEletronico{

public String pegaValorTotalDisponivel();

public String sacar(Integer valor);

public String reposicaoCedulas(Integer cedula, Integer quantidade);

public String armazenaCotaMinima(Integer minimo);

public String pegaRelatorioCedulas();
}
