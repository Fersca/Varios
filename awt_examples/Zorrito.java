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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.RenderingHints;

public class Zorrito {

    //Juego -- Contiene los parámetros del juego (vidas, timers, etc)
    Juego juego;
    Display display;

    public Zorrito(boolean buffer, int cantMalos) {

        //Crea los objetos del juego.
        this.juego = new Juego();

        //Crea el display y lo setea al juego
        this.display = new Display(buffer,juego);
        this.juego.setDisplay(this.display);

        //carga la cantidad de malos
        this.juego.cantidadMalos = cantMalos;
        
        //Crea los personajes del juego
        this.juego.crearPersonajes();

        //Comienza el juego
        this.juego.comenzar();

    }


    public static void main(String[] args) {
        
        //tengo que poner esto porque sino me tomaba el doble de pixels en la pantalla
        System.setProperty("sun.java2d.uiScale", "1");

        //Chequea si se pidió la ayuda
        if(args.length>0 && "-help".equals(args[0])) {

            String ayuda = """
            Zorrito 1.0
            -----------

            -help   : Muestra esta ayuda en pantalla
            -buffer : No usa el buffer de imágenes
            -size   : indica la cantidad de pájaros. Ej: -size:25

            """;

            System.out.println(ayuda);
            System.exit(0);

        }

        //Verifica si se pidió sin buffer
        boolean conBuffer = true;
        //cantidad de pajaros
        int size =20;

        //recorre los parámetros
        for (String s : args) {

            //verifica si viene sin buffer
            if ("-buffer".equals(s)){
                conBuffer = false;
                System.out.println("No usa el buffer de imágenes");
            }

            //verifica si viene con size
            if (s.contains("-size:")){
                String[] partes = s.split(":");
                size = Integer.parseInt(partes[1]);
            }
                
            
        }
                
        new Zorrito(conBuffer, size);

    }
}

class Juego {
    //Cantidad de malos
    public int cantidadMalos;
    
    //Variable que indica si terminó el juego
    boolean terminado = false;

    // Timer para mover la imagen cada N milisegundos
    Timer timer = new Timer();

    Display display;

    public void setDisplay(Display d){
        this.display = d;
    }

    public void comenzar(){
        //comienza el timer del juego
        timer.scheduleAtFixedRate(comienzaJuego(timer), 0, 50);

    }

    //Lista de teclas presionadas
    public Set<Integer> pressedKeys = new HashSet<>();
        
    //Nivel de Zoom
    double zoom=1;

    //Tiempo actual para el cronómetro
    long initTimeMillis;

    //Lista de personajes
    public ArrayList<Character> personajes = new ArrayList<Character>();
    
    //Personaje principal
    public Character principal;
    
    //Centro general del mapa
    int general_x=0;
    int general_y=0;

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
        int ancho = display.getWidth();
        int alto = display.getHeight();

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
     
        //trackea los personajes en el display
        this.display.trackeaPersonajes(this.personajes,this.display);

    }

    private Collection<Character> creaListaDePersonajes() {

        ArrayList<Character> personajesCreados = new ArrayList<Character>();

        //crea los personajes
        Character zorrito = new Character("Zorrito","/Users/Fernando.Scasserra/Downloads/zorro.png",15,movimientoNulo);
        zorrito.setImagenColision("/Users/Fernando.Scasserra/Downloads/zorro_muerto.png");

        //Crea la jaula
        Character jaula = new Character("Jaula","/Users/Fernando.Scasserra/Downloads/jaula.png",5,movimientoNulo);
        jaula.x = jaulaX;
        jaula.y = jaulaY;
        jaula.colisiona = false;

        //Crea el mapa
        Character bosque = new Character("Jaula","/Users/Fernando.Scasserra/Downloads/bosque.png",1,movimientoNulo);
        bosque.x = 0;
        bosque.y = 0;
        bosque.fixedSize=true;
        bosque.fixed_witdh=1440;
        bosque.fixed_heigth=875;
        bosque.colisiona = false;

        //agrega los personajes a la lista
        personajesCreados.add(bosque);
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

        for (int i=0;i<this.cantidadMalos;i++){

            //pajaro estandar
            Character pajaro = new Character("Pajaro"+i,"/Users/Fernando.Scasserra/Downloads/pajaro.png",15,movimientoRebote);
            pajaro.velocidadX = random.nextInt(20) + 1; 
            //System.out.println(pajaro.velocidadX);
            pajaro.velocidadY = random.nextInt(20) + 1; 
            //System.out.println(pajaro.velocidadY);
            pajaro.avanzando_y = movimientosArriba_Abajo[random.nextInt(2)];
            //System.out.println(pajaro.avanzando_y);
            pajaro.avanzando_x = movimientosIzquierda_Derecha[random.nextInt(2)];
            //System.out.println(pajaro.avanzando_x);            
            
            //los pone en la punta de la pantalla
            pajaro.x = 1440;
            pajaro.y = 875;
            enemigos.add(pajaro);
        }

        return enemigos;

    }

    private TimerTask comienzaJuego(Timer timer){

        //obtiene el tiempo actual.
        initTimeMillis = System.currentTimeMillis();
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

                // Le avisa al display que hay que repintar
                display.doRepaint();

                //si queda solo la jaula, corta el timer
                if (vivos == 2) {
                    timer.cancel();
                    terminado = true;
                }
            }
        };        
    }

    //Ejecuta una acción en base a la tecla que se haya presionado
    public void acciónDeTeclaPresionada() {
        
        // Verifica combinaciones específicas de teclas
        if (pressedKeys.contains(KeyEvent.VK_J) && pressedKeys.contains(KeyEvent.VK_I)) {
            principal.x -= 7;
            principal.y -= 7;
        } else if (pressedKeys.contains(KeyEvent.VK_L) && pressedKeys.contains(KeyEvent.VK_K)) {
            principal.x += 7;
            principal.y += 7;
        }
        else if (pressedKeys.contains(KeyEvent.VK_L) && pressedKeys.contains(KeyEvent.VK_I)) {
            principal.x += 7;
            principal.y -= 7;
        } 
        else if (pressedKeys.contains(KeyEvent.VK_J) && pressedKeys.contains(KeyEvent.VK_K)) {
            principal.x -= 7;
            principal.y += 7;
        }               
        else if (pressedKeys.contains(KeyEvent.VK_J) && pressedKeys.size()==1) {
            principal.x -= 10;
        }           
        else if (pressedKeys.contains(KeyEvent.VK_L) && pressedKeys.size()==1) {
            principal.x += 10;
        }           
        else if (pressedKeys.contains(KeyEvent.VK_I) && pressedKeys.size()==1) {
            principal.y -= 10;
        }           
        else if (pressedKeys.contains(KeyEvent.VK_K) && pressedKeys.size()==1) {
            principal.y += 10;
        }           
        else if (pressedKeys.contains(KeyEvent.VK_Z) && pressedKeys.size()==1) {
            this.zoom = this.zoom+0.1;
        }           
        else if (pressedKeys.contains(KeyEvent.VK_X) && pressedKeys.size()==1) {
            this.zoom = this.zoom-0.1;
        }           
        else if (pressedKeys.contains(KeyEvent.VK_V) && pressedKeys.size()==1) {
            this.general_x=this.general_x+10;
        }           
        else if (pressedKeys.contains(KeyEvent.VK_C) && pressedKeys.size()==1) {
            this.general_x=this.general_x-10;
        }           
        else if (pressedKeys.contains(KeyEvent.VK_F) && pressedKeys.size()==1) {
            this.general_y=this.general_y+10;
        }           
        else if (pressedKeys.contains(KeyEvent.VK_R) && pressedKeys.size()==1) {
            this.general_y=this.general_y-10;
        }           
        else if (pressedKeys.contains(KeyEvent.VK_E) && pressedKeys.size()==1) {
            resetJuego();
        } else if (pressedKeys.contains(KeyEvent.VK_Q) && pressedKeys.size()==1){
            timer.cancel();
            System.exit(0);
        }
    }

    private void resetJuego() {
        terminado=false;
        personajes.clear();
        crearPersonajes();

        //Cancela el timer y comienza de nuevo otro.
        timer.cancel();
        timer = new Timer();
        timer.scheduleAtFixedRate(comienzaJuego(timer), 0, 50);

    }

}

class Display extends Frame {

    //Canvas para dibujar el juego
    MyCanvas canvas = new MyCanvas(this);

    public boolean imageWithBuffer = true;

    public Juego juego;
    
    public void doRepaint(){
        this.canvas.repaint();
    }

    public void trackeaPersonajes(ArrayList<Character> personajes, Frame f){

        //crea el tracker de imágenes
        MediaTracker tracker = new MediaTracker(f);
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

    public Display(boolean imageWithBuffer, Juego juego){
        super("Zorrito 1.0");
        //setUndecorated(true); // Elimina los bordes y barra de título
        setBackground(Color.BLACK);      
        setExtendedState(Frame.MAXIMIZED_BOTH); // Maximiza la ventana.  
    
        add(this.canvas);
        setSize(1440, 875);
        setVisible(true);

        this.imageWithBuffer = imageWithBuffer;
        this.juego = juego;

        //Listener para las teclas
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                juego.pressedKeys.add(e.getKeyCode()); // Añade la tecla presionada al conjunto
                juego.acciónDeTeclaPresionada();
                canvas.repaint();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                juego.pressedKeys.remove(e.getKeyCode()); // Elimina la tecla liberada del conjunto
            }
        });
        
        //Listener para cerrar la ventana
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                juego.timer.cancel(); // Asegúrate de cancelar el timer cuando cierres la ventana
                System.exit(0);
            }
        });

    }

    class MyCanvas extends Canvas {

        public MyCanvas(Display d) {
            this.rootDisplay = d;
        }

        Display rootDisplay;
        
        @Override        
        public void paint(Graphics gOrigin) {

            BufferStrategy bs=null;
            Graphics2D g;

            if (imageWithBuffer){
                // Asegurarse de que exista una BufferStrategy
                if (getBufferStrategy() == null) {
                    createBufferStrategy(2); // Crea una estrategia de doble buffer
                    return; // Salir del método para permitir que la estrategia se cree correctamente
                }

                bs = getBufferStrategy();
                gOrigin = bs.getDrawGraphics();

                g = (Graphics2D) gOrigin;

                //TODO: Lo saqué y no pasó nada, hay que ver para que sirve
                //g = (Graphics2D) g.create();
                
                // Aplicar anti-aliasing para suavizar las líneas y el texto
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
                // Mejorar la calidad del renderizado
                g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
                // Mejorar la calidad del texto
                g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
                            
                g.setColor(Color.BLACK);            
                g.fillRect(0, 0, getWidth(), getHeight()); // Dibuja un fondo
                
            } else {
                g = (Graphics2D) gOrigin;                
            }

            //Dibuja los personajes
            for (Character c : this.rootDisplay.juego.personajes){
                if (c.img!=null){      
                    
                    //TODO: Crear un método que esté dentro del Frame y se encarga de dibujar el objeto en cuestion
                    //Para eso hay que obtener algunos datos del objeto, son pocos.
                    //Hacer un refactor del método drawImage también del lado del Character, no hace falta ya que todo lo 
                    //se que calcula desde ahi para gráficos se puede poner acá y los otros datos se pueden calcular en 
                    //cuanto cambien como la posición, radio, etc
                    c.drawImage(this, g,this.rootDisplay.juego.zoom, this.rootDisplay.juego.general_x, this.rootDisplay.juego.general_y);
                }
            }

            //vuelve a centrar el centro el 0,0
            AffineTransform tx = new AffineTransform();        
            tx.translate(0, 0); // Traslación        
            g.setTransform(tx);    
        
            // Define la fuente del texto
            g.setFont(new Font("SansSerif", Font.BOLD, 20));

            String text;
            if (this.rootDisplay.juego.principal.colisionado){
                // Define el color del texto
                g.setColor(Color.RED);
                // Dibuja el texto en el Canvas
                text = "Status: COME";

            } else {
                // Define el color del texto
                g.setColor(Color.WHITE);
                // Dibuja el texto en el Canvas
                text = "Status: CAZANDO ";
            }
            
            //imprime el status
            g.drawString(text, 50, 50);

            //imprime el cronómetro
            g.setColor(Color.WHITE);
            g.drawString(obtenerCronometro(), 700, 50);           

            //imprime el zoom
            String printZoom = String.format("%.2f", this.rootDisplay.juego.zoom);
            g.drawString("Zoom: "+printZoom+"x", 900, 50);            

            //imprime el cartel de ganador
            if (this.rootDisplay.juego.terminado){
                // Define la fuente del texto
                g.setFont(new Font("SansSerif", Font.BOLD, 100));
                g.setColor(Color.YELLOW);
                text = "¡¡GANO!!";
                g.drawString(text, 600, 300);
            }

            //libera los recursos
            if (imageWithBuffer){
                g.dispose(); // Liberar los recursos del Graphics
                bs.show(); // Mostrar el contenido del buffer    
            }

        }

        private String obtenerCronometro() {
            long tiempoActual = System.currentTimeMillis();
            long diff = tiempoActual-this.rootDisplay.juego.initTimeMillis;
            return "Tiempo: "+diff/1000;
        }
    }

}

/**
 * Clase que guarda el personaje (objeto del juego)
 * para ser transportado entre el juego y el display
 * además de contener la info del personaje.
 */
class Character {
    public Image img;
    public Image img_colision;
    public int x = 100;
    public int y = 100;
    public int fixed_witdh;
    public int fixed_heigth;
    public boolean fixedSize= false;
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
    public int numImagen = 0;
    //caché de imágenes
    private static HashMap<String, Image> imagenes = new HashMap<String, Image>();

    public Character(String name, String imageFile, int scale, Function<Character, Void> movimientoPersonaje){

        //chequea en el caché de imágenes
        if (imagenes.containsKey(imageFile)){
            this.img = imagenes.get(imageFile);
        } else {
            this.img = Toolkit.getDefaultToolkit().getImage(imageFile);
            imagenes.put(imageFile, this.img);
        }

        //this.img = Toolkit.getDefaultToolkit().getImage(imageFile);
        this.scale = scale;
        this.movimiento = movimientoPersonaje;
        this.name = name;    
    };

    public void setImagenColision(String imageFileColision){
        if (imageFileColision!=null)
            this.img_colision = Toolkit.getDefaultToolkit().getImage(imageFileColision);
    }
    
    public void drawImage(Canvas canvas, Graphics2D g2d, double zoom, int general_x, int general_y){
    
        //TODO: Este método no está bien acá, justamente toda la parte gráfica tiene que estar en el display
        //el cual tome los parámetros del personaje para dibujarlo. La clase personaje tiene que estar desacoplada del display

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

        //va cambiando la imagen
        if (numImagen==0)
            imgTemp = img;
        else {
            if (img_colision!=null)
                imgTemp = img_colision;
            else
                imgTemp = img;
        }

        //escala la imagen
        int newWidth=0;
        int newHeight=0;

        if (fixedSize){
            newWidth = fixed_witdh;
            newHeight = fixed_heigth;
        } else {
            newWidth = img.getWidth(canvas) / scale;
            newHeight = img.getHeight(canvas) / scale;    
        }

        //calcula el centro y el radio
        centroX = x+newWidth/2;
        centroY = y+newHeight/2;
        //deja el radio más grande
        radio = (newWidth>newHeight)?newWidth/2:newHeight/2;    

        AffineTransform tx = new AffineTransform();
        
        //tx.translate(general_x+centroX, general_y+centroY); // Traslación
        tx.translate((general_x+centroX)*zoom, (general_y+centroY)*zoom); // Traslación
        tx.rotate(Math.toRadians(angulo)); // Rotación
        
        //escalo la imagen (no hace falta si uso el newWidh como escala)
        //double dScale = (double)1/(double)scale;
        tx.scale(zoom, zoom); // Escalado        
        
        g2d.setTransform(tx);    

        // Dibuja la imagen en la nueva posición
        //empieza en la posición (-) with / 2 porque el centro de coordenadas con el que empieza
        //a dibujar es 0,0, entonces me rotaba desde una punta
        //g2d.drawImage(imgTemp, (int)(-((double)newWidth/2)*zoom), (int)(-((double)newHeight/2)*zoom), (int)(((double)newWidth)*zoom), (int)(((double)newHeight)*zoom), canvas); 
        g2d.drawImage(imgTemp, -(newWidth/2), -(newHeight/2), newWidth, newHeight, canvas); 
        //g2d.drawImage(img, 300, 300, 700, 700, -(newWidth/2), -(newHeight/2), newWidth, newHeight, canvas);

        angulo = angulo + rotaAngulo;

    };

    //Ejecuta la función de movimiento sobre el personaje
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
        return distancia <(this.radio+c.radio);

    }

}