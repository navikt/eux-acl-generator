package eux.acl.generator.service;

import eux.acl.generator.modell.CdmUttrekk;
import eux.acl.generator.modell.EuSedFundament;
import eux.acl.generator.modell.UttrekkCdmMeta;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class CdmMetaService {
    private static final List<String> CDM_TITTEL_TYPER = List
            .of("_uxFieldSetContainer", "_uxSelectAutoCompleteInput", "_rinaArray");
    private static final List<String> CDM_TEKST_TYPER = List
            .of("_uxTextInput", "_uxTextAreaInput", "_rinaIRAutocomplete");
    private static final List<String> CDM_DATO_TYPER = List
            .of("_uxDateInput");
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
        String sedType = cdmFilFlattened.get("context.inputs.sedName").toString().trim();
        String cdmType = cdmFilFlattened.get("context.type").toString().trim();
        String sedTitleId = cdmFilFlattened.get("context.inputs.sedTitle").toString().split("_")[1].toLowerCase();
        uttrekkCdmMeta.getCdmStiVsMalTagnavn().put("meta", sedType + ".sedTitle");
        uttrekkCdmMeta.getCdmStiVsTagtype().put("meta", cdmType);
        uttrekkCdmMeta.getCdmStiVsTekstId().put("meta", sedTitleId);
        uttrekkCdmMeta.getCdmStiVsIndeks().put("meta", sedType);

        cdmFilFlattened.keySet().stream().sorted().forEach(k -> {
            try {
                String v = cdmFilFlattened.get(k).toString();
                if (k.contains("context.key")) {
                    String cdmSti = k.replace(".context.key", "");
                    uttrekkCdmMeta.getCdmStiVsMalTagnavn().put(cdmSti, v);
                } else if (k.contains(".context.type")) {
                    String cdmSti = k.replace(".context.type", "");
                    uttrekkCdmMeta.getCdmStiVsTagtype().put(cdmSti, v);
                } else if (k.endsWith(".context.inputs.label[0].string")) {
                    String cdmSti = k.replace(".context.inputs.label[0].string", "");
                    uttrekkCdmMeta.getCdmStiVsTekstId().put(cdmSti, v.split("_")[1].toLowerCase());
                } else if (k.endsWith(".context.inputs.label")) {
                    String cdmSti = k.replace(".context.inputs.label", "");
                    uttrekkCdmMeta.getCdmStiVsTekstId().put(cdmSti, v.split("_")[1].toLowerCase());
                } else if (k.endsWith("context.inputs.sedTitle")) {
                    uttrekkCdmMeta.getCdmStiVsTekstId().put("sedMeta", v.split("_")[1].toLowerCase());
                } else if (k.endsWith("context.inputs.label[0].prefix")) {
                    String cdmSti = k.replace(".context.inputs.label[0].prefix", "");
                    uttrekkCdmMeta.getCdmStiVsIndeks().put(cdmSti, v.trim());
                } else if (k.endsWith("context.inputs.preLabel")) {
                    String cdmSti = k.replace(".context.inputs.preLabel", "");
                    uttrekkCdmMeta.getCdmStiVsIndeks().put(cdmSti, v);
                } else if (k.contains("context.inputs.parentPreLabels[")) {
                    String cdmSti = k.split(".context.inputs.parentPreLabels")[0];
                    if (uttrekkCdmMeta.getCdmStiVsPreIndeks().containsKey(cdmSti)) {
                        String preIndeks = uttrekkCdmMeta.getCdmStiVsPreIndeks().get(cdmSti) + v + ".";
                        uttrekkCdmMeta.getCdmStiVsPreIndeks().put(cdmSti, preIndeks);
                    } else {
                        uttrekkCdmMeta.getCdmStiVsPreIndeks().put(cdmSti, v + ".");
                    }
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
        TreeMap<String, String> cdmStiVsMalTagnavn = cdmUttrekk.getCdmStiVsMalTagnavn();
        Map<String, String> cdmStiVsMalTagtype = cdmUttrekk.getCdmStiVsTagtype();
        TreeMap<String, EuSedFundament> euSedStiVsEuSedFundament = new TreeMap<>();
        cdmStiVsMalTagnavn
                .forEach((cdmSti, tagnavn) -> {
                    String tagType = cdmStiVsMalTagtype.get(cdmSti);
                    String indeks = cdmUttrekk.getCdmStiVsPreIndeks().getOrDefault(cdmSti, "")
                            + cdmUttrekk.getCdmStiVsIndeks().get(cdmSti);
                    if ("_rinaRoot".equals(tagType)) {
                        String euSedSti = byggEuSedSti(cdmSti, cdmStiVsMalTagnavn, cdmStiVsMalTagtype);
                        EuSedFundament euSedFundament = EuSedFundament.builder()
                                .verdiType("tittel")
                                .cdmType(cdmUttrekk.getCdmStiVsTagtype().get(cdmSti))
                                .indeks(indeks)
                                .ledetekst(cdmUttrekk.getCdmStiVsLedetekst().get(cdmSti))
                                .tekstlengde(cdmUttrekk.getCdmStiVsTekstlengde().get(cdmSti))
                                .build();
                        euSedStiVsEuSedFundament.put(euSedSti, euSedFundament);
                    } else if (List.of("_uxMultiSelect", "_uxMultiCheckbox", "_uxRadioButton").contains(tagType)) {
                        String euSedSti = byggEuSedSti(cdmSti, cdmStiVsMalTagnavn, cdmStiVsMalTagtype);
                        EuSedFundament euSedFundament = EuSedFundament.builder()
                                .verdiType("enum")
                                .cdmType(cdmUttrekk.getCdmStiVsTagtype().get(cdmSti))
                                .indeks(indeks)
                                .ledetekst(cdmUttrekk.getCdmStiVsLedetekst().get(cdmSti))
                                .enumNavn(cdmUttrekk.getCdmStiVsEnumNavn().get(cdmSti))
                                .enumNavnId(cdmUttrekk.getCdmStiVsTekstId().get(cdmSti))
                                .build();
                        euSedStiVsEuSedFundament.put(euSedSti, euSedFundament);
                    } else if (List.of("_uxHiddenArrayContainer").contains(tagType)) {
                        String euSedSti = byggEuSedSti(cdmSti, cdmStiVsMalTagnavn, cdmStiVsMalTagtype);
                        indeks = cdmUttrekk.getCdmStiVsPreIndeks().getOrDefault(cdmSti + ".children[0]", "")
                                + cdmUttrekk.getCdmStiVsIndeks().get(cdmSti + ".children[0]");
                        EuSedFundament euSedFundament = EuSedFundament.builder()
                                .verdiType("enum")
                                .cdmType(cdmUttrekk.getCdmStiVsTagtype().get(cdmSti + ".children[0]"))
                                .indeks(indeks)
                                .ledetekst(cdmUttrekk.getCdmStiVsLedetekst().get(cdmSti + ".children[0]"))
                                .enumNavn(cdmUttrekk.getCdmStiVsEnumNavn().get(cdmSti + ".children[0]"))
                                .enumNavnId(cdmUttrekk.getCdmStiVsTekstId().get(cdmSti + ".children[0]"))
                                .build();
                        euSedStiVsEuSedFundament.put(euSedSti, euSedFundament);
                    } else if (List.of("_uxTextInput", "_uxTextAreaInput", "_rinaIRAutocomplete").contains(tagType)) {
                        String euSedSti = byggEuSedSti(cdmSti, cdmStiVsMalTagnavn, cdmStiVsMalTagtype);
                        EuSedFundament euSedFundament = EuSedFundament.builder()
                                .verdiType("tekst")
                                .cdmType(cdmUttrekk.getCdmStiVsTagtype().get(cdmSti))
                                .indeks(indeks)
                                .ledetekst(cdmUttrekk.getCdmStiVsLedetekst().get(cdmSti))
                                .tekstlengde(cdmUttrekk.getCdmStiVsTekstlengde().get(cdmSti))
                                .build();
                        euSedStiVsEuSedFundament.put(euSedSti, euSedFundament);
                    } else if (List.of("_uxDateInput").contains(tagType)) {
                        String euSedSti = byggEuSedSti(cdmSti, cdmStiVsMalTagnavn, cdmStiVsMalTagtype);
                        EuSedFundament euSedFundament = EuSedFundament.builder()
                                .verdiType("dato")
                                .cdmType(cdmUttrekk.getCdmStiVsTagtype().get(cdmSti))
                                .indeks(indeks)
                                .ledetekst(cdmUttrekk.getCdmStiVsLedetekst().get(cdmSti))
                                .tekstlengde(cdmUttrekk.getCdmStiVsTekstlengde().get(cdmSti))
                                .build();
                        euSedStiVsEuSedFundament.put(euSedSti, euSedFundament);
                    } else if (List.of("_uxFieldSetContainer", "_uxSelectAutoCompleteInput", "_rinaArray").contains(tagType)) {
                        String euSedSti = byggEuSedSti(cdmSti, cdmStiVsMalTagnavn, cdmStiVsMalTagtype);
                        EuSedFundament euSedFundament = EuSedFundament.builder()
                                .verdiType("tittel")
                                .cdmType(cdmUttrekk.getCdmStiVsTagtype().get(cdmSti))
                                .indeks(indeks)
                                .ledetekst(cdmUttrekk.getCdmStiVsLedetekst().get(cdmSti))
                                .tekstlengde(cdmUttrekk.getCdmStiVsTekstlengde().get(cdmSti))
                                .build();
                        euSedStiVsEuSedFundament.put(euSedSti, euSedFundament);
                    } else if (List.of("_uxHiddenGroupContainer").contains(tagType)) {
                        if (cdmUttrekk.getCdmStiVsLedetekst().get(cdmSti) != null) {
                            String euSedSti = byggEuSedSti(cdmSti, cdmStiVsMalTagnavn, cdmStiVsMalTagtype);
                            EuSedFundament euSedFundament = EuSedFundament.builder()
                                    .verdiType("tittel")
                                    .cdmType(cdmUttrekk.getCdmStiVsTagtype().get(cdmSti))
                                    .indeks(cdmUttrekk.getCdmStiVsIndeks().get(cdmSti))
                                    .ledetekst(cdmUttrekk.getCdmStiVsLedetekst().get(cdmSti))
                                    .tekstlengde(cdmUttrekk.getCdmStiVsTekstlengde().get(cdmSti))
                                    .build();
                            euSedStiVsEuSedFundament.put(euSedSti, euSedFundament);
                        }
                    }
                });
        return euSedStiVsEuSedFundament;
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
                .cdmStiVsPreIndeks(uttrekkCdmMeta.getCdmStiVsPreIndeks())
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
                cdmStiVsLedetekst.put(cdmSti, tekstIdVsTekst.get(tekstId + "_label"));
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
            if (cdmTekstIdVsEnumNavn.get(tekstId) != null) {
                cdmStiVsEnumNavn.put(cdmSti, cdmTekstIdVsEnumNavn.get(tekstId));
            }
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
                        if (tekstIdVsTekst.containsKey(entry.getValue() + "_codeListEntry")) {
                            return true;
                        } else {
                            log.error("Mangler tekstIdVsTekst for: v={} ", entry.getValue());
                            return false;
                        }
                    })
                    .collect(Collectors.toMap(
                            entry -> entry.getKey(),
                            entry -> tekstIdVsTekst.get(entry.getValue() + "_codeListEntry")));
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

}
