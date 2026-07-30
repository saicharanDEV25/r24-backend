package com.r24.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "gallery")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Gallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String beforeImageUrl;

    @Column(nullable = false)
    private String afterImageUrl;

    @Column(length = 500)
    private String description;

    private Boolean active = true;

}