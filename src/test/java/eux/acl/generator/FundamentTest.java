package eux.acl.generator;

import eux.acl.generator.facade.FundamentFacade;
import org.junit.jupiter.api.Test;

import java.util.List;

import static eux.acl.generator.modell.SedTyper.*;
import static eux.acl.generator.service.FilService.*;

public class FundamentTest {


    /**
     * Sammenlikner CDM og ACL versjoner
     * <p>
     * v4.2 med v4.3. Det som er likt skrives til ei fil,
     * det som kun fins i v4.2 skrives til ei anna fil, og det som kun fins i v4.3 til ei tredje fil.
     * Filene som inneholder justeringer som er manuellt lagt inn ( ..adjustments.json) blir ikke overskrevet.
     * De genereres kun når disse filene ikke finnes fra tidligere.
     */

    /**
     * Trekker ut enum-er fra cdm-meta-filer og lager ei fil pr unike enum med info som seinere kan brukes til å
     * generere kodeverksfiler.
     */

    @Test
    public void lager_EttEuSedFundament_v44() {
        FundamentFacade aclMalFacade = new FundamentFacade();
        aclMalFacade.trekkUtEuSedFundamentOgSkrivTilFil(List.of("U004"), V44);
    }

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
