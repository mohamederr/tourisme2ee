package com.example.tourisme2e.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResponse<T> {
    private final List<T> contenu;
    private final int page;
    private final int taille;
    private final long totalElements;
    private final int totalPages;

    public PageResponse(Page<T> springPage) {
        this.contenu = springPage.getContent();
        this.page = springPage.getNumber();
        this.taille = springPage.getSize();
        this.totalElements = springPage.getTotalElements();
        this.totalPages = springPage.getTotalPages();
    }
}