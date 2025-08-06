package eux.acl.generator.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class LogiskModellService {

    /**
     *  Id (som brukes i CDM-filene) vs navn på enum
     */
    public Map<String, String> uttrekkFraLogiskModell(String logiskModellXml) {
        if (logiskModellXml == null) {
            return null;
        }
        Document xml = Jsoup.parse(logiskModellXml);
        Elements elementer = xml.select("Element:has(ATR_Stereotype > value:contains(ENUM))");
        return elementer.stream()
                .collect(Collectors.toMap(
                        e -> e.getElementsByTag("GUID").text()
                        , e -> e.getElementsByTag("ATR_Type").text()
                        , (k, v) -> v)); // Overskriv hvis k allerede eksisterer
    }

}
