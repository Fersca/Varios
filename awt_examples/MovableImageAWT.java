import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

public class MovableImageAWT extends Frame {

    private ArrayList<Character> personajes = new ArrayList<Character>();
    private Character principal;
    private Set<Integer> pressedKeys = new HashSet<>();
    // Timer para mover la imagen cada N milisegundos
    Timer timer = new Timer();
    BufferStrategy bs;

    public void crearPersonajes(){

        //Crea el movimiento nulo
        Function<Character, Void> movimientoNulo =(Character c) -> {return null;};

        //Crea el movimiento de rebote
        Function<Character, Void> movimientoRebote =(Character c) -> { 

            //avanza al personaje
            if ("derecha".equals(c.avanzando_x))
                c.x = c.x+c.velocidad;
            if ("izquierda".equals(c.avanzando_x))
                c.x = c.x-c.velocidad;
            if ("abajo".equals(c.avanzando_y))
                c.y = c.y+c.velocidad;
            if ("arriba".equals(c.avanzando_y))
                c.y = c.y-c.velocidad;

            int ancho = getWidth();
            int alto = getHeight();
    
            if (c.centroX>ancho)
                c.avanzando_x="izquierda";
            if (c.centroX<0)
                c.avanzando_x="derecha";
            if (c.centroY>alto)
                c.avanzando_y="arriba";
            if (c.centroY<0)
                c.avanzando_y="abajo";

            return null;
        };

        //crea los personajes
        Character zorrito = new Character("Zorrito","/Users/Fernando.Scasserra/Downloads/zorro.png",8,movimientoNulo);
        zorrito.setImagenColision("/Users/Fernando.Scasserra/Downloads/zorro_muerto.png");

        //pajaro estandar
        Character pajaro = new Character("Pajaro","/Users/Fernando.Scasserra/Downloads/pajaro.png",6,movimientoRebote);

        //pajaro con velocidad y para arriba
        Character pajaro2 = new Character("Pajaro","/Users/Fernando.Scasserra/Downloads/pajaro.png",6,movimientoRebote);
        pajaro2.velocidad = 2;
        pajaro2.avanzando_y = "arriba";
        
        //agrega los personajes a la lista
        personajes.add(zorrito);
        personajes.add(pajaro);
        personajes.add(pajaro2);

        //Setea a zorrito como personaje pricipal
        this.principal = zorrito;        

        //crea el tracker de imágenes
        MediaTracker tracker = new MediaTracker(this);
        int i = 0;
        for (Character c : personajes) {
            tracker.addImage(c.img, i);
            i++;
        }    
        try {
            tracker.waitForAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public MovableImageAWT() {
        super("Imagen Móvil AWT");

        //Crea los personajes del juego
        crearPersonajes();

        //setUndecorated(true); // Elimina los bordes y barra de título
        setBackground(Color.BLACK);      
        setExtendedState(Frame.MAXIMIZED_BOTH); // Maximiza la ventana.  
    
        MyCanvas canvas = new MyCanvas();
        add(canvas);
        setSize(800, 600);
        setVisible(true);
        
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                // Actualiza la posición de la imagen
                for (Character c : personajes) {
                    c.seMueve();
                }

                //Calcular si colisionaron
                for (Character c : personajes) {
                    //No chequea colisión contra el principal.
                    if (c.name.equals(principal.name))
                        continue;
                    principal.verificaColision(c);
                }

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
                moveImage(principal);
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

    private void moveImage(Character c) {
        // Verifica combinaciones específicas de teclas
        if (pressedKeys.contains(KeyEvent.VK_J) && pressedKeys.contains(KeyEvent.VK_I)) {
            c.x -= 7;
            c.y -= 7;
        } else if (pressedKeys.contains(KeyEvent.VK_L) && pressedKeys.contains(KeyEvent.VK_K)) {
            c.x += 7;
            c.y += 7;
        }
        else if (pressedKeys.contains(KeyEvent.VK_L) && pressedKeys.contains(KeyEvent.VK_I)) {
            c.x += 7;
            c.y -= 7;
        } 
        else if (pressedKeys.contains(KeyEvent.VK_J) && pressedKeys.contains(KeyEvent.VK_K)) {
            c.x -= 7;
            c.y += 7;
        }               
        else if (pressedKeys.contains(KeyEvent.VK_J) && pressedKeys.size()==1) {
            c.x -= 10;
        }           
        else if (pressedKeys.contains(KeyEvent.VK_L) && pressedKeys.size()==1) {
            c.x += 10;
        }           
        else if (pressedKeys.contains(KeyEvent.VK_I) && pressedKeys.size()==1) {
            c.y -= 10;
        }           
        else if (pressedKeys.contains(KeyEvent.VK_K) && pressedKeys.size()==1) {
            c.y += 10;
        } else if (pressedKeys.contains(KeyEvent.VK_Q) && pressedKeys.size()==1){
            timer.cancel();
            System.exit(0);
        }

    }

    class MyCanvas extends Canvas {

        @Override        
        public void paint(Graphics g) {

            // Asegurarse de que exista una BufferStrategy
            if (getBufferStrategy() == null) {
                createBufferStrategy(3); // Crea una estrategia de doble buffer
                return; // Salir del método para permitir que la estrategia se cree correctamente
            }

            BufferStrategy bs = getBufferStrategy();
            Graphics bg = bs.getDrawGraphics();

            bg.setColor(Color.BLACK);            
            bg.fillRect(0, 0, getWidth(), getHeight()); // Dibuja un fondo
            
            //Dibuja los personajes
            for (Character c : personajes){
                if (c.img!=null){
                    c.drawImage(this, bg);
                }
            }

            // Define la fuente del texto
            bg.setFont(new Font("SansSerif", Font.BOLD, 20));

            String text;
            if (principal.colisionado){
                // Define el color del texto
                bg.setColor(Color.RED);
                // Dibuja el texto en el Canvas
                text = "MUERTO";

            } else {
                // Define el color del texto
                bg.setColor(Color.WHITE);
                // Dibuja el texto en el Canvas
                text = "VIVO";
            }

            bg.drawString(text, 50, 50);

            bg.dispose(); // Liberar los recursos del Graphics
            bs.show(); // Mostrar el contenido del buffer

        }
    }

    public static void main(String[] args) {
        new MovableImageAWT();
    }
}


//Clase que guarda el personaje
class Character {
    public Image img;
    public Image img_colision;
    public int x = 100;
    public int y = 100;
    private int scale;
    public Function<Character, Void> movimiento;
    public String name;
    public int centroX;
    public int centroY;
    public int radio;
    public boolean colisionado;
    public String avanzando_x = "derecha";
    public String avanzando_y = "abajo";
    public int velocidad = 1;

    public Character(String name, String imageFile, int scale, Function<Character, Void> movimientoPersonaje){
        this.img = Toolkit.getDefaultToolkit().getImage(imageFile);
        this.scale = scale;
        this.movimiento = movimientoPersonaje;
        this.name = name;    
    };

    public void setImagenColision(String imageFileColision){
        if (imageFileColision!=null)
            this.img_colision = Toolkit.getDefaultToolkit().getImage(imageFileColision);
    }

    public void drawImage(Canvas canvas, Graphics g){
    
        Image imgTemp;
        if (colisionado)
            imgTemp = img_colision;
        else
            imgTemp = img;

        int newWidth = img.getWidth(canvas) / scale;
        int newHeight = img.getHeight(canvas) / scale;

        g.drawImage(imgTemp, x, y, newWidth, newHeight, canvas);

        centroX = x+newWidth/2;
        centroY = y+newHeight/2;
        //deja el radio más grande
        radio = (newWidth>newHeight)?newWidth/2:newHeight/2;    
    };

    public void seMueve(){
        this.movimiento.apply(this);
    };

    //Verifica si hay una colisión entre los dos objetos
    public void verificaColision(Character c){

        //Calcula los lados del triángulo
        int lado1 = this.x-c.x;
        int lado2 = this.y-c.y;

        //da vuelta los valores si son negativos
        if (lado1<0)
            lado1 = c.x-this.x;

        if (lado2<0)
            lado2 = c.y-this.y;

        //calcula la hipotenusa
        double distancia = Math.sqrt(lado1*lado1 + lado2*lado2);  

        //si la distancia es menor a la suma de los radios, hay colisión
        if (distancia <(this.radio+c.radio)){
            colisionado=true;
        } else {
            colisionado = false;
        }

    }

}