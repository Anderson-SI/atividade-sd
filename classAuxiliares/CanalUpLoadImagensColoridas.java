package br.com.exemplo.prova.classAuxiliares;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
/*
 autore: Anderson Costa Moreira Santana
         Taylan Nalimar Cruz Celestino
*/
public class CanalUpLoadImagensColoridas {
/*
* Essa classe uso para fazer a conexção com RabbitMQ
* Crio uma exchange
* Crio uma fila
* Publico as fotos Originais
* */
    private static byte[] imagem;
    private static boolean instancia = true;
    private final static String FILA_MENSAGEM_NAO_PROCESSADA = "FilaImagemNaoProcessada";
    private final static String EXCHANGE_DIRECT = "exchangeFilaMensagemNaoProcessada";
    private final static String ROUTING_KEY = "keyRotaFilaImagem";

    public CanalUpLoadImagensColoridas(){
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

    public static boolean isInstancia() {
        return instancia;
    }

    public static void setInstancia(boolean instancia) {
        CanalUpLoadImagensColoridas.instancia = instancia;
    }

    private static Runnable enviar = new Runnable() {

        @Override
        public void run() {
            try {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost("localhost");
                //factory.setUsername("guest");
                //factory.setPassword("guest");

                Connection connection = factory.newConnection();
                Channel canal = connection.createChannel();
                canal.exchangeDeclare(EXCHANGE_DIRECT, "direct");
                canal.queueDeclare(FILA_MENSAGEM_NAO_PROCESSADA, false, false, false, null);
                canal.queueBind(FILA_MENSAGEM_NAO_PROCESSADA, EXCHANGE_DIRECT, ROUTING_KEY);
                canal.basicPublish(EXCHANGE_DIRECT, ROUTING_KEY, null, imagem);

            } catch (TimeoutException | IOException e) {
                e.printStackTrace();
            }
        }
    };
    public static void main(String[] args) {
        new Thread(enviar).start();
    }

}
