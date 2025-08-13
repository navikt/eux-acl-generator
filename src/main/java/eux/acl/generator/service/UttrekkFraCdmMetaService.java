package eux.acl.generator.service;

import eux.acl.generator.modell.UttrekkCdmMeta;
import eux.acl.generator.modell.EuSedFundament;
import eux.acl.generator.modell.UttrekkFundament;
import eux.acl.generator.modell.EnumkodeFundament;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class UttrekkFraCdmMetaService {
    private static final List<String> CDM_TEKST_TYPER = List
            .of("_uxTextInput", "_uxTextAreaInput", "_uxDateInput", "_rinaIRAutocomplete");
    private static final List<String> CDM_ENUM_TYPER = List
            .of("_uxMultiSelect", "_uxMultiCheckbox", "_uxRadioButton", "_uxHiddenArrayContainer");
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

    public TreeMap<String, EuSedFundament> lagEuSedFundament(UttrekkFundament uttrekkFundament) {
        if (uttrekkFundament == null || uttrekkFundament.getCdmStiVsMalTagnavn() == null || uttrekkFundament.getCdmStiVsTagtype() == null) {
            return null;
        }
        Map<String, String> cdmStiVsMalTagnavn = uttrekkFundament.getCdmStiVsMalTagnavn();
        Map<String, String> cdmStiVsMalTagtype = uttrekkFundament.getCdmStiVsTagtype();
        TreeMap<String, EuSedFundament> euSedStiVsEuSedFundament = new TreeMap<>();
        cdmStiVsMalTagnavn
                .forEach((cdmSti, tagnavn) -> {
                    if (CDM_TEKST_TYPER.contains(cdmStiVsMalTagtype.get(cdmSti))
                            || CDM_ENUM_TYPER.contains(cdmStiVsMalTagtype.get(cdmSti))) {
                        String euSedSti = byggEuSedSti(cdmSti, cdmStiVsMalTagnavn, cdmStiVsMalTagtype);
                        if (CDM_ENUM_TYPER.contains(cdmStiVsMalTagtype.get(cdmSti))) {
                            EuSedFundament euSedFundament = EuSedFundament.builder()
                                    .verdiType("enum")
                                    .cdmType(uttrekkFundament.getCdmStiVsTagtype().get(cdmSti + ".children[0]"))
                                    .indeks(uttrekkFundament.getCdmStiVsIndeks().get(cdmSti + ".children[0]"))
                                    .ledetekst(uttrekkFundament.getCdmStiVsLedetekst().get(cdmSti + ".children[0]"))
                                    .enumNavn(uttrekkFundament.getCdmStiVsEnumNavn().get(cdmSti + ".children[0]"))
                                    .tekstId(uttrekkFundament.getCdmStiVsTekstId().get(cdmSti + ".children[0]"))
//                                    .tekstlengde(uttrekkFundament.getCdmStiVsTekstlengde().get(cdmSti + ".children[0]"))
                                    .build();
                            euSedStiVsEuSedFundament.put(euSedSti, euSedFundament);
                        }
                        if (CDM_TEKST_TYPER.contains(cdmStiVsMalTagtype.get(cdmSti))) {
                            EuSedFundament euSedFundament = EuSedFundament.builder()
                                    .verdiType("tekst")
                                    .cdmType(uttrekkFundament.getCdmStiVsTagtype().get(cdmSti))
                                    .indeks(uttrekkFundament.getCdmStiVsIndeks().get(cdmSti))
                                    .ledetekst(uttrekkFundament.getCdmStiVsLedetekst().get(cdmSti))
//                                    .enumNavn(uttrekkFundament.getCdmStiVsEnumtype().get(cdmSti))
                                    .tekstId(uttrekkFundament.getCdmStiVsTekstId().get(cdmSti))
                                    .tekstlengde(uttrekkFundament.getCdmStiVsTekstlengde().get(cdmSti))
                                    .build();
                            euSedStiVsEuSedFundament.put(euSedSti, euSedFundament);
                        }
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


    public UttrekkFundament lagCdmUttrekkFundament(Map<String, Object> cdmMetaSed,
                                                   Map<String, String> tekstIdVsTekst,
                                                   Map<String, String> tekstIdVsEnumNavn) {
        UttrekkCdmMeta uttrekkCdmMeta = lagUttrekkFraCdmMeta(cdmMetaSed);
        return UttrekkFundament.builder()
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

    public TreeMap<String, String> lagCdmStiVsEnumNavn(UttrekkCdmMeta uttrekkCdmMeta, Map<String, String> cdmTekstIdVsEnumNavn) {
        if (uttrekkCdmMeta == null || uttrekkCdmMeta.getCdmStiVsTekstId() == null || cdmTekstIdVsEnumNavn == null) {
            return null;
        }
        TreeMap<String, String> cdmStiVsEnumNavn = new TreeMap<>();
        uttrekkCdmMeta.getCdmStiVsTekstId().forEach((cdmSti, tekstId) -> {
            if (cdmTekstIdVsEnumNavn.containsKey(tekstId)) {
                cdmStiVsEnumNavn.put(cdmSti, cdmTekstIdVsEnumNavn.get(tekstId));
            }
        });
        return cdmStiVsEnumNavn;
    }

    public Map<String, String> lagCdmStiVsEnumkodeTekst(UttrekkCdmMeta
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
            , UttrekkFundament uttrekkFundament) {
        uttrekkFundament.getCdmStiVsEnumNavn().forEach((cdmSti, enumnavn) -> {
            if (!enumNavnVsEnumInnhold.containsKey(enumnavn)) {
                enumNavnVsEnumInnhold.put(enumnavn, new TreeMap<>());
            }
        });
    }

    public void fyllInnEnumkodeId(Map<String, TreeMap<Integer, EnumkodeFundament>> enumtypeVsEnumInnhold
            , UttrekkFundament uttrekkFundament) {
        Set<String> tekstIdTilEnumSomMangler = new HashSet<>();
        uttrekkFundament.getCdmStiVsEnumkodeId()
                .forEach((cdmSti, enumkodeId) -> {
                            String cdmStiJustert = StringUtils.substringBeforeLast(cdmSti, ".");
                            String cdmIndeks = StringUtils.getDigits(StringUtils.substringAfterLast(cdmSti, "["));
                            int indeksNr = Integer.parseInt(cdmIndeks);
                            String enumNavn = uttrekkFundament.getCdmStiVsEnumNavn().get(cdmStiJustert);
                            if (enumNavn == null) {
                                String tekstId = uttrekkFundament.getCdmStiVsTekstId().get(cdmStiJustert);
                                tekstIdTilEnumSomMangler.add(tekstId);
                            } else {
                                if (!enumtypeVsEnumInnhold.get(enumNavn).containsKey(indeksNr)) {
                                    enumtypeVsEnumInnhold.get(enumNavn).put(indeksNr, new EnumkodeFundament());
                                }
                                enumtypeVsEnumInnhold.get(enumNavn).get(indeksNr).setEnumkodeId(enumkodeId);
                            }
                        }
                );
        if (!tekstIdTilEnumSomMangler.isEmpty()) {
            tekstIdTilEnumSomMangler.forEach(tekstId -> log.error("EnumNavn mangler, tekstId={}", tekstId));
        }
    }

    public void fyllInnEnumkodeBeskrivelse
            (Map<String, TreeMap<Integer, EnumkodeFundament>> enumNavnVsEnumInnhold
                    , UttrekkFundament uttrekkFundament) {
        Set<String> tekstIdTilEnumSomMangler = new HashSet<>();
        uttrekkFundament.getCdmStiVsEnumkodeTekst().forEach((cdmSti, enumkodeTekst) -> {
                    String cdmStiJustert = StringUtils.substringBeforeLast(cdmSti, ".");
                    String indeks = StringUtils.getDigits(StringUtils.substringAfterLast(cdmSti, "["));
                    int cdmIndeksNr = Integer.parseInt(indeks); // for korrekt sortering
                    String enumNavn = uttrekkFundament.getCdmStiVsEnumNavn().get(cdmStiJustert);
                    if (enumNavn == null) {
                        String tekstId = uttrekkFundament.getCdmStiVsTekstId().get(cdmStiJustert);
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
            tekstIdTilEnumSomMangler.forEach(tekstId -> log.error("EnumBeskr: EnumNavn mangler, tekstId={}", tekstId));
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
                                                            .trim()
                                                            .replace(" ", "_")
                                                            .replace(".", "")
                                                            .replace(",", "")
//                                                    .replace("-", "")
                                                            .replace("/", "-")
                                                            .replace("§", "")
                                                            .toLowerCase()
                                            );
                                        } else {
                                            enumVerdi.setEnumkode(enumVerdi.getEnumkodeId());
                                        }
                                    }
                            );

                });


    }
}
