package br.com.exemplo.prova.servidorUpLoadImagem;

import br.com.exemplo.prova.classAuxiliares.CanalUpLoadImagensColoridas;
import br.com.exemplo.prova.classAuxiliares.SerializableImagem;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

/*
 autore: Anderson Costa Moreira Santana
         Taylan Nalimar Cruz Celestino
*/
public class ServidorUpLoadTres {

    // origem das imagens
    static final String pathDiretorioImagensColoridas = ""; // colocar o path da imagens que serão processadas
    static SerializableImagem imagem = new SerializableImagem();

    public static void main(String[] args) throws Exception {

        System.out.println("Caso acontece algum erro, cheque os path dos diretório das imagens de origem\n" +
                "e do diretório de destino: ");
        // aqui é um artificio para criar apenas uma instanciar do objeto CanalUpLoadImagensColoridas
        CanalUpLoadImagensColoridas exchange = new CanalUpLoadImagensColoridas();
        if (exchange.isInstancia()) {
            exchange.main(new String[]{});
            exchange.setInstancia(false);
        }

        // lista de nomes dos arquivos
        List<String> listaNomes = imagem.listaNomesArquivos(pathDiretorioImagensColoridas);

        // faço uma interação na lista de nomes para passar por parâmentro na função;
        for (String nomeImagem: listaNomes) {
            String caminhaImagem = pathDiretorioImagensColoridas + nomeImagem;

            // lê uma imagem cujo nome passo por parâmetro.
            BufferedImage img = ImageIO.read(new File(caminhaImagem));
            byte[] foto = imagem.getSerializableImagem(img);

            System.out.print("upload de imagem colorida");
            String tempoEspera = "........";
            for (char ch : tempoEspera.toCharArray()) {
                if (ch == '.') {
                    try {
                        Thread.sleep(1000); // faço a thread parar por alguns 8 segundos
                        System.out.print(".");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            System.out.println(" ok!");
            // seto a imagem convertida em um array de bytes depois invoco o metódo main
            // da classe CanalUpLoadImagensColoridas para colocar a imagem na fila para
            // ser processada
            exchange.setImagem(foto);
            exchange.main(new String[]{});
        }
    }
}
