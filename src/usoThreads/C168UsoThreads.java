package usoThreads;
//1 crear clase que implemente la interface Runnable
//2 escribir el codigo de la tarea dentro del run()
//3 instanciar la clase nueva y almacenarla en un objeto del tipo runnable
//4 instanciar la clase Threads  pasando como parametro el obj runnable
//5 poner en marcha el hilo con metodo start

//clase 169: detener un hilo de ejecucion

	import java.awt.geom.*;

	import javax.swing.*;

	import java.util.*;
	import java.awt.*;
	import java.awt.event.*;

	//2 laminas: superior con una pelota que rebota cuando se encuentra en los limites del marco
	//inferior con 2 button
	public class C168UsoThreads {

		//marco
		public static void main(String[] args) {
			// TODO Auto-generated method stub
			
			JFrame marco=new MarcoRebote();
			
			marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			marco.setVisible(true);

		}

		Thread t;

	}


	//1 crear clase que implemente la interface Runnable...............................................................
	class PelotaHilos implements Runnable{
		
		private Pelota pelota;
		private Component componente;

		public PelotaHilos(Pelota unaPelota,Component unComponente) {
			pelota=unaPelota;
			componente=unComponente;
		}
		
		//2 escribir el codigo de la tarea dentro del run()
		@Override
		public void run() {
			for (int i=1; i<=3000; i++){
				
				pelota.mueve_pelota(componente.getBounds());
				
				componente.paint(componente.getGraphics());
				
				//metodo sleep de la clase Thread para pausar la ejecucion 
				//lanza una excepcion, mientras esta en ejecucion no se puede hacer nada, ni siquiera solicitar su interrupcion
				//si la solicito alli lanza una exception: InterruptedException
				//para que me detenga el hilo tengo que cambiar lo que hay en el catch
				try {
					
					Thread.sleep(4);
					
				} catch (InterruptedException e) {
					
					System.out.println("hilo bloqueado imposible su interrupcion");
				}
				
			}
			
		}
		
	}


	//Movimiento de la pelota-----------------------------------------------------------------------------------------

	
	class Pelota{
		
		// Mueve la pelota invirtiendo posición si choca con límites
		//recibe las dimensiones de la lamina
		public void mueve_pelota(Rectangle2D limites){
			
			//incremento x y para que la pelota se vaya moviendo
			x+=dx;
			
			y+=dy;
			
			//getMinX: detecto punto maximo y minimo, cuando los encuentro invierto la coordenada x o y
			if(x<limites.getMinX()){
				
				x=limites.getMinX();
				
				dx=-dx;
			}
			
			if(x + TAMX>=limites.getMaxX()){
				
				x=limites.getMaxX() - TAMX;
				
				dx=-dx;
			}
			
			if(y<limites.getMinY()){
				
				y=limites.getMinY();
				
				dy=-dy;
			}
			
			if(y + TAMY>=limites.getMaxY()){
				
				y=limites.getMaxY()-TAMY;
				
				dy=-dy;
				
			}
			
		}
		
		//Forma de la pelota en su posición inicial
		
		public Ellipse2D getShape(){
			
			return new Ellipse2D.Double(x,y,TAMX,TAMY);
			
		}	
		
		private static final int TAMX=15;
		
		private static final int TAMY=15;
		
		private double x=0;
		
		private double y=0;
		
		private double dx=1;
		
		private double dy=1;
		
		
	}

	// Lámina que dibuja las pelotas----------------------------------------------------------------------


	class LaminaPelota extends JPanel{
		
		//Añadimos pelota a la lámina
		
		public void add(Pelota b){
			
			pelotas.add(b);
		}
		//pinto las pelotas
		public void paintComponent(Graphics g){
			
			super.paintComponent(g);
			
			Graphics2D g2=(Graphics2D)g;
			
			for(Pelota b: pelotas){
				
				g2.fill(b.getShape());
			}
			
		}
		
		private ArrayList<Pelota> pelotas=new ArrayList<Pelota>();
	}


	//Marco con lámina y botones------------------------------------------------------------------------------

	class MarcoRebote extends JFrame{
		
		public MarcoRebote(){
			
			setBounds(600,300,400,350);
			
			setTitle ("Rebotes");
			
			//agrego la lamina de la pelota en el centro
			lamina=new LaminaPelota();
			
			add(lamina, BorderLayout.CENTER);
			
			
			//construyo la lamina de los botones que va  en la parte inferior
			JPanel laminaBotones=new JPanel();
			
			
			//pongo los botones a la escucha para que se ejecute la funcion comienza el juego
			ponerBoton(laminaBotones, "Dale!", new ActionListener(){
				
				public void actionPerformed(ActionEvent evento){
					
					comienza_el_juego();
				}
				
			});
			
			//metodo encargado de dibujar los botones
			
			ponerBoton(laminaBotones, "Salir", new ActionListener(){
				
				public void actionPerformed(ActionEvent evento){
					
					System.exit(0);
					
				}
				
			});
			
			//dibuja el boton de detener hilo........................................................................
			//cuando pulse el boton se llamara al metodo detener
			ponerBoton(laminaBotones, "Detener", new ActionListener(){
				
				public void actionPerformed(ActionEvent evento){
					
					detener();
					
				}
				
			});
			
			add(laminaBotones, BorderLayout.SOUTH);
		}
		
		
		//Ponemos botones
		
		public void ponerBoton(Container c, String titulo, ActionListener oyente){
			
			JButton boton=new JButton(titulo);
			
			c.add(boton);
			
			boton.addActionListener(oyente);
			
		}
		
		//Añade pelota y la bota 3000 veces
		//la pelota va a tener 3000 movimientos
		
		public void comienza_el_juego (){
			
						
				Pelota pelota=new Pelota();
				
				lamina.add(pelota);
				
				//3 instanciar la clase nueva y almacenarla en un objeto del tipo runnable
			
				Runnable r= new PelotaHilos(pelota, lamina);
				
				//4 instanciar la clase Threads  pasando como parametro el obj runnable
				//creo una tarea con esas instrucciones anteriores
				
				t= new Thread(r);
				
				//5 poner en marcha el hilo con metodo start
				
				t.start();
			
		}
		
		
		//metodo detener, detiene el hilo de ejecucion
		
		public void detener() {
			
			//metodo obsoleto
			//t.stop();
			
			//solicitud de dentencion del hilo
			t.interrupt();
			
		}
		
		private LaminaPelota lamina;
		Thread t;
		
	}


