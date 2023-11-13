package io.apexapps.dlvdatamanager.views.home;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.apexapps.dlvdatamanager.data.AllData;
import io.apexapps.dlvdatamanager.data.service.DataImporterService;
import io.apexapps.dlvdatamanager.views.MainLayout;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@PageTitle("Home")
@Route(value = "/", layout = MainLayout.class)
@Slf4j
public class HomeView extends Composite<VerticalLayout> {
    private final DataImporterService dataImporterService;
    private final ObjectMapper mapper;
    private final MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
    private final Upload upload = new Upload(buffer);

    public HomeView(DataImporterService dataImporterService, ObjectMapper mapper) {
        this.dataImporterService = dataImporterService;
        this.mapper = mapper;
        addClassName("home-view");
        getContent().setHeightFull();
        getContent().setWidthFull();

        upload.setAcceptedFileTypes(".json");
        upload.setDropAllowed(true);
        getContent().add(upload);

        upload.addSucceededListener(event -> {
            try {
                AllData result = mapper.readValue(
                        buffer.getInputStream(event.getFileName()),
                        AllData.class
                );
                dataImporterService.importAll(result);
            } catch (IOException e) {
                log.error("Unable to parse the file {}", event.getFileName(), e);
            }
        });
    }
}
