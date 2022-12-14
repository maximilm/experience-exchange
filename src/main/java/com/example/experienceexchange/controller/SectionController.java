package com.example.experienceexchange.controller;

import com.example.experienceexchange.dto.SectionDto;
import com.example.experienceexchange.service.interfaceService.ISectionService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sections")
public class SectionController {

    private final ISectionService sectionService;

    public SectionController(ISectionService sectionService) {
        this.sectionService = sectionService;
    }

    @JsonView(SectionDto.AdminDetails.class)
    @PostMapping("/new-section")
    @ResponseStatus(HttpStatus.CREATED)
    public SectionDto createSection(@RequestBody @Validated(SectionDto.Create.class) SectionDto sectionDto) {
        return sectionService.createSection(sectionDto);
    }

    @JsonView(SectionDto.AdminDetails.class)
    @PutMapping("/{id}/settings")
    public SectionDto editSection(
            @PathVariable("id") Long sectionId,
            @RequestBody @Validated(SectionDto.Edit.class) SectionDto sectionDto) {
        return sectionService.editSection(sectionId, sectionDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSection(@PathVariable("id") Long sectionId) {
        sectionService.deleteSection(sectionId);
    }
}
