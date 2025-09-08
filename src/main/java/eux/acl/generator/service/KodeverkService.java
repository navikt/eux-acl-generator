package eux.acl.generator.service;

import eux.acl.generator.modell.CdmUttrekk;
import eux.acl.generator.modell.EnumkodeFundament;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

@Slf4j
public class KodeverkService {
    private final FilService filService;
    private final CdmMetaService cdmMetaService;

    public KodeverkService() {
        this.filService = new FilService();
        this.cdmMetaService = new CdmMetaService();
    }

    public void lagEnumFundamentFilOgSkrivTilFil(String sedType, Map<String,
                                                         String> tekstIdVsEnumNavn,
                                                 Map<String, String> tekstIdVsTekst,
                                                 String cdmVersjon) {
        Map<String, Object> cdmMetaSed = filService.lesCdmMetaFilForEnSed(sedType, cdmVersjon);
        CdmUttrekk cdmUttrekk = cdmMetaService.lagCdmUttrekk(
                cdmMetaSed, tekstIdVsTekst, tekstIdVsEnumNavn);
        Map<String, TreeMap<Integer, EnumkodeFundament>> enumNavnVsEnumInnhold = new HashMap<>();
        opprettEnumene(enumNavnVsEnumInnhold, cdmUttrekk);
        fyllInnEnumkodeId(enumNavnVsEnumInnhold, cdmUttrekk, sedType, cdmVersjon);
        fyllInnEnumkodeBeskrivelse(enumNavnVsEnumInnhold, cdmUttrekk, sedType, cdmVersjon);
        fyllInnEnumkoderMedBeskrivelser(enumNavnVsEnumInnhold);
        enumNavnVsEnumInnhold.forEach((enumNavn, enuminnhold) -> {
            filService.skrivKodeverksfundamentTilFil(enumNavn, enuminnhold, sedType, cdmVersjon);
//                        enumtyperPrSed.add(enumNavn + ": " + sedType);
//                        enumtyper.add(enumNavn);
        });
    }

    private void opprettEnumene(Map<String, TreeMap<Integer, EnumkodeFundament>> enumNavnVsEnumInnhold
            , CdmUttrekk cdmUttrekk) {
        cdmUttrekk.getCdmStiVsEnumNavn().forEach((cdmSti, enumnavn) -> {
            if (!enumNavnVsEnumInnhold.containsKey(enumnavn)) {
                enumNavnVsEnumInnhold.put(enumnavn, new TreeMap<>());
            }
        });
    }

    private void fyllInnEnumkodeId(Map<String, TreeMap<Integer, EnumkodeFundament>> enumNavnVsEnumInnhold
            , CdmUttrekk cdmUttrekk, String sedType, String cdmVersjon) {
        Set<String> tekstIdTilEnumSomMangler = new HashSet<>();
        cdmUttrekk.getCdmStiVsEnumkodeId()
                .forEach((cdmSti, enumkodeId) -> {
                            String cdmStiJustert = StringUtils.substringBeforeLast(cdmSti, ".");
                            String cdmIndeks = StringUtils.getDigits(StringUtils.substringAfterLast(cdmSti, "["));
                            int indeksNr = Integer.parseInt(cdmIndeks);
                            String enumNavn = cdmUttrekk.getCdmStiVsEnumNavn().get(cdmStiJustert);
                            if (enumNavn == null) {
                                String tekstId = cdmUttrekk.getCdmStiVsTekstId().get(cdmStiJustert);
                                tekstIdTilEnumSomMangler.add(tekstId);
                            } else {
                                if (!enumNavnVsEnumInnhold.get(enumNavn).containsKey(indeksNr)) {
                                    enumNavnVsEnumInnhold.get(enumNavn).put(indeksNr, new EnumkodeFundament());
                                }
                                enumNavnVsEnumInnhold.get(enumNavn).get(indeksNr).setEnumkodeId(enumkodeId);
                            }
                        }
                );
        if (!tekstIdTilEnumSomMangler.isEmpty()) {
            tekstIdTilEnumSomMangler.forEach(tekstId -> log.error("EnumNavn mangler (i logisk modell). " +
                    "versjon={}, sedType={}, navnId={}", cdmVersjon, sedType, tekstId));
        }
    }

    private void fyllInnEnumkodeBeskrivelse
            (Map<String, TreeMap<Integer, EnumkodeFundament>> enumNavnVsEnumInnhold
                    , CdmUttrekk cdmUttrekk, String sedType, String cdmVersjon) {
        Set<String> tekstIdTilEnumSomMangler = new HashSet<>();
        cdmUttrekk.getCdmStiVsEnumkodeTekst().forEach((cdmSti, enumkodeTekst) -> {
                    String cdmStiJustert = StringUtils.substringBeforeLast(cdmSti, ".");
                    String indeks = StringUtils.getDigits(StringUtils.substringAfterLast(cdmSti, "["));
                    int cdmIndeksNr = Integer.parseInt(indeks); // for korrekt sortering
                    String enumNavn = cdmUttrekk.getCdmStiVsEnumNavn().get(cdmStiJustert);
                    if (enumNavn == null) {
                        String ledetekst = cdmUttrekk.getCdmStiVsLedetekst().get(cdmStiJustert);
                        tekstIdTilEnumSomMangler.add(ledetekst);
                    } else {
                        if (!enumNavnVsEnumInnhold.get(enumNavn).containsKey(cdmIndeksNr)) {
                            enumNavnVsEnumInnhold.get(enumNavn).put(cdmIndeksNr, new EnumkodeFundament());
                        }
                        if (enumNavnVsEnumInnhold.get(enumNavn).get(cdmIndeksNr).getEnumkodeBeskrivelse() != null
                                && !enumNavnVsEnumInnhold.get(enumNavn).get(cdmIndeksNr).getEnumkodeBeskrivelse()
                                .equals(enumkodeTekst)) {
                            log.error("Ulike verdier i ulike forekomster av samme enum: " +
                                            "sedType={}, versjon={}, enumNavn={}, cdmIndeksNr={}, enumTekst_1={}"
                                    , sedType, cdmVersjon, enumNavn, cdmIndeksNr, enumkodeTekst);
                            log.error("Ulike verdier i ulike forekomster av samme enum: " +
                                            "sedType={}, versjon={}, enumNavn={}, cdmIndeksNr={}, enumTekst_2={}"
                                    , sedType, cdmVersjon, enumNavn, cdmIndeksNr
                                    , enumNavnVsEnumInnhold.get(enumNavn).get(cdmIndeksNr).getEnumkodeBeskrivelse());
                        }
                        enumNavnVsEnumInnhold.get(enumNavn).get(cdmIndeksNr).setEnumkodeBeskrivelse(enumkodeTekst);
                    }
                }
        );
        if (!tekstIdTilEnumSomMangler.isEmpty()) {
            tekstIdTilEnumSomMangler.forEach(tekstId -> log.error("EnumBskr mangler (i logisk modell). " +
                    "versjon={}, sedType={}, ledetekst=\"{}\"", cdmVersjon, sedType, tekstId));
        }
    }


    public List<String> sammenliknerEnumkoder(List<String> enumNavnFelles, List<String> oppsummering,
                                              String versjonGammel, String versjonNy) {
        List<String> enumNavnMedEndringer = new ArrayList<>();
        enumNavnFelles.forEach(enumNavn -> {
            List<String> enumkodeLinjerNy = enumkoderFundamentTilEnumkodelinjer(enumNavn, versjonNy);
            List<String> enumkodeLinjerGml = enumkoderFundamentTilEnumkodelinjer(enumNavn, versjonGammel);
            List<String> enumkoderNye = enumkodeLinjerNy.stream().filter(k -> !enumkodeLinjerGml.contains(k)).toList();
            List<String> enumkoderUtgaatte = enumkodeLinjerGml.stream().filter(k -> !enumkodeLinjerNy.contains(k)).toList();
            if (!enumkoderNye.isEmpty() || !enumkoderUtgaatte.isEmpty()) {
                oppsummering.add("---");
                oppsummering.add(enumNavn);
                oppsummering.add("--- Nye:");
                oppsummering.addAll(enumkoderNye);
                oppsummering.add("---Utgått:");
                oppsummering.addAll(enumkoderUtgaatte);
                enumNavnMedEndringer.add(enumNavn);
            }
        });
        return enumNavnMedEndringer;
    }

    public void skrivEnumkodeEndringeneTilFil(List<String> oppsummeringLinjer,
                                              List<String> enumNavnNyeFiler,
                                              List<String> enumNavnUtgaatteFiler,
                                              String cdmVersjon) {
        oppsummeringLinjer.add("========================");
        oppsummeringLinjer.add("--- Nye filer:");
        oppsummeringLinjer.addAll(enumNavnNyeFiler);
        oppsummeringLinjer.add("--- Utgåtte filer:");
        oppsummeringLinjer.addAll(enumNavnUtgaatteFiler);
        oppsummeringLinjer.add("---");
        String kodeverksdiffFilnavn = "_kodeverkDiff";
        filService.skrivKodeverkOppsummeringTilEiFil(oppsummeringLinjer, kodeverksdiffFilnavn, cdmVersjon);
        log.info("Fil med oversikt over kodeverksendringer:  {}", kodeverksdiffFilnavn);
    }

    public void kopierNyeOgEndredeEnumerTilEgenLokasjon(List<String> navnNyeOgEndredeEnumFiler, String cdmVersjon) {
        navnNyeOgEndredeEnumFiler.forEach(enumNavn -> {
            TreeMap<Integer, EnumkodeFundament> enumInnhold = filService.lesEnumFundamentFil(enumNavn, cdmVersjon);
            filService.skrivNyeKodeverksfundamentTilFil(enumInnhold, enumNavn, cdmVersjon);
        });
    }

    public void opprettEnumnavnemappingFil(List<String> enumNavnEndredeOgNyeFiler, String cdmVersjon) {
        List<String> enumNavnVsFilnavn = new ArrayList<>();
        enumNavnEndredeOgNyeFiler.forEach(enumNavn -> {
            enumNavnVsFilnavn.add(enumNavn + ": " + enumNavn);
        });
        String enumfilnavnMapping = "_enumFilnavnMapping";
        filService.skrivKodeverkOppsummeringTilEiFil(enumNavnVsFilnavn, enumfilnavnMapping, cdmVersjon);
        log.info("Fil med mappinger for å endre navn på enum-filer:  {}", enumfilnavnMapping);
    }

    private List<String> enumkoderFundamentTilEnumkodelinjer(String enumNavn, String cdmVersjonNy) {
        TreeMap<Integer, EnumkodeFundament> enumIdVsEnumkodeInfoNy = filService.lesEnumFundamentFil(enumNavn, cdmVersjonNy);
        return enumIdVsEnumkodeInfoNy.values().stream()
                .map(f -> f.getEnumkodeId() + " " + f.getEnumkodeBeskrivelse())
                .toList();
    }

    private void fyllInnEnumkoderMedBeskrivelser
            (Map<String, TreeMap<Integer, EnumkodeFundament>> enumtypeVsEnumInnhold) {
        enumtypeVsEnumInnhold.values()
                .forEach(enumKodeSett -> {
                    enumKodeSett.values()
                            .forEach(enumVerdi -> {
                                        if (StringUtils.isNumeric(enumVerdi.getEnumkodeId())) {
                                            enumVerdi.setEnumkode(enumVerdi.getEnumkodeBeskrivelse()
                                                    .trim().toLowerCase()
                                                    .replace(".", "")
                                                    .replace(",", "")
                                                    .replace(" - ", "_")
                                                    .replace("-", "")
                                                    .replace(" / ", "_")
                                                    .replace("/", "_")
                                                    .replace("(", "")
                                                    .replace(")", "")
                                                    .replace(" ", "_")
                                                    .replace("§", "")
                                                    .replace("æ", "ae")
                                                    .replace("ø", "oe")
                                                    .replace("å", "aa")
                                            );
                                        } else {
                                            enumVerdi.setEnumkode(enumVerdi.getEnumkodeId());
                                        }
                                    }
                            );
                });
    }

    public void opprettEnumFilForAcl(String filnavn, String cdmVersjon) {
        TreeMap<Integer, EnumkodeFundament> nyEnumFundament = filService.lesNyEnumFundamentFil("nyeOgEndrede/" + filnavn, cdmVersjon);
        TreeMap<Integer, EnumkodeFundament> endretEnumFundament = filService.lesManueltEndretEnumFundamentFil(filnavn, cdmVersjon);
        List<String> aclEnum = new ArrayList<>();
        nyEnumFundament.keySet().forEach(kodeNr -> {
            if (endretEnumFundament != null
                    && endretEnumFundament.containsKey(kodeNr)
                    && endretEnumFundament.get(kodeNr).getEnumkode() != null) {
                aclEnum.add(endretEnumFundament.get(kodeNr).getEnumkode() + "=" + nyEnumFundament.get(kodeNr).getEnumkodeId());
            } else {
                aclEnum.add(nyEnumFundament.get(kodeNr).getEnumkode() + "=" + nyEnumFundament.get(kodeNr).getEnumkodeId());
            }
        });
        // TODO: Les mappingfil (_enumFilnavnMapping.txt) med nytt filnavn - ikke nødvendig i denne versjonen (ingen navnekollisjoner)
        filService.opprettAclEnumfil(aclEnum, filnavn, cdmVersjon);
    }

}
