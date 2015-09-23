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
	private static final int SERVERPORTSEND = 5000;			//Port for sending motor commands
	private static final int SERVERPORTGPS = 5001;			//Use a separate port for receiving GPS
	private static final int SERVERPORTWHEEL = 5002;		//Use this port for receiving wheel motion
	private static final String SERVER_IP = "192.168.0.82";
	private DataOutputStream outChannel;
	private String tag = "Socket";
	private Thread clientThread0;
	private Thread clientThread1;
	private Thread clientThread2;
    List<ROSServiceReporter> targets;
	
	@Override
	public IBinder onBind(Intent arg0) {
		Log.d(tag, "Ros/Socket bind");
		clientThread0 = new ClientThread(SERVERPORTSEND);
		clientThread1 = new ClientThread(SERVERPORTGPS);
		clientThread2 = new ClientThread(SERVERPORTWHEEL);
		clientThread0.start();
		clientThread1.start();
		clientThread2.start();
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
	    				commandString.concat("\0");	//needed as an end of line marker
	    				byte[] bytes = commandString.getBytes();
	    				outChannel.write(bytes);
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

		private final int SERVERPORT;
		
		public ClientThread(int socketPort){
			this.SERVERPORT = socketPort;
		}
		
		@Override
		public void run() {
			while(!isInterrupted()){
				try{
					if(socket == null || socket.isClosed()){
						//Log.d(tag,"Trying to open Socket");
						InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
						socket = new Socket(serverAddr, SERVERPORT);
						while(socket==null){
							socket = new Socket(serverAddr, SERVERPORT);
						}
						
						if(SERVERPORT==SERVERPORTSEND){							//outChannel is set up on the send port only
							outChannel = new DataOutputStream(socket.getOutputStream());
						} else if(SERVERPORT==SERVERPORTGPS){
							GPSCommunicationThread commThread = new GPSCommunicationThread(socket);	//only need comm port for receive sockets
							new Thread(commThread).start();
						} else { //SERVERPORTWHEEL
							WheelCommunicationThread commThread = new WheelCommunicationThread(socket);	//only need comm port for receive sockets
							new Thread(commThread).start();
						}
							
						Log.d(tag,"Socket "+SERVERPORT+" Opened");
					}
				} catch(UnknownHostException e1){
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} 
			}
		}
	}
	
	class GPSCommunicationThread implements Runnable {

		private Socket clientSocket;
		private DataInputStream input;
		
		public GPSCommunicationThread(Socket clientSocket){
			this.clientSocket = clientSocket;
			try{
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
		        			// Log.d(tag, "Received: "+data + ", length:" + data.length());
		        			 Location location = new Location("");
		        			 String[] locationInfo = data.split(",");
		        			 //Log.d(tag,"Location Info Size: "+locationInfo.length);
		        			 if(locationInfo.length>=4){
			        			 location.setLatitude(Double.valueOf(locationInfo[0]));
			        			 location.setLongitude(Double.valueOf(locationInfo[1]));
			        			 location.setSpeed(Float.valueOf(locationInfo[2]));
			        			 location.setBearing(Float.valueOf(locationInfo[3])); 
		        			 }
		        			
		        			 synchronized (reporters) {
		     					targets = new ArrayList<ROSServiceReporter>(reporters);
		     				}
		     				for(ROSServiceReporter rosReporter : targets) {
		     					try {
		     							rosReporter.reportGPS(location);

			     					} catch (RemoteException e) {
			     						Log.e(tag,"report",e);
			     					}
		     				}
		        			 
		        		 }
		        	 }
				
				} catch(IOException e){
					e.printStackTrace();
				}
				catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
	class WheelCommunicationThread implements Runnable {

		private Socket clientSocket;
		private DataInputStream input;
		
		public WheelCommunicationThread(Socket clientSocket){
			this.clientSocket = clientSocket;
			try{
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
		        			 /////Insert motor code here
		        			 
		        			 
		        			 double forward = 0;
		        			 double right = 0;
		        			 Location location = new Location("");
		        			 String[] locationInfo = data.split(",");
		        			 //Log.d(tag,"Location Info Size: "+locationInfo.length);
		        			 if(locationInfo.length>=4){
			        			 location.setLatitude(Double.valueOf(locationInfo[0]));
			        			 location.setLongitude(Double.valueOf(locationInfo[1]));
			        			 location.setSpeed(Float.valueOf(locationInfo[2]));
			        			 location.setBearing(Float.valueOf(locationInfo[3])); 
		        			 }
		        			
		        			 synchronized (reporters) {
		     					targets = new ArrayList<ROSServiceReporter>(reporters);
		     				}
		     				for(ROSServiceReporter rosReporter : targets) {
		     					try {
		     							rosReporter.reportWheel(forward, right);

			     					} catch (RemoteException e) {
			     						Log.e(tag,"report",e);
			     					}
		     				}
		        			 
		        		 }
		        	 }
				
				} catch(IOException e){
					e.printStackTrace();
				}
				catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
	
	
	 @Override
	    public void onDestroy() {
	        if(clientThread0 != null){
	            clientThread0.interrupt();
	            clientThread0 = null;
	        }
	        if(clientThread1 != null){
	            clientThread1.interrupt();
	            clientThread1 = null;
	        }
	        Log.d(tag,"Socket onDestroy");
	        super.onDestroy();
	    }

	    @Override
	    public void onCreate() {
	        super.onCreate();
	        Log.d(tag, "Socket SocketMain onCreate");

	    }

}
