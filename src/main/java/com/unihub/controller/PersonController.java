package com.unihub.controller;

import com.unihub.dto.response.PersonResponse;
import com.unihub.dto.request.UpdatePersonRequest;
import com.unihub.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping("/me")
    public ResponseEntity<PersonResponse> getMyProfile(
            @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(
                personService.getMyProfile(userDetails.getUsername())
        );
    }

    @PostMapping("/me")
    public ResponseEntity<PersonResponse> updateMyProfile(
            @Valid @RequestBody UpdatePersonRequest request,
            @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(
                personService.updateMyProfile(userDetails.getUsername(), request)
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PersonResponse> getPersonById(@PathVariable  String id){
    return   ResponseEntity.ok(
            personService.getPersonById(id)
    );
    }
}
