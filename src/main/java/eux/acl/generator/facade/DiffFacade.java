package eux.acl.generator.facade;

import eux.acl.generator.modell.*;
import eux.acl.generator.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

@Slf4j
public class DiffFacade {
    private final FilService filService;
    private final KodeverkService kodeverkService;
    private final EuSedService euSedService;
    private final AclService aclService;


    public DiffFacade() {
        this.filService = new FilService();
        this.kodeverkService = new KodeverkService();
        this.euSedService = new EuSedService();
        this.aclService = new AclService();
    }

    public void sammenliknerCdmEuSedMedAclEuSed(List<String> sedTyper, String cdmVersjon) {
        sedTyper.forEach(sedType -> {
            var cdmSedListe = euSedService.lagCdmEuSedFraFundament(sedType, cdmVersjon);
            var aclSedListe = aclService.lagEuSedFraAclMal(sedType, cdmVersjon);
            for (int nr = 0; nr < aclSedListe.size(); nr++) {
                if (!aclSedListe.get(nr).equals(cdmSedListe.get(nr))) {
                    log.error(sedType + " mismatch:\nAcl={}\nCdm={}", aclSedListe.get(nr), cdmSedListe.get(nr));
                    throw new RuntimeException("Mismatch mellom CDM- og ACL-Sed - se loggen");
                }
            }
            if (cdmSedListe.size() != aclSedListe.size()) {
                log.error(sedType + " mismatch. Ulikt linjeantall. Acl={}, Cdm={}"
                        , aclSedListe.size(), cdmSedListe.size());
                throw new RuntimeException("Mismatch mellom CDM- og ACL-Sed - se loggen");
            } else {
                log.info("EuSed fra CDM og ACL er identiske for {}", sedType);
            }
        });
    }

    public void sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(List<String> sedTyper, String cdmForrigeVersjon, String cdmNyVersjon) {
        Map<String, List<String>> sektorVsNyeStier = new HashMap<>();
        sedTyper.forEach(sedType -> {
            EuSedCdmVersjonsdiff diff = euSedService.diffMellomUlikeCdmSedVersjoner(sedType, cdmForrigeVersjon, cdmNyVersjon);
            filService.skrivDiffTilFil(diff.getUendredeStierListe(), sedType, cdmNyVersjon, FilService.FIL_MED_UENDREDE_STIER);
            filService.skrivDiffTilFil(diff.getNyeStierListe(), sedType, cdmNyVersjon, FilService.FIL_MED_ENDRINGER_STIER);
            filService.skrivDiffTilFil(diff.getUtgaatteStierListe(), sedType, cdmNyVersjon, FilService.FIL_MED_UTGAATTE_STIER);
            euSedService.leggTilNyeDiffer(sedType, diff, sektorVsNyeStier);
        });
        euSedService.skrivDiffOppsummeringTilEiFil(sektorVsNyeStier, cdmNyVersjon);
    }

    public void sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(List<String> sedTyper,
                                                                                  String aclVersjon,
                                                                                  String nyCdmVersjon) {
        Map<String, List<String>> sektorVsNyeStier = new HashMap<>();
        sedTyper.forEach(sedType -> {
            TreeMap<String, EuSedFundament> euSedStiVsFundament = filService.lesEuSedFundamentFil(sedType, nyCdmVersjon);
            List<String> cdmEuSedStiListe = euSedService.lagCdmEuSedFraFundament(sedType, nyCdmVersjon);
            Map<String, String> euSedStiVsAclSedSti = aclService.hentAclMal(sedType, aclVersjon);
            List<String> endredeEnumer = filService.lesEndredeOgNyeEnumNavn(nyCdmVersjon);
            if (euSedStiVsFundament == null || cdmEuSedStiListe == null || euSedStiVsAclSedSti == null) {
                return;
            }
            List<String> euSedStiVsAclSedStiEndretEnum = new ArrayList<>();
            List<String> euSedStiVsAclSedStiNyMal = new ArrayList<>();
            List<String> euSedStiVsAclSedStiNye = new ArrayList<>();
            List<String> euSedStiVsAclSedUendret = new ArrayList<>();
            List<String> aclMalBasisPrSektor = new ArrayList<>();
            euSedStiVsFundament.forEach((euSedSti, f) -> {
                if (euSedStiVsAclSedSti.containsKey(euSedSti)) {
                    if (endredeEnumer.contains(euSedStiVsFundament.get(euSedSti).getEnumNavn())) {
                        // kun enum er endret
                        String euSedStiVsAclSedStiVsEnum = euSedSti + ": "
                                + euSedStiVsAclSedSti.get(euSedSti) + ": "
                                + euSedStiVsFundament.get(euSedSti).getEnumNavn();
                        euSedStiVsAclSedStiNyMal.add(euSedStiVsAclSedStiVsEnum);
                        euSedStiVsAclSedStiEndretEnum.add(euSedStiVsAclSedStiVsEnum);
                        euSedStiVsAclSedStiNye.add(euSedStiVsAclSedStiVsEnum);
                        aclMalBasisPrSektor.add(euSedStiVsAclSedStiVsEnum.replaceFirst("\\.", ":"));
                    } else {
                        // som før
                        euSedStiVsAclSedStiNyMal.add(euSedSti + ": " + euSedStiVsAclSedSti.get(euSedSti));
                        euSedStiVsAclSedUendret.add(euSedSti + ": " + euSedStiVsAclSedSti.get(euSedSti));
                        aclMalBasisPrSektor.add(euSedSti.replaceFirst("\\.", ":") + ": " + euSedStiVsAclSedSti.get(euSedSti)  );
                    }
                } else {
                    if (endredeEnumer.contains(euSedStiVsFundament.get(euSedSti).getEnumNavn())) {
                        // ny og med ny enum
                        String euSedStiVsEnum = euSedSti + ": : " + euSedStiVsFundament.get(euSedSti).getEnumNavn();
                        euSedStiVsAclSedStiNyMal.add(euSedStiVsEnum);
                        euSedStiVsAclSedStiEndretEnum.add(euSedStiVsEnum);
                        euSedStiVsAclSedStiNye.add(euSedStiVsEnum);
                        aclMalBasisPrSektor.add(euSedStiVsEnum.replaceFirst("\\.", ":"));
                    } else {
                        // ny
                        if ("enum".equals(euSedStiVsFundament.get(euSedSti).getVerdiType())) {
                            String euSedStiVsEnum = euSedSti + ": : " + euSedStiVsFundament.get(euSedSti).getEnumNavn();
                            euSedStiVsAclSedStiNyMal.add(euSedStiVsEnum);
                            euSedStiVsAclSedStiNye.add(euSedStiVsEnum);
                            aclMalBasisPrSektor.add(euSedStiVsEnum.replaceFirst("\\.", ":"));
                        } else {
                            euSedStiVsAclSedStiNyMal.add(euSedSti);
                            euSedStiVsAclSedStiNye.add(euSedSti);
                            aclMalBasisPrSektor.add(euSedSti.replaceFirst("\\.", ":"));
                        }
                    }
                }
            });
            String sektor = StringUtils.splitByCharacterType(sedType)[0];
            if (!sektorVsNyeStier.containsKey(sektor)) {
                sektorVsNyeStier.put(sektor, new ArrayList<>());
            }
            sektorVsNyeStier.get(sektor).addAll(aclMalBasisPrSektor);
            // Utgåtte acl-stier
            List<String> euSedStiVsAclSedStiUtgaatte = euSedStiVsAclSedSti.keySet().stream()
                    .filter(s -> !cdmEuSedStiListe.contains(s))
                    .map(s -> s + ": " + euSedStiVsAclSedSti.get(s))
                    .toList();
            filService.skrivDiffTilFil(euSedStiVsAclSedStiNyMal, sedType, nyCdmVersjon, FilService.FIL_NY_ACL_MAL_STIER);
            filService.skrivDiffTilFil(euSedStiVsAclSedStiNye, sedType, nyCdmVersjon, FilService.FIL_MED_ENDRINGER_STIER);
            filService.skrivDiffTilFil(euSedStiVsAclSedStiEndretEnum, sedType, nyCdmVersjon, FilService.FIL_ENDRET_ENUM_STIER);
            filService.skrivDiffTilFil(euSedStiVsAclSedUendret, sedType, nyCdmVersjon, FilService.FIL_UENDREDE_ACL_STIER);
            filService.skrivDiffTilFil(euSedStiVsAclSedStiUtgaatte, sedType, nyCdmVersjon, FilService.FIL_UTGAATTE_ACL_STIER);
        });
        sektorVsNyeStier.forEach((sektor, aclBasis) ->
                filService.skrivAclMalBasisPrSektorTilFil(sektorVsNyeStier.get(sektor), sektor, nyCdmVersjon)
        );
    }

    public void sammenliknerEnumeneForToCdmVersjonerOgKopiererEnumFundamentFilene(String cdmVersjonGml, String cdmVersjonNy) {
        List<String> enumNavnNyVersjon = filService.lesEnumFundamentFilnavnene(cdmVersjonNy);
        List<String> enumNavnGammelVersjon = filService.lesEnumFundamentFilnavnene(cdmVersjonGml);
        List<String> enumNavnFelles = enumNavnNyVersjon.stream().filter(enumNavnGammelVersjon::contains).toList();
        List<String> enumNavnHeltNye = enumNavnNyVersjon.stream().filter(n -> !enumNavnGammelVersjon.contains(n)).toList();
        List<String> enumNavnUtgaatte = enumNavnGammelVersjon.stream().filter(n -> !enumNavnNyVersjon.contains(n)).toList();
        List<String> oppsummering = new ArrayList<>();
        List<String> enumNavnMedEndringer = kodeverkService.sammenliknerEnumkoder(enumNavnFelles, oppsummering, cdmVersjonGml, cdmVersjonNy);
        kodeverkService.skrivEnumkodeEndringeneTilFil(oppsummering, enumNavnHeltNye, enumNavnUtgaatte, cdmVersjonNy);
        List<String> enumNavnEndredeOgNyeFiler = new ArrayList<>(enumNavnHeltNye);
        enumNavnEndredeOgNyeFiler.addAll(enumNavnMedEndringer);
        kodeverkService.kopierNyeOgEndredeEnumerTilEgenLokasjon(enumNavnEndredeOgNyeFiler, cdmVersjonNy);
        // fil for å endre navn på enumfiler (når navnet er det samme men innholdet er endret i ny versjon)
        kodeverkService.opprettEnumnavnemappingFil(enumNavnEndredeOgNyeFiler, cdmVersjonNy);
        // Lager også alle enum-filene
        List<String> alleEnumnavn = filService.lesNavneneTilAlleKodeverksfiler(cdmVersjonNy);
        alleEnumnavn.forEach(enumNavn -> {
            List<String> enumkodeMapping = new ArrayList<>();
            TreeMap<Integer, EnumkodeFundament> enumNavnVsEnumInfo = filService.lesEnumFundamentFil(enumNavn, cdmVersjonNy);
            enumNavnVsEnumInfo.forEach((i, enumInfo) ->
                    enumkodeMapping.add(enumInfo.getEnumkode() + "=" + enumInfo.getEnumkodeId()));

            filService.opprettAclEnumfil(enumkodeMapping, "alle/" + enumNavn, cdmVersjonNy);
        });
    }

}
