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
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.RenderingHints;

public class MovableImageAWT extends Frame {

    private ArrayList<Character> personajes = new ArrayList<Character>();
    private Character principal;
    private Set<Integer> pressedKeys = new HashSet<>();
    // Timer para mover la imagen cada N milisegundos
    Timer timer = new Timer();
    BufferStrategy bs;
    boolean imageWithBuffer = false;
    MyCanvas canvas = new MyCanvas();
    boolean terminado = false;

    //posicion de la jaula en el mapa
    int jaulaX = 375;
    int jaulaY = 360;

    //Crea el movimiento nulo
    Function<Character, Void> movimientoNulo =(Character c) -> {return null;};

    //Crea el movimiento de rebote
    Function<Character, Void> movimientoRebote =(Character c) -> { 

        //Si está colisionado me lo manda a la jaula.
        if (c.colisionado){
            c.x = jaulaX+25;
            c.y = jaulaY+40;
            return null;
        }

        //avanza al personaje
        if ("derecha".equals(c.avanzando_x))
            c.x = c.x+c.velocidadX;
        if ("izquierda".equals(c.avanzando_x))
            c.x = c.x-c.velocidadX;
        if ("abajo".equals(c.avanzando_y))
            c.y = c.y+c.velocidadY;
        if ("arriba".equals(c.avanzando_y))
            c.y = c.y-c.velocidadY;

        //calcula la dirección del movimiento
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


    public void crearPersonajes(){

        //crea los personajes
        personajes.addAll(creaListaDePersonajes());
        
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

    private Collection<Character> creaListaDePersonajes() {

        ArrayList<Character> personajesCreados = new ArrayList<Character>();

        //crea los personajes
        Character zorrito = new Character("Zorrito","/Users/Fernando.Scasserra/Downloads/zorro.png",14,movimientoNulo);
        zorrito.setImagenColision("/Users/Fernando.Scasserra/Downloads/zorro_muerto.png");

        Character jaula = new Character("Jaula","/Users/Fernando.Scasserra/Downloads/jaula.png",5,movimientoNulo);
        jaula.x = jaulaX;
        jaula.y = jaulaY;
        jaula.colisiona = false;

        //agrega los personajes a la lista
        personajesCreados.add(zorrito);

        //crea muchos pájaros
        for (Character p : crearEnemigos()) {
            personajesCreados.add(p);    
        }
        personajesCreados.add(jaula);

        //Setea a zorrito como personaje pricipal
        this.principal = zorrito;        
        
        return personajesCreados; 

    }


    private ArrayList<Character> crearEnemigos(){

        Random random = new Random();
        ArrayList<Character> enemigos = new ArrayList<Character>();
        String[] movimientosArriba_Abajo = {"arriba","abajo"};
        String[] movimientosIzquierda_Derecha = {"izquierda","derecha"};

        for (int i=0;i<2;i++){

            //pajaro estandar
            Character pajaro = new Character("Pajaro"+i,"/Users/Fernando.Scasserra/Downloads/pajaro.png",15,movimientoRebote);
            pajaro.velocidadX = random.nextInt(10) + 1; 
            System.out.println(pajaro.velocidadX);
            pajaro.velocidadY = random.nextInt(10) + 1; 
            System.out.println(pajaro.velocidadY);
            pajaro.avanzando_y = movimientosArriba_Abajo[random.nextInt(2)];
            System.out.println(pajaro.avanzando_y);
            pajaro.avanzando_x = movimientosIzquierda_Derecha[random.nextInt(2)];
            System.out.println(pajaro.avanzando_x);            
            
            //los pone en la punta de la pantalla
            pajaro.x = 1440;
            pajaro.y = 875;
            enemigos.add(pajaro);
        }

        return enemigos;

    }

    private TimerTask comienzaJuego(MyCanvas canvas, Timer timer){
        return new TimerTask() {
            @Override
            public void run() {

                // Actualiza la posición de la imagen
                for (Character c : personajes) {
                    c.seMueve();
                }

                //Calcular si colisionaron
                boolean colisionPrincipal = false;

                //cuenta los pajaros vivos
                int vivos = 0;
                for (Character c : personajes) {

                    //si hay uno colisionado, no verifica de nuevo colisión, porque me lo hacía avanzar.
                    if (c.colisionado) continue;

                    //No chequea colisión contra el principal.
                    if (c.name.equals(principal.name))
                        continue;
                    
                    if (principal.verificaColision(c)){
                        c.colisionado = true;
                        colisionPrincipal = true;
                    } else {
                        c.colisionado = false;
                        vivos++;
                    }
                }

                principal.colisionado = colisionPrincipal;

                // Repinta el canvas para mostrar la imagen en la nueva posición
                canvas.repaint();

                //si queda solo la jaula, corta el timer
                if (vivos == 1) {
                    timer.cancel();
                    terminado = true;
                }
            }
        };        
    }

    public MovableImageAWT(boolean buffer) {
        super("Imagen Móvil AWT");

        //carga el parámetro del buffer
        this.imageWithBuffer = buffer;

        //Crea los personajes del juego
        crearPersonajes();

        //setUndecorated(true); // Elimina los bordes y barra de título
        setBackground(Color.BLACK);      
        setExtendedState(Frame.MAXIMIZED_BOTH); // Maximiza la ventana.  
    
        add(canvas);
        setSize(1440, 875);
        setVisible(true);
 
        //comienza el timer del juego
        timer.scheduleAtFixedRate(comienzaJuego(canvas, timer), 0, 50);

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
        }           
        else if (pressedKeys.contains(KeyEvent.VK_R) && pressedKeys.size()==1) {
            resetJuego();
        } else if (pressedKeys.contains(KeyEvent.VK_Q) && pressedKeys.size()==1){
            timer.cancel();
            System.exit(0);
        }

        //System.out.println("x: "+c.centroX+" y: "+c.centroY);

    }

    private void resetJuego() {
        terminado=false;
        personajes.clear();
        crearPersonajes();

        //comienza el timer del juego
        timer = new Timer();
        timer.scheduleAtFixedRate(comienzaJuego(canvas, timer), 0, 50);

    }

    class MyCanvas extends Canvas {

        @Override        
        public void paint(Graphics g) {

            BufferStrategy bs=null;

            if (imageWithBuffer){
                // Asegurarse de que exista una BufferStrategy
                if (getBufferStrategy() == null) {
                    createBufferStrategy(2); // Crea una estrategia de doble buffer
                    return; // Salir del método para permitir que la estrategia se cree correctamente
                }

                bs = getBufferStrategy();
                g = bs.getDrawGraphics();

                g.setColor(Color.BLACK);            
                g.fillRect(0, 0, getWidth(), getHeight()); // Dibuja un fondo
            } 

            //Dibuja los personajes
            for (Character c : personajes){
                if (c.img!=null){
                    c.drawImage(this, g);
                }
            }

            // Define la fuente del texto
            g.setFont(new Font("SansSerif", Font.BOLD, 20));

            String text;
            if (principal.colisionado){
                // Define el color del texto
                g.setColor(Color.RED);
                // Dibuja el texto en el Canvas
                text = "COME!!";

            } else {
                // Define el color del texto
                g.setColor(Color.WHITE);
                // Dibuja el texto en el Canvas
                text = "CAZANDO...";
            }
            
            g.drawString(text, 50, 50);

            if (terminado){
                // Define la fuente del texto
                g.setFont(new Font("SansSerif", Font.BOLD, 100));
                g.setColor(Color.YELLOW);
                text = "GANO!!";
                g.drawString(text, 600, 300);
            }

            if (imageWithBuffer){
                g.dispose(); // Liberar los recursos del Graphics
                bs.show(); // Mostrar el contenido del buffer    
            }

        }
    }

    public static void main(String[] args) {
        
        //tengo que poner esto porque sino me tomaba el doble de pixels en la pantalla
        System.setProperty("sun.java2d.uiScale", "1");

        if (args.length>0 && "-buffer".equals(args[0])){
            System.out.println("No usando buffer de imágenes");
            new MovableImageAWT(false);
        } else {
            new MovableImageAWT(true);
        }
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
    public int velocidadX = 1;
    public int velocidadY = 1;
    public int rotaAngulo = 0;
    private int angulo = 0;
    public boolean colisiona = true;

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
    
        //Cambia la imagen si está colisionado
        Image imgTemp=null;
        if (colisionado){
            //pongo este if porque sino aparece un cuadrado negro porque no carga
            //la imagen de colisión si no la tiene.
            if (img_colision!=null)
                imgTemp = img_colision;
            else
                imgTemp = img;

            rotaAngulo = 5;
        } else {
            imgTemp = img;
            rotaAngulo = 0;
        }

        //escala la imagen
        int newWidth = img.getWidth(canvas) / scale;
        int newHeight = img.getHeight(canvas) / scale;

        //calcula el centro y el radio
        centroX = x+newWidth/2;
        centroY = y+newHeight/2;
        //deja el radio más grande
        radio = (newWidth>newHeight)?newWidth/2:newHeight/2;    

        Graphics2D g2d = (Graphics2D) g;

        g2d = (Graphics2D) g2d.create();

        
        // Aplicar anti-aliasing para suavizar las líneas y el texto
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Mejorar la calidad del renderizado
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Mejorar la calidad del texto
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        
        AffineTransform tx = new AffineTransform();
        tx.translate(centroX, centroY); // Traslación
        tx.rotate(Math.toRadians(angulo)); // Rotación
        
        //escalo la imagen (no hace falta si uso el newWidh como escala)
        //double dScale = (double)1/(double)scale;
        //tx.scale(3, 3); // Escalado        
        
        g2d.setTransform(tx);    

        // Dibuja la imagen en la nueva posición
        //empieza en la posición (-) with / 2 porque el centro de coordenadas con el que empieza
        //a dibujar es 0,0, entonces me rotaba desde una punta
        g2d.drawImage(imgTemp, -(newWidth/2), -(newHeight/2), newWidth, newHeight, canvas); 
        //g2d.drawImage(imgTemp, 0, 0, canvas);
        g2d.dispose(); 
        angulo = angulo + rotaAngulo;

    };

    public void seMueve(){
        this.movimiento.apply(this);
    };

    //Verifica si hay una colisión entre los dos objetos
    public boolean verificaColision(Character c){

        //si no colisiona, como la jaula, sale así no lo rota.
        if (!c.colisiona) return false;
        
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
            return true;
        } else {
            return false;
        }

    }

}