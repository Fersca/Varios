import java.awt.AWTException;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
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
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Point;

//Para la captura de pantalla
import javax.imageio.ImageIO;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//Para la pantalla completa
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;


public class Zorrito {

    //Juego -- Contiene los parámetros del juego (vidas, timers, etc)
    Juego juego;
    Display display;

    public Zorrito(boolean buffer, int cantMalos, boolean centrar, boolean sinFondo) {

        //Crea los objetos del juego.
        this.juego = new Juego();
        
        //Setea la variable de fondo invicible
        this.juego.sinFondo = sinFondo;

        if (sinFondo){
            //captura la pantalla
            capturaPantalla();            
        }

        //Crea el display y lo setea al juego
        this.display = new Display(juego);
        
        //linkea el display al juego
        this.juego.setDisplay(this.display);
        
        //carga la cantidad de malos
        this.juego.cantidadMalos = cantMalos;
        
        //setea si el personaje se centra o no
        this.juego.centrar = centrar;
        
        //Crea los personajes del juego
        this.juego.crearPersonajes();

        //Seteo la funcion de terminado
        this.display.terminadoFunc = juego.terminadoFunc();
        
        //Comienza el juego
        this.juego.comenzar();

    }


    public static void main(String[] args) {
        
        //tengo que poner esto porque sino me tomaba el doble de pixels en la pantalla
        System.setProperty("sun.java2d.uiScale", "1");
        System.out.println("Inicia Zorrito 1.0");

        //Chequea si se pidió la ayuda
        if(args.length>0 && "-help".equals(args[0])) {

            String ayuda = """
            Zorrito 1.0
            -----------

            -help       : Muestra esta ayuda en pantalla
            -size       : indica la cantidad de pájaros. Ej: -size:25
            -no-centrar : No centra al personaje en la pantalla
            -sin-fondo  : El juego se da sobre la pantalla actual

            """;

            System.out.println(ayuda);
            System.exit(0);

        }

        //Verifica si se pidió sin buffer
        boolean conBuffer = true;
        //cantidad de pajaros
        int size =20;
        //no centra al personaje
        boolean centrar =true;
        //setea el efecto de fondo invicible
        boolean sinFondo =false;
                
        //recorre los parámetros
        for (String s : args) {

            //verifica si viene sin buffer
            if ("-no-centrar".equals(s)){
                centrar = false;
                System.out.println("No centra al personaje");
            }
           
            //verifica si viene con size
            if (s.contains("-size:")){
                String[] partes = s.split(":");
                size = Integer.parseInt(partes[1]);
                System.out.println("Enemigos: "+size);
            }

            //Verifica que se haya pedido sin fondo
            if ("-sin-fondo".equals(s)){
                sinFondo = true;
                System.out.println("Se pone fondo invicible");
            }
                                    
        }
                
        new Zorrito(conBuffer, size, centrar, sinFondo);

    }
    
    private void capturaPantalla(){
        
        // Crea el objeto Robot.
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            ex.printStackTrace();
            return;
        }

        // Determina el tamaño de la pantalla.
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

        // Captura la imagen de la pantalla.
        BufferedImage screenFullImage = robot.createScreenCapture(screenRect);

        try {
            // Guarda la captura de pantalla en un archivo.
            ImageIO.write(screenFullImage, "PNG", new File("screenshot.png"));
        } catch (IOException ex) {           
            ex.printStackTrace();
        }

    }
}

class Juego {
    
    //Cantidad de malos
    public int cantidadMalos;

    //Centrar personaje
    public boolean centrar;

    //Centrar personaje
    public boolean sinFondo;
    
    //Variable que indica si terminó el juego
    //0 - andando, 1 - Ganó, 2 - Cazado
    int terminado = 0;
    
    // Timer para mover la imagen cada N milisegundos
    Timer timer = new Timer();

    //link al objeto del display
    Display display;
    
    //tiempo entre cada iteración del juego
    private int delay = 50;

    //setea el objeto display
    public void setDisplay(Display d){
        this.display = d;
    }

    //comienza el juego
    public void comenzar(){
        //comienza el timer del juego
        timer.scheduleAtFixedRate(comienzaJuego(timer), 0, delay);

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

        //gira al personaje
        c.angulo = c.angulo + c.rotaAngulo;
        return null;
    };

    //Crea el movimiento de cazar
    Function<Character, Void> movimientoCazar =(Character c) -> { 

        //Si está colisionado me lo manda a la jaula pero lo descoliciona.
        if (c.colisionado){
            c.follow.cazado=true;
            return null;
        }

        //obtiene la posicion de la presa
        int presaX = c.follow.x;
        int presaY = c.follow.y;
        
        //obtiene la posicion del cazador
        int cazadorX = c.x;
        int cazadorY = c.y;
        
        if (presaX<cazadorX)
            c.x = c.x-c.velocidadX;
        else 
            c.x = c.x+c.velocidadX;

        if (presaY<cazadorY)
            c.y = c.y-c.velocidadY;
        else 
            c.y = c.y+c.velocidadY;
        
        //gira al personaje
        c.angulo = c.angulo + c.rotaAngulo;
        return null;
    };
    
    
    
    public Function<Void, Integer> terminadoFunc(){
        return (Void) -> {return this.terminado;};
    }
    
    public void crearPersonajes(){

        //crea los personajes
        personajes.addAll(creaListaDePersonajes());
     
        //trackea los personajes en el display
        this.display.trackeaPersonajes(this.personajes,this.display);

    }

    private Collection<Character> creaListaDePersonajes() {

        ArrayList<Character> personajesCreados = new ArrayList<Character>();

        //crea los personajes
        Character zorrito = new Character("Zorrito","zorro.png",20,movimientoNulo);
        zorrito.x = display.getWidth() / 2;
        zorrito.y = display.getHeight() / 2;
        
        zorrito.setImagenColision("zorro_muerto.png");

        //Crea la jaula
        Character jaula = new Character("Jaula","jaula.png",5,movimientoNulo);
        jaula.x = jaulaX;
        jaula.y = jaulaY;
        jaula.colisiona = false;

        //Crea el mapa
        Character bosque;
        if (sinFondo){
            bosque = new Character("Bosque","screenshot.png",1,movimientoNulo);            
        } else {
            bosque = new Character("Bosque","bosque.png",1,movimientoNulo);            
        }        
        
        bosque.x = 0;
        bosque.y = 0;
        bosque.drawFromCenter=false;
        bosque.fixedSize=true;
        bosque.fixed_witdh=display.getWidth();
        bosque.fixed_heigth=display.getHeight();
        bosque.colisiona = false;

        //crea el aguila, la pone arriba a la derecha
        Character aguila = new Character("Aguila","aguila.png",5,movimientoCazar);
        aguila.x = display.getWidth();
        aguila.y = 0;
        aguila.velocidadX = 2;
        aguila.velocidadY = 2;
        
        aguila.follow = zorrito;
        
        
        //agrega los personajes a la lista
        personajesCreados.add(bosque);
        personajesCreados.add(zorrito);
        personajesCreados.add(aguila);

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
            Character pajaro = new Character("Pajaro"+i,"pajaro.png",20,movimientoRebote);
            pajaro.velocidadX = random.nextInt(20) + 1; 
            //System.out.println(pajaro.velocidadX);
            pajaro.velocidadY = random.nextInt(20) + 1; 
            //System.out.println(pajaro.velocidadY);
            pajaro.avanzando_y = movimientosArriba_Abajo[random.nextInt(2)];
            //System.out.println(pajaro.avanzando_y);
            pajaro.avanzando_x = movimientosIzquierda_Derecha[random.nextInt(2)];
            //System.out.println(pajaro.avanzando_x);            
            
            //los pone en la punta de la pantalla
            pajaro.x = display.getWidth();
            pajaro.y = display.getHeight();
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

                //detecta la posicion del mouse para moverlo;
                PointerInfo pi = MouseInfo.getPointerInfo();
                Point p = pi.getLocation();
                mueveSegunMouse(p.x,p.y);
                
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
                        c.setColision(true);
                        colisionPrincipal = true;
                    } else {
                        c.setColision(false);
                        
                        //al agrear este if me aseguro que es un pajaro
                        if (c.follow==null)
                            vivos++;
                    }
                }


                //Setea al principal como colisionado
                principal.setColision(colisionPrincipal);
                
                if (principal.cazado) {
                    timer.cancel();
                    terminado = 2;
                } else if (vivos == 2) {
                    //si queda solo la jaula, corta el timer
                    timer.cancel();
                    terminado = 1;
                }
                                               
                //manda a dibujar con buffer
                display.bufferedDraw();
            }
        };        
    }

    private void mueveSegunMouse(int x, int y){                        
                
        if ( (y<(display.getHeight()/7)*3) && (x<(display.getWidth()/7)*3) ) {
            mueveArribaIzquierda();
            return;
        }
        if ( (y<(display.getHeight()/7)*3) && (x>(display.getWidth()/7)*4) ) {
            mueveArribaDerecha();
            return;
        }
        if ( (y>(display.getHeight()/7)*4) && (x<(display.getWidth()/7)*3) ) {
            mueveAbajoIzquierda();
            return;
        }
        if ( (y>(display.getHeight()/7)*4) && (x>(display.getWidth()/7)*4) ) {
            mueveAbajoDerecha();
            return;
        }                
        if (y<(display.getHeight()/7)*3){
            mueveArriba();
            return;
        }
        if (y>(display.getHeight()/7)*4){
            mueveAbajo();
            return;
        }
        if (x<(display.getWidth()/7)*3){
            mueveIzquierda();
            return;
        }
        if (x>(display.getWidth()/7)*4){
            mueveDerecha();
            return;
        }
                
    }
    private void mueveArribaIzquierda(){
        if (centrar){
            general_x  += 4;
            general_y  += 4;                
        }

        principal.x -= 4;
        principal.y -= 4;        
    }
    private void mueveAbajoDerecha(){    
        if (centrar){
            general_x  -= 4;
            general_y  -= 4;
        }

        principal.x += 4;
        principal.y += 4;        
    }
    private void mueveArribaDerecha(){    
        if (centrar){
            general_x  -= 4;
            general_y  += 4;
        }

        principal.x += 4;
        principal.y -= 4;        
    }
    private void mueveAbajoIzquierda(){   
        if (centrar){            
            general_x  += 4;
            general_y  -= 4;
        }

        principal.x -= 4;
        principal.y += 4;        
    }
    private void mueveIzquierda(){   
        if (centrar){            
            general_x  += 6;
        }

        principal.x -= 6;        
    }
    private void mueveDerecha(){   
        if (centrar){
            general_x  -= 6;
        }

        principal.x += 6;        
    }
    private void mueveArriba(){   
        if (centrar){            
            general_y  += 6;
        }

        principal.y -= 6;        
    }
    private void mueveAbajo(){   
        if (centrar){
            general_y  -= 6;
        }

        principal.y += 6;        
    }
    
    //Ejecuta una acción en base a la tecla que se haya presionado
    public void acciónDeTeclaPresionada() {
        
        // Verifica combinaciones específicas de teclas
        if (pressedKeys.contains(KeyEvent.VK_J) && pressedKeys.contains(KeyEvent.VK_I)) {
            mueveArribaIzquierda();
        } else if (pressedKeys.contains(KeyEvent.VK_L) && pressedKeys.contains(KeyEvent.VK_K)) {
            mueveAbajoDerecha();
        }
        else if (pressedKeys.contains(KeyEvent.VK_L) && pressedKeys.contains(KeyEvent.VK_I)) {
            mueveArribaDerecha();
        } 
        else if (pressedKeys.contains(KeyEvent.VK_J) && pressedKeys.contains(KeyEvent.VK_K)) {
            mueveAbajoIzquierda();
        }               
        else if (pressedKeys.contains(KeyEvent.VK_J) && pressedKeys.size()==1) {
            mueveIzquierda();
        }           
        else if (pressedKeys.contains(KeyEvent.VK_L) && pressedKeys.size()==1) {
            mueveDerecha();
        }           
        else if (pressedKeys.contains(KeyEvent.VK_I) && pressedKeys.size()==1) {
            mueveArriba();
        }           
        else if (pressedKeys.contains(KeyEvent.VK_K) && pressedKeys.size()==1) {
            mueveAbajo();
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
        terminado=0;
        personajes.clear();
        crearPersonajes();

        //Cancela el timer y comienza de nuevo otro.
        timer.cancel();
        timer = new Timer();
        timer.scheduleAtFixedRate(comienzaJuego(timer), 0, delay);

    }

}

class Display extends Frame {

    //Canvas para dibujar el juego
    private final MyCanvas canvas;

    private final Juego juego;
    
    public Function<Void, Integer> terminadoFunc;
    
    public void bufferedDraw(){
        this.canvas.draw();
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

    public Display(Juego juego){
        super("Zorrito 1.0");

        if (juego.sinFondo){

            // Obtiene el entorno gráfico y el dispositivo gráfico predeterminados.
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice device = env.getDefaultScreenDevice();

            // Intenta establecer el modo de pantalla completa.
            if (device.isFullScreenSupported()) {
                // Oculta la barra de título y otros elementos de la ventana.
                //setUndecorated(true);

                // Activa el modo de pantalla completa.
                device.setFullScreenWindow(this);
            }         
                    
        }
        
        //crea el canvas
        this.canvas = new MyCanvas(this);
        
        setBackground(Color.BLACK);      
        setExtendedState(Frame.MAXIMIZED_BOTH); // Maximiza la ventana.         
        //setSize(1440, 875);        
        add(this.canvas);        
        setVisible(true);

        //setea el ícono
        setIconImage(Toolkit.getDefaultToolkit().getImage("zorro.png"));
        
        //Espera medio segundo a que se maximize la pantalla.
        //para que los getWidth y los getHeigth tomen el valor correcto.
        //Solo si no usa fondo invicible, sino no hace falta        
        if (!juego.sinFondo){
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }            
        }
                        
        this.juego = juego;        
                               
        //Listener para las teclas
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                juego.pressedKeys.add(e.getKeyCode()); // Añade la tecla presionada al conjunto
                juego.acciónDeTeclaPresionada();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                juego.pressedKeys.remove(e.getKeyCode()); // Elimina la tecla liberada del conjunto
            }
        });
        
        //Listener para cerrar la ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                juego.timer.cancel(); // Asegúrate de cancelar el timer cuando cierres la ventana
                System.exit(0);
            }
        });

    }

    private class MyCanvas extends Canvas {

        public MyCanvas(Display d) {
            this.rootDisplay = d;
        }

        Display rootDisplay;
        
        private void drawImageCanvas(boolean drawFromCenter, Image imgTemp, int centroX, int centroY, int angulo, int newWidth, int newHeight, Graphics2D g2d, double zoom, int general_x, int general_y){


            //Crea el objeto para aplicar los efectos
            AffineTransform tx = new AffineTransform();

            //Translada la imagen a la posición del mapa
            tx.translate((general_x+centroX)*zoom, (general_y+centroY)*zoom); // Traslación
            
            //Rota la imagen
            tx.rotate(Math.toRadians(angulo));

            //Aplica el zoom
            tx.scale(zoom, zoom); // Escalado        

            //Aplica la transformación
            g2d.setTransform(tx);    

            // Dibuja la imagen en la nueva posición
            //empieza en la posición (-) with / 2 porque el centro de coordenadas con el que empieza
            //a dibujar es el 0,0 entonces no lo dibujaba centrado, y cuando rotaba lo hacías desde la
            //punta, la variable drawFromCenter es para indicar eso.
            g2d.drawImage(imgTemp, drawFromCenter?-(newWidth/2):0, drawFromCenter?-(newHeight/2):0, newWidth, newHeight, this);
            
        };
                
        private void drawElementosComunes(Graphics2D g){

            //Dibuja los personajes
            for (Character c : this.rootDisplay.juego.personajes){
                if (c.img!=null){      
                                        
                    //Repinta el personaje
                    drawImageCanvas(c.drawFromCenter, c.getImagen(), c.centroX, c.centroY, c.angulo, c.getWidth(canvas), c.getHeight(canvas), g, this.rootDisplay.juego.zoom, this.rootDisplay.juego.general_x, this.rootDisplay.juego.general_y);
                    
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

            //Chequea si el juego está termiando mediante la función enviada. (desacoplata)            
            int codigoTerminado = terminadoFunc.apply(null);
            if (codigoTerminado==2) {
                // Define la fuente del texto
                g.setFont(new Font("SansSerif", Font.BOLD, 100));
                g.setColor(Color.RED);
                text = "¡¡CAZADO!!";
                g.drawString(text, 600, 300);
            }            
            if (codigoTerminado==1) {
                // Define la fuente del texto
                g.setFont(new Font("SansSerif", Font.BOLD, 100));
                g.setColor(Color.YELLOW);
                text = "¡¡GANO!!";
                g.drawString(text, 600, 300);
            } 

            
        }
        public void draw() {
                                    
            // Obtén la BufferStrategy actual del Canvas
            BufferStrategy bs = getBufferStrategy();
            if (bs == null) {
                createBufferStrategy(2); // Crea una estrategia de doble buffer
                return;
            }

            // Obtén el objeto Graphics para dibujar en el búfer
            Graphics2D g = (Graphics2D) bs.getDrawGraphics();

            // Aplicar anti-aliasing para suavizar las líneas y el texto
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Mejorar la calidad del renderizado
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            // Mejorar la calidad del texto
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

            g.setColor(Color.BLACK);            
            g.fillRect(0, 0, getWidth(), getHeight()); // Dibuja un fondo
            
            //Muestra los elementos comunes (que van con buffer o no)
            drawElementosComunes(g);
            
            g.dispose(); // Liberar los recursos del Graphics
            bs.show(); // Mostrar el contenido del buffer    
            
            // Asegúrate de que la operación de dibujo se completa
            Toolkit.getDefaultToolkit().sync();
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
    public int x = 500;
    public int y = 500;
    public int fixed_witdh;
    public int fixed_heigth;
    public int width = 0;
    public int height = 0;    
    public boolean fixedSize= false;
    public boolean drawFromCenter=true;
    private int scale;
    public Function<Character, Void> movimiento;
    public String name;
    public int centroX;
    public int centroY;
    public int radio=0;
    public boolean colisionado;
    public String avanzando_x = "derecha";
    public String avanzando_y = "abajo";
    public int velocidadX = 1;
    public int velocidadY = 1;
    public int rotaAngulo = 0;
    public int angulo = 0;
    public boolean colisiona = true;
    public int numImagen = 0;
    public boolean cazado = false;
    
    //setea otro caracter para que lo siga con el movimiento
    public Character follow;
    
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
    
    public void setColision(boolean colision){
        this.colisionado = colision;
        
        //Si está colisionado lo hace girar
        if (colision){
            this.rotaAngulo = 5;
        } else {
            this.rotaAngulo = 0;
        }       
    }
    
    public int getWidth(Canvas canvas){
        
        if (fixedSize){
            return fixed_witdh;
        } else {
            this.width = img.getWidth(canvas) / scale;            
            return this.width;
        }        
    }
    
    public int getHeight(Canvas canvas){
        if (fixedSize){
            return fixed_heigth;
        } else {
            this.height = img.getHeight(canvas) / scale;
            return this.height;
        }        
    }

    public void setImagenColision(String imageFileColision){
        if (imageFileColision!=null)
            this.img_colision = Toolkit.getDefaultToolkit().getImage(imageFileColision);
    }
    
    public Image getImagen(){
        
        //Cambia la imagen si está colisionado
        Image imgTemp;
        if (colisionado){
            //pongo este if porque sino aparece un cuadrado negro porque no carga
            //la imagen de colisión si no la tiene.
            if (img_colision!=null)
                imgTemp = img_colision;
            else
                imgTemp = img;
        } else {
            imgTemp = img;
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

        return imgTemp;
    }
    
    //Ejecuta la función de movimiento sobre el personaje
    public void seMueve(){
        
        //Aplica el algoritmo de movimiento
        this.movimiento.apply(this);
        
        //Para todos calcula el centro y el radio TODO: Sacar esto de acá
        //La primera ver que aun no se calcularon los valores, no funcione, pero la siguiente si.
        
        //TODO: Fix, sacar esto
        if (name.equals("Bosque")){
            return;
        }
        
        centroX = x+width/2;
        centroY = y+height/2;
        
        //deja el radio más grande si es la primera ver que se calcula
        if (radio==0)
            radio = (width>height)?width/2:height/2;    
        
        if (this.colisionado){
            this.angulo = angulo + rotaAngulo;
        }
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