package iterador;

import java.util.List;
import java.util.Queue;

public interface Carpeta<T, U>
{
    U aplica(U u, Queue<T> list, Function2<T,U,U> function);
}
