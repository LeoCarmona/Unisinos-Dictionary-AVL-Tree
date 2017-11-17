package com.github.leocarmona.unisinos_dictionary_avl_tree.tradutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.github.leocarmona.unisinos_dictionary_avl_tree.tradutor.Dicionario;
import com.github.leocarmona.unisinos_dictionary_avl_tree.tradutor.Tradutor;

/**
 * Classe responsável por testar as classes {@link Tradutor} e {@link Tradutor.Utils}.
 * 
 * @author leonardo.carmona
 *
 */
public class TradutorTest {

    private static final String       LINHA              = "<palavra_ingles>be<traducao1>ser<traducao2>estar<traducao3>haver<traducao4>ficar<traducao5>existir";

    private static final String       PALAVRA_INGLES     = "be";
    private static final String       TRADUCAO_1         = "ser";
    private static final String       TRADUCAO_2         = "estar";
    private static final String       TRADUCAO_3         = "haver";
    private static final String       TRADUCAO_4         = "ficar";
    private static final String       TRADUCAO_5         = "existir";
    private static final List<String> DEFINICOES         = Collections.unmodifiableList(Arrays.asList(TRADUCAO_1, TRADUCAO_2, TRADUCAO_3, TRADUCAO_4, TRADUCAO_5));

    private static final String       XML_PALAVRA_INGLES = "<palavra_ingles>";
    private static final String       XML_TRADUCAO_1     = "<traducao1>";
    private static final String       XML_TRADUCAO_2     = "<traducao2>";
    private static final String       XML_TRADUCAO_3     = "<traducao3>";
    private static final String       XML_TRADUCAO_4     = "<traducao4>";
    private static final String       XML_TRADUCAO_5     = "<traducao5>";
    private static final String       XML_TRADUCAO_6     = "<traducao6>";
    private static final String       XML_TRADUCAO_7     = "<traducao7>";

    // ====================================================================================================
    // Validações dos métodos Tradutor.insereTraducao e Tradutor.traduzPalavra
    // ====================================================================================================

    /**
     * Valida a inserção e tradução das palavras.
     */
    @Test
    public void lerLinhaDoArquivo_insereTraducaoTraduzPalavraTest() {
        Tradutor tradutor = new Tradutor();
        
        Assert.assertEquals(new ArrayList<>(), tradutor.traduzPalavra("nao encontrado"));
        
        tradutor.insereTraducao("be", new ArrayList<>(Arrays.asList("ser", "estar")));
        Assert.assertEquals(new ArrayList<>(Arrays.asList("ser", "estar")), tradutor.traduzPalavra("be"));
        
        tradutor.insereTraducao("be", new ArrayList<>(Arrays.asList("haver")));
        Assert.assertEquals(new ArrayList<>(Arrays.asList("ser", "estar", "haver")), tradutor.traduzPalavra("be"));
    }

    // ====================================================================================================
    // Validações do método Tradutor.lerLinhaDoArquivo
    // ====================================================================================================

    /**
     * Valida o resgate da linha em formato de {@link Dicionario}.
     */
    @Test
    public void lerLinhaDoArquivo_linhaValidaTest() {
        Tradutor tradutor = new Tradutor();
        Dicionario dicionario = tradutor.lerLinhaDoArquivo(LINHA);

        Assert.assertEquals(PALAVRA_INGLES, dicionario.getPalavra());
        Assert.assertEquals(DEFINICOES, dicionario.getDefinicoes());
    }

    // ====================================================================================================
    // Validações do método Tradutor.Utils.getValorEntre
    // ====================================================================================================

    /**
     * Valida o resgate das palavras entre as outras.
     */
    @Test
    public void getPalavraEntre_linhaValidaTest() {
        String palavraEmIngles = Tradutor.Utils.getPalavraEntre(LINHA, XML_PALAVRA_INGLES, XML_TRADUCAO_1);
        Assert.assertEquals(PALAVRA_INGLES, palavraEmIngles);

        String traducao1 = Tradutor.Utils.getPalavraEntre(LINHA, XML_TRADUCAO_1, XML_TRADUCAO_2);
        Assert.assertEquals(TRADUCAO_1, traducao1);

        String traducao2 = Tradutor.Utils.getPalavraEntre(LINHA, XML_TRADUCAO_2, XML_TRADUCAO_3);
        Assert.assertEquals(TRADUCAO_2, traducao2);

        String traducao3 = Tradutor.Utils.getPalavraEntre(LINHA, XML_TRADUCAO_3, XML_TRADUCAO_4);
        Assert.assertEquals(TRADUCAO_3, traducao3);

        String traducao4 = Tradutor.Utils.getPalavraEntre(LINHA, XML_TRADUCAO_4, XML_TRADUCAO_5);
        Assert.assertEquals(TRADUCAO_4, traducao4);

        String traducao5 = Tradutor.Utils.getPalavraEntre(LINHA, XML_TRADUCAO_5, XML_TRADUCAO_6);
        Assert.assertEquals(TRADUCAO_5, traducao5);

        String naoEncontrado = Tradutor.Utils.getPalavraEntre(LINHA, XML_TRADUCAO_6, XML_TRADUCAO_7);
        Assert.assertEquals(null, naoEncontrado);
    }

}
