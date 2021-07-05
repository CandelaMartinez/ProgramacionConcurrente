package usoThreads;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//Clase 176 - 177 realizar igualmente las transferencias por mas que el saldo sea insuficiente, se pone el hilo a la espera de que ingrese dinero y libera el codigo
//todos los hilos realizan la transferencia, si no tiene saldo, espera a que entre dinero para hacerla
//con los metodos de la interface Condition puedo establecer varias condiciones, creandolas: igual que cree saldo suficiente

public class C173BancoSincronizar {
	
	public static void main(String[] args) {
		
		Banco b= new Banco();
		
		for(int i=0;i<100;i++) {
			
			EjecucionTransferencias r= new EjecucionTransferencias(b, i, 2000);
			
			Thread t= new Thread(r);
			
			
			t.start();
			
		}
		
		
		
	}

}

//crear 100 cuentas corrientes
//cargar cada cuenta con 2000 euros

class Banco{
	//variables de clase
	private final double [] cuentas;
	
	//para usar los metodos de la clase ReentrantLock tengo que instanciar la interface Lock desde su clase ReentrantLock por polimorfismo
	private Lock cierreBanco= new ReentrantLock();
	
	//con los metodos de la interface Condition puedo establecer varias condiciones, creandolas: igual que cree saldo suficiente
	//interface condition para poder usar metodos await() y  signalAll()
	private Condition saldoSuficiente;
	
	//constructor
	public Banco() {
		//creo las 100 cuentas
		cuentas=new double[100];
		
		//cargo cada cuenta con 2mil euros
		for (int i=0; i<cuentas.length;i++) {
			
			cuentas[i]=2000;
			
		}
		
		//cierreBanco tiene que establecerse en base a una condicion
		saldoSuficiente= cierreBanco.newCondition();
		
	}
	
	//metodo que hace transferencias
	public void transferencia(int cuentaOrigen, int cuentaDestino, double cantidad) {
		
		//bloqueo el metodo con la instancia de la interface Lock para que no entre ningun hilo mas
		cierreBanco.lock();
		
		
		try {
		//controlar que la cantidad a transferir no supere el saldo de la cuenta
		//si no es suficiente, entra en el while
	while(cuentas[cuentaOrigen]<cantidad) {
			
			
		
		try {
			
			
			//mientras la condicion se cumple, pongo el hilo a la espera de que entre dinero y se libera el bloqueo
			//asi entra al proximo hilo
			saldoSuficiente.await();
		
		} catch (InterruptedException e) {
			
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
		System.out.printf(" saldo total: %10.2f%n", getSaldoTotal());
		
		//despierta a los hilos en espera que se despierten para ver si a alguno le sirve la transferencia que hice
		saldoSuficiente.signalAll();
		
		}finally {
			
			//tanto si ocurre una excepcion o no, desbloquee siempre
			
			cierreBanco.unlock();
			
		}
		
	}
	
	//metodo que me devuelve el saldo total sumando el de todas las cuentas
	public double getSaldoTotal() {
		
		double sumaCuentas=0;
		
		
		for(double a:cuentas) {
			
			sumaCuentas+=a;
		}
		
		return sumaCuentas;
		
	}
	
	
}


//clase con metodo run que ejecute los hilos

class EjecucionTransferencias implements Runnable{

	//campos de clase 
	private Banco banco;
	private int deLaCuenta;
	private double cantidadMax;
	
	//constructor: instanciar objeto de la clase Banco para poder usar los metodos de esa clase denro de esta clase
	//cuenta origen, cantidad de dinero a transferir maxima
	public EjecucionTransferencias(Banco b, int de, double max) {
		
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
			banco.transferencia(deLaCuenta, paraLaCuenta, cantidad);
			
			//dormir los hilos para que podamos ver en consola la informacion, tiempo randon
			try {
				
				Thread.sleep((int)Math.random()*10 );
			
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			
		}
		
		
		
	}
	
}














