package com.github.leocarmona.unisinos_dictionary_avl_tree.tradutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Classe responsável por testar as classes {@link Dicionario} e {@link Dicionario.Utils}.
 * 
 * @author leonardo.carmona
 *
 */
public class DicionarioTest {

    // ====================================================================================================
    // Validações do método Dicionario.adicionarDefinicoes
    // ====================================================================================================

    /**
     * Método responsável por validar a adição de difinições.
     */
    @Test
    public void adicionarDefinicoesTest() {
        Dicionario dicionario = new Dicionario();
        boolean adicionou;

        dicionario.setPalavra("be");

        // Testa a adição do primeiro valor e seu retorno

        adicionou = dicionario.adicionarDefinicoes(new ArrayList<>(Arrays.asList("ser", "SeR", "   SER   ")));
        Assert.assertEquals(true, adicionou);
        Assert.assertEquals(Arrays.asList("ser"), dicionario.getDefinicoes());

        // Testa a adição do segundo valor e seu retorno

        adicionou = dicionario.adicionarDefinicoes(new ArrayList<>(Arrays.asList("estar", "EsTaR", "   ESTAR   ")));
        Assert.assertEquals(true, adicionou);
        Assert.assertEquals(Arrays.asList("ser", "estar"), dicionario.getDefinicoes());

        // Testa a não adição do primeiro valor e seu retorno

        adicionou = dicionario.adicionarDefinicoes(new ArrayList<>(Arrays.asList("ser", "SeR", "   SER   ", "estar", "EsTaR", "   ESTAR   ")));
        Assert.assertEquals(false, adicionou);
        Assert.assertEquals(Arrays.asList("ser", "estar"), dicionario.getDefinicoes());
    }

    // ====================================================================================================
    // Validações do método Dicionario.adicionarDefinicao
    // ====================================================================================================

    /**
     * Método responsável por validar a adição de difinição.
     */
    @Test
    public void adicionarDefinicaoTest() {
        Dicionario dicionario = new Dicionario();

        dicionario.setPalavra("be");
        dicionario.adicionarDefinicao("ser");
        dicionario.adicionarDefinicao("SER");
        dicionario.adicionarDefinicao("estar");
        dicionario.adicionarDefinicao("ESTAR");
        dicionario.adicionarDefinicao("haver");
        dicionario.adicionarDefinicao("HAVER");

        Assert.assertEquals(Arrays.asList("ser", "estar", "haver"), dicionario.getDefinicoes());
    }

    // ====================================================================================================
    // Validações do método Dicionario.removerDefinicao
    // ====================================================================================================

    /**
     * Método responsável por validar a remoção de difinição.
     */
    @Test
    public void removerDefinicaoTest() {
        Dicionario dicionario = new Dicionario();
        boolean adicionou, removeu;

        dicionario.setPalavra("be");

        // Testa a exclusão com letras minúsculas

        adicionou = dicionario.adicionarDefinicao("ser");
        Assert.assertEquals(true, adicionou);
        Assert.assertEquals(new ArrayList<>(Arrays.asList("ser")), dicionario.getDefinicoes());

        removeu = dicionario.removerDefinicao("ser");
        Assert.assertEquals(true, removeu);
        Assert.assertEquals(Arrays.asList(), dicionario.getDefinicoes());

        // Testa a exclusão com letras maiúsculas

        adicionou = dicionario.adicionarDefinicao("ser");
        Assert.assertEquals(true, adicionou);
        Assert.assertEquals(new ArrayList<>(Arrays.asList("ser")), dicionario.getDefinicoes());

        removeu = dicionario.removerDefinicao("SER");
        Assert.assertEquals(true, removeu);
        Assert.assertEquals(Arrays.asList(), dicionario.getDefinicoes());

        // Testa a não exclusão

        removeu = dicionario.removerDefinicao("ser");
        Assert.assertEquals(false, removeu);
        Assert.assertEquals(new ArrayList<>(Arrays.asList()), dicionario.getDefinicoes());
    }

    // ====================================================================================================
    // Validações do método Dicionario.possuiDefinicoes
    // ====================================================================================================

    /**
     * Método responsável por validar a indicação se possui definições.
     */
    @Test
    public void possuiDefinicoesTest() {
        Dicionario dicionario = new Dicionario();

        Assert.assertEquals(false, dicionario.possuiDefinicoes());
        
        dicionario.adicionarDefinicao("ser");
        Assert.assertEquals(true, dicionario.possuiDefinicoes());
    }

    // ====================================================================================================
    // Validações do método Dicionario.Utils.ajustarDefinicoes
    // ====================================================================================================

    /**
     * Método responsável por validar o ajuste de definições.
     */
    @Test
    public void ajustarDefinicoesTest() {
        List<String> definicoes = new ArrayList<>(Arrays.asList(" ser  ", null, "   ", "", "  SeR  ", "EsTaR ", " eStAr", "  PaLaVra   CoMpOsTa  "));
        List<String> definicoesEsperadas = Arrays.asList("ser", "estar", "palavra composta");

        Dicionario.Utils.ajustarDefinicoes(definicoes);

        Assert.assertEquals(definicoesEsperadas, definicoes);
    }

}
