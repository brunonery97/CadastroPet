package service;

import entities.Endereco;
import entities.Pet;
import enums.Sexo;
import enums.Tipo;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CadastrarPet {

    public static void cadastrarPet(Scanner sc) {

        String formulario = "C:\\Users\\bruni\\OneDrive\\Documentos\\CursosProgramacaoAtual\\DesafioPetShop\\formulario.txt";
        List<String> respostas = new ArrayList<>();
        Endereco endereco = null;

        try (BufferedReader leitor = new BufferedReader(new FileReader(formulario));) {
            String linha;
            int cont = 0;

            while ((linha = leitor.readLine()) != null) {
                if (cont == 3) {
                    System.out.println(linha);
                    System.out.print("Rua: ");
                    String rua = sc.nextLine();

                    System.out.print("Numero: ");
                    String numero = sc.nextLine();

                    System.out.print("Cidade: ");
                    String cidade = sc.nextLine();

                    endereco = new Endereco(rua, numero, cidade);

                } else {
                    System.out.println(linha);
                    System.out.print("R: ");
                    String resposta = sc.nextLine();
                    respostas.add(resposta);
                }
                cont++;
            }
        } catch (IOException e) {
            System.out.println("Erro ao leo o formulario" + e.getMessage());
            return;
        }

        String nomeSobrenome = respostas.get(0);
        String tipoStr = respostas.get(1);
        String sexoStr = respostas.get(2);
        String idadeStr = respostas.get(3);
        String pesoStr = respostas.get(4);
        String raca = respostas.get(5);

        Tipo tipoEnum = Tipo.valueOf(tipoStr.toUpperCase());
        Sexo sexoEnum = Sexo.valueOf(sexoStr.toUpperCase());
        double idade = Double.parseDouble(idadeStr.replace(",","."));
        double peso = Double.parseDouble(pesoStr.replace(",","."));

        Pet pet = new Pet(nomeSobrenome, tipoEnum, sexoEnum, endereco, idade, peso, raca);
        ArquivoPet.salvarPetArquivoTXT(pet);
        System.out.println();
        System.out.println("****PET CADASTRADO*****");
    }
}
