
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.util.Util;

import java.io.*;
import java.util.List;
import java.util.LinkedList;



public class Caixa extends ReceiverAdapter {
    public JChannel CaixaRestaurante;
    final List<String> state=new LinkedList<String>();
    public String[] pedidos = {"Hamburguer", "Batata Frita","Hamburguer de Frango","Hamburguer Vegano", "Milkshake", "Refrigerante"};

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
        CaixaRestaurante=new JChannel();
        CaixaRestaurante.setReceiver(this);
        CaixaRestaurante.connect("CaixaRestaurante");
        CaixaRestaurante.getState(null, 10000);
        eventLoop();
    }

    private void eventLoop() {
        System.out.println("# Aguarde o cliente efetuar um pedido #");
        BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
        TelaInicial Tela = new TelaInicial(this);
        Tela.pack();
        Tela.setVisible(true);
    }
    
    public static void main(String[] args) throws Exception {
        new Caixa().start();
    }        
}
