package eux.acl;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import no.nav.eux.rina.cpi.service.EessiAclClient;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AclMappingTest {

  private static final String SEDTYPE_V41 = "4.1";

  @Test
  public void mapEuSedToNav_sedVerEr2_korrektSedVerEr3() {
    String sedJson = "{" +
      "  \"H001\": {" +
      "    \"sedGVer\": \"4\"," +
      "    \"sedVer\": \"1\"" +
      "  }" +
      "}";
    String korrektSedVersjonFraRina = "4.3";
    JsonNode sedNode = EessiAclClient.mapEuSedToNav(sedJson, korrektSedVersjonFraRina);
    assertThat(sedNode.at("/sedVer").textValue()).isEqualTo("3");
  }

  @Test(expected = EessiAclException.class)
  public void inconsistentUseOfVariableAndFolderTest() throws Exception {
    Map<String, Object> flattenedNavSed = new HashMap<>();
    flattenedNavSed.put("$nav.bruker.navn.fornavn", "navn_erMappeHer");
    flattenedNavSed.put("$nav.bruker.navn", "navn_erVariabelHer");

    EessiAcl.createJsonNodeWithEmptyFieldsRemoved(flattenedNavSed);
  }

  @Test(expected = EessiAclException.class)
  public void templateNotFoundTest() throws Exception {
    EessiAcl.mapEuToNav("{\"F0XX\": {}}", SEDTYPE_V41);
  }

  @Test
    (expected = EessiAclException.class)
  public void sedIsEmptyTest() throws Exception {
    BiMap<String, Object> flattenedTemplate = HashBiMap.create();
    flattenedTemplate.put("$F001.folder", "nav.bruker.fornavn");

    EessiAcl.mapFlattenedEuSedToNavSed(null, flattenedTemplate);
  }

  @Test(expected = EessiAclException.class)
  public void templateIsEmptyTest() throws Exception {
    BiMap<String, Object> flattenedTemplate = HashBiMap.create();
    Map<String, Object> flattenedEuSed = new HashMap<>();

    EessiAcl.mapFlattenedEuSedToNavSed(flattenedEuSed, flattenedTemplate);
  }

  @Test(expected = EessiAclException.class)
  public void sedTypeUnknownTest() throws Exception {
    BiMap<String, Object> flattenedTemplate = HashBiMap.create();
    flattenedTemplate.put("$UNKNOWN_SEDTYPE.folder", "nav.bruker.fornavn");
    Map<String, Object> flattenedEuSed = new HashMap<>();

    EessiAcl.mapFlattenedEuSedToNavSed(flattenedEuSed, flattenedTemplate);
  }
}
