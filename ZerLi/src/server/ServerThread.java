package server;

public class ServerThread extends Thread implements Runnable{
	
@Override
public void run() {
	// TODO Auto-generated method stub
	super.run();
	GuiOpener.init_launch();
}
}
