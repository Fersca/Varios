package ancestroComun;

public interface BuscarAncestroComun
{
    String buscarAncestroComun(String[] commitHashes, String[][] parentHashes, String commitHash1, String commitHash2);
}
