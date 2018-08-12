//Bunyamin Iseri 150130203

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class p2pTester {
	static DatagramSocket ds = null;
	static DatagramPacket dp = null;
	static int turn;//1 for my turn, 0 for your turn
	public static void sendMessage(InetAddress IP) throws IOException{
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String message;
		System.out.println("Enter your message: ");
        message = scanner.nextLine();
        message="CHATMSG<"+message+">";
        byte[] sendData = message.getBytes(); //calculate bytes of the message
        DatagramPacket dp = new DatagramPacket(sendData, sendData.length); //create new packet same size of message
        dp.setAddress(IP); //set destination IP
        ds.send(dp);
        turn=0; //it's receiver's turn now
		
	}
	public static void getMessage() throws IOException{
		System.out.println("Waiting...");
        
        while (true) {// loop for reading received socket
            byte[] buffer = new byte[65000]; // max char length
            dp = new DatagramPacket(buffer, buffer.length);//create new packet same size of buffer
            ds.receive(dp);
            String s = new String(dp.getData(), 0, dp.getLength());
            System.out.println("Received: "+s);
            System.out.println("Received data packet from :"+dp.getAddress().toString());
            turn=1;
        }
        
	}
	public static void main(String[] args) throws IOException {
		int port;
		String IP2 = null,port2 = null;
		String recIP;
		
					
		System.out.println("Type 1 for sending message or type 0 for waiting message...");
		Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        
		
        if(choice==1){//send
        	
        	ds= new DatagramSocket(5678);
        	InetAddress IP = InetAddress.getLocalHost();//our IP
        	port = ds.getLocalPort();
        	System.out.println("Enter destination IP: ");
        	IP2 = scanner.nextLine();// set IP2 to destination IP
        	port2="5679"; //as given in project
        	
        	
        	InetSocketAddress address = new InetSocketAddress(IP2, Integer.parseInt(port2));
        	String msg = "CHATREQ FROM<"+IP.getHostAddress()+":"+port+">"; //show requester's ip and port
        	
        	byte[] sendMsg = msg.getBytes(); 
            dp = new DatagramPacket(sendMsg, sendMsg.length);//creates new packet same size of message
            dp.setSocketAddress(address); 
            
            ds.send(dp); //sends message 
            turn=0;//receiver's turn
        }
        
        else if(choice==0){//receive
        	ds= new DatagramSocket(5678);
        	byte[] buffer = new byte[65000]; // max char length
            dp = new DatagramPacket(buffer, buffer.length);//create new packet same as buffer size
            ds.receive(dp);
            String s = new String(dp.getData(), 0, dp.getLength());//get data from packet and put it into string
            System.out.println("Received: "+s); //display received message 
            recIP=dp.getAddress().toString(); // save address to recIP string
            System.out.println("Received data packet from :"+recIP); // disply sender's address
            turn=1;//sender's turn
        	
        }
        else{
        	System.out.println("You've entered wrong input!");
        	
        }
        
        while(true){//while loop for changing turns after sending and receiving 
        			//every user has only 1 turn
            
            if(turn==1){//sender's turn
            	InetAddress destIP;
            	destIP=dp.getAddress(); //get address of datagram packet
                sendMessage(destIP);//send message to that address
                turn=0;//give turn to receiver
            }
            else if(turn==0){//receiver's turn
                getMessage();
                turn=1;//give turn to sender
            }
        }
		
	}//end main
}//end class

