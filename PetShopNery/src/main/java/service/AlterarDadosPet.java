package service;

import entities.Pet;
import java.nio.file.*;
import java.util.List;
import java.util.Scanner;

public class AlterarDadosPet {

    private static final Path PATH_DIR = Path.of("C:\\Users\\bruni\\OneDrive\\Documentos\\CursosProgramacaoAtual\\DesafioPetShop\\PetsCadastrados");

    public static void alterarDadosPet(Scanner sc) {

        List<Pet> resultado = BuscarPet.buscarPet(sc);

        if (resultado.isEmpty()) {
            System.out.println("Nenhum Pet foi encontrado.");
            return;
        }

        System.out.print("Escolha o número do Pet que você deseja alterar: ");
        int numAlterar;
        try {
            numAlterar = Integer.parseInt(sc.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Entrada inválida. Operação cancelada.");
            return;
        }

        if (numAlterar < 1 || numAlterar > resultado.size()) {
            System.out.println("Opção inválida.");
            return;
        }

        Pet petParaAlterar = resultado.get(numAlterar - 1);


        System.out.println("O Pet que você deseja alterar é " + petParaAlterar.getNomeSobrenome() + " (s/n)?");
        String resp = sc.nextLine();
        if (!resp.equalsIgnoreCase("s")) {
            System.out.println("Opção Inválida.");
            return;
        }

        System.out.println("\nO que deseja alterar?");
        System.out.println("1 - Nome");
        System.out.println("2 - Idade");
        System.out.println("3 - Peso (kg)");
        System.out.print("R: ");
        String opcaoStr = sc.nextLine().trim();
        int opcao;
        try {
            opcao = Integer.parseInt(opcaoStr);
        } catch (Exception e) {
            System.out.println("Opção inválida.");
            return;
        }

        switch (opcao) {
            case 1:
                System.out.print("Informe o novo Nome e Sobrenome: ");
                String novoNome = sc.nextLine();
                if (novoNome.isEmpty()) {
                    System.out.println("Nome vazio. Nenhuma alteração realizada.");
                    return;
                }
                petParaAlterar.setNomeSobrenome(novoNome);
                break;

            case 2:
                System.out.print("Informe a nova idade: ");
                String idadeStr = sc.nextLine();
                try {
                    double novaIdade = Double.parseDouble(idadeStr.replace(",", "."));
                    petParaAlterar.setIdade(novaIdade);
                } catch (Exception e) {
                    System.out.println("Idade inválida. Nenhuma alteração realizada.");
                    return;
                }
                break;

            case 3:
                System.out.print("Informe o novo peso (Kg): ");
                String pesoStr = sc.nextLine();
                try {
                    double novoPeso = Double.parseDouble(pesoStr.replace(",", "."));
                    petParaAlterar.setPeso(novoPeso);
                } catch (Exception e) {
                    System.out.println("Peso inválido. Nenhuma alteração realizada.");
                    return;
                }
                break;

            default:
                System.out.println("Opção inválida.");
                return;
        }

    }


}

