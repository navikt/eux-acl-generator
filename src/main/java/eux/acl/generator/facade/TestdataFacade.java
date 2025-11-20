package eux.acl.generator.facade;

import eux.acl.generator.service.*;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class TestdataFacade {
    private final FilService filService;
    private final TestdataService testdataService;


    public TestdataFacade() {
        this.filService = new FilService();
        this.testdataService = new TestdataService();
    }

    public void lagTestdata(List<String> sedTyper, String cdmVersjon, int brukEnumkodeNr) {
        sedTyper.forEach(sedType -> testdataService.lagSedMedTestdata(sedType, cdmVersjon, brukEnumkodeNr));
    }

}
