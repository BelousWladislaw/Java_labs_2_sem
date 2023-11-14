import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final int PORT = 8080;
    private static List<Socket> clients = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Сервер запущен на порту " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                clients.add(clientSocket);
                System.out.println("Новый клиент подключен: " + clientSocket);

                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcastMessage(String message) {
        for (Socket client : clients) {
            try {
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                out.println(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void removeClient(Socket clientSocket) {
        clients.remove(clientSocket);
        System.out.println("Клиент отключен: " + clientSocket);
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader in;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String username = in.readLine();
            System.out.println("Пользователь " + username + " присоединился.");

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println(username + ": " + message);
                Server.broadcastMessage(username + ": " + message);
            }

            Server.removeClient(clientSocket);
            System.out.println("Пользователь " + username + " отсоединился.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}