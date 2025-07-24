package eux.acl.generator.service;


import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static java.util.Arrays.asList;


public class UttrekkAvKodeverkFraCdmService {

  public Map<String, String> lesKodeverkFraCdm(Map<String, Object> cdmMalFlattened) {
    Map<String, String> cdmFilePathVsKey = new HashMap<>();
    Map<String, String> cdmFilePathVsType = new HashMap<>();
    Map<String, String> cdmStiVsOption = new HashMap<>();
    TreeMap<String, String> stierVsKodeverk = new TreeMap<>();
    cdmMalFlattened.keySet().stream().sorted().forEach(k -> {
      if (k.contains("context.key")) {
        String path = k.replace(".context.key", "");
        cdmFilePathVsKey.put(path, cdmMalFlattened.get(k).toString());
      }
      if (k.contains("context.type")) {
        String path = k.replace(".context.type", "");
        cdmFilePathVsType.put(path, cdmMalFlattened.get(k).toString());
      }
      if ((k.contains(".inputs.options") && k.contains(".val"))
        || (k.contains(".inputs.selectOptions") && k.contains(".id"))) {
        String path = k.replace(".inputs.options", "")
          .replace(".inputs.selectOptions", "")
          .replace(".val", "")
          .replace(".id", "")
          .replace(".context[", ".option[");
        cdmStiVsOption.put(path, cdmMalFlattened.get(k).toString());
      }
    });
    cdmStiVsOption
      .forEach(
        (p, t) -> {
          String[] cdmFilePathLevels = p.split("\\.");
          String[] cdmFilePathRaw = {""};
          String[] templatePathBuild = {""};
          asList(cdmFilePathLevels).forEach(child -> {
            if (child.contains("option[")) {
              templatePathBuild[0] += "." + child; // optional[#]
            } else {
              cdmFilePathRaw[0] += "." + child;
              String cdmPath = cdmFilePathRaw[0].replaceFirst(".", "");
              String type = cdmFilePathVsType.get(cdmPath);
              if ("_rinaArray".equals(type)
                || "_uxMultiCheckbox".equals(type)
                || "_uxHiddenArrayContainer".equals(type)
                || "_uxMultiSelect".equals(type)) {
                templatePathBuild[0] += "." + cdmFilePathVsKey.get(cdmPath) + "[0]";
              } else if (cdmFilePathVsKey.get(cdmPath) == null) {
                // unnlat å sette inn null i stien
              } else {
                templatePathBuild[0] += "." + cdmFilePathVsKey.get(cdmPath);
              }
            }
          });
          String option = cdmStiVsOption.get(p);
          stierVsKodeverk.put(templatePathBuild[0].replaceFirst(".", ""), option);
        }
      );
    return stierVsKodeverk;
  }

  public Map<String, String> finnEuStiVsLandkodesett(Map<String, Object> cdmMalFlattened) {
    Map<String, String> cdmFilePathVsKey = new HashMap<>();
    Map<String, String> cdmFilePathVsType = new HashMap<>();
    Map<String, Integer> cdmFilePathVsAntall = new HashMap<>();
    cdmMalFlattened.keySet().stream().sorted().forEach(k -> {
      if (k.contains("context.key")) {
        String path = k.replace(".context.key", "");
        cdmFilePathVsKey.put(path, cdmMalFlattened.get(k).toString());
      }
      if (k.contains("context.type")) {
        String path = k.replace(".context.type", "");
        cdmFilePathVsType.put(path, cdmMalFlattened.get(k).toString());
      }
      if (k.contains("context.inputs.selectOptions") && k.endsWith("label[0].prefix")) {
        String enumSti = k.split("\\.context\\.inputs\\.selectOptions")[0];
        if (cdmFilePathVsAntall.containsKey(enumSti)) {
          Integer antall = cdmFilePathVsAntall.get(enumSti);
          cdmFilePathVsAntall.put(enumSti, antall + 1);
        } else {
          cdmFilePathVsAntall.put(enumSti, 1);
        }
      }
    });
    Map<String, Integer> stierVsAntall = new HashMap<>();
    cdmFilePathVsAntall
      .forEach(
        (p, t) -> {
          String[] cdmFilePathLevels = p.split("\\.");
          String[] cdmFilePathRaw = {""};
          String[] templatePathBuild = {""};
          asList(cdmFilePathLevels).forEach(child -> {
            cdmFilePathRaw[0] += "." + child;
            String cdmPath = cdmFilePathRaw[0].replaceFirst(".", "");
            String type = cdmFilePathVsType.get(cdmPath);
            if ("_rinaArray".equals(type)
              || "_uxMultiCheckbox".equals(type)
              || "_uxHiddenArrayContainer".equals(type)
              || "_uxMultiSelect".equals(type)) {
              templatePathBuild[0] += "." + cdmFilePathVsKey.get(cdmPath) + "[0]";
            } else if (cdmFilePathVsKey.get(cdmPath) == null) {
              // unnlat å sette inn null i stien
            } else {
              templatePathBuild[0] += "." + cdmFilePathVsKey.get(cdmPath);
            }
          });
          Integer antall = cdmFilePathVsAntall.get(p);
          stierVsAntall.put(templatePathBuild[0].replaceFirst(".", ""), antall);
        }
      );
    Map<String, String> stiVsLandkodesett = new HashMap<>();
    stierVsAntall
      .forEach((k, v) -> {
                 switch (v) {
                   case 32: {
                     stiVsLandkodesett.put(k, "euEftaLand");
                     break;
                   }
                   case 229: {
                     stiVsLandkodesett.put(k, "nasjonaliteter");
                     break;
                   }
                   case 250: {
                     stiVsLandkodesett.put(k, "verdensLand");
                     break;
                   }
                   case 281: {
                     stiVsLandkodesett.put(k, "verdensLandHistorisk");
                     break;
                   }
                   default: {

                   }
                 }
               }
      );
    return stiVsLandkodesett;
  }

  public Map<String, String> lesMalFraCdm(Map<String, Object> cdmMalFlattened, String sedType) {
    Map<String, String> cdmFilePathVsKey = new HashMap<>();
    Map<String, String> cdmFilePathVsType = new HashMap<>();
    cdmMalFlattened.keySet().stream().sorted().forEach(k -> {
      if (k.contains("context.key")) {
        String path = k.replace(".context.key", "");
        cdmFilePathVsKey.put(path, cdmMalFlattened.get(k).toString());
      }
      if (k.contains("context.type")) {
        String path = k.replace(".context.type", "");
        cdmFilePathVsType.put(path, cdmMalFlattened.get(k).toString());
      }
    });
    Map<String, String> aclAlleStierVsType = new HashMap<>();
    cdmFilePathVsKey
      .forEach(
        (p, t) -> {
          String[] cdmFilePathLevels = p.split("\\.");
          String[] cdmFilePathRaw = {""};
          String[] templatePathBuild = {""};
          asList(cdmFilePathLevels).forEach(child -> {
            cdmFilePathRaw[0] += "." + child;
            String cdmPath = cdmFilePathRaw[0].replaceFirst(".", "");
            String type = cdmFilePathVsType.get(cdmPath);
            if ("_rinaArray".equals(type)
              || "_uxMultiCheckbox".equals(type)
              || "_uxHiddenArrayContainer".equals(type)
              || "_uxMultiSelect".equals(type)) {
              templatePathBuild[0] += "." + cdmFilePathVsKey.get(cdmPath) + "[0]";
            } else if (cdmFilePathVsKey.get(cdmPath) == null) {
              // unnlat å sette inn null i stien
            } else {
              templatePathBuild[0] += "." + cdmFilePathVsKey.get(cdmPath);
            }
          });
          String type = cdmFilePathVsType.get(p);
          aclAlleStierVsType.put(templatePathBuild[0].replaceFirst(".", ""), type);
        }
      );
    Map<String, String> aclMalStiVsType = new HashMap<>();
    aclMalStiVsType.put(sedType + ".sedGVer", "");
    aclMalStiVsType.put(sedType + ".sedVer", "");
    aclAlleStierVsType.forEach((p, v) -> {
      if (!"_uxFieldSetContainer".equals(v)
        && !"_rinaArray".equals(v)
        && !"_uxHiddenGroupContainer".equals(v)
        && !"_rinaTranslateContainer".equals(v)
      ) {
        aclMalStiVsType.put(p, v);
      }
    });
    return aclMalStiVsType;
  }

  public List<String> lesTekstIderForEnSed(Map<String, Object> cdmMalFlattened) {
    Map<String, String> cdmFilePathVsKey = new HashMap<>();
    Map<String, String> cdmFilePathVsType = new HashMap<>();
    Map<String, String> cdmStiVsOption = new HashMap<>();
    Map<String, String> cdmStiVsTekstId = new HashMap<>();
    Map<String, String> cdmStiVsIndeks = new HashMap<>();
    cdmMalFlattened.keySet().stream().sorted().forEach(k -> {
      if (k.contains("context.key")) {
        String path = k.replace(".context.key", "");
        cdmFilePathVsKey.put(path, cdmMalFlattened.get(k).toString());
      }
      if (k.contains("context.type")) {
        String path = k.replace(".context.type", "");
        cdmFilePathVsType.put(path, cdmMalFlattened.get(k).toString());
      }
      if ((k.contains(".inputs.options") && k.contains(".val"))
        || (k.contains(".inputs.selectOptions") && k.contains(".id"))) {
        String path = k.replace(".inputs.options", "")
          .replace(".inputs.selectOptions", "")
          .replace(".val", "")
          .replace(".id", "")
          .replace(".context[", ".option[");
        cdmStiVsOption.put(path, cdmMalFlattened.get(k).toString());
      }
      if (k.contains(".label[")) {
        if (k.endsWith(".string")) {
          String path = k.replace(".string", "")
            .replace(".context.inputs.label", ".string");
          cdmStiVsTekstId.put(path, cdmMalFlattened.get(k).toString());
        }
        if (k.endsWith(".prefix")) {
          String path = k.replace(".prefix", "")
            .replace(".context.inputs.label", ".prefix");
          cdmStiVsIndeks.put(path, cdmMalFlattened.get(k).toString());
        }
      }
    });
    return cdmStiVsTekstId.values().stream().toList();
  }

  public Map<String, String> lagCdmStiVsTekst(Map<String, Object> cdmMalFlattened
    , Map<String, String> tekstIdVsTekst) {
    Map<String, String> cdmStiVsTekst = new TreeMap<>();
    cdmMalFlattened.forEach((k, v) -> {
      if (k.endsWith(".context.inputs.label[0].string")) {
        String cdmSti = k.replace(".context.inputs.label[0].string", "");
        String tekstId = v.toString()
                .replace("forms.", "")
                .replace("4.4_", "4.3_");
        String tekst = tekstIdVsTekst.get(tekstId);
        cdmStiVsTekst.put(cdmSti, " " + tekst);
      }
      if (k.endsWith(".context.inputs.label")) {
        String cdmSti = k.replace(".context.inputs.label", "");
        String tekst = tekstIdVsTekst.get(v.toString()
                .replace("forms.", "")
                .replace("4.4_", "4.3_"));
        cdmStiVsTekst.put(cdmSti, " " + tekst);
      }
      if (k.endsWith("context.inputs.sedTitle")) {
        String sedType = cdmMalFlattened.get(k.replace(".sedTitle", ".sedName")).toString().trim();
        String tekst = tekstIdVsTekst.get(v.toString()
                        .replace("forms.", "")
                        .replace("4.4_", "4.3_"));
        cdmStiVsTekst.put("sedTitle", sedType + " - " + tekst);
      }
    });
    return cdmStiVsTekst;
  }

  public LinkedHashMap<String, String> lagCdmStiVsIndeks(
          LinkedHashMap<String, Object> cdmMalFlattened) {
    LinkedHashMap<String, String> cdmStiVsIndeks = new LinkedHashMap<>();
    cdmMalFlattened.forEach((k, v) -> {
      if (k.endsWith(".context.inputs.label[0].prefix")) {
        String cdmSti = k.replace(".context.inputs.label[0].prefix", "");
        cdmStiVsIndeks.put(cdmSti, v.toString().trim());
      }
      if (k.endsWith(".context.inputs.preLabel")) {
        String cdmSti = k.replace(".context.inputs.preLabel", "").trim();
        cdmStiVsIndeks.put(cdmSti, v.toString());
      }
      if (k.endsWith("context.inputs.sedTitle")) {
        String sedType = cdmMalFlattened.get(k.replace(".sedTitle", ".sedName")).toString();
        cdmStiVsIndeks.put("sedTitle", sedType);
      }
    });
    return cdmStiVsIndeks;
  }

  public Map<String, String> lagCdmStiVsType(Map<String, Object> cdmMalFlattened) {
    Map<String, String> cdmStiVsType = new HashMap<>();
    cdmMalFlattened.forEach((k, v) -> {
      if (k.contains(".context.type")) {
        String path = k.replace(".context.type", "");
        cdmStiVsType.put(path, v.toString());
      }
      if (k.endsWith("context.inputs.sedTitle")) {
        String sedType = cdmMalFlattened.get(k.replace("inputs.sedTitle", "type")).toString();
        cdmStiVsType.put("sedTitle", sedType);
      }
    });
    return cdmStiVsType;
  }

  public Map<String, String> lagCdmStiVsEnumNavn(Map<String, Object> cdmMalFlattened) {
    Set<String> enumStier = new HashSet<>();
    Map<String, String> cdmStiVsDataTyper = new HashMap<>();
    Map<String, String> cdmStiVsEnumNavn = new HashMap<>();
    cdmMalFlattened.forEach((k, v) -> {
      if (k.contains(".context.inputs.eessiDataType") && "ENUM".equals(v.toString())) {
        enumStier.add(k.replace(".context.inputs.eessiDataType", ".children[0].children[0]"));
        enumStier.add(k.replace(".context.inputs.eessiDataType", ".children[0].children[0].children[0]"));
      }
      if (k.endsWith(".context.inputs.eessiDataInfo[0].value")) {
        cdmStiVsDataTyper.put(k.replace(".context.inputs.eessiDataInfo[0].value", ".children[0].children[0]"), v.toString());
        cdmStiVsDataTyper.put(k.replace(".context.inputs.eessiDataInfo[0].value", ".children[0].children[0].children[0]"), v.toString());
      }
    });
    enumStier.forEach(k -> {
      cdmStiVsEnumNavn.put(k, cdmStiVsDataTyper.get(k));
    });
    return cdmStiVsEnumNavn;
  }

  public Map<String, TreeMap<String, String>> lagCdmStiVsKoderOgKodenavn(
    Map<String, Object> cdmSedFlattened, Map<String, String> tekstIdVsTekst) {
    Map<String, String> cdmStiVsKode = new HashMap<>();
    Map<String, String> cdmStiVsTekst = new HashMap<>();
    Map<String, TreeMap<String, String>> cdmStiVsKodeVsKodenavn = new TreeMap<>();
    cdmSedFlattened.forEach((cdmSti, tekstId) -> {
      leggTilCdmStiVsEnumKode(cdmSti, tekstId.toString().replace("4.4_", "4.3_"), cdmStiVsKode);
      leggTilCdmStiVsEnumTekst(cdmSti, tekstId.toString().replace("4.4_", "4.3_"), tekstIdVsTekst, cdmStiVsTekst);
    });  // 4.4_B4FCF16B-45DF-E511-80BF-000D3A22E8CD_sedTitle
    cdmStiVsKode.forEach((cdmStiKode, enumKode) -> {
      if (StringUtils.isNumeric(enumKode)) {
        String cdmSti = StringUtils.substringBeforeLast(cdmStiKode, ".");
        if (!cdmStiVsKodeVsKodenavn.containsKey(cdmSti)) {
          TreeMap<String, String> kodeVsNavn = new TreeMap<>();
          cdmStiVsKodeVsKodenavn.put(cdmSti, kodeVsNavn);
        }
        cdmStiVsKodeVsKodenavn.get(cdmSti)
          .put(enumKode, cdmStiVsTekst.get(cdmStiKode));
      }
    });
    return cdmStiVsKodeVsKodenavn;
  }

  public void leggTilCdmStiVsEnumKode(String cdmSti, String kode,
                                      Map<String, String> cdmStiVsEnumKode) {
    if (cdmSti.contains(".context.inputs.selectOptions") && cdmSti.contains("].id")) {
      String sti = cdmSti.replace(".context.inputs.selectOptions", ".kode")
        .replace("].id", "]");
      cdmStiVsEnumKode.put(sti, kode.toString());
    }
    if (cdmSti.contains(".context.inputs.options") && cdmSti.contains("].val")) {
      String sti = cdmSti.replace(".context.inputs.options", ".kode")
        .replace("].val", "]");
      cdmStiVsEnumKode.put(sti, kode.toString());
    }
  }

  public void leggTilCdmStiVsEnumTekst(String k, String v,
                                       Map<String, String> tekstIdVsTekst,
                                       Map<String, String> cdmStiVsTekst) {
    if (k.contains(".context.inputs.selectOptions") && k.contains(".label[0].string")) {
      String sti = k.replace(".context.inputs.selectOptions", ".kode")
        .replace(".label[0].string", "");
      String tekst = tekstIdVsTekst.get(v.replace("forms.", ""));
      if (tekst != null) {
        cdmStiVsTekst.put(sti, tekst.trim());
      } else {
        cdmStiVsTekst.put(sti, null);
      }
    }
    if (k.contains(".context.inputs.options") && k.contains(".label[0].string")) {
      String sti = k.replace(".context.inputs.options", ".kode")
        .replace(".label[0].string", "");
      String tekst = tekstIdVsTekst.get(v.replace("forms.", ""));
      if (tekst != null) {
        cdmStiVsTekst.put(sti, tekst.trim());
      } else {
        cdmStiVsTekst.put(sti, null);
      }
    }
  }
}

