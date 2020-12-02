package com.myaudiolibrary.web.repository;

import com.myaudiolibrary.web.model.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtistRepository extends JpaRepository<Artist,Integer> {

      Page<Artist>findAllByNameIgnoreCaseContaining(String name, Pageable pageable);
      Boolean existsByNameIgnoreCase(String name);
}
