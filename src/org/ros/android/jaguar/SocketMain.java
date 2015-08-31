package org.ros.android.jaguar;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class SocketMain extends Service{
	
	private Socket socket;
	private static final int SERVERPORT = 5000;
	private static final String SERVER_IP = "192.168.0.100";
	private DataOutputStream outChannel;
	private String tag = "Socket";
	private Thread clientThread;
    private ROSServiceManager jaguarManager;
    Location currentLocation;
	
	@Override
	public IBinder onBind(Intent arg0) {
		Log.d(tag, "Ros/Socket bind");
		clientThread = new ClientThread();
		clientThread.start();
		return socketBinder;
	}
	
	private List<ROSServiceReporter> reporters = new ArrayList<ROSServiceReporter>();
	
	
	 public ROSService.Stub socketBinder = new ROSService.Stub() {
	        @Override public void add(ROSServiceReporter reporter) throws RemoteException {
	            synchronized (reporters) {
	                reporters.add(reporter);
	            }
	        }

	        @Override public void remove(ROSServiceReporter reporter) throws RemoteException {
	            synchronized (reporters) {
	                reporters.remove(reporter);
	            }
	        }

	        @Override
	        public void publishCommand(String commandString) throws RemoteException {
	        	try{
	    			if (socket!=null && !socket.isClosed()){
	    				Log.d("Socket","Attempting to Write "+commandString);
	    				//PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
	    				outChannel.writeChars(commandString);
	    				outChannel.flush();
	    				Log.d(tag,"Writing "+commandString);
	    			} else{
	    				Log.d(tag,"Socket is closed");
	    			}
	    		} catch(UnknownHostException e){
	    			e.printStackTrace();
	    		} catch (IOException e) {
	    			e.printStackTrace();
	    		} catch (Exception e){
	    			e.printStackTrace();
	    		}
	        }
	    };
	 
	
	class ClientThread extends Thread{

		@Override
		public void run() {
			while(!isInterrupted()){
				try{
					if(socket == null || socket.isClosed()){
						Log.d(tag,"Trying to open Socket");
						InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
						socket = new Socket(serverAddr, SERVERPORT);
						outChannel = new DataOutputStream(socket.getOutputStream());
						CommunicationThread commThread = new CommunicationThread(socket);
						new Thread(commThread).start();
						Log.d(tag,"Socket Opened");
					}
				} catch(UnknownHostException e1){
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	class CommunicationThread implements Runnable {

		private Socket clientSocket;
		private DataInputStream input;
		
		public CommunicationThread(Socket clientSocket){
			this.clientSocket = clientSocket;
			try{
				//this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
				this.input = new DataInputStream(clientSocket.getInputStream());
				} catch(IOException e){
					e.printStackTrace();
				}
		}
		
		@Override
		public void run() {
			while(!Thread.currentThread().isInterrupted()){
				try{
					byte[] b = new byte[10000];
					if(!clientSocket.isClosed()){
		        		 input.read(b);
		        		 String data = new String(b);
		        		 data = data.trim();
		        		 if(data!=null && data.length()!=0){
		        			 Log.d(tag, "Received: "+data + " length:" + data.length());
		        			 //This is where you decode the gps information and make a location object!!
		        		 }
		        	 }
				
				} catch(IOException e){
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
	 @Override
	    public void onDestroy() {
	        if(clientThread != null){
	            clientThread.interrupt();
	            clientThread = null;
	        }
	        Log.d(tag,"Socket onDestroy");
	        super.onDestroy();
	    }

	    @Override
	    public void onCreate() {
	        super.onCreate();
	        Log.d(tag, "Socket SocketMain onCreate");

	        jaguarManager = new ROSServiceManager(this, new ROSServiceManager.OnConnectedListener() {
	            @Override public void onConnected() {
	                Log.d(tag, "Jaguar connected - adding reporter");
	                jaguarManager.add(jaguarServiceReporter);

	            }
	            @Override public void onDisconnected() {
	                Log.d(tag, "Jaguar disconnected - removing reporter");
	                jaguarManager.remove(jaguarServiceReporter);
	            }
	        });

	    }

	    private ROSServiceReporter jaguarServiceReporter = new ROSServiceReporter.Stub() {
	        @Override
	        public void reportGPS(Location location) throws RemoteException {
	            currentLocation=location;
	        }
	    };


}
