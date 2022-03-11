
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.util.Util;

import java.io.*;
import java.util.List;
import java.util.LinkedList;


public class Restaurante extends ReceiverAdapter {
    public JChannel CaixaRestaurante;
    public JChannel CozinheiroRestaurante;
    final List<String> state=new LinkedList<String>();


    public void receive(Message msg) {
        
        String line=msg.getSrc() + ": " + msg.getObject();
        System.out.println(line);
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
    }

    private void start() throws Exception {
        /* Caixa Restaurante */
        CaixaRestaurante=new JChannel();
        CaixaRestaurante.setReceiver(this);
        CaixaRestaurante.connect("CaixaRestaurante");
        CaixaRestaurante.getState(null, 10000);
        /* Cozinheiro Restaurante */
        CozinheiroRestaurante=new JChannel();
        CozinheiroRestaurante.setReceiver(this);
        CozinheiroRestaurante.connect("CozinheiroRestaurante");
        CozinheiroRestaurante.getState(null, 10000);
        eventLoop();
    }

    private void eventLoop() {
        BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
    }

    
    public static void main(String[] args) throws Exception {
        new Restaurante().start();
    }        
}
