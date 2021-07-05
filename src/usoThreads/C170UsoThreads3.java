package usoThreads;
//conseguir detener los hilos de forma individual y a voluntad



	import java.awt.geom.*;

	import javax.swing.*;

	import java.util.*;
	import java.awt.*;
	import java.awt.event.*;

	//2 laminas: superior con una pelota que rebota cuando se encuentra en los limites del marco
	//inferior con 2 button
	public class C170UsoThreads3 {

		//marco
		public static void main(String[] args) {
			// TODO Auto-generated method stub
			
			JFrame marco=new MarcoRebote3();
			
			marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			marco.setVisible(true);

		}

		Thread t;

	}


	//1 crear clase que implemente la interface Runnable...............................................................
	class PelotaHilos3 implements Runnable{
		
		private Pelota3 pelota3;
		private Component componente3;

		public PelotaHilos3(Pelota3 unaPelota,Component unComponente) {
			pelota3=unaPelota;
			componente3=unComponente;
		}
		
		//2 escribir el codigo de la tarea dentro del run()
		@Override
		public void run() {
			
			
			while(!Thread.interrupted()){
				
				pelota3.mueve_pelota(componente3.getBounds());
				
				componente3.paint(componente3.getGraphics());
				
				//si pulso detener, lanza una excepcion, pero en el catch pongo que se interrumpa
				
				try {
					
					Thread.sleep(4);
					
				} catch (InterruptedException e) {
					
					
					Thread.currentThread().interrupt();
					
				}
				
			}
			
		}
		
	}


	//Movimiento de la pelota-----------------------------------------------------------------------------------------

	
	class Pelota3{
		
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


	class LaminaPelota3 extends JPanel{
		
		//Añadimos pelota a la lámina
		
		public void add(Pelota3 b){
			
			pelotas.add(b);
		}
		//pinto las pelotas
		public void paintComponent(Graphics g){
			
			super.paintComponent(g);
			
			Graphics2D g2=(Graphics2D)g;
			
			for(Pelota3 b: pelotas){
				
				g2.fill(b.getShape());
			}
			
		}
		
		private ArrayList<Pelota3> pelotas=new ArrayList<Pelota3>();
	}


	//Marco con lámina y botones------------------------------------------------------------------------------

	class MarcoRebote3 extends JFrame{
		
		public MarcoRebote3(){
			
			setBounds(600,300,400,350);
			
			setTitle ("Rebotes");
			
			//agrego la lamina de la pelota en el centro
			lamina=new LaminaPelota3();
			
			add(lamina, BorderLayout.CENTER);
			
			
			
			
			//construyo la lamina de los botones que va  en la parte inferior
			JPanel laminaBotones=new JPanel();
			
			//creo los botones y lo pongo a la escucha. los agrego
			//............................................................
			arranca1= new JButton("Hilo 1");
			
			arranca1.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent evento) {
					comienza_el_juego(evento);
					
				}
				
				
				
			});
			
			laminaBotones.add(arranca1);
			//..............................................................
			arranca2= new JButton("Hilo 2");
			
			arranca2.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent evento) {
					comienza_el_juego(evento);
					
				}
				
				
				
			});
			
			laminaBotones.add(arranca2);
			//...............................................................
			arranca3= new JButton("Hilo 3");
			
			arranca3.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent evento) {
					comienza_el_juego(evento);
					
				}
				
				
				
			});
			
			laminaBotones.add(arranca3);
			//................................................................
			detener1= new JButton("Detener Hilo 1");
			
			detener1.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent evento) {
					
					detener3(evento);
				}
				
				
				
			});
			
			laminaBotones.add(detener1);
			//................................................................
			detener2= new JButton("Detener Hilo 2");
			
			detener2.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent evento) {
					
					detener3(evento);
				}
				
				
				
			});
			
			laminaBotones.add(detener2);
			//...............................................................
			detener3= new JButton("Detener Hilo 3");
			
			detener3.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent evento) {
					
					detener3(evento);
				}
				
				
				
			});
			
			laminaBotones.add(detener3);
			//....................................................................
			
			
			
			add(laminaBotones, BorderLayout.SOUTH);
		}
			
			
		
		//pregunta que elemento desencadena el evento para saber que hilo intenta arrancar
		
		public void comienza_el_juego (ActionEvent e){
			
						
				Pelota3 pelota=new Pelota3();
				
				lamina.add(pelota);
				
				
			
				Runnable r= new PelotaHilos3(pelota, lamina);
				
				if(e.getSource().equals(arranca1)) {
					
					t1= new Thread(r);
					t1.start();
					
				}else if(e.getSource().equals(arranca2)) {
					
					t2= new Thread(r);
					t2.start();
					
				}else if(e.getSource().equals(arranca3)) {
					
					t3= new Thread(r);
					t3.start();
				}
				
				
			
		}
		
		
		//metodo detener, detiene el hilo de ejecucion
		
		public void detener3(ActionEvent e) {
			
			if(e.getSource().equals(detener1)) {
			
				t1.interrupt();
				
			}else if(e.getSource().equals(detener2)) {
			
				t2.interrupt();
				
			}else if(e.getSource().equals(detener3)) {
			
				t3.interrupt();
				
			}
		}
		
		private LaminaPelota3 lamina;
		Thread t1;
		Thread t2;
		Thread t3;
		JButton arranca1;
		JButton arranca2;
		JButton arranca3;
		JButton detener1;
		JButton detener2;
		JButton detener3;
	}


