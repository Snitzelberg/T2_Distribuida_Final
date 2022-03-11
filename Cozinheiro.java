
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.util.Util;

import java.io.*;
import java.util.List;
import java.util.LinkedList;


public class Cozinheiro extends ReceiverAdapter {
    public JChannel CozinheiroRestaurante;
    public String[] pedidos = {"Hamburguer", "Batata Frita","Hamburguer de Frango","Hamburguer Vegano", "Milkshake", "Refrigerante"};
    final List<String> state=new LinkedList<String>();

    public void receive(Message msg) {
        System.out.println(msg.getObject());
    }

    public void getState(OutputStream output) throws Exception {
        synchronized(state) {
            Util.objectToStream(state, new DataOutputStream(output));
         }
    }

    @SuppressWarnings("unchecked")
    public void setState(InputStream input) throws Exception {
        List<String> list=(List<String>)Util.objectFromStream(new DataInputStream(input));
        synchronized(state) {
            state.clear();
            state.addAll(list);
        }
        System.out.println("received state (" + list.size() + " messages in chat history):");
        for(String str: list) {
            System.out.println(str);
        }
    }

    private void start() throws Exception {
        CozinheiroRestaurante=new JChannel();
        CozinheiroRestaurante.setReceiver(this);
        CozinheiroRestaurante.connect("CozinheiroRestaurante");
        CozinheiroRestaurante.getState(null, 10000);
        eventLoop();
    }

    private void eventLoop() {
        BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
        TelaCozinheiro Tela = new TelaCozinheiro(this);
        Tela.pack();
        Tela.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        new Cozinheiro().start();
    }        
}