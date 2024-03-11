import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Robot;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.UIManager;

public class mouse_awt extends Frame implements ActionListener {

    Button boton;
    TextField campo;

    public mouse_awt() {
        // Configurar el layout
    	setLayout(new FlowLayout()); // 2 filas, 1 columna

        // Crear el botón y añadirlo a la ventana
        boton = new Button("Haz clic aquí");
        add(boton);

        //agrega un text field
        campo = new TextField(20);
        add(campo);

        // Añadir listener al botón
        boton.addActionListener(this);

        // Configurar el tamaño y la visibilidad de la ventana
        setTitle("Ejemplo de aplicación");
        setSize(250, 250);
        setVisible(true);

        // Añadir un listener para cerrar la ventana
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                dispose();
		        System.exit(0);
            }
        });
    }

    // Manejar el clic del botón
    public void actionPerformed(ActionEvent e) {
        // Acción a realizar cuando el botón es presionado
        System.out.println(campo.getText());

        try {
            Robot robot = new Robot();

            int a = 0;
            int b = 0;

            for (int c = 0; c<100; c++){
                robot.mouseMove(a,b);
                a++;
                b++;
                Thread.sleep(10);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {

	try {
		
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		//Crea la ventana
    	new mouse_awt();

        } catch (Exception e) {
        	e.printStackTrace();
        }

    }

}

//var ventana = new MiVentanaAWT();