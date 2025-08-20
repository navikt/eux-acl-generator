package eux.acl.generator.service;

import eux.acl.generator.modell.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class CdmMetaService {
    private static final List<String> CDM_TEKST_TYPER = List
            .of("_uxTextInput", "_uxTextAreaInput", "_uxDateInput", "_rinaIRAutocomplete");
    private static final List<String> CDM_ENUM_TYPER = List
            .of("_uxMultiSelect", "_uxMultiCheckbox", "_uxRadioButton");
    private static final List<String> CDM_LISTE_TYPER = List
            .of("_rinaArray", "_uxMultiCheckbox", "_uxHiddenArrayContainer", "_uxMultiSelect");

    /**
     * Trekker ut info fra Cdm-meta. Cdm-stien er alltid nøkkel (...children[#].children[#].children[#]...).
     */
    public UttrekkCdmMeta lagUttrekkFraCdmMeta(Map<String, Object> cdmFilFlattened) {
        if (cdmFilFlattened == null || cdmFilFlattened.isEmpty()) {
            return null;
        }
        UttrekkCdmMeta uttrekkCdmMeta = new UttrekkCdmMeta();
        cdmFilFlattened.keySet().stream().sorted().forEach(k -> {
            try {
                String v = cdmFilFlattened.get(k).toString();
                if (k.contains("context.key")) {
                    String cdmSti = k.replace(".context.key", "");
                    uttrekkCdmMeta.getCdmStiVsMalTagnavn().put(cdmSti, v);
                } else if (k.contains("context.type")) {
                    String cdmSti = k.replace(".context.type", "");
                    uttrekkCdmMeta.getCdmStiVsTagtype().put(cdmSti, v);
                } else if (k.endsWith(".context.inputs.label[0].string")) {
                    String cdmSti = k.replace(".context.inputs.label[0].string", "");
                    uttrekkCdmMeta.getCdmStiVsTekstId().put(cdmSti, v.split("_")[1].toLowerCase());
                } else if (k.endsWith(".context.inputs.label")) {
                    String cdmSti = k.replace(".context.inputs.label", "");
                    uttrekkCdmMeta.getCdmStiVsTekstId().put(cdmSti, v.split("_")[1].toLowerCase());
                } else if (k.endsWith("context.inputs.sedTitle")) {
                    String sedTittelId = v.trim();
                    uttrekkCdmMeta.getCdmStiVsTekstId().put("sedTitle", sedTittelId);
                } else if (k.endsWith("context.inputs.label[0].prefix")) {
                    String cdmSti = k.replace(".context.inputs.label[0].prefix", "");
                    uttrekkCdmMeta.getCdmStiVsIndeks().put(cdmSti, v);
                } else if ((k.contains(".context.inputs.options[") && k.contains(".label[0].string"))
                        || (k.contains(".context.inputs.selectOptions[") && k.contains(".label[0].string"))) {
                    String cdmSti = k.replace(".context.inputs.options[", ".enumkodeTekstId[")
                            .replace(".context.inputs.selectOptions[", ".enumkodeTekstId[")
                            .replace(".label[0].string", "");
                    if (!v.contains("_")) {
                        log.error("kodeverdi mangler _  v={}", v);
                        throw new RuntimeException("kodeverdi mangler _  v=" + v);
                    }
                    uttrekkCdmMeta.getCdmStiVsEnumkodeTekstId().put(cdmSti, v.split("_")[1].toLowerCase());
                } else if ((k.contains(".inputs.options") && k.contains(".val"))
                        || (k.contains(".inputs.selectOptions") && k.contains(".id"))) {
                    String cdmSti = k.replace(".inputs.options", "")
                            .replace(".inputs.selectOptions", "")
                            .replace(".val", "")
                            .replace(".id", "")
                            .replace(".context[", ".enumkodeId[");
                    uttrekkCdmMeta.getCdmStiVsEnumkodeId().put(cdmSti, v);

                } else if (k.endsWith(".valFnId") && v.equals("maxLength")) {
                    String cdmSti = k.replace(".context.validators.validatorFn[", ".param[")
                            .replace(".valFnId", "");
                    uttrekkCdmMeta.getCdmStiVsParameterNavn().put(cdmSti, v);
                } else if (k.endsWith(".params[0]")) {
                    String cdmSti = k.replace(".context.validators.validatorFn[", ".param[")
                            .replace(".params[0]", "");
                    uttrekkCdmMeta.getCdmStiVsParameterVerdi().put(cdmSti, v);
                }
            } catch (Exception e) {
                log.error("cdmSti={}, Melding={} ", k, e.getLocalizedMessage(), e);
            }
        });
        return uttrekkCdmMeta;
    }

    public TreeMap<String, EuSedFundament> lagEuSedFundament(CdmUttrekk cdmUttrekk) {
        if (cdmUttrekk == null || cdmUttrekk.getCdmStiVsMalTagnavn() == null || cdmUttrekk.getCdmStiVsTagtype() == null) {
            return null;
        }
        Map<String, String> cdmStiVsMalTagnavn = cdmUttrekk.getCdmStiVsMalTagnavn();
        Map<String, String> cdmStiVsMalTagtype = cdmUttrekk.getCdmStiVsTagtype();
        TreeMap<String, EuSedFundament> euSedStiVsEuSedFundament = new TreeMap<>();
        cdmStiVsMalTagnavn
                .forEach((cdmSti, tagnavn) -> {
                    if (List.of("_uxMultiSelect")
                            .contains(cdmStiVsMalTagtype.get(cdmSti))) {
                        String euSedSti = byggEuSedSti(cdmSti, cdmStiVsMalTagnavn, cdmStiVsMalTagtype);
                        EuSedFundament euSedFundament = EuSedFundament.builder()
                                .verdiType("enum")
                                .cdmType(cdmUttrekk.getCdmStiVsTagtype().get(cdmSti))
                                .indeks(cdmUttrekk.getCdmStiVsIndeks().get(cdmSti))
                                .ledetekst(cdmUttrekk.getCdmStiVsLedetekst().get(cdmSti))
                                .enumNavn(cdmUttrekk.getCdmStiVsEnumNavn().get(cdmSti))
                                .enumNavnId(cdmUttrekk.getCdmStiVsTekstId().get(cdmSti))
                                .build();
                        euSedStiVsEuSedFundament.put(euSedSti, euSedFundament);
                    } else if (List.of("_uxHiddenArrayContainer")
                                .contains(cdmStiVsMalTagtype.get(cdmSti))) {
                            String euSedSti = byggEuSedSti(cdmSti, cdmStiVsMalTagnavn, cdmStiVsMalTagtype);
                            EuSedFundament euSedFundament = EuSedFundament.builder()
                                    .verdiType("enum")
                                    .cdmType(cdmUttrekk.getCdmStiVsTagtype().get(cdmSti+ ".children[0]"))
                                    .indeks(cdmUttrekk.getCdmStiVsIndeks().get(cdmSti+ ".children[0]"))
                                    .ledetekst(cdmUttrekk.getCdmStiVsLedetekst().get(cdmSti+ ".children[0]"))
                                    .enumNavn(cdmUttrekk.getCdmStiVsEnumNavn().get(cdmSti + ".children[0]"))
                                    .enumNavnId(cdmUttrekk.getCdmStiVsTekstId().get(cdmSti+ ".children[0]"))
                                    .build();
                            euSedStiVsEuSedFundament.put(euSedSti, euSedFundament);
                        } else if (List.of("_uxMultiCheckbox", "_uxRadioButton")
                            .contains(cdmStiVsMalTagtype.get(cdmSti))) {
                        String euSedSti = byggEuSedSti(cdmSti, cdmStiVsMalTagnavn, cdmStiVsMalTagtype);
                        EuSedFundament euSedFundament = EuSedFundament.builder()
                                .verdiType("enum")
                                .cdmType(cdmUttrekk.getCdmStiVsTagtype().get(cdmSti))
                                .indeks(cdmUttrekk.getCdmStiVsIndeks().get(cdmSti))
                                .ledetekst(cdmUttrekk.getCdmStiVsLedetekst().get(cdmSti))
                                .enumNavn(cdmUttrekk.getCdmStiVsEnumNavn().get(cdmSti))
                                .enumNavnId(cdmUttrekk.getCdmStiVsTekstId().get(cdmSti))
                                .build();
                        euSedStiVsEuSedFundament.put(euSedSti, euSedFundament);
                    } else if (CDM_TEKST_TYPER.contains(cdmStiVsMalTagtype.get(cdmSti))) {
                            String euSedSti = byggEuSedSti(cdmSti, cdmStiVsMalTagnavn, cdmStiVsMalTagtype);
                            EuSedFundament euSedFundament = EuSedFundament.builder()
                                    .verdiType("tekst")
                                    .cdmType(cdmUttrekk.getCdmStiVsTagtype().get(cdmSti))
                                    .indeks(cdmUttrekk.getCdmStiVsIndeks().get(cdmSti))
                                    .ledetekst(cdmUttrekk.getCdmStiVsLedetekst().get(cdmSti))
//                                    .enumNavn(uttrekkFundament.getCdmStiVsEnumtype().get(cdmSti))
//                                    .tekstId(cdmUttrekk.getCdmStiVsTekstId().get(cdmSti))
                                    .tekstlengde(cdmUttrekk.getCdmStiVsTekstlengde().get(cdmSti))
                                    .build();
                            euSedStiVsEuSedFundament.put(euSedSti, euSedFundament);
                        }
                });
        return euSedStiVsEuSedFundament;
    }

    public List<String> lagEuSedFraCdm(UttrekkCdmMeta uttrekkCdmMeta) {
        Map<String, String> cdmStiVsMalTagnavn = uttrekkCdmMeta.getCdmStiVsMalTagnavn();
        Map<String, String> cdmStiVsMalTagtype = uttrekkCdmMeta.getCdmStiVsTagtype();
        return cdmStiVsMalTagnavn.keySet().stream()
                .filter(cdmSti -> CDM_TEKST_TYPER.contains(cdmStiVsMalTagtype.get(cdmSti))
                        || CDM_ENUM_TYPER.contains(cdmStiVsMalTagtype.get(cdmSti)))
                .map(cdmSti -> byggEuSedSti(cdmSti, cdmStiVsMalTagnavn, cdmStiVsMalTagtype))
                .distinct()
                .sorted()
                .toList();
    }

    private String byggEuSedSti(String cdmSti, Map<String, String> cdmStiVsTagnavn, Map<String, String> cdmStiVsTagtype) {
        String[] cdmStiBuilder = {""};
        String[] euSedStiBuilder = {""};
        Arrays.asList(cdmSti.split("\\."))
                .forEach(childLevel -> {
                    cdmStiBuilder[0] += cdmStiBuilder[0].isEmpty() ? childLevel : "." + childLevel;
                    String cdmStiX = cdmStiBuilder[0];
                    String tagType = cdmStiVsTagtype.get(cdmStiX);
                    if (cdmStiVsTagnavn.get(cdmStiX) != null) {
                        euSedStiBuilder[0] += "." + cdmStiVsTagnavn.get(cdmStiX);
                        if (CDM_LISTE_TYPER.contains(tagType)) {
                            // setter inn at det er ei liste
                            euSedStiBuilder[0] += "[0]";
                        }
                    }
                });
        return euSedStiBuilder[0].replaceFirst(".", "");
    }

    public CdmUttrekk lagCdmUttrekk(Map<String, Object> cdmMetaSed,
                                    Map<String, String> tekstIdVsTekst,
                                    Map<String, String> tekstIdVsEnumNavn) {
        UttrekkCdmMeta uttrekkCdmMeta = lagUttrekkFraCdmMeta(cdmMetaSed);
        return CdmUttrekk.builder()
                .cdmStiVsMalTagnavn(uttrekkCdmMeta.getCdmStiVsMalTagnavn())
                .cdmStiVsTagtype(uttrekkCdmMeta.getCdmStiVsTagtype())
                .cdmStiVsIndeks(uttrekkCdmMeta.getCdmStiVsIndeks())
                .cdmStiVsLedetekst(lagCdmStiVsLedetekst(uttrekkCdmMeta, tekstIdVsTekst))
                .cdmStiVsEnumNavn(lagCdmStiVsEnumNavn(uttrekkCdmMeta, tekstIdVsEnumNavn))
                .cdmStiVsEnumkodeTekst(lagCdmStiVsEnumkodeTekst(uttrekkCdmMeta, tekstIdVsTekst))
                .cdmStiVsEnumkodeId(uttrekkCdmMeta.getCdmStiVsEnumkodeId())
                .cdmStiVsTekstId(uttrekkCdmMeta.getCdmStiVsTekstId())
                .cdmStiVsTekstlengde(lagCdmStiVsTekstlengde(uttrekkCdmMeta))
                .build();
    }

    public CdmUttrekkEnum lagCdmUttrekkEnumEr(Map<String, Object> cdmMetaSed,
                                              Map<String, String> tekstIdVsTekst) {
        UttrekkCdmMeta uttrekkCdmMeta = lagUttrekkFraCdmMeta(cdmMetaSed);
        return CdmUttrekkEnum.builder()
                .cdmStiVsEnumkodeTekst(lagCdmStiVsEnumkodeTekst(uttrekkCdmMeta, tekstIdVsTekst))
                .cdmStiVsEnumkodeId(uttrekkCdmMeta.getCdmStiVsEnumkodeId())
                .build();
    }

    public Map<String, String> lagCdmStiVsLedetekst(UttrekkCdmMeta uttrekkCdmMeta, Map<String, String> tekstIdVsTekst) {
        if (uttrekkCdmMeta == null || uttrekkCdmMeta.getCdmStiVsTekstId() == null || tekstIdVsTekst == null) {
            return null;
        }
        Map<String, String> cdmStiVsLedetekst = new HashMap<>();
        uttrekkCdmMeta.getCdmStiVsTekstId().forEach((cdmSti, tekstId) -> {
            if (tekstId == null) {
                log.error("tekstId mangler, cdmSti={}", cdmSti);
            } else {
                cdmStiVsLedetekst.put(cdmSti, tekstIdVsTekst.get(tekstId));
            }
        });
        return cdmStiVsLedetekst;
    }

    private TreeMap<String, String> lagCdmStiVsEnumNavn(UttrekkCdmMeta uttrekkCdmMeta, Map<String, String> cdmTekstIdVsEnumNavn) {
        if (uttrekkCdmMeta == null || uttrekkCdmMeta.getCdmStiVsTekstId() == null || cdmTekstIdVsEnumNavn == null) {
            return null;
        }
        TreeMap<String, String> cdmStiVsEnumNavn = new TreeMap<>();
        uttrekkCdmMeta.getCdmStiVsTekstId().forEach((cdmSti, tekstId) -> {
            cdmStiVsEnumNavn.put(cdmSti, cdmTekstIdVsEnumNavn.get(tekstId));
        });
        return cdmStiVsEnumNavn;
    }

    private Map<String, String> lagCdmStiVsEnumkodeTekst(UttrekkCdmMeta
                                                                uttrekkCdmMeta, Map<String, String> tekstIdVsTekst) {
        if (uttrekkCdmMeta == null || uttrekkCdmMeta.getCdmStiVsEnumkodeTekstId() == null || tekstIdVsTekst == null) {
            return null;
        }
        try {
            return uttrekkCdmMeta.getCdmStiVsEnumkodeTekstId().entrySet().stream()
                    .filter(entry -> {
                        if (tekstIdVsTekst.containsKey(entry.getValue())) {
                            return true;
                        } else {
                            log.error("Mangler tekstIdVsTekst for: v={} ", entry.getValue());
                            return false;
                        }
                    })
                    .collect(Collectors.toMap(
                            entry -> entry.getKey(),
                            entry -> tekstIdVsTekst.get(entry.getValue())));
        } catch (Exception e) {
            log.error("Kunne ikke lage mapping mellom tekstId og tekst");
            throw new RuntimeException(e);
        }
    }

    private Map<String, String> lagCdmStiVsTekstlengde(UttrekkCdmMeta uttrekkCdmMeta) {
        Map<String, String> cdmStiVsTekstlengde = new HashMap<>();
        uttrekkCdmMeta.getCdmStiVsParameterNavn().forEach((cdmStiParam, parameternavn) -> {
            String cdmSti = StringUtils.substringBeforeLast(cdmStiParam, ".");
            String tekstlengde = uttrekkCdmMeta.getCdmStiVsParameterVerdi().get(cdmStiParam);
            cdmStiVsTekstlengde.put(cdmSti, tekstlengde);

        });
       return cdmStiVsTekstlengde;
    }

    public void opprettEnumene(Map<String, TreeMap<Integer, EnumkodeFundament>> enumNavnVsEnumInnhold
            , CdmUttrekk cdmUttrekk) {
        cdmUttrekk.getCdmStiVsEnumNavn().forEach((cdmSti, enumnavn) -> {
            if (!enumNavnVsEnumInnhold.containsKey(enumnavn)) {
                enumNavnVsEnumInnhold.put(enumnavn, new TreeMap<>());
            }
        });
    }

    public void fyllInnEnumkodeId(Map<String, TreeMap<Integer, EnumkodeFundament>> enumNavnVsEnumInnhold
            , CdmUttrekk cdmUttrekk, String sedType) {
        Set<String> tekstIdTilEnumSomMangler = new HashSet<>();
        cdmUttrekk.getCdmStiVsEnumkodeId()
                .forEach((cdmSti, enumkodeId) -> {
                            String cdmStiJustert = StringUtils.substringBeforeLast(cdmSti, ".");
                            String cdmIndeks = StringUtils.getDigits(StringUtils.substringAfterLast(cdmSti, "["));
                            int indeksNr = Integer.parseInt(cdmIndeks);
                            String enumNavn = cdmUttrekk.getCdmStiVsEnumNavn().get(cdmStiJustert);
                            if (enumNavn == null) {
                                String tekstId = cdmUttrekk.getCdmStiVsTekstId().get(cdmStiJustert);
//                                if (!enumNavnVsEnumInnhold.containsKey(tekstId )) {
//                                    enumNavnVsEnumInnhold.put(tekstId, new TreeMap<>());
//                                }
//                                if (!enumNavnVsEnumInnhold.get(tekstId).containsKey(indeksNr)) {
//                                    enumNavnVsEnumInnhold.get(tekstId).put(indeksNr, new EnumkodeFundament());
//                                }
//                                enumNavnVsEnumInnhold.get(tekstId).get(indeksNr).setEnumkodeId(enumkodeId);
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
            tekstIdTilEnumSomMangler.forEach(tekstId -> log.error("EnumNavn mangler. " +
                    "sedType={}, tekstId={}", sedType, tekstId));
        }
    }

    public void fyllInnEnumkodeBeskrivelse
            (Map<String, TreeMap<Integer, EnumkodeFundament>> enumNavnVsEnumInnhold
                    , CdmUttrekk cdmUttrekk, String sedType) {
        Set<String> tekstIdTilEnumSomMangler = new HashSet<>();
        cdmUttrekk.getCdmStiVsEnumkodeTekst().forEach((cdmSti, enumkodeTekst) -> {
                    String cdmStiJustert = StringUtils.substringBeforeLast(cdmSti, ".");
                    String indeks = StringUtils.getDigits(StringUtils.substringAfterLast(cdmSti, "["));
                    int cdmIndeksNr = Integer.parseInt(indeks); // for korrekt sortering
                    String enumNavn = cdmUttrekk.getCdmStiVsEnumNavn().get(cdmStiJustert);
                    if (enumNavn == null) {
                        String tekstId = cdmUttrekk.getCdmStiVsTekstId().get(cdmStiJustert);
                        tekstIdTilEnumSomMangler.add(tekstId);
                    } else {
                        if (!enumNavnVsEnumInnhold.get(enumNavn).containsKey(cdmIndeksNr)) {
                            enumNavnVsEnumInnhold.get(enumNavn).put(cdmIndeksNr, new EnumkodeFundament());
                        }
                        if (enumNavnVsEnumInnhold.get(enumNavn).get(cdmIndeksNr).getEnumkodeBeskrivelse() != null
                                && !enumNavnVsEnumInnhold.get(enumNavn).get(cdmIndeksNr).getEnumkodeBeskrivelse()
                                .equals(enumkodeTekst)) {
                            log.error("ENUM feil. Ulike verdier i ulike forekomster av samme enum: " +
                                            "enumNavn={}, cdmIndeksNr={}, enumTekst_1={}, enumTekst_2={}"
                                    , enumNavn, cdmIndeksNr, enumkodeTekst
                                    , enumNavnVsEnumInnhold.get(enumNavn).get(cdmIndeksNr).getEnumkodeBeskrivelse());
                        }
                        enumNavnVsEnumInnhold.get(enumNavn).get(cdmIndeksNr).setEnumkodeBeskrivelse(enumkodeTekst);
                    }
                }
        );
        if (!tekstIdTilEnumSomMangler.isEmpty()) {
            tekstIdTilEnumSomMangler.forEach(tekstId -> log.error("EnumBskr mangler. " +
                    "sedType={}, tekstId={}", sedType, tekstId));
        }
    }

    public void fyllInnEnumkoderMedBeskrivelser
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
}
