package entities;

import enums.Sexo;
import enums.Tipo;

public class Pet {

    private String nomeSobrenome;
    private Tipo tipo;
    private Sexo sexo;
    private Endereco endereco;
    private Double idade;
    private Double peso;
    private String raca;

    public Pet(String nomeSobrenome, Tipo tipo, Sexo sexo, Endereco endereco, Double idade, Double peso, String raca) {
        this.nomeSobrenome = nomeSobrenome;
        this.tipo = tipo;
        this.sexo = sexo;
        this.endereco = endereco;
        this.idade = idade;
        this.peso = peso;
        this.raca = raca;
    }

    public String getNomeSobrenome() {
        return nomeSobrenome;
    }

    public void setNomeSobrenome(String nomeSobrenome) {
        this.nomeSobrenome = nomeSobrenome;
    }

    public Double getIdade() {
        return idade;
    }

    public void setIdade(Double idade) {
        this.idade = idade;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public Endereco getEndereco() {
        return endereco;
    }
}