import gnu.prolog.term.AtomTerm;
import gnu.prolog.term.CompoundTerm;
import gnu.prolog.term.IntegerTerm;
import gnu.prolog.term.Term;
import gnu.prolog.term.VariableTerm;
import gnu.prolog.vm.Environment;
import gnu.prolog.vm.Interpreter;
import gnu.prolog.vm.PrologCode;
import gnu.prolog.vm.PrologException;

public class FactorialProlog
{

	//Variables para el entorno de prolog
	private static Environment env;
	private static Interpreter interpreter;

	public static void main(String[] args) throws PrologException{

		//Inicializa el engine de prolog
		setup();
		
		//pide el factorial de un numero
		int factorial = generateFactorial(5);
		
		//imprime el resultado
		System.out.println("Resultado: "+factorial);
 		
	}

	public static int generateFactorial(int numero) throws PrologException {

		//crea la variable que voy a extraer el prolog
		VariableTerm answerTerm = new VariableTerm("Resultado");

		//creo los argumentos a pasarle al programa
		Term[] args = { new IntegerTerm(numero), answerTerm };

		//ejecuto el programa "factorial"
		CompoundTerm goalTerm = new CompoundTerm(AtomTerm.get("factorial"), args);

		//corro el interprete
		int returnCode = interpreter.runOnce(goalTerm);

		//si devolvio ok obtengo la variable y la devuelvo
		if (returnCode == PrologCode.SUCCESS || returnCode == PrologCode.SUCCESS_LAST){	
			Term value = answerTerm.dereference();
			return ((IntegerTerm) value).value;
		} else {
			return 0;
		}

	}

	private static void setup() {
		//Crea el environment
		env = new Environment();
		//Obtiene el archivo con el código de prolog
		env.ensureLoaded(AtomTerm.get(FactorialProlog.class.getResource("factorial.pro").getFile()));
		//Crea la instancia del interprete
		interpreter = env.createInterpreter();
		//Inicializa el environment con el interprete
		env.runInitialization(interpreter);
	}

}
