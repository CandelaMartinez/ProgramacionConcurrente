package usoThreads;

public class C171SincronizandoHilos {

	public static void main(String[] args) {
		
		HilosVarios hilo1= new HilosVarios();
		hilo1.start();
		
		
	
		//.......................................................................
		//hasta que hilo1 no termine su ejecucion no podre comenzar la ejecucion de hilo2
		HilosVarios2 hilo2= new HilosVarios2(hilo1);
		hilo2.start();

	}

}

//clase para crear hilos concurrentes
class HilosVarios extends Thread{
	
	//metodo que dice que hace el hilo
	public void run() {
		
		for(int i=0; i<15; i++) {
			
			System.out.println("Ejecutando hilo"+i+ " ----Hilo: "+getName());
			
			try {
				Thread.sleep(700);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			
		}
		
	}
	
}

class HilosVarios2 extends Thread{
	
	private Thread hilo;
	
	public HilosVarios2(Thread hilo) {
		
		this.hilo=hilo;
		
	}
	
	
	public void run() {
		
		//esta tarea no comienza hasta que no termina la anterior
		try {
			hilo.join();
		} catch (InterruptedException e1) {
			
			e1.printStackTrace();
		}
		
		
		for(int i=0; i<15; i++) {
			
			System.out.println("Ejecutando hilo"+i+ " ----Hilo: "+getName());
			
			try {
				Thread.sleep(700);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			
		}
		
	}
	
}


