package eux.acl.generator.service;

import com.github.wnameless.json.unflattener.JsonUnflattener;
import eux.acl.generator.modell.EnumkodeFundament;
import eux.acl.generator.modell.EuSedFundament;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class TestdataService {
    private FilService filService;

    public TestdataService() {
        filService = new FilService();
    }

    public void lagSedMedTestdata(String sedType, String cdmVersjon, int enumkodeNr) {
        TreeMap<String, EuSedFundament> euSedStiVsEuSedStiInfo = filService.lesEuSedFundamentFil(sedType, cdmVersjon);
        int[] datoTeller = {2001};
        TreeMap<String, String> testSedLinjer = new TreeMap<>();
        euSedStiVsEuSedStiInfo.forEach((s, fundament) -> {
            String testdata = "";

            if ("tekst".equals(fundament.getVerdiType())) {
                testdata = "æøå";
            } else if ("dato".equals(fundament.getVerdiType())) {
                testdata =  datoTeller[0] + "-01-01";
                datoTeller[0]++;
            } else if ("enum".equals(fundament.getVerdiType())) {
                String enumNavn = fundament.getEnumNavn();
                TreeMap<Integer, EnumkodeFundament> enumkodeNrVsInfo = filService.lesEnumFundamentFil(enumNavn, cdmVersjon);
                if (enumkodeNr < enumkodeNrVsInfo.size()) {
                    testdata = enumkodeNrVsInfo.get(enumkodeNr).getEnumkodeId();
                } else {
                    testdata = enumkodeNrVsInfo.get(enumkodeNrVsInfo.size() - 1).getEnumkode();
                }
            }
            testSedLinjer.put(s, testdata);
            String stiUtenEnum = s.replace(".value[0]", "");
            if (stiUtenEnum.contains("[0]")) {
                String sti2 = StringUtils.replaceOnce(s,"[0]", "[1]");
                String sti3 = StringUtils.replaceOnce(s,"[0]", "[2]");
                testSedLinjer.put(sti2, testdata);
                testSedLinjer.put(sti3, testdata);
            }

        });
//        testSedLinjer.put(sedType + ".sedGVer", cdmVersjon.split("\\.")[0]);
//        testSedLinjer.put(sedType + ".sedVer", cdmVersjon.split("\\.")[1]);
        String testSedJson = JsonUnflattener.unflatten(testSedLinjer);
        filService.opprettSedMedTestdata(testSedJson, sedType, cdmVersjon);
    }
}

