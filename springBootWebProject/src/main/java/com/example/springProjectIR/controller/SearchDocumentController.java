package com.example.springProjectIR.controller;

import com.example.springProjectIR.DTO.DocumentDTO;
import com.example.springProjectIR.Model.Document;
import com.example.springProjectIR.Service.SearchService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/search")
@CrossOrigin
public class SearchDocumentController {

    private final SearchService searchService;

    public SearchDocumentController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping
    public List<DocumentDTO> search(String searchContent){

        List<Document> documents = this.searchService.searchDocuments(searchContent);
        List<DocumentDTO> documentDTOS = new ArrayList<>();
        for(Document document:documents){
            DocumentDTO documentDTO = new DocumentDTO(
                    document.getLink(),
                    document.getTitle(),
                    document.getScore(),
                    document.getH1(),
                    document.getOriginalBody()
            );
            documentDTOS.add(documentDTO);
        }
        return documentDTOS;
    }
}
