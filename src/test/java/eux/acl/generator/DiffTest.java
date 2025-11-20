package eux.acl.generator;

import eux.acl.generator.facade.DiffFacade;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static eux.acl.generator.modell.SedTyper.*;
import static eux.acl.generator.service.FilService.*;

public class DiffTest {

    /**
     * Sjekker at motoren for å generere SED-maler fra CDMen er korrekt ved å
     * sammenlikne genererte CDM-SEDer med eksisterende ACL-SEDer i samme versjon.
     */

    @Test
    public void sammenliknerToACL_versjoner() {
        DiffFacade diffFacade = new DiffFacade();
        diffFacade.sammenliknerToAclVersjonerOgSkriverDiffTilFil(SEDTYPER_A, V43, V44);
        diffFacade.sammenliknerToAclVersjonerOgSkriverDiffTilFil(SEDTYPER_DA, V43, V44);
        diffFacade.sammenliknerToAclVersjonerOgSkriverDiffTilFil(SEDTYPER_F, V43, V44);
        diffFacade.sammenliknerToAclVersjonerOgSkriverDiffTilFil(SEDTYPER_H, V43, V44);
        diffFacade.sammenliknerToAclVersjonerOgSkriverDiffTilFil(SEDTYPER_M, V43, V44);
        diffFacade.sammenliknerToAclVersjonerOgSkriverDiffTilFil(SEDTYPER_P, V43, V44);
        diffFacade.sammenliknerToAclVersjonerOgSkriverDiffTilFil(SEDTYPER_R, V43, V44);
        diffFacade.sammenliknerToAclVersjonerOgSkriverDiffTilFil(SEDTYPER_S, V43, V44);
        diffFacade.sammenliknerToAclVersjonerOgSkriverDiffTilFil(SEDTYPER_U, V43, V44);
        diffFacade.sammenliknerToAclVersjonerOgSkriverDiffTilFil(SEDTYPER_X, V43, V44);
    }

    @Test
    @Disabled("Kjøring feiler på Github - trolig fordi input-filene til testen ikke har blitt generert")
    public void sammenlikner_CDM_euSed_ACL_euSed_forrigeVersjon() {
        DiffFacade diffFacade = new DiffFacade();
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_A, V43);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_DA, V43);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_F, V43);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_H, V43);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_M, V43);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_P, V43);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_R, V43);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_S, V43);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_U, V43);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_X, V43);
    }

    /**
     * Sammenlikner SEDer generert i to ulike CDM versjoner.
     * Det som er uendret, nytt og utgått skrives til hver sin fil.
     *
     */
    @Test
    @Disabled("På github er ikke input-filene generert og da feiler testen")
    public void sammenlikner_euSedCdm_iUlikeVersjoner_skriverDiffTilFiler() {
        DiffFacade diffFacade = new DiffFacade();
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_A, V43, V44);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_DA, V43, V44);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_F, V43, V44);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_H, V43, V44);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_M, V43, V44);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_P, V43, V44);
//        diffFacade.sammenlikneCdmSedVersjonerOgSkrivDiffTilFiler(SEDTYPER_P3000, V43, V44);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_R, V43, V44);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_S, V43, V44);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_U, V43, V44);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_X, V43, V44);
    }

    @Test
    public void sammenlikner_nyCdmVersjon_med_gammelAclVersjon_skriverNyAclVersjonTilTilFiler() {
        DiffFacade diffFacade = new DiffFacade();
        diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_A, V43, V44);
        diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_DA, V43, V44);
        diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_F, V43, V44);
        diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_H, V43, V44);
        diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_M, V43, V44);
        diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_P, V43, V44);
        diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_R, V43, V44);
        diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_S, V43, V44);
        diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_U, V43, V44);
        diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_X, V43, V44);
    }

    // sammenlikne enum innhold i ulike versjoner
    // kodeverk - stier der kodeverket er endret
    @Test
    public void sammenlikner_kodeverkCdm_iUlikeVersjoner_skriverDiffTilFiler() {
        DiffFacade diffFacade = new DiffFacade();
        diffFacade.sammenliknerEnumeneForToCdmVersjonerOgKopiererEnumFundamentFilene(V43, V44);
    }

}
