package eux.acl.generator;

import com.github.wnameless.json.flattener.JsonFlattener;
import eux.acl.generator.facade.FundamentFacade;
import eux.acl.generator.old.UttrekkAvKodeverkFraCdmService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import org.junit.jupiter.api.Test;


import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static eux.acl.generator.service.FilService.*;

public class FundamentTest {


    private static final String MAPPE_CDMv43 = "src/test/resources/cdm-v43-meta";
    private static final String MAPPE_ACLv42 = "src/main/resources/sedtemplates/v42";
    public static final String MAPPE_ACLv43_MELLOMBELS = "target/v43Raw";
    public static String JUSTERINGER_FILNAVN_ENDE = "-v43-adjustments.json";
    public static String UENDRET_FILNAVN_ENDE = "-v43-flat.json";

    private static final String UTF_8 = StandardCharsets.UTF_8.name();
    private UttrekkAvKodeverkFraCdmService cdmReader = new UttrekkAvKodeverkFraCdmService();

    /**
     * Sammenlikner CDM og ACL versjoner
     * <p>
     * v4.2 med v4.3. Det som er likt skrives til ei fil,
     * det som kun fins i v4.2 skrives til ei anna fil, og det som kun fins i v4.3 til ei tredje fil.
     * Filene som inneholder justeringer som er manuellt lagt inn ( ..adjustments.json) blir ikke overskrevet.
     * De genereres kun når disse filene ikke finnes fra tidligere.
     */

    public static List<String> SEDTYPER_F_H_S_U() {
        List<String> sedtyper = new ArrayList<>();
        sedtyper.addAll(SEDTYPER_U);
        sedtyper.addAll(SEDTYPER_F);
        sedtyper.addAll(SEDTYPER_H);
        sedtyper.addAll(SEDTYPER_S);
        return sedtyper;
    }

    /**
     * Trekker ut enum-er fra cdm-meta-filer og lager ei fil pr unike enum med info som seinere kan brukes til å
     * generere kodeverksfiler.
     */


    @Test
    public void lager_euSedFundament_v44() {
        FundamentFacade aclMalFacade = new FundamentFacade();
        aclMalFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_A, V44);
        aclMalFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_DA, V44);
        aclMalFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_F, V44);
        aclMalFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_H, V44);
        aclMalFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_M, V44);
        aclMalFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_P, V44);
        aclMalFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_R, V44);
        aclMalFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_S, V44);
        aclMalFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_U, V44);
        aclMalFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_X, V44);
    }

    @Test
    public void lager_euSedFundament_v43() {
        // NB! Mangler logisk modell for 4.3 - bruker v4.4 !!
        FundamentFacade aclMalFacade = new FundamentFacade();
        aclMalFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_A, V43);
        aclMalFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_DA, V43);
        aclMalFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_F, V43);
        aclMalFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_H, V43);
        aclMalFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_M, V43);
        aclMalFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_P, V43);
        aclMalFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_R, V43);
        aclMalFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_S, V43);
        aclMalFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_U, V43);
        aclMalFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_X, V43);
    }

    @Test
    public void lager_kodeverkFundament_v44() {
        FundamentFacade aclMalFacade = new FundamentFacade();
        aclMalFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_M, V44);
        aclMalFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_A, V44);
        aclMalFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_DA, V44);
        aclMalFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_F, V44);
        aclMalFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_H, V44);
        aclMalFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_P, V44);
        aclMalFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_R, V44);
        aclMalFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_S, V44);
        aclMalFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_U, V44);
        aclMalFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_X, V44);
    }
    @Test
    public void lager_kodeverkFundament_v43() {
        FundamentFacade aclMalFacade = new FundamentFacade();
        aclMalFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_M, V43);
        aclMalFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_A, V43);
        aclMalFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_DA, V43);
        aclMalFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_F, V43);
        aclMalFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_H, V43);
        aclMalFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_P, V43);
        aclMalFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_R, V43);
        aclMalFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_S, V43);
        aclMalFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_U, V43);
        aclMalFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_X, V43);
    }

}
