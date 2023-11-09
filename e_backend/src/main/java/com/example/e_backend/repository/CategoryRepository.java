package com.example.e_backend.repository;

import com.example.e_backend.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    public Category findByName(String name);
    @Query("Select c from Category c Where c.name=:name and c.parentCategory.name=:parentCategoryName ")
    public Category findByNameAndParant(@Param("name")String name,@Param("parentCategoryName")String parentCategoryName);

//    @Query("SELECT c FROM Category c WHERE c.name = :name AND c.parentCategory.name = :parentCategoryName")
//    public Category findByNameAndParant(@Param("name") String name, @Param("parentCategoryName") String parentCategoryName, Pageable pageable);


}
