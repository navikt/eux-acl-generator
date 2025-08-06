package eux.acl.generator.service;

import eux.acl.generator.modell.CdmUttrekk;
import eux.acl.generator.modell.CdmUttrekkFundament;
import eux.acl.generator.modell.EnumkodeFundament;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class UttrekkFraCdmMetaService {

    /**
     * Trekker ut info fra Cdm-meta. Cdm-stien er alltid nøkkel (...children[#].children[#].children[#]...).
     */
    public CdmUttrekk uttrekkFraCdmMetaSed(Map<String, Object> cdmFilFlattened) {
        if (cdmFilFlattened == null || cdmFilFlattened.isEmpty()) {
            return null;
        }
        CdmUttrekk cdmUttrekk = new CdmUttrekk();
        cdmFilFlattened.keySet().stream().sorted().forEach(k -> {
            String v = cdmFilFlattened.get(k).toString();
            if (k.contains("context.key")) {
                String cdmSti = k.replace(".context.key", "");
                cdmUttrekk.getCdmStiVsMalTagnavn().put(cdmSti, v);
            } else if (k.contains("context.type")) {
                String cdmSti = k.replace(".context.type", "");
                cdmUttrekk.getCdmStiVsTagtype().put(cdmSti, v);
            } else if (k.endsWith(".context.inputs.label[0].string")) {
                String cdmSti = k.replace(".context.inputs.label[0].string", "");
                cdmUttrekk.getCdmStiVsTekstId().put(cdmSti, v.split("_")[1].toLowerCase());
            } else if (k.endsWith(".context.inputs.label")) {
                String cdmSti = k.replace(".context.inputs.label", "");
                cdmUttrekk.getCdmStiVsTekstId().put(cdmSti, v.split("_")[1].toLowerCase());
            } else if (k.endsWith("context.inputs.sedTitle")) {
                String sedTittelId = v.trim();
                cdmUttrekk.getCdmStiVsTekstId().put("sedTitle", sedTittelId);
            } else if (k.endsWith("context.inputs.label[0].prefix")) {
                String cdmSti = k.replace(".context.inputs.label[0].prefix", "");
                cdmUttrekk.getCdmStiVsIndeks().put(cdmSti, v);
            } else if ((k.contains(".inputs.options") && k.contains(".val"))
                    || (k.contains(".inputs.selectOptions") && k.contains(".id"))) {
                String cdmSti = k.replace(".inputs.options", "")
                        .replace(".inputs.selectOptions", "")
                        .replace(".val", "")
                        .replace(".id", "")
                        .replace(".context[", ".enumkodeId[");
                cdmUttrekk.getCdmStiVsEnumkodeId().put(cdmSti, v);
            } else if ((k.contains(".context.inputs.options[") && k.contains(".label[0].string"))
                    || (k.contains(".context.inputs.selectOptions[") && k.contains(".label[0].string"))) {
                String cdmSti = k.replace(".context.inputs.options[", ".enumkodeTekstId[")
                        .replace(".context.inputs.selectOptions[", ".enumkodeTekstId[")
                        .replace(".label[0].string", "");
                cdmUttrekk.getCdmStiVsEnumkodeTekstId().put(cdmSti, v.split("_")[1].toLowerCase());
            }

        });
        return cdmUttrekk;
    }

    public List<String> lagEuSed(CdmUttrekkFundament cdmUttrekk) {
        if (cdmUttrekk == null || cdmUttrekk.getCdmStiVsMalTagnavn() == null) {
            return null;
        }
        Map<String, String> cdmStiVsMalTagnavn = cdmUttrekk.getCdmStiVsMalTagnavn();
        Map<String, String> cdmStiVsMalTagtype = cdmUttrekk.getCdmStiVsTagtype();
        List<String> cdmSedFlattened = new ArrayList<>();
        cdmStiVsMalTagnavn
                .forEach((s, v) -> {
                    String[] cdmStiSomArray = s.split("\\.");
                    String[] cdmStiBuilder = {""};
                    String[] cdmSedBuilder = {""};
                    String[] type = {""};
                    Arrays.stream(cdmStiSomArray).toList().forEach(childLevel -> {
                        cdmStiBuilder[0] += cdmStiBuilder[0].isEmpty() ? childLevel : "." + childLevel;
                        String cdmSti = cdmStiBuilder[0];
                        type[0] = cdmStiVsMalTagtype.get(cdmSti);
                        if (cdmStiVsMalTagnavn.get(cdmSti) != null) {
                            cdmSedBuilder[0] += "." + cdmStiVsMalTagnavn.get(cdmSti);
                            // setter inn at det er ei liste
                            if ("_rinaArray".equals(type[0])
                                    || "_uxMultiCheckbox".equals(type[0])
                                    || "_uxHiddenArrayContainer".equals(type[0])
                                    || "_uxMultiSelect".equals(type[0])) {
                                cdmSedBuilder[0] += "[0]";
                            }
                        }
                    });
                    // En SED er bygget opp av kun tagger av disse typene
                    if (List.of("_uxTextInput", "_uxTextAreaInput", "_uxDateInput"
                            , "_uxMultiCheckbox", "_uxMultiSelect", "_uxRadioButton"
                            , "_uxHiddenArrayContainer", "_rinaIRAutocomplete").contains(type[0])) {
                        cdmSedFlattened.add(cdmSedBuilder[0].replaceFirst(".", ""));
                    }
                });
        // fjerner dupliserte stier
        return new HashSet<>(cdmSedFlattened).stream().sorted().toList();
    }

    public CdmUttrekkFundament lagCdmUttrekkFundament(Map<String, Object> cdmMetaSed,
                                                      Map<String, String> tekstIdVsTekst,
                                                      Map<String, String> tekstIdVsEnumtype) {
        CdmUttrekk cdmUttrekk = uttrekkFraCdmMetaSed(cdmMetaSed);
        return CdmUttrekkFundament.builder()
                .cdmStiVsMalTagnavn(cdmUttrekk.getCdmStiVsMalTagnavn())
                .cdmStiVsTagtype(cdmUttrekk.getCdmStiVsTagtype())
                .cdmStiVsIndeks(cdmUttrekk.getCdmStiVsIndeks())
                .cdmStiVsLedetekst(lagCdmStiVsLedetekst(cdmUttrekk, tekstIdVsTekst))
                .cdmStiVsEnumtype(lagCdmStiVsEnumtype(cdmUttrekk, tekstIdVsEnumtype))
                .cdmStiVsEnumkodeTekst(lagCdmStiVsEnumkodeTekst(cdmUttrekk, tekstIdVsTekst))
                .cdmStiVsEnumkodeId(cdmUttrekk.getCdmStiVsEnumkodeId())
                .cdmStiVsTekstId(cdmUttrekk.getCdmStiVsTekstId())
                .build();
    }


    public Map<String, String> lagCdmStiVsLedetekst(CdmUttrekk cdmUttrekk, Map<String, String> tekstIdVsTekst) {
        if (cdmUttrekk == null || cdmUttrekk.getCdmStiVsTekstId() == null || tekstIdVsTekst == null) {
            return null;
        }
        Map<String, String> cdmStiVsLedetekst = new HashMap<>();
        cdmUttrekk.getCdmStiVsTekstId().forEach((cdmSti, tekstId) -> {
            if (tekstId == null) {
                log.error("tekstId mangler, cdmSti={}", cdmSti);
            } else {
                cdmStiVsLedetekst.put(cdmSti, tekstIdVsTekst.get(tekstId));
            }
        });
        return cdmStiVsLedetekst;
    }

    public Map<String, String> lagCdmStiVsEnumtype(CdmUttrekk cdmUttrekk, Map<String, String> cdmTekstIdVsEnumtype) {
        if (cdmUttrekk == null || cdmUttrekk.getCdmStiVsTekstId() == null || cdmTekstIdVsEnumtype == null) {
            return null;
        }
        Map<String, String> cdmStiVsEnumtype = new HashMap<>();
        cdmUttrekk.getCdmStiVsTekstId().forEach((cdmSti, tekstId) -> {
            if (cdmTekstIdVsEnumtype.containsKey(tekstId)) {
                cdmStiVsEnumtype.put(cdmSti, cdmTekstIdVsEnumtype.get(tekstId));
            }
        });
        return cdmStiVsEnumtype;
    }

    public Map<String, String> lagCdmStiVsEnumkodeTekst(CdmUttrekk cdmUttrekk, Map<String, String> tekstIdVsTekst) {
        if (cdmUttrekk == null || cdmUttrekk.getCdmStiVsEnumkodeTekstId() == null || tekstIdVsTekst == null) {
            return null;
        }
        try {
            return cdmUttrekk.getCdmStiVsEnumkodeTekstId().entrySet().stream()
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

    public void opprettEnumtypene(Map<String, TreeMap<Integer, EnumkodeFundament>> enumtypeVsEnumInnhold
            , CdmUttrekkFundament cdmUttrekkFundament) {
        cdmUttrekkFundament.getCdmStiVsEnumtype().forEach((cdmSti, enumtype) -> {
            if (!enumtypeVsEnumInnhold.containsKey(enumtype)) {
                enumtypeVsEnumInnhold.put(enumtype, new TreeMap<>());
            }
        });
    }

    public void fyllInnEnumkodeId(Map<String, TreeMap<Integer, EnumkodeFundament>> enumtypeVsEnumInnhold
            , CdmUttrekkFundament cdmUttrekkFundament) {
        cdmUttrekkFundament.getCdmStiVsEnumkodeId()
                .forEach((cdmSti, enumkodeId) -> {
                            String cdmStiJustert = StringUtils.substringBeforeLast(cdmSti, ".");
                            String cdmIndeks = StringUtils.getDigits(StringUtils.substringAfterLast(cdmSti, "["));
                            int indeksNr = Integer.parseInt(cdmIndeks);
                            String enumtype = cdmUttrekkFundament.getCdmStiVsEnumtype().get(cdmStiJustert);
                            if (!enumtypeVsEnumInnhold.get(enumtype).containsKey(indeksNr)) {
                                enumtypeVsEnumInnhold.get(enumtype).put(indeksNr, new EnumkodeFundament());
                            }
                            enumtypeVsEnumInnhold.get(enumtype).get(indeksNr).setEnumkodeId(enumkodeId);
                        }
                );
    }

    public void fyllInnEnumkodeBeskrivelse(Map<String, TreeMap<Integer, EnumkodeFundament>> enumtypeVsEnumInnhold
            , CdmUttrekkFundament cdmUttrekkFundament) {
        cdmUttrekkFundament.getCdmStiVsEnumkodeTekst().forEach((cdmSti, enumkodeTekst) -> {
                    String cdmStiJustert = StringUtils.substringBeforeLast(cdmSti, ".");
                    String indeks = StringUtils.getDigits(StringUtils.substringAfterLast(cdmSti, "["));
                    int cdmIndeksNr = Integer.parseInt(indeks); // for korrekt sortering
                    String enumtype = cdmUttrekkFundament.getCdmStiVsEnumtype().get(cdmStiJustert);
                    if (!enumtypeVsEnumInnhold.get(enumtype).containsKey(cdmIndeksNr)) {
                        enumtypeVsEnumInnhold.get(enumtype).put(cdmIndeksNr, new EnumkodeFundament());
                    }
                    if (enumtypeVsEnumInnhold.get(enumtype).get(cdmIndeksNr).getEnumkodeBeskrivelse() != null
                            && !enumtypeVsEnumInnhold.get(enumtype).get(cdmIndeksNr).getEnumkodeBeskrivelse()
                            .equals(enumkodeTekst)) {
                        log.error("ENUM feil. Ulike verdier i ulike forekomster av samme enum: " +
                                        "enumtype={}, enumTekst_1={}, enumTekst_2={}"
                                , enumtype, enumkodeTekst, enumtypeVsEnumInnhold.get(enumtype).get(indeks).getEnumkodeBeskrivelse());
                    }
                    enumtypeVsEnumInnhold.get(enumtype).get(cdmIndeksNr).setEnumkodeBeskrivelse(enumkodeTekst);
                }
        );
    }

    public void fyllInnEnumkoderMedBeskrivelser(Map<String, TreeMap<Integer, EnumkodeFundament>> enumtypeVsEnumInnhold) {
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
