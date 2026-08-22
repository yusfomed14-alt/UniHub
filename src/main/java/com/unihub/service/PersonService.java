package com.unihub.service;

import com.unihub.dto.response.PersonResponse;
import com.unihub.dto.request.UpdatePersonRequest;
import com.unihub.entity.Person;
import com.unihub.exception.ResourceNotFoundException;
import com.unihub.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PersonService {
private final PersonRepository personRepository;

    // ── Get own profile ───────────────────────────────────
    public PersonResponse getMyProfile(String email){
        Person person=personRepository.findByEmail(email)
                .orElseThrow(()->new ResourceNotFoundException("Person not found"));

        return mapToResponse(person);
    }

    // ── Get person by ID (Admin only) ─────────────────────
    public PersonResponse getPersonById(String id){
        Person person = personRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException(
                        "Person not found with id "+id));

        return mapToResponse(person);

    }

    // ── Update own profile ────────────────────────────────
    @Transactional
    public PersonResponse updateMyProfile(String email, UpdatePersonRequest request){
        Person person=personRepository.findByEmail(email)
                .orElseThrow(()->new ResourceNotFoundException("Person not found with email "+email));

        person.setFirstName(request.getFirstName());
        person.setLastName(request.getLastName());
        person.setDateOfBirth(request.getDateOfBirth());
        person.setProfilePictureUrl(request.getPrfilePictureUrl());
        person.setPhoneNumber(request.getPhoneNumber());
        person.setUpdatedBy(person.getEmail());

        personRepository.save(person);

        return mapToResponse(person);
    }

    // ── Map entity to response ────────────────────────────
    private PersonResponse mapToResponse(Person person){
        return PersonResponse.builder()
                .id(person.getId())
                .email(person.getEmail())
                .role(person.getRole())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .dateOfBirth(person.getDateOfBirth())
                .phoneNumber(person.getPhoneNumber())
                .department(person.getDepartment())
                .faculty(person.getFaculty())
                .createdBy(person.getCreatedBy())
                .updatedBy(person.getUpdatedBy())
                .createdAt(person.getCreateAt())
                .updatedAt(person.getUpdateAt())
                .build();

    }
}
