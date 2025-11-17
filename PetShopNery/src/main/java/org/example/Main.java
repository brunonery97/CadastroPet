package org.example;

import entities.Pet;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {


        List<Pet> petList = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        int opcao = 0;

        do {
            System.out.println("--------------MENU PETSHOP--------------");
            System.out.println("1 - Cadastrar um novo Pet");
            System.out.println("2 - Alterar dados do Pet");
            System.out.println("3 - Deletar um pet cadastrado");
            System.out.println("4 - Listar todos os Pets");
            System.out.println("5 - Listar pets por algum critério (idade, nome, raça)");
            System.out.println("6 - Sair");

            try {
                System.out.print("Esolha o número do serviço: ");
                opcao = sc.nextInt();
                sc.nextLine();
                System.out.println();
            } catch (Exception e) {
                sc.nextLine();
            }


            switch (opcao){
                case 1:
                    System.out.println("-----CADASTAR PET-----");
                    // CHAMAR O SERVIÇO DE CADASTRAR PET
                    break;

                case 2:
                    System.out.println("-----ALTERAR DADOS DO PET-----");
                    // CHAMAR O SERVIÇO DE ALTERAR O CADASTRO
                    break;

                case 3:
                    System.out.println("-----DELETAR PET-----");
                    //CHAMAR O SERVIÇO DE DELETAR
                    break;

                case 4:
                    System.out.println("-----Lista de Pets-----");
                    //CHAMAR O SERVIÇO DE LISTAR TODOS OS PETS
                    break;

                case 5:
                    System.out.println("-----Lista Pets Por Critérios-----");
                    //CHAMAR O SERVIÇO DE LISTAR POR CRITERIO
                    break;

                case 6:
                    System.out.println("*****SISTEMA FINALIZADO*****");
                    break;

                default:
                    System.out.println("Opção Inválida, informe um número válido.");
            }
            System.out.println();
        } while (opcao != 6);


        sc.close();
    }
}