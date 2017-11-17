package com.github.leocarmona.unisinos_dictionary_avl_tree.arvore;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.github.leocarmona.unisinos_dictionary_avl_tree.tradutor.Dicionario;

/**
 * Classe responsável por testar a classe {@link ArvoreAvl}.
 * 
 * @author leonardo.carmona
 *
 */
public class ArvoreAvlTest {

    // ====================================================================================================
    // Validações dos métodos ArvoreAvl.adicionar e ArvoreAvl.pesquisarDicionarioPelaPalavra
    // ====================================================================================================

    /**
     * Método responsável por validar a adição e pesquisa de dicionário pela palavra.
     */
    @Test
    public void adicionar_pesquisarDicionarioPelaPalavra_Test() {
        ArvoreAvl avl = new ArvoreAvl();
        Dicionario[] dicionarios = new Dicionario[3];
        Dicionario dicionarioResgatado;

        dicionarios[0] = new Dicionario("be", new ArrayList<>(Arrays.asList("ser")));
        dicionarios[1] = new Dicionario("i", new ArrayList<>(Arrays.asList("eu")));
        dicionarios[2] = new Dicionario("am", new ArrayList<>(Arrays.asList("sou")));

        for (int i = 0; i < dicionarios.length; i++) {
            avl.adicionar(dicionarios[i]);

            dicionarioResgatado = avl.pesquisarDicionarioPelaPalavra(dicionarios[i].getPalavra());

            Assert.assertEquals(dicionarios[i], dicionarioResgatado);
            Assert.assertEquals(dicionarios[i].getPalavra(), dicionarioResgatado.getPalavra());
            Assert.assertEquals(dicionarios[i].getDefinicoes(), dicionarioResgatado.getDefinicoes());
        }
    }

    // ====================================================================================================
    // Validações dos métodos ArvoreAvl.getConteudo, ArvoreAvl.balancear, ArvoreAvl.rotacionar e ArvoreAvl.percorrerEmOrdem
    // ====================================================================================================

    /**
     * Método responsável por validar o resgate de conteúdo, balancear, rotacionar e percorrer em ordem.
     */
    @Test
    public void getConteudo_balancear_rotacionar_percorrerEmOrdemTest() {
        ArvoreAvl avl = new ArvoreAvl();
        Dicionario[] dicionarios = new Dicionario[3];

        dicionarios[0] = new Dicionario("be", new ArrayList<>(Arrays.asList("ser")));
        dicionarios[1] = new Dicionario("i", new ArrayList<>(Arrays.asList("eu")));
        dicionarios[2] = new Dicionario("am", new ArrayList<>(Arrays.asList("sou")));

        for (int i = 0; i < dicionarios.length; i++) {
            avl.adicionar(dicionarios[i]);
        }

        Assert.assertEquals(dicionarios[0].getPai(), null);
        Assert.assertEquals(dicionarios[0].getEsquerda(), dicionarios[1]);
        Assert.assertEquals(dicionarios[0].getDireita(), dicionarios[2]);
        
        Assert.assertEquals(dicionarios[1].getPai(), dicionarios[0]);
        Assert.assertEquals(dicionarios[1].getEsquerda(), null);
        Assert.assertEquals(dicionarios[1].getDireita(), null);
        
        Assert.assertEquals(dicionarios[2].getPai(), dicionarios[0]);
        Assert.assertEquals(dicionarios[2].getEsquerda(), null);
        Assert.assertEquals(dicionarios[2].getDireita(), null);

        Assert.assertEquals(Arrays.asList(dicionarios[1], dicionarios[0], dicionarios[2]), avl.getConteudo());
    }

}
