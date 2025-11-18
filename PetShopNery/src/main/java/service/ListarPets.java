package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ListarPets {

    public static void listaPet (){

        File pasta = new File("C:\\Users\\bruni\\OneDrive\\Documentos\\CursosProgramacaoAtual\\DesafioPetShop\\PetsCadastrados");
        File[] arquivos = pasta.listFiles();

        if (arquivos == null){
            System.out.println("Nenhum pet Cadastrado.");
            return;
        }

        int cont = 1;

        for (File arquivo : arquivos){
            if ( !arquivo.isFile()){
                continue;
            }
            try (BufferedReader leitor = new BufferedReader (new FileReader(arquivo))){
                String nome = "";
                String tipo = "";
                String sexo = "";
                String endereco = "";
                String idade = "";
                String peso = "";
                String raca = "";

                String linha;

                while ((linha = leitor.readLine()) !=null){
                    if (linha.startsWith("1 - ")) nome = linha.substring(4);
                    else if (linha.startsWith("2 - ")) tipo = linha.substring(4);
                    else if (linha.startsWith("3 - ")) sexo = linha.substring(4);
                    else if (linha.startsWith("4 - ")) endereco = linha.substring(4);
                    else if (linha.startsWith("5 - ")) idade = linha.substring(4);
                    else if (linha.startsWith("6 - ")) peso = linha.substring(4);
                    else if (linha.startsWith("7 - ")) raca = linha.substring(4);
                }
                System.out.println(cont + " - "
                + nome + " - "
                + tipo + " - "
                + sexo + " - "
                + endereco + " - "
                + idade + " - "
                + peso + " - "
                + raca);

                cont++;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
