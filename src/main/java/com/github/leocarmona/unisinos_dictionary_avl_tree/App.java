package com.github.leocarmona.unisinos_dictionary_avl_tree;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.github.leocarmona.unisinos_dictionary_avl_tree.tradutor.Dicionario;
import com.github.leocarmona.unisinos_dictionary_avl_tree.tradutor.Tradutor;

/**
 * Classe responsável pela inicialização externa do tradutor.
 * 
 * @author leonardo.carmona
 *
 */
public class App {

    private static final String   DIVISOR           = "==================================================";
    private static final Tradutor TRADUTOR          = new Tradutor();
    private static final File     DICIONARIO_PADRAO = new File("dicionario.dat");
    private static final Scanner  TECLADO           = new Scanner(System.in);

    /**
     * Ponto de entrada inicial do aplicativo.
     * 
     * @param args
     *            Argumentos para o aplicativo.
     */
    public static void main(String[] args) {
        App.carregarDicionarioPadrao();

        while (true) {
            App.exibirMenu();
        }
    }

    /**
     * Método responsável por carregar o dicionário padrão.
     */
    private static void carregarDicionarioPadrao() {
        System.out.println("Carregando dicionário de: " + DICIONARIO_PADRAO.getAbsolutePath());

        if (DICIONARIO_PADRAO.exists()) {
            TRADUTOR.carregaDicionario(DICIONARIO_PADRAO.getAbsolutePath());
        } else {
            System.out.println("O dicionário não foi encontrado.");
            System.out.println("Criando um novo dicionário.");

            TRADUTOR.insereTraducao("be", new ArrayList<>(Arrays.asList("ser", "estar", "haver", "ficar", "existir")));
            TRADUTOR.insereTraducao("i", new ArrayList<>(Arrays.asList("eu")));
            TRADUTOR.insereTraducao("am", new ArrayList<>(Arrays.asList("sou")));

            salvarDicionario();
        }

        System.out.println("");
    }

    /**
     * Método responsável por exibir o menu.
     */
    private static void exibirMenu() {
        System.out.println(DIVISOR);
        System.out.println("Insira uma das opções:\n");
        System.out.println("1) Traduzir palavra");
        System.out.println("2) Inserir definição");
        System.out.println("3) Salvar dicionário");
        System.out.println("4) Sair");

        int resposta = 0;

        while (true) {
            boolean erro = false;

            System.out.print("\nOpção: ");

            try {
                resposta = TECLADO.nextInt();
            } catch (Exception e) {
                TECLADO.next();
                erro = true;
            }

            if (erro || resposta < 1 || resposta > 4) {
                continue;
            }

            break;
        }

        switch (resposta) {
            case 1:
                traduzirPalavra();
                break;

            case 2:
                inserirDefinicao();
                break;

            case 3:
                salvarDicionario();
                break;
        }

        System.out.println("\n" + DIVISOR + "\n");

        if (resposta == 4) {
            System.exit(0);
        }
    }

    /**
     * Método responsável por traduzir uma palavra.
     */
    private static void traduzirPalavra() {
        System.out.print("\nPalavra a ser traduzida: ");

        String palavra = TECLADO.next().trim();
        List<String> definicoes = TRADUTOR.traduzPalavra(palavra);

        if (!definicoes.isEmpty()) {
            System.out.println("\nTraduções existentes: " + definicoes);
        } else {
            System.out.println("\nNão há traduções para a palavra '" + palavra + "'.");
            System.out.print("Deseja adicionar novas definições (s/n)? ");

            while (true) {
                String opc = TECLADO.next().toLowerCase();

                if ("s".equals(opc)) {
                    List<String> novasDefinicoes = App.perguntarDefinicoes();

                    TRADUTOR.insereTraducao(palavra, novasDefinicoes);

                    break;
                }

                if ("n".equals(opc)) {
                    break;
                }
            }
        }
    }

    /**
     * Método responsável por inserir novas definições.
     */
    private static void inserirDefinicao() {
        System.out.print("\nPalavra a ser traduzida: ");

        String palavra = TECLADO.next().trim();
        List<String> definicoes = App.perguntarDefinicoes();

        TRADUTOR.insereTraducao(palavra, definicoes);
    }

    /**
     * Método responsável por perguntar as definições.
     * 
     * @return As definições.
     */
    private static List<String> perguntarDefinicoes() {
        List<String> definicoes = new ArrayList<>();

        do {
            System.out.print("Definições separadas por vírgula: ");
            definicoes = new ArrayList<>(Arrays.asList(TECLADO.next().split(",")));

            Dicionario.Utils.ajustarDefinicoes(definicoes);
        } while (definicoes.isEmpty());

        return definicoes;
    }

    /**
     * Método responsável por salvar o dicionário.
     */
    private static void salvarDicionario() {
        TRADUTOR.salvaDicionario(DICIONARIO_PADRAO.getAbsolutePath());

        System.out.println("\nDicionário salvo com sucesso em " + DICIONARIO_PADRAO.getAbsolutePath() + "");
    }

}
