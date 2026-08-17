package com.unihub.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="otp")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Otp {
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    @Column(name="id")
    private UUID id;

    @ManyToOne(fetch =FetchType.LAZY)
    @JoinColumn(name="person_id",nullable = false)
    private Person person;

    @Column(name = "otp_code",nullable = false)
    private String otpCode;

    @Column(name = "otp_expiry", nullable = false)
    private LocalDateTime otpExpiry;

    @CreationTimestamp
    @Column(name="created_at",nullable = false,updatable = false)
    private LocalDateTime createdAt;
}
