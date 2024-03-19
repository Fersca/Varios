import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class MovableImageAWT extends Frame {
    
    //Imágenes
    private Image img;
    private int x = 100, y = 100; // Posición inicial de la imagen
    
    private Image pajaro;
    private int p_x = 1, p_y = 1; // Posición inicial de la imagen
    

    private Set<Integer> pressedKeys = new HashSet<>();

    public MovableImageAWT() {
        super("Imagen Móvil AWT");

        //setUndecorated(true); // Elimina los bordes y barra de título
        setBackground(Color.BLACK);      
        setExtendedState(Frame.MAXIMIZED_BOTH); // Maximiza la ventana.  

        // Carga las imágenes 
        img = Toolkit.getDefaultToolkit().getImage("/Users/Fernando.Scasserra/Downloads/zorro.png");
        pajaro = Toolkit.getDefaultToolkit().getImage("/Users/Fernando.Scasserra/Downloads/pajaro.png");        
        
        MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(img, 0);
        tracker.addImage(pajaro,1);

        try {
            tracker.waitForAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        MyCanvas canvas = new MyCanvas();
        add(canvas);
        setSize(800, 600);
        setVisible(true);
        
        // Timer para mover la imagen cada N milisegundos
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Actualiza la posición de la imagen
                p_x += 1;
                p_y += 1;
                
                // Repinta el canvas para mostrar la imagen en la nueva posición
                canvas.repaint();
                
                // Puedes añadir condiciones para detener o cambiar la dirección del movimiento
            }
        }, 0, 50); // Comienza de inmediato, se repite cada DELAY milisegundos

        //Listener para las teclas
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                pressedKeys.add(e.getKeyCode()); // Añade la tecla presionada al conjunto
                moveImage();
                canvas.repaint();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(e.getKeyCode()); // Elimina la tecla liberada del conjunto
            }
        });

        //Listener para cerrar la ventana
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                timer.cancel(); // Asegúrate de cancelar el timer cuando cierres la ventana
                System.exit(0);
            }
        });
    }

    private void moveImage() {
        // Verifica combinaciones específicas de teclas
        if (pressedKeys.contains(KeyEvent.VK_J) && pressedKeys.contains(KeyEvent.VK_I)) {
            x -= 7;
            y -= 7;
        } else if (pressedKeys.contains(KeyEvent.VK_L) && pressedKeys.contains(KeyEvent.VK_K)) {
            x += 7;
            y += 7;
        }
        else if (pressedKeys.contains(KeyEvent.VK_L) && pressedKeys.contains(KeyEvent.VK_I)) {
            x += 7;
            y -= 7;
        } 
        else if (pressedKeys.contains(KeyEvent.VK_J) && pressedKeys.contains(KeyEvent.VK_K)) {
            x -= 7;
            y += 7;
        }               
        else if (pressedKeys.contains(KeyEvent.VK_J) && pressedKeys.size()==1) {
            x -= 10;
        }           
        else if (pressedKeys.contains(KeyEvent.VK_L) && pressedKeys.size()==1) {
            x += 10;
        }           
        else if (pressedKeys.contains(KeyEvent.VK_I) && pressedKeys.size()==1) {
            y -= 10;
        }           
        else if (pressedKeys.contains(KeyEvent.VK_K) && pressedKeys.size()==1) {
            y += 10;
        }           

    }

    class MyCanvas extends Canvas {
        @Override
        public void paint(Graphics g) {
            if (img != null && pajaro != null) {
                int newWidth_i = img.getWidth(this) / 5;
                int newHeight_i = img.getHeight(this) / 5;
                g.drawImage(img, x, y, newWidth_i, newHeight_i, this);

                int newWidth_p = pajaro.getWidth(this) / 6;
                int newHeight_p = pajaro.getHeight(this) / 6;
                g.drawImage(pajaro, p_x, p_y, newWidth_p, newHeight_p, this);
                
                boolean solapa_x = ((p_x>x && p_x <(x+newWidth_i)) || ((p_x+newWidth_p)>x && (p_x+newWidth_p) <(x+newWidth_i))); 
                boolean solapa_y = ((p_y>y && p_y <(y+newHeight_i)) || ((p_y+newHeight_p)>x && (p_y+newHeight_p) <(y+newHeight_i))); 

                if (solapa_x && solapa_y){
                    // Define el color del texto
                    g.setColor(Color.RED);
                } else {
                    // Define el color del texto
                    g.setColor(Color.WHITE);
                }

                // Define la fuente del texto
                g.setFont(new Font("SansSerif", Font.BOLD, 20));
                
                // Dibuja el texto en el Canvas
                String text = "x:"+x+" y:"+y+" p_x:"+p_x+" p_y:"+p_y;

                g.drawString(text, 50, 50); // Ajusta las coordenadas según necesites                

            }
        }
    }

    public static void main(String[] args) {
        new MovableImageAWT();
    }
}
