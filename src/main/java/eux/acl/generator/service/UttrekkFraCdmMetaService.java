package eux.acl.generator.service;

import eux.acl.generator.modell.CdmUttrekk;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class UttrekkFraCdmMetaService {

    /**
     * Trekker ut info fra Cdm-meta. Cdm-stien er nøkkel og binder infoen sammen.
     */
    public CdmUttrekk uttrekkFraCdmMetaFil(Map<String, Object> cdmFilFlattened, String sedType) {
        if (cdmFilFlattened == null || cdmFilFlattened.isEmpty()) {
            return null;
        }
        CdmUttrekk cdmUttrekk = new CdmUttrekk();
        cdmFilFlattened.keySet().stream().sorted().forEach(k -> {
            if (k.contains("context.key")) {
                String cdmSti = k.replace(".context.key", "");
                cdmUttrekk.cdmStiVsMalTagnavn.put(cdmSti, cdmFilFlattened.get(k).toString());
            } else if (k.contains("context.type")) {
                String cdmSti = k.replace(".context.type", "");
                cdmUttrekk.cdmStiVsTagtype.put(cdmSti, cdmFilFlattened.get(k).toString());
            } else if (k.endsWith(".context.inputs.label[0].string")) {
                String cdmSti = k.replace(".context.inputs.label[0].string", "");
                cdmUttrekk.cdmStiVsTekstId.put(cdmSti, cdmFilFlattened.get(k).toString().split("_")[1].toLowerCase());
            } else if (k.endsWith(".context.inputs.label")) {
                String cdmSti = k.replace(".context.inputs.label", "");
                cdmUttrekk.cdmStiVsTekstId.put(cdmSti, cdmFilFlattened.get(k).toString().split("_")[1].toLowerCase());
            } else if (k.endsWith("context.inputs.sedTitle")) {
                String sedTittelId = cdmFilFlattened.get(k).toString().trim();
                cdmUttrekk.cdmStiVsTekstId.put("sedTitle", sedTittelId);
            } else if (k.endsWith("context.inputs.label[0].prefix")) {
                String cdmSti = k.replace(".context.inputs.label[0].prefix", "");
                cdmUttrekk.cdmStiVsIndeks.put(cdmSti, cdmFilFlattened.get(k).toString());
            } else if ((k.contains(".inputs.options") && k.contains(".val"))
                    || (k.contains(".inputs.selectOptions") && k.contains(".id"))) {
                String cdmSti = k.replace(".inputs.options", "")
                        .replace(".inputs.selectOptions", "")
                        .replace(".val", "")
                        .replace(".id", "")
                        .replace(".context[", ".enumVerdi[");
                cdmUttrekk.cdmStiTilEnum.add(cdmSti.split(".enumVerdi")[0]);
                cdmUttrekk.cdmStiVsEnumVerdier.put(cdmSti, cdmFilFlattened.get(k).toString());
            }
        });
        return cdmUttrekk;
    }

    public List<String> lagCdmSed(CdmUttrekk cdmUttrekk) {
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
                    // SEDen er bygget opp av tagger av disse typene
                    if (List.of("_uxTextInput", "_uxTextAreaInput", "_uxDateInput"
                            , "_uxMultiCheckbox", "_uxMultiSelect", "_uxRadioButton"
                            , "_uxHiddenArrayContainer", "_rinaIRAutocomplete").contains(type[0])) {
                        cdmSedFlattened.add(cdmSedBuilder[0].replaceFirst(".", ""));
                    }
                });
        // fjerner dupliserte stier
        return new HashSet<>(cdmSedFlattened).stream().sorted().toList();
    }

    public void settInnCdmStiVsEnumtype(CdmUttrekk cdmUttrekk, Map<String, String> cdmTekstIdVsEnumtype) {
        if (cdmUttrekk == null || cdmUttrekk.getCdmStiVsTekstId() == null || cdmTekstIdVsEnumtype == null) {
            return;
        }
        Map<String, String> cdmStiVsEnumtype = new HashMap<>();
        cdmUttrekk.getCdmStiVsTekstId().forEach((cdmSti, tekstId) -> {
            if (cdmTekstIdVsEnumtype.containsKey(tekstId)) {
                cdmStiVsEnumtype.put(cdmSti, cdmTekstIdVsEnumtype.get(tekstId));
            }
        });
        cdmUttrekk.setCdmStiVsEnumtype(cdmStiVsEnumtype);
    }

}
