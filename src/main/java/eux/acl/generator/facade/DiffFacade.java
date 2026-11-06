package eux.acl.generator.facade;

import eux.acl.generator.modell.*;
import eux.acl.generator.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static eux.acl.generator.service.FilService.*;

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
            TreeMap<String, EuSedFundament> cdmEuSedStiVsFundament = euSedService.lagCdmEuSedMedFundament(sedType, cdmVersjon);
            List<String> cdmSedListe = cdmEuSedStiVsFundament.keySet().stream().toList();
            List<String> aclSedListe = aclService.lagEuSedFraAclMal(sedType, cdmVersjon);
            for (int nr = 0; nr < aclSedListe.size(); nr++) {
                if (!aclSedListe.get(nr).equals(cdmSedListe.get(nr))) {
                    log.error(sedType + " mismatch:\nAcl={}\nCdm={}", aclSedListe.get(nr), cdmEuSedStiVsFundament.get(nr));
                    throw new RuntimeException("Mismatch mellom CDM- og ACL-Sed - se loggen");
                }
            }
            if (cdmEuSedStiVsFundament.size() != aclSedListe.size()) {
                log.error(sedType + " mismatch. Ulikt linjeantall. Acl={}, Cdm={}"
                        , aclSedListe.size(), cdmEuSedStiVsFundament.size());
                throw new RuntimeException("Mismatch mellom CDM- og ACL-Sed - se loggen");
            } else {
                log.info("I {} er CDM og ACL utgavene identiske for {}", cdmVersjon, sedType);
            }
        });
    }

    public void sammenliknerToAclVersjonerOgSkriverDiffTilFil(List<String> sedTyper, String forrigeAclVersjon, String nyAclVersjon) {
        sedTyper.forEach(sedType -> {
            List<String> navSedForrige = aclService.ekstraherNavSedFraAclMal(sedType, MAPPE_RESULTATER_ACL, forrigeAclVersjon);
            List<String> navSedNy = aclService.ekstraherNavSedFraAclMal(sedType, MAPPE_RESULTATER_ACL, nyAclVersjon);
            List<String> uendredeLinjer = navSedNy.stream().filter(navSedForrige::contains).toList();
            List<String> nyeLinjer = navSedNy.stream().filter(linje -> !navSedForrige.contains(linje)).toList();
            List<String> utgaatteLinjer = navSedForrige.stream().filter(linje -> !navSedNy.contains(linje)).toList();
            int antallEndringer = nyeLinjer.size();
            filService.skrivAclVsAclDiffTilFil(uendredeLinjer, sedType + "_" + antallEndringer, nyAclVersjon, FilService.FIL_MED_UENDREDE_STIER);
            filService.skrivAclVsAclDiffTilFil(nyeLinjer, sedType + "_" + antallEndringer, nyAclVersjon, FIL_MED_ENDREDE_STIER);
            filService.skrivAclVsAclDiffTilFil(utgaatteLinjer, sedType + "_" + antallEndringer, nyAclVersjon, FIL_MED_UTGAATTE_STIER);
            log.info("Diff mellom ny og gammel Acl-versjon skrevet til filer. Sedtype: {}", sedType);
        });
    }

    public void sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(List<String> sedTyper, String cdmForrigeVersjon, String cdmNyVersjon) {
        Map<String, List<String>> sektorVsNyeStier = new HashMap<>();
        sedTyper.forEach(sedType -> {
            SedVersjonsdiff diff = euSedService.diffMellomUlikeCdmSedVersjoner(sedType, cdmForrigeVersjon, cdmNyVersjon);
            filService.skrivCdmAclDiffTilFil(diff.getUendredeStierListe(), sedType, cdmNyVersjon, FilService.FIL_MED_UENDREDE_STIER);
            filService.skrivCdmAclDiffTilFil(diff.getNyeStierListe(), sedType, cdmNyVersjon, FilService.FIL_MED_ENDREDE_STIER);
            filService.skrivCdmAclDiffTilFil(diff.getUtgaatteStierListe(), sedType, cdmNyVersjon, FilService.FIL_MED_UTGAATTE_STIER);
            euSedService.leggTilNyeDiffer(sedType, diff, sektorVsNyeStier);
            log.info("Diff mellom CDM-versjoner. Filer opprettet for {}", sedType);
        });
        euSedService.skrivDiffOppsummeringTilEiFil(sektorVsNyeStier, cdmNyVersjon);
    }

    public void sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(List<String> sedTyper,
                                                                                  String aclVersjon,
                                                                                  String nyCdmVersjon) {
        Map<String, List<String>> sektorVsNyeStier = new HashMap<>();
        sedTyper.forEach(sedType -> {

            TreeMap<String, EuSedFundament> euSedStiVsFundament = euSedService.lagCdmEuSedMedFundament(sedType, nyCdmVersjon);
            List<String> cdmEuSedStiListe = euSedStiVsFundament.keySet().stream().toList();
            Map<String, String> euSedStiVsAclSedSti = aclService.hentAclMal(sedType, aclVersjon);
            List<String> endredeEnumer = filService.lesEndredeOgNyeEnumNavn(nyCdmVersjon);
            if (euSedStiVsFundament == null || euSedStiVsFundament == null || euSedStiVsAclSedSti == null) {
                return;
            }
//            List<String> euSedStiVsAclSedStiEndretEnum = new ArrayList<>();
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
//                        euSedStiVsAclSedStiEndretEnum.add(euSedStiVsAclSedStiVsEnum);
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
//                        euSedStiVsAclSedStiEndretEnum.add(euSedStiVsEnum);
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
            filService.skrivCdmAclDiffTilFil(euSedStiVsAclSedStiNyMal, sedType, nyCdmVersjon, FilService.FIL_NY_ACL_MAL_STIER);
            filService.skrivCdmAclDiffTilFil(euSedStiVsAclSedStiNye, sedType, nyCdmVersjon, FilService.FIL_MED_ENDREDE_STIER);
//            filService.skrivDiffTilFil(euSedStiVsAclSedStiEndretEnum, sedType, nyCdmVersjon, FilService.FIL_ENDRET_ENUM_STIER);
            filService.skrivCdmAclDiffTilFil(euSedStiVsAclSedUendret, sedType, nyCdmVersjon, FilService.FIL_UENDREDE_ACL_STIER);
            filService.skrivCdmAclDiffTilFil(euSedStiVsAclSedStiUtgaatte, sedType, nyCdmVersjon, FilService.FIL_UTGAATTE_ACL_STIER);
            log.info("{}: ACL {} vs CDM {}. Diff skrevet til filer. Se: {}",
                    sedType, aclVersjon, nyCdmVersjon, MAPPE_ENDRINGER_ACL_TARGET);
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
    }

}
