package br.com.exemplo.prova.processamento;

import br.com.exemplo.prova.classAuxiliares.CanalUploadImagensTonsCinza;
import br.com.exemplo.prova.classAuxiliares.SerializableImagem;
import com.rabbitmq.client.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
/*
 autore: Anderson Costa Moreira Santana
         Taylan Nalimar Cruz Celestino
*/
public class ProcessamentoUm {

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        //factory.setUsername("guest");
        //factory.setPassword("guest");

        CanalUploadImagensTonsCinza publicarParaServidor = new CanalUploadImagensTonsCinza();
        if (publicarParaServidor.isInstance()) {
            publicarParaServidor.main(new String[]{});
            publicarParaServidor.setInstance(false);
        }

        // constante definida com o nome da exchanges
        Scanner teclado = new Scanner(System.in);
        final String FILA_MENSAGEM_NAO_PROCESSADA = "FilaImagemNaoProcessada";
        final String EXCHANGE_FANOUT = "exchangeFilaMensagemProcessada";
        final String pathDiretorioImagensPretoBranco = "C:\\Users\\Ander\\Desktop\\imagensProcessadas\\";
        SerializableImagem imagem = new SerializableImagem();
        List<byte[]> listaImagens = new ArrayList<>();

        // estabelece a conexão com RabbitMQ e cria dois canais;
        // um para receber imagens e outro para enviar
        Connection connection = factory.newConnection();
        Channel canalReceberMensagem = connection.createChannel();
        Channel canalEnviarMensagem = connection.createChannel();

        canalEnviarMensagem.exchangeDeclare(EXCHANGE_FANOUT, "fanout");

        Consumer consumerImagen = new DefaultConsumer(canalReceberMensagem) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                BufferedImage mmm = imagem.getBufferedImage(body);

                // tranformo a imagem em tons de cinza
                BufferedImage imgTonsCinza = imagem.imgPretoBranco(mmm);
                byte[] img = imagem.getSerializableImagem(imgTonsCinza);

                // faço a thread esperar 10 segundos
                System.out.print("processando imagem ");
                String tempoEspera = "..........";
                for (char ch : tempoEspera.toCharArray()) {
                    if (ch == '.') {
                        try {
                            Thread.sleep(1000);
                            System.out.print(".");
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }

                // coloco a imagem processada na fila
                publicarParaServidor.setMensagem(img);
                publicarParaServidor.main(new String[]{});
                System.out.println(" Concluída!");

            }
        };
        // consumindo da fila das imagens não processadas
        canalReceberMensagem.basicConsume(FILA_MENSAGEM_NAO_PROCESSADA, true, consumerImagen);

    }
}

