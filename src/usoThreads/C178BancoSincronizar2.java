package usoThreads;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//Clase 179:modificar codigo para usar los metodos de la clase object wait() notifyAll()
//si uso los metodos de la clase object solo puedo usar una condicion para nuestros hilos (la del while)
//con los metodos de la interface Condition puedo establecer varias condiciones, creandolas: igual que cree saldo suficiente

public class C178BancoSincronizar2 {
	
	public static void main(String[] args) {
		
		Banco2 b= new Banco2();
		
		for(int i=0;i<100;i++) {
			
			EjecucionTransferencias2 r= new EjecucionTransferencias2(b, i, 2000);
			
			Thread t= new Thread(r);
			
			
			t.start();
			
		}
		
		
		
	}

}

//crear 100 cuentas corrientes
//cargar cada cuenta con 2000 euros

class Banco2{
	//variables de clase
	private final double [] cuentas;

	
	//constructor
	public Banco2() {
		//creo las 100 cuentas
		cuentas=new double[100];
		
		//cargo cada cuenta con 2mil euros
		for (int i=0; i<cuentas.length;i++) {
			
			cuentas[i]=2000;
			
		}
		
	
		
	}
	
	//metodo que hace transferencias
	public synchronized void transferencia2(int cuentaOrigen, int cuentaDestino, double cantidad) {
		
		
		
		
		
		//controlar que la cantidad a transferir no supere el saldo de la cuenta
		//si no es suficiente, entra en el while
	while(cuentas[cuentaOrigen]<cantidad) {
		
		//queda a la espera
		try {
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		}
		
		//imprime el hilo que va a realizar la transferencia
		System.out.println(" hilo de operacion "+Thread.currentThread());
		
		//descontar la cantidad a transferir de la cuenta origen
		cuentas[cuentaOrigen]-= cantidad;
		
		//me informa lo que esta haciendo
		System.out.printf("%10.2f de %d para %d", cantidad, cuentaOrigen, cuentaDestino);
		
		//incremento saldo en cuenta destino
		cuentas[cuentaDestino]+=cantidad;
		
		//imprime el saldo total con formato y salto de linea
		System.out.printf(" saldo total: %10.2f%n", getSaldoTotal2());
		
		//informa que se ejecuto una operacion, que vean a los que estan a la espera si les sirve y cumplen la condicion o no
			notifyAll();
			
	}
	
	//metodo que me devuelve el saldo total sumando el de todas las cuentas
	public double getSaldoTotal2() {
		
		double sumaCuentas=0;
		
		
		for(double a:cuentas) {
			
			sumaCuentas+=a;
		}
		
		return sumaCuentas;
		
	}
	
	
}


//clase con metodo run que ejecute los hilos

class EjecucionTransferencias2 implements Runnable{

	//campos de clase 
	private Banco2 banco;
	private int deLaCuenta;
	private double cantidadMax;
	
	//constructor: instanciar objeto de la clase Banco para poder usar los metodos de esa clase denro de esta clase
	//cuenta origen, cantidad de dinero a transferir maxima
	public EjecucionTransferencias2(Banco2 b, int de, double max) {
		
		banco= b;
		deLaCuenta= de;
		cantidadMax=max;
		
		
	}
	
	//ejecuta cada hilo, sera tranferencias infinitas en un bucle
	//cuenta destino aleatoria
	
	@Override
	public void run() {
		
		while(true) {
			
			//casting a int  porque random genera un double entre 0 y 1
			int paraLaCuenta=(int)(100*Math.random());
			
			//cantidad maxima a transferir 2mil euros
			double cantidad= cantidadMax*Math.random();
			
			//realizar la transferencia
			banco.transferencia2(deLaCuenta, paraLaCuenta, cantidad);
			
			//dormir los hilos para que podamos ver en consola la informacion, tiempo randon
			try {
				
				Thread.sleep((int)Math.random()*10 );
			
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			
		}
		
		
		
	}
	
}














