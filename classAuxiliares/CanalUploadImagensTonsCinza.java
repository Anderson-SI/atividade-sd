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
public class CanalUploadImagensTonsCinza {
    /*
     * Essa classe uso para fazer a conexção com RabbitMQ
     * declaro uma exchange
     * Publico as fotos Processadas para tons de Ciza
     * */
    private static byte[] mensagem;
    private static boolean instance = true;


    public CanalUploadImagensTonsCinza() {
    }

    public byte[] getMensagem() {
        return mensagem;
    }

    public void setMensagem(byte[] mensagem) {
        this.mensagem = mensagem;
    }

    public static boolean isInstance() {
        return instance;
    }

    public static void setInstance(boolean instance) {
        CanalUploadImagensTonsCinza.instance = instance;
    }


    private static Runnable enviar = new Runnable() {

        @Override
        public void run() {
            try {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost("localhost");
                //factory.setUsername("guest");
                //factory.setPassword("guest");
                final String EXCHANGE_FANOUT = "exchangeFilaMensagemProcessada";
                final String FILA_MENSAGEM_PROCESSADA = "filaImagemProcessada";
                Connection connection = factory.newConnection();
                Channel canal = connection.createChannel();
                canal.exchangeDeclare(EXCHANGE_FANOUT, "fanout");
                //canal.queueDeclare(FILA_MENSAGEM_PROCESSADA, false, false, false, null);
                //canal.queueBind(FILA_MENSAGEM_PROCESSADA, EXCHANGE_FANOUT, "");
                canal.basicPublish(EXCHANGE_FANOUT, "", null, mensagem);

            } catch (TimeoutException | IOException e) {
                e.printStackTrace();
            }
        }
    };
    public static void main(String[] args) {
        new Thread(enviar).start();
    }
}
